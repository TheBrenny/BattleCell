package z5217759.brennfleck.jarod.battlecell.entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.thebrenny.jumg.entities.Entity;
import com.thebrenny.jumg.entities.EntityLiving;
import com.thebrenny.jumg.entities.messaging.Message;
import com.thebrenny.jumg.entities.messaging.MessageListener;
import com.thebrenny.jumg.entities.messaging.Postman;
import com.thebrenny.jumg.util.Angle;
import com.thebrenny.jumg.util.Images;
import com.thebrenny.jumg.util.Logger;
import com.thebrenny.jumg.util.MathUtil;

import z5217759.brennfleck.jarod.battlecell.entities.ai.EntityState;
import z5217759.brennfleck.jarod.battlecell.entities.ai.EntityStateMachine;
import z5217759.brennfleck.jarod.battlecell.entities.messages.BCMessage;
import z5217759.brennfleck.jarod.battlecell.entities.messages.MessageAttack;

public abstract class BCEntity extends EntityLiving implements MessageListener<BCEntity> {
	public static final int DEFENSE_MAX = 7;
	//TODO: Make these whichever is most used.
	public static final int[] DEFAULT_IDLE_COUNTER_TRIGGERS = {20, 20};
	public static final int[] DEFAULT_WALK_COUNTER_TRIGGERS = {20, 20};
	public static final int[] DEFAULT_ATTACK_COUNTER_TRIGGERS = {60, 20};
	public static final int[] DEFAULT_DEAD_COUNTER_TRIGGERS = {50, 0};
	private static final int ATTACK_COUNTER_STAGE = 1; // the sprite animation which we actually attack on
	
	private final Type type;
	
	protected Color color;
	protected AnimationState animationState;
	protected boolean facingRight = true;
	
	protected int spriteMeta = 0;
	protected int spriteCounter = 0;
	protected int[] spriteCounterTriggers = {0};
	protected boolean stopSpriteCounting = false;
	
	protected int healthCounter = 0;
	protected final int healthCounterMax = 120;
	protected final int healthFadeCounter = 15;
	
	protected int defenseWarrior;
	protected int defenseMagician;
	protected int defenseArcher;
	
	private boolean isControlled = false;
	
	public BCEntity(String name, float x, float y, Type type) {
		this(name, x, y, type, new Color(new Random().nextInt(0x1000000))); // 0xFFFFFF is the max integer for a colour, and nextInt is exclusive
	}
	public BCEntity(String name, float x, float y, Type type, Color color) {
		super(name, type.id, x, y, 0, type.tileMapY, type.speed, 7.0F);
		this.type = type;
		setAnchor((float) (Entity.ENTITY_SIZE / 2), (float) (Entity.ENTITY_SIZE / 2));
		this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
		this.setAnimationState(AnimationState.IDLE);
		Postman.getInstance().registerListener(this);
	}
	public BCEntity setControlled(boolean isControlled) {
		this.isControlled = isControlled;
		return this;
	}
	public BCEntity setDefensiveTraits(int warrior, int magician, int archer) {
		return setDefenseWarrior(warrior).setDefenseMagician(defenseMagician).setDefenseArcher(defenseArcher);
	}
	public BCEntity setDefenseWarrior(int warrior) {
		this.defenseWarrior = warrior;
		return this;
	}
	public BCEntity setDefenseMagician(int magician) {
		this.defenseMagician = magician;
		return this;
	}
	public BCEntity setDefenseArcher(int archer) {
		this.defenseArcher = archer;
		return this;
	}
	
	public boolean isControlled() {
		return this.isControlled;
	}
	
