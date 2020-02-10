package z5217759.brennfleck.jarod.battlecell.gui;

import java.awt.Graphics2D;

import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenMenu;
import com.thebrenny.jumg.gui.components.GuiButton;
import com.thebrenny.jumg.gui.components.GuiLabel;

/**
 * ScreenMenuServerClosed
 */
public class ScreenMenuServerConnectionLost extends ScreenMenu {
	public ScreenMenuServerConnectionLost(String reason) {
		addComponent(new GuiLabel(Screen.getWidth() / 2, Screen.getHeight() / 2 - 20, "Connection with server lost: " + reason).align(GuiLabel.ALIGN_CENTRE, GuiLabel.ALIGN_VERTICAL_BOTTOM).setFont(GuiLabel.TITLE_FONT));
		addComponent(new GuiButton(Screen.getWidth() / 2 - 100, Screen.getHeight() / 2 + 20, 200, 40, "Back", new Runnable() {
			public void run() {
				Screen.screenBack();
			}
		}));
	}
	
	public void tick() {}
	public void render(Graphics2D g2d) {}
}
