package z5217759.brennfleck.jarod.battlecell.entities;

import java.awt.image.BufferedImage;

import com.thebrenny.jumg.entities.EntityMoving;
import com.thebrenny.jumg.util.Angle;
import com.thebrenny.jumg.util.Images;
import com.thebrenny.jumg.util.MathUtil;

public class EntityProjectile extends EntityMoving {
	private static final float PROJECTILE_IMPACT_RADIUS = 0.1F;
	public static BufferedImage ENTITY_MAP = Images.getImage("small_entity_map");
	public static int ENTITY_SIZE = 16;
	
	public static final int PROJECTILE_STATE_MOVING = 0;
	public static final int PROJECTILE_STATE_HIT = 1;
	
	protected BCEntity owner;
	protected BCEntity target;
	protected int projectileState;
	
	public EntityProjectile(float x, float y, Type type, BCEntity owner, BCEntity target) {
		super("genUID:" + type.name(), type.id, x, y, type.tileMapX, type.tileMapY, type.speed);
		this.owner = owner;
		this.target = target;
		this.setAngle(Angle.getAngle(this.getAnchoredTileLocation(), target.getAnchoredTileLocation()));
		this.setState(PROJECTILE_STATE_MOVING);
		this.setSize((float) ENTITY_SIZE, (float) ENTITY_SIZE);
		this.setAnchor((float) ENTITY_SIZE / 2, (float) ENTITY_SIZE / 2);
	}
	
	public void tick() {
		if(MathUtil.distanceSqrd(target.getAnchoredTileLocation(), this.getAnchoredTileLocation()) <= PROJECTILE_IMPACT_RADIUS) {
			// send message to hurt target. (call owner to "hurt(target, damage)")
			this.setState(PROJECTILE_STATE_HIT);
			this.getLevel().removeEntity(this);
			this.owner.getBrain().actuallyHurtTarget();
		} else {
			this.setAngle(Angle.getAngle(this.getAnchoredTileLocation(), target.getAnchoredTileLocation()));
			this.addMovementMove();
			this.move();
		}
	}
	
	public void setState(int state) {
		this.projectileState = state;
	}
	
	public BufferedImage getRawImage() {
		if(this.image == null) this.image = Images.getSubImage(EntityProjectile.ENTITY_MAP, EntityProjectile.ENTITY_SIZE, mapX, mapY);
		return this.image;
	}
	
	public static enum Type {
		MAGIC_BALL(100, 0, 0, 0.085F), ARROW(101, 1, 0, 0.1F);
		
		public final int id;
		public final int tileMapX;
		public final int tileMapY;
		public final float speed;
		
		private Type(int id, int tileMapX, int tileMapY, float speed) {
			this.id = id;
			this.tileMapX = tileMapX;
			this.tileMapY = tileMapY;
			this.speed = speed;
		}
	}
}