	public final void tick() {
		if(this.brain != null) {
			this.brain.tick();
		}
		updateCounters();
		update();
		move();
	}
	public abstract void update();
	public final void handleMessage(Message message) {
		if(!(message instanceof BCMessage)) return;
		
		BCMessage msg = (BCMessage) message;
		if(msg.getReceiver() != this) return;
		if(msg instanceof BCMessage) {
			switch(msg.getType()) {
			case ATTACK:
				hurt((MessageAttack) msg);
				break;
			case TARGET:
				// TODO: influence retaliation modifier. something like onTarget(tgt) => compute score for current target and tgt and attack the better score,
				break;
			default:
				iHaveMail((BCMessage) msg);
				break;
			}
		}
	}
	public void iHaveMail(BCMessage msg) {
		Logger.log("Unhandled message: " + msg.getUniqueString());
	}
	
	public EntityStateMachine getBrain() {
		return (EntityStateMachine) super.getBrain();
	}
	
	public Entity setAngle(float angle) {
		Entity e = super.setAngle(angle);
		facingRight = getAngle().getAngle() < 180;
		return e;
	}
	public Entity setAngle(Angle angle) {
		Entity e = super.setAngle(angle);
		facingRight = getAngle().getAngle() < 180;
		return e;
	}
	
	public abstract void attack(BCEntity target);
	public float hurt(MessageAttack msg) {
		float amount = msg.getDamage();
		float dampW = MathUtil.map((float) (defenseWarrior / DEFENSE_MAX), 0, DEFENSE_MAX, 1, 0.1F);
		float dampM = MathUtil.map((float) (defenseMagician / DEFENSE_MAX), 0, DEFENSE_MAX, 1, 0.1F);
		float dampA = MathUtil.map((float) (defenseArcher / DEFENSE_MAX), 0, DEFENSE_MAX, 1, 0.1F);
		
		BCEntity sender = msg.getSender();
		amount *= sender instanceof EntityWarrior ? dampW : sender instanceof EntityMagician ? dampM : sender instanceof EntityArcher ? dampA : 1;
		
		return super.hurt(amount);
	}
	public float heal(float amount) {
		this.healthCounter = this.healthCounterMax;
		return super.heal(amount);
	}
	public void setCounterTrigger(int ... counters) {
		this.spriteCounterTriggers = counters;
		this.spriteCounter = 0;
		this.stopSpriteCounting = false;
	}
	public void updateCounters() {
		if(!this.stopSpriteCounting) {
			this.spriteCounter++;
			if(new Random().nextInt(4) == 0) this.spriteCounter++;
			// if the sprite meta is less than our triggers length, and we achieved the counter trigger.
			if(this.spriteMeta < this.spriteCounterTriggers.length && this.spriteCounter >= this.spriteCounterTriggers[this.spriteMeta]) {
				this.spriteCounter = 0;
				this.spriteMeta = MathUtil.wrap(0, this.spriteMeta + 1, this.spriteCounterTriggers.length);
				if(this.spriteMeta == 0 && !this.animationState.spriteDoesLoop) {
					this.spriteMeta = this.spriteCounterTriggers.length - 1;
					this.stopSpriteCounting = true;
				} else counterEvent(this.spriteMeta);
				setSpriteMeta(this.spriteMeta); // this just makes sure a reset occurs.
			}
		}
		
		if(healthCounter > 0) this.healthCounter--;
		this.healthCounter = this.healthCounter < 0 ? 0 : this.healthCounter;
	}
	/**
	 * Called whenever the sprite counter hits a counter trigger as set by
	 * {@link #setCounterTrigger(int...)}.
	 * 
	 * @param counterStage
	 *        - Which stage was triggered. 0 is reset.
	 */
	public void counterEvent(int counterStage) {
		if(this.getBrain().getState() == EntityState.ATTACK && counterStage == ATTACK_COUNTER_STAGE) this.attack(this.getBrain().getTarget());
	}
	public int getSpriteMeta() {
		return spriteMeta;
	}
	public void setSpriteMeta(int spriteMeta) {
		this.spriteMeta = spriteMeta;
		this.spriteMeta %= this.spriteCounterTriggers.length;
		
		this.requestImageUpdate();
	}
	public AnimationState getAnimationState() {
		return this.animationState;
	}
	public void setAnimationState(AnimationState state) {
		if(this.animationState != state) {
			this.animationState = state;
			this.stopSpriteCounting = false;
			this.setSpriteMeta(0);
			this.requestImageUpdate();
			this.onAnimationStateChanged(this.animationState);
		}
	}
	public abstract void onAnimationStateChanged(AnimationState state);
	
