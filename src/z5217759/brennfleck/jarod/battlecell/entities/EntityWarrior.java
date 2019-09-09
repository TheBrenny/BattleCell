package z5217759.brennfleck.jarod.battlecell.entities;

public class EntityWarrior extends BCEntity {
	int countSwitch = 0;
	
	public EntityWarrior(String name, float x, float y) {
		super(name, x, y, BCEntity.Type.WARRIOR);
		setCounterTrigger(20, 40);
		this.setState(State.WALK);
	}
	
	public void update() {
		if(countSwitch == 10) {
			this.setState(State.IDLE);
		} else if(countSwitch == 20) {
			this.setState(State.WALK);
			countSwitch = 0;
		}
	}
	public void counterEvent(int counter, int counterStage) {
		incrementSprite();
		countSwitch++;
		
		if(countSwitch % 5 == 0) {
			if(this.isFacingRight()) this.setFacingLeft();
			else if(this.isFacingLeft()) this.setFacingRight();
		}
	}
}
