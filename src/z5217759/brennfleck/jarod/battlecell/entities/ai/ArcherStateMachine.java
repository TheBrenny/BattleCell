package z5217759.brennfleck.jarod.battlecell.entities.ai;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity.Type;
import z5217759.brennfleck.jarod.battlecell.entities.EntityArcher;

public class ArcherStateMachine extends BCStateMachine {
    public ArcherStateMachine(EntityArcher owner) {
        super(owner);
    }
    
    public EntityArcher getOwner() {
        return (EntityArcher) super.getOwner();
    }
    
    public float getTargetDistance() {
        return Type.ARCHER.attackDistance;
    }
}
