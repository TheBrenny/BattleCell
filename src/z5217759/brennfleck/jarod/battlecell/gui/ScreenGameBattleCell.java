package z5217759.brennfleck.jarod.battlecell.gui;

import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenGame;
import com.thebrenny.jumg.input.KeyBindings;
import com.thebrenny.jumg.level.tiles.Tile;
import com.thebrenny.jumg.util.MathUtil;

import z5217759.brennfleck.jarod.battlecell.BattleCell;
import z5217759.brennfleck.jarod.battlecell.entities.EntityArcher;
import z5217759.brennfleck.jarod.battlecell.entities.EntityMagician;
import z5217759.brennfleck.jarod.battlecell.entities.EntityWarrior;
import z5217759.brennfleck.jarod.battlecell.hud.BCHudManager;
import z5217759.brennfleck.jarod.battlecell.level.BattleCellLevel;

public class ScreenGameBattleCell extends ScreenGame {
	public ScreenGameBattleCell() {
		super(new BattleCellLevel(), new BCHudManager(Screen.getWidth(), Screen.getHeight()));
		setCameraX(-Screen.getWidth() / 2);
		setCameraY(-Screen.getHeight() / 2);
		
		// TODO: TESTING!
		EntityMagician player = (EntityMagician) new EntityMagician(BattleCell.getMainGame().getUsername(), 0, 0).setControlled(true);
		this.level.addEntity(player);
		this.level.addEntity(new EntityWarrior("WARRIOR", 10, -11));
		this.level.addEntity(new EntityArcher("ARCHER", -10, -11));
		this.setEntityToFollow(player);
		this.moveCamera();
	}
	
	public void moveCamera() {
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
	}
	
	
}
