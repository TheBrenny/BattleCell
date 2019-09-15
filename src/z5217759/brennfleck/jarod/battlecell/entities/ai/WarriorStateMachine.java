package z5217759.brennfleck.jarod.battlecell.entities.ai;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity.Type;
import z5217759.brennfleck.jarod.battlecell.entities.EntityWarrior;

public class WarriorStateMachine extends EntityStateMachine {
	public WarriorStateMachine(EntityWarrior owner) {
		super(owner);
	}
	
	public EntityWarrior getOwner() {
		return (EntityWarrior) super.getOwner();
	}
	
	public float getTargetDistance() {
		return Type.WARRIOR.attackDistance;
	}
}