	public void setFacingRight() {
		if(!this.facingRight) requestImageUpdate();
		this.facingRight = true;
	}
	public void setFacingLeft() {
		if(this.facingRight) requestImageUpdate();
		this.facingRight = false;
	}
	public boolean isFacingRight() {
		return this.facingRight;
	}
	public boolean isFacingLeft() {
		return !this.facingRight;
	}
	
	public void showHealthBar() {
		this.healthCounter = this.healthCounterMax;
	}
	public boolean canRenderHealthBar() {
		return this.healthCounter > 0;
	}
	public BufferedImage getHealthBarImage() {
		BufferedImage hbImg = super.getHealthBarImage();
		BufferedImage bi = new BufferedImage(hbImg.getWidth(), hbImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		float fade = (float) (healthCounter > healthFadeCounter ? 1.0F : MathUtil.map(healthCounter, healthFadeCounter, 0, 1.0F, 0.0F));
		
		Graphics2D g2d = bi.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fade));
		g2d.drawImage(hbImg, 0, 0, null);
		g2d.dispose();
		
		return bi;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public BufferedImage getBaseImage() {
		return Images.getSubImage(Entity.ENTITY_MAP, Entity.ENTITY_SIZE, this.animationState.getSpriteStart() + this.spriteMeta, mapY);
	}
	public BufferedImage getOverlayImage() {
		return Images.getSubImage(Entity.ENTITY_MAP, Entity.ENTITY_SIZE, this.animationState.getSpriteStart() + this.spriteMeta, mapY + 1);
	}
	public BufferedImage getRawImage() {
		if(this.image == null) {
			BufferedImage base = getBaseImage();
			BufferedImage over = Images.recolour(getOverlayImage(), this.color);
			this.image = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = this.image.createGraphics();
			if(base != null) g2d.drawImage(base, 0, 0, null);
			if(over != null) g2d.drawImage(over, 0, 0, null);
			g2d.dispose();
			if(isFacingLeft()) {
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-this.image.getWidth(), 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				this.image = op.filter(this.image, null);
			}
		}
		return this.image;
	}
	public BufferedImage getImage() {
		return getRawImage();
	}
	public Color getColor() {
		return this.color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public BCEntity getOwner() {
		return this;
	}
	
	public static enum Type {
		WARRIOR(0, 0, 0.05F, 1, 1),
		MAGICIAN(1, 2, 0.045F, 7, 0.7F),
		ARCHER(2, 4, 0.055F, 11.5F, 0.45F),
		DUMMY(99, 6, 1, 0, 0);
		
		public final int id;
		public final int tileMapY;
		public final float speed;
		public final float attackDistance;
		public final float attackDamage;
		
		private Type(int id, int tileMapY, float speed, float attackDistance, float attackDamage) {
			this.id = id;
			this.tileMapY = tileMapY;
			this.speed = speed;
			this.attackDistance = attackDistance;
			this.attackDamage = attackDamage;
		}
	}
	public static enum AnimationState {
		IDLE(0, 0, 2, true),
		WALK(1, 2, 2, true),
		ATTACK(2, 4, 2, true),
		DEAD(3, 6, 2, false);
		
		public final int stateID;
		public final int spriteStart;
		public final int spriteCount;
		public final boolean spriteDoesLoop;
		
		private AnimationState(int stateID, int spriteStart, int spriteCount, boolean spriteDoesLoop) {
			this.stateID = stateID;
			this.spriteCount = spriteCount;
			this.spriteStart = spriteStart;
			this.spriteDoesLoop = spriteDoesLoop;
		}
		
		public int getStateID() {
			return stateID;
		}
		public int getSpriteStart() {
			return spriteStart;
		}
		public int getSpriteCount() {
			return spriteCount;
		}
	}
}
