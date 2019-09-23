package z5217759.brennfleck.jarod.battlecell.gui;

import java.awt.geom.Point2D;
import java.util.Random;

import com.thebrenny.jumg.entities.EntityLiving;
import com.thebrenny.jumg.entities.messaging.Message;
import com.thebrenny.jumg.entities.messaging.MessageListener;
import com.thebrenny.jumg.entities.messaging.Postman;
import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenGame;
import com.thebrenny.jumg.input.KeyBindings;
import com.thebrenny.jumg.util.MathUtil;
import com.thebrenny.jumg.util.TimeUtil;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity;
import z5217759.brennfleck.jarod.battlecell.entities.EntityArcher;
import z5217759.brennfleck.jarod.battlecell.entities.EntityMagician;
import z5217759.brennfleck.jarod.battlecell.entities.EntityWarrior;
import z5217759.brennfleck.jarod.battlecell.hud.BCHudManager;
import z5217759.brennfleck.jarod.battlecell.level.BattleCellLevel;

public class ScreenGameBattleCell extends ScreenGame implements MessageListener<ScreenGameBattleCell> {
	private long gameFinishedTime = -1;
	private long timeUntilReset = 10000;
	private BCEntity player;
	
	public ScreenGameBattleCell() { // TODO: You'll probably need to pass an array of players.
		super(new BattleCellLevel(), new BCHudManager(Screen.getWidth(), Screen.getHeight()));
		
		int characters = 60;
		
		setCameraX(-Screen.getWidth() / 2);
		setCameraY(-Screen.getHeight() / 2);
		
		// TODO: TESTING!
		// this.player = allPlayers[0].setControlled(true);
		this.player = newRandomEntity().setControlled(true);
		this.level.addEntity(player);
		for(int i = 1; i < characters; i++) this.level.addEntity(newRandomEntity());
		this.setEntityToFollow(player);
		this.moveCamera();
	}
	
	public void moveCamera() {
		if(KeyBindings.isPressed("cam_left") || KeyBindings.isPressed("cam_right") || KeyBindings.isPressed("cam_up") || KeyBindings.isPressed("cam_down")) this.entityToFollow = null;
		if(this.entityToFollow != null) super.moveCamera();
		else {
			long camX = (long) (KeyBindings.isPressed("cam_left") ? -1 : KeyBindings.isPressed("cam_right") ? 1 : 0);
			long camY = (long) (KeyBindings.isPressed("cam_up") ? -1 : KeyBindings.isPressed("cam_down") ? 1 : 0);
			long camMoveX = (long) (camX * Screen.getCameraSpeed());
			long camMoveY = (long) (camY * Screen.getCameraSpeed());
			
			long newX = (long) MathUtil.clamp(-getMapWidth() / 2, getCameraX() + camMoveX, (getMapWidth() / 2) - getWidth());
			long newY = (long) MathUtil.clamp(-getMapHeight() / 2, getCameraY() + camMoveY, (getMapHeight() / 2) - getHeight());
			
			setCameraX(newX);
			setCameraY(newY);
		}
		
		if(KeyBindings.isPressed("cam_reset")) this.setEntityToFollow(player);
	}
	
	public BCEntity newRandomEntity() {
		BCEntity e = null;
		
		float x = MathUtil.random(getLevel().topLeftTile().x+1, getLevel().bottomRightTile().x-1);
		float y = MathUtil.random(getLevel().topLeftTile().y+1, getLevel().bottomRightTile().y-1);
		
		Point2D.Float p = (Point2D.Float) getLevel().getNearestWalkableTile(x, y);
		x = p.x;
		y = p.y;
		
		int w = MathUtil.random(0, BCEntity.DEFENSE_MAX + 1);
		int m = MathUtil.random(0, BCEntity.DEFENSE_MAX + 1 - w);
		int a = BCEntity.DEFENSE_MAX + 1 - w - m;
		int r = new Random().nextInt(3);
		if(r == 0) e = new EntityWarrior("genUID:WAR", x, y);
		else if(r == 1) e = new EntityMagician("genUID:MAG", x, y);
		else if(r == 2) e = new EntityArcher("genUID:ARC", x, y);
		
		return e.setDefensiveTraits(w, m, a);
	}
	
	public void update() {
		Postman.getInstance().update();
		super.update();
		EntityLiving[] alive;
		if((alive = this.getLevel().getAliveEntities()).length == 1) {
			// game over
			if(alive[0] == this.player); // GG YOU WIN!
			if(gameFinishedTime == -1) this.gameFinishedTime = TimeUtil.getEpoch();
			if(TimeUtil.getElapsed(this.gameFinishedTime) > this.timeUntilReset) {
				// TODO: reset back to screen lobby
			}
		}
	}
	
	public void handleMessage(Message message) {
		// TODO: Handle playing sounds. Lower volume based on camera location
	}
	public ScreenGameBattleCell getOwner() {
		return this;
	}
}
