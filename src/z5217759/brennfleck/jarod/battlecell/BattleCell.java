package z5217759.brennfleck.jarod.battlecell;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import com.thebrenny.jumg.MainGame;
import com.thebrenny.jumg.input.KeyBindings;
import com.thebrenny.jumg.level.tiles.Tile;
import com.thebrenny.jumg.util.Images;
import com.thebrenny.jumg.util.Logger;

import z5217759.brennfleck.jarod.battlecell.gui.ScreenMenuMain;
import z5217759.brennfleck.jarod.battlecell.level.tile.BCTile;

/**
 * This is the main game.
 *
 * @author z5217759 - Jarod Brennfleck
 * @assessmentItem Computer Games - 17. Generic Odyssey - 2D Game using Java
 * @date 01 Aug 19
 *
 */
public class BattleCell extends MainGame {
	private String username;
	private boolean debugDown = false;
	
	public BattleCell(String[] args) {
		super(args, "BattleCell", "z5217759.brennfleck.jarod.battlecell");
		this.setUsername(System.getProperty("user.name", "Player" + new Random().nextLong()));
		this.setCursor(this.getCursor());
	}
	
	protected void loadAI() {}
	protected void loadImages() {
		String loc = getGameInfo().packageRoot() + ".res";
		Images.addImage(loc, "cursor");
		Images.addImage(loc, "tile_map");
		Images.addImage(loc, "title");
		Images.addImage(loc, "background");
	}
	protected void loadItems() {}
	protected void loadTiles() {
		for(BCTile.Type tileType : BCTile.Type.values()) {
			Tile.registerTile(new BCTile(tileType));
		}
	}
	protected void setupKeyBinds() {
		KeyBindings.addKey("main_click", MouseEvent.BUTTON1, false, false);
		KeyBindings.addKey("cam_up", KeyEvent.VK_W, false, true);
		KeyBindings.addKey("cam_down", KeyEvent.VK_S, false, true);
		KeyBindings.addKey("cam_left", KeyEvent.VK_A, false, true);
		KeyBindings.addKey("cam_right", KeyEvent.VK_D, false, true);
		// Just a click and let loose game?
	}
	
	public boolean initialise() {
		Logger.log("Setting screen to main menu.");
		getScreenManager().setScreen(new ScreenMenuMain());
		return true;
	}
	
	@Override
	public void update() {
		super.update();
		if(KeyBindings.isPressed("debug")) {
			if(!debugDown) {
				
				debugDown = true;
				this.debugging = !this.debugging;
				Logger.log("Visual debugging is [{0}].", (Object) (this.debugging ? "on" : "off"));
			}
		} else {
			debugDown = false;
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public static BattleCell getMainGame() {
		return (BattleCell) MainGame.getMainGame();
	}
	
	public static void main(String[] args) {
		new BattleCell(args);
	}
	
	public final Cursor getCursor() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Cursor c = tk.createCustomCursor(Images.getImage("cursor"), new Point(0, 0), "game_cursor");
		return c;
	}
}