package z5217759.brennfleck.jarod.battlecell.gui;

import java.awt.Graphics2D;

import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenMenu;
import com.thebrenny.jumg.gui.components.GuiButton;
import com.thebrenny.jumg.gui.components.GuiLabel;
import com.thebrenny.jumg.gui.components.GuiTextBox;
import com.thebrenny.jumg.util.Images;

import z5217759.brennfleck.jarod.battlecell.BattleCell;

public class ScreenMenuNewGame extends ScreenMenu {
	public ScreenMenuNewGame() {
		super(Images.getImage("background"));
		// Will change to Images.getImage("game_background");

		int width = BattleCell.getMainGame().getDisplay().getWidth();
		int height = BattleCell.getMainGame().getDisplay().getHeight();

		addComponent(new GuiLabel(width / 2, height / 12 * 3, "Start or Join a game").setFont(GuiLabel.TITLE_FONT).align(GuiLabel.ALIGN_CENTRE));

		addComponent(new GuiButton(width / 2 - 125, height / 12 * 7, 250, 50, "Create New Game", new Runnable() {
			public void run() {
				// TODO: Next up!
				// Create a Game Server and open up the game lobby (passing the server object as a param)
				int gameServer = -1; // This will change to the actual game server object!
				Screen.screenForward(new ScreenMenuLobby(gameServer));
			}
		}));
		addComponent(new GuiButton(width / 2 + 75, height / 12 * 8, 50, 50, "Join", new Runnable() {
			public void run() {
				// TODO:
				// Open a game client and send a request for game details,
				// Game client will collect info and pass back to here (we're in a thread)
				// Change screen to lobby and pass through Game Client data.
			}
		}));
		addComponent(new GuiTextBox(width/2 - 125, height / 12 * 8, 190, 50, "IP Address"));
		addComponent(new GuiButton(width / 2 - 125, height / 12 * 9, 250, 50, "Open to Invites", new Runnable() {
			public void run() {
				// TODO:
				// Open on the server port and listen for a specific packet.
				// Once found, alert the user and ask if they want to join.
				// If the say no, reject packet and continue listening.
				// If they say yes, close the port, open a game client and send a request for game details,
				// Game client will collect info and pass back to here (we're in a thread)
				// Change screen to lobby and pass through Game Client data.
			}
		}));
		addComponent(new GuiButton(width / 2 - 75, height / 12 * 10, 150, 50, "Back", new Runnable() {
			public void run() {
				Screen.screenBack();
			}
		}));
	}
	
	public void tick() {
	}
	public void render(Graphics2D g2d) {
	}
}
