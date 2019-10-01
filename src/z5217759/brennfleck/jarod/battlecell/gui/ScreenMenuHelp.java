package z5217759.brennfleck.jarod.battlecell.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenMenu;
import com.thebrenny.jumg.gui.components.GuiButton;
import com.thebrenny.jumg.gui.components.GuiLabel;
import com.thebrenny.jumg.util.Images;
import com.thebrenny.jumg.util.Logger;

import z5217759.brennfleck.jarod.battlecell.BattleCell;

public class ScreenMenuHelp extends ScreenMenu {
	public ScreenMenuHelp() {
		super(Images.getImage("background"));
		Logger.startSection("menuHelpInit", "Setting up the help menu screen.");
		
		int width = BattleCell.getMainGame().getDisplay().getWidth();
		int height = BattleCell.getMainGame().getDisplay().getHeight();
		
		addComponent(new GuiLabel(width / 2, height / 12 * 2, ScreenMenuHelp.getHelpContent()).align("centre", "top").adjustFont(24F).setColor(Color.WHITE));
		addComponent(new GuiButton(width / 2 - 100, height / 12 * 10, 200, 50, "Back", new Runnable() {
			public void run() {
				Screen.screenBack();
			}
		}));
		
		Logger.endSection("menuHelpInit", "Help menu screen has been set up.");
	}
	
	public void tick() {}
	public void render(Graphics2D g2d) {}
	
	public static final String getHelpContent() {
		//@formatter:off
		String[] content = {
			"All you gotta do is pick your hero, pick their talents, pick their attack...",
			"",
			"AND OFF THEY GO!"
		};
		//@formatter:on
		return String.join("\n", content);
	}
}
