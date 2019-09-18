package z5217759.brennfleck.jarod.battlecell.entities.ai;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.thebrenny.jumg.entities.Entity;
import com.thebrenny.jumg.entities.ai.pathfinding.Node;
import com.thebrenny.jumg.entities.ai.pathfinding.NodeList;
import com.thebrenny.jumg.entities.ai.states.State;
import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.level.tiles.Tile;
import com.thebrenny.jumg.util.Angle;
import com.thebrenny.jumg.util.MathUtil;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity;
import z5217759.brennfleck.jarod.battlecell.entities.BCEntity.AnimationState;

public enum EntityState implements State<BCEntity> {
	IDLE(AnimationState.IDLE) {
		@Override
		public void tick(BCEntity entity) {
			EntityStateMachine brain = entity.getBrain();
			
			brain.setNextTarget();
			
			if(brain.hasTarget()) brain.changeState(EntityState.PERSUE);
		}
	},
	PERSUE(AnimationState.WALK) {
		public void tick(BCEntity entity) {
			EntityStateMachine brain = entity.getBrain();
			
			if(brain.hasTarget() && brain.getTarget().isAlive()) {
				NodeList path = brain.getPathToTarget();
				if(path != null) {
					@SuppressWarnings("unused")
					int proximTest = path.testStepReached(entity.getAnchoredTileX(), entity.getAnchoredTileY(), NodeList.DEFAULT_TEST_DISTANCE);
					if(brain.getTargetDistance() >= MathUtil.distance(entity.getAnchoredTileLocation(), brain.getTarget().getAnchoredTileLocation())) brain.changeState(EntityState.ATTACK);
					else entity.addMovementTo(path.getCurrentStep());
				}
			} else brain.changeState(EntityState.IDLE);
		}
		public void renderDebug(BCEntity entity, Graphics2D g2d, long camX, long camY, long camW, long camH) {
			super.renderDebug(entity, g2d, camX, camY, camW, camH);
			
			EntityStateMachine brain = entity.getBrain();
			NodeList path = brain.getPathToTarget();
			if(path != null) {
				int pathPointSize = 6;
				for(int i = 0; i < path.size(); i++) {
					Node s = path.get(i);
					g2d.setColor(Color.BLUE);
					if(i == path.size() - 1) g2d.setColor(Color.GREEN);
					g2d.drawOval((int) (s.getPoint().x * Tile.TILE_SIZE - camX - pathPointSize / 2), (int) (s.getPoint().y * Tile.TILE_SIZE - camY - pathPointSize / 2), pathPointSize, pathPointSize);
					if(i < path.size() - 1) {
						Node d = path.get(i + 1);
						g2d.setColor(Color.CYAN);
						g2d.drawLine((int) (s.getPoint().x * Tile.TILE_SIZE - camX), (int) (s.getPoint().y * Tile.TILE_SIZE - camY), (int) (d.getPoint().x * Tile.TILE_SIZE - camX), (int) (d.getPoint().y * Tile.TILE_SIZE - camY));
					}
				}
			}
		}
	},
	ATTACK(AnimationState.ATTACK) {
		public void tick(BCEntity entity) {
			EntityStateMachine brain = entity.getBrain();
			
			if(brain.hasTarget()) {
				if(brain.getTargetDistance() >= MathUtil.distance(entity.getAnchoredTileLocation(), brain.getTarget().getAnchoredTileLocation())) {
					entity.setAngle(Angle.getAngle(entity.getAnchoredTileLocation(), brain.getTarget().getAnchoredTileLocation()));
					// entity.attack(brain.getTarget()); // This is actually called in BCEntity#counterEvent(int).
				} else brain.changeState(EntityState.PERSUE);
				if(!brain.getTarget().isAlive()) {
					brain.setTarget(null);
					brain.changeState(EntityState.IDLE);
				}
			} else brain.changeState(EntityState.IDLE);
		}
	},
	DEAD(AnimationState.DEAD) {
		public void tick(BCEntity entity) {
			/*
			 * wait until game is reset
			 */
		}
	},
	GLOBAL(null) {
		public void tick(BCEntity entity) {
			if(!entity.isAlive()) entity.getBrain().changeState(EntityState.DEAD);
		}
	};
	
	private AnimationState animationState;
	
	private EntityState(AnimationState animationState) {
		this.animationState = animationState;
	}
	
	public void enter(BCEntity entity) {
		entity.setAnimationState(this.animationState);
	}
	public void exit(BCEntity entity) {
	}
	public void renderDebug(BCEntity entity, Graphics2D g2d, long camX, long camY, long camW, long camH) {
		if(Screen.canCameraSeeEntity(entity)) {
			g2d.setFont(new Font("Consolas", Font.PLAIN, 10));
			FontMetrics mets = g2d.getFontMetrics();
			
			Rectangle2D bb = entity.getBoundingBox();
			int eX = (int) entity.getAnchoredX();
			final long cx = Screen.getCameraX();
			final long cy = Screen.getCameraY();
			
			g2d.drawString(this.toString(), (int) (eX - (mets.stringWidth(this.toString()) / 2) - cx), (int) (bb.getY() + Entity.ENTITY_SIZE + (mets.getMaxAscent() * 2) - cy));
		}
	}
}
