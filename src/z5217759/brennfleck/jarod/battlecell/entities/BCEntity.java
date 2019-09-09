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
import com.thebrenny.jumg.util.Images;
import com.thebrenny.jumg.util.MathUtil;

public abstract class BCEntity extends EntityLiving {
	protected Color color;
	protected State state;
	protected boolean facingRight;

	protected int spriteMeta = 0;
	protected int spriteCounter = 0;
	protected int[] spriteCounterTriggers = {0};
	
	protected int healthCounter = 0;
	protected int healthCounterMax = 60;
	protected int healthFadeCounter = 15;
	
	private boolean isControlled = false;
	
	public BCEntity(String name, float x, float y, Type type) {
		this(name, x, y, type, new Color(new Random().nextInt(0x1000000))); // 0xFFFFFF is the max integer for a colour,
																			// and nextInt is exclusive
	}
	public BCEntity(String name, float x, float y, Type type, Color color) {
		super(name, type.id, x, y, 0, type.tileMapY, type.speed, 1.0F);
		setAnchor((float) (Entity.ENTITY_SIZE / 2), (float) (Entity.ENTITY_SIZE / 2));
		this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
		this.state = State.IDLE;
	}
	public BCEntity setControlled(boolean isControlled) {
		this.isControlled = isControlled;
		return this;
	}
	
	public boolean isControlled() {
		return this.isControlled;
	}
	
	public final void tick() {
		updateCounters();
		update();
	}
	public abstract void update();
	
	public void setCounterTrigger(int ... counters) {
		this.spriteCounterTriggers = counters;
	}
	
	public void updateCounters() {
		if(spriteCounterTriggers[0] > 0) {
			this.spriteCounter++;
			for(int i = 0; i < spriteCounterTriggers.length; i++) {
				if(spriteCounter == spriteCounterTriggers[i]) {
					counterEvent(this.spriteCounter, (i + 1) % spriteCounterTriggers.length);
					if(i == spriteCounterTriggers.length - 1) this.spriteCounter = 0;
				}
				
			}
		}
		
		if(healthCounter > 0) this.healthCounter--;
		this.healthCounter = this.healthCounter < 0 ? 0 : this.healthCounter;
	}
	
	/**
	 * Called whenever the sprite counter hits a counter trigger as set by
	 * {@link #setCounterTrigger(int...)}.
	 * 
	 * @param counter
	 *        - The number of the counter.
	 * @param counterStage
	 *        - Which stage was triggered. 0 is reset.
	 */
	public void counterEvent(int counter, int counterStage) {
	}
	
	public State getState() {
		return this.state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
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

	public int getSpriteMeta() {
		return spriteMeta;
	}
	public void incrementSprite() {
		this.spriteMeta++;
		this.setSpriteMeta(spriteMeta);
	}
	public void setSpriteMeta(int spriteMeta) {
		this.spriteMeta = spriteMeta;
		this.spriteMeta %= 2;
		
		this.requestImageUpdate();
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
		
		float fade = (float) (healthCounter > healthFadeCounter ? 1.0F : MathUtil.map(healthCounter, healthFadeCounter, 0, 0.0F, 1.0F));
		
		Graphics2D g2d = bi.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fade));
		g2d.drawImage(hbImg, 0, 0, null);
		g2d.dispose();
		
		return bi;
	}
	
	public BufferedImage getBaseImage() {
		return Images.getSubImage(Entity.ENTITY_MAP, Entity.ENTITY_SIZE, this.state.getSpriteStart() + this.spriteMeta, mapY);
	}
	public BufferedImage getOverlayImage() {
		return Images.getSubImage(Entity.ENTITY_MAP, Entity.ENTITY_SIZE, this.state.getSpriteStart() + this.spriteMeta, mapY + 1);
	}
	public BufferedImage getRawImage() {
		if(this.image == null) {
			BufferedImage base = getBaseImage();
			BufferedImage over = Images.recolour(getOverlayImage(), this.color);
			this.image = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = this.image.createGraphics();
			g2d.drawImage(base, 0, 0, null);
			g2d.drawImage(over, 0, 0, null);
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
	
	public Color getColor() {
		return this.color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public static enum Type {
		WARRIOR(0, 0, 1.1F), MAGE(1, 2, 0.9F), ARCHER(2, 4, 1.1F);
		
		public final int id;
		public final int tileMapY;
		public final float speed;
		
		private Type(int id, int tileMapY, float speed) {
			this.id = id;
			this.tileMapY = tileMapY;
			this.speed = speed;
		}
	}
	public static enum State {
		IDLE(0, 0, 2), WALK(1, 2, 2), ATTACK(2, 4, 2), DEAD(3, 6, 1);
		
		public final int stateID;
		public final int spriteStart;
		public final int spriteCount;
		
		private State(int stateID, int spriteStart, int spriteCount) {
			this.stateID = stateID;
			this.spriteCount = spriteCount;
			this.spriteStart = spriteStart;
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
