package z5217759.brennfleck.jarod.battlecell.gui;

import java.awt.Graphics2D;

import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenMenu;
import com.thebrenny.jumg.gui.components.GuiButton;
import com.thebrenny.jumg.gui.components.GuiImageLabel;
import com.thebrenny.jumg.gui.components.GuiLabel;
import com.thebrenny.jumg.util.Images;
import com.thebrenny.jumg.util.Logger;

import z5217759.brennfleck.jarod.battlecell.BattleCell;

public class ScreenMenuMain extends ScreenMenu {
	public ScreenMenuMain() {
		super(Images.getImage("background"));
		Logger.startSection("menuMainInit", "Setting up the main menu screen.");
		
		int width = BattleCell.getMainGame().getDisplay().getWidth();
		int height = BattleCell.getMainGame().getDisplay().getHeight();
		
		addComponent(new GuiImageLabel(width / 2, height / 12 * 3, Images.getImage("title")).align(GuiLabel.ALIGN_CENTRE));
		addComponent(new GuiButton(width / 2 - 125, height / 12 * 6, 250, 50, "Start Game!", new Runnable() {
			public void run() {
				//Screen.screenForward(new ScreenGameBattleCell());
				Screen.screenForward(new ScreenMenuNewGame());
				// Screen forward to lobby
				// Then to battle (with prepare for battle hud)
				// Then HUD disappears when all players ready.
				// Then game fight after countdown
				// Then rewards are reaped
				// Coin for how fast you win - coin used to purchase gucci looks
			}
		}));
		addComponent(new GuiButton(width / 2 - 125, height / 12 * 8, 250, 50, "Help", new Runnable() {
			public void run() {
				Screen.screenForward(new ScreenMenuHelp());
			}
		}));
		addComponent(new GuiButton(width / 2 - 125, height / 12 * 10, 250, 50, "Quit", new Runnable() {
			public void run() {
				BattleCell.getMainGame().stop();
			}
		}));
		
		Logger.endSection("menuMainInit", "Main menu screen has been set up.");
	}
	
	public void tick() {}
	public void render(Graphics2D g2d) {}
}
