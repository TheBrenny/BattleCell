package z5217759.brennfleck.jarod.battlecell.entities;

public class EntityArrow extends EntityProjectile {
	public EntityArrow(float x, float y, BCEntity owner, BCEntity target) {
		super(x, y, Type.ARROW, owner, target);
	}
}
