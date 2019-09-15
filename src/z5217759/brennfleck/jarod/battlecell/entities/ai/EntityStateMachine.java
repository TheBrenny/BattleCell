package z5217759.brennfleck.jarod.battlecell.entities.ai;

import java.util.ArrayList;

import com.thebrenny.jumg.entities.Entity;
import com.thebrenny.jumg.entities.ai.pathfinding.NodeList;
import com.thebrenny.jumg.entities.ai.pathfinding.PathFinding;
import com.thebrenny.jumg.entities.ai.states.DefaultStateMachine;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity;

public abstract class EntityStateMachine extends DefaultStateMachine<BCEntity, EntityState> {
	public static final int PATH_LIFE = 30;
	
	private BCEntity target;
	private NodeList pathToTarget;
	private int pathLifeLeft = -1;
	
	public EntityStateMachine(BCEntity owner) {
		super(owner, EntityState.IDLE, EntityState.GLOBAL);
	}
	
	public void setNextTarget() {
		// TODO: add in the modifier values!
		
		ArrayList<Entity> entities = new ArrayList<Entity>(this.getOwner().getLevel().getNearbyEntities(this.getOwner().getTileX(), this.getOwner().getTileY(), 0, this.getOwner()));
		Entity e;
		BCEntity bcE;
		for(int i = 0; i < entities.size(); i++) {
			e = entities.get(i);
			if(e instanceof BCEntity) {
				bcE = (BCEntity) e;
				if(bcE.isAlive()) this.target = bcE;
			}
		}
	}
	public boolean hasTarget() {
		return this.target != null;
	}
	public void setTarget(BCEntity target) {
		this.target = target;
	}
	public BCEntity getTarget() {
		return this.target;
	}
	public abstract float getTargetDistance();
	public float getTargetDistanceSqrd() {
		return getTargetDistance() * getTargetDistance();
	}
	public synchronized NodeList getPathToTarget() {
		if(pathToTarget == null) {
			PathFinding pf = new PathFinding(this.getOwner().getAnchoredTileLocation(), this.getTarget().getAnchoredTileLocation(), this.getOwner().getLevel());
			this.pathToTarget = pf.search(true);
			this.pathLifeLeft = PATH_LIFE;
		}
		return pathToTarget;
	}
	
	public void tick() {
		super.tick();
		if(pathLifeLeft > 0) this.pathLifeLeft--;
		else if(pathLifeLeft == 0) {
			this.pathToTarget = null;
			this.pathLifeLeft = -1;
		}
	}
	
}
