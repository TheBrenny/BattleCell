package z5217759.brennfleck.jarod.battlecell.entities;

import z5217759.brennfleck.jarod.battlecell.entities.ai.MagicianStateMachine;

public class EntityMagician extends BCEntity {
    public static final int[] IDLE_COUNTER_TRIGGERS = DEFAULT_IDLE_COUNTER_TRIGGERS;
    public static final int[] WALK_COUNTER_TRIGGERS = DEFAULT_WALK_COUNTER_TRIGGERS;
    public static final int[] ATTACK_COUNTER_TRIGGERS = {60, 80};
    public static final int[] DEAD_COUNTER_TRIGGERS = {50};
    
    public EntityMagician(String name, float x, float y) {
        super(name, x, y, BCEntity.Type.MAGICIAN);
        setBrain(new MagicianStateMachine(this));
    }

    public void attack(BCEntity target) {
        this.getLevel().addEntity(new EntityMagicBall(this.getAnchoredTileX(), this.getAnchoredTileY(), this, this.getBrain().getTarget()));
    }
    public void update() {
	}
    
    public void onAnimationStateChanged(AnimationState state) {
        switch(state) {
        case IDLE:
            setCounterTrigger(IDLE_COUNTER_TRIGGERS);
            break;
        case WALK:
            setCounterTrigger(WALK_COUNTER_TRIGGERS);
            break;
        case ATTACK:
            setCounterTrigger(ATTACK_COUNTER_TRIGGERS);
            break;
        case DEAD:
            setCounterTrigger(DEAD_COUNTER_TRIGGERS);
            break;
        }
    }
}
