package z5217759.brennfleck.jarod.battlecell.entities;

import java.awt.geom.Point2D;

public class EntityDummy extends BCEntity {
	public EntityDummy(String name, float x, float y) {
		super(name, x, y, Type.DUMMY);
		setCounterTrigger(new int[] {20, 40});
	}
	public void update() {
	}
	
	public void setLocation(Point2D location) {
		this.boundingBox.x = (float) location.getX();
		this.boundingBox.y = (float) location.getY();
	}

	public EntityDummy changeType(BCEntity.Type type) {
		this.type = type;
		this.mapY = type.tileMapY;
		this.requestImageUpdate();
		return this;
	}
	
	public void onAnimationStateChanged(AnimationState state) {
	}
	public void attack(BCEntity target) {
	}
}
