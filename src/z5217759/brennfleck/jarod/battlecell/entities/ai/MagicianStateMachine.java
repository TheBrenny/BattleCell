package z5217759.brennfleck.jarod.battlecell.entities.ai;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity.Type;
import z5217759.brennfleck.jarod.battlecell.entities.EntityMagician;

public class MagicianStateMachine extends EntityStateMachine {
    public MagicianStateMachine(EntityMagician owner) {
        super(owner);
    }
    
    public EntityMagician getOwner() {
        return (EntityMagician) super.getOwner();
    }
    
    public float getTargetDistance() {
        return Type.MAGICIAN.attackDistance;
    }
}
