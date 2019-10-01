package z5217759.brennfleck.jarod.battlecell.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.thebrenny.jumg.entities.messaging.Postman;
import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenMenu;
import com.thebrenny.jumg.gui.components.ComponentPanel;
import com.thebrenny.jumg.gui.components.GuiButton;
import com.thebrenny.jumg.gui.components.GuiLabel;
import com.thebrenny.jumg.gui.components.GuiList;
import com.thebrenny.jumg.gui.components.GuiNumberSpinner;
import com.thebrenny.jumg.util.MathUtil;

import z5217759.brennfleck.jarod.battlecell.BattleCell;
import z5217759.brennfleck.jarod.battlecell.entities.BCEntity;
import z5217759.brennfleck.jarod.battlecell.entities.EntityDummy;
import z5217759.brennfleck.jarod.battlecell.gui.components.GuiCharacterPreview;

public class ScreenMenuLobby extends ScreenMenu {
	private boolean isHost = false;
	private EntityDummy entity;
	
	private GuiLabel characterLabel;
	private GuiCharacterPreview charPreview;
	private GuiLabel defRemainLbl;
	private GuiNumberSpinner warDefSpinner;
	private GuiNumberSpinner magDefSpinner;
	private GuiNumberSpinner arcDefSpinner;
	private GuiNumberSpinner redSpinner;
	private GuiNumberSpinner greenSpinner;
	private GuiNumberSpinner blueSpinner;
	
	private int selectedCharacter = 0;
	private int defPointRemain = BCEntity.DEFENSE_MAX;
	private int defenseWarrior = 0;
	private int defenseMagician = 0;
	private int defenseArcher = 0;
	
	public ScreenMenuLobby(int gameData) {
		//this game data becomes passed game data.
		
		// bg should eventually become a big pic of the game map which moves according to the mouse pos.
		
		int width = BattleCell.getMainGame().getDisplay().getWidth();
		int height = BattleCell.getMainGame().getDisplay().getHeight();
		
		ComponentPanel pnl = new ComponentPanel(0, 0, width / 3, height - 200);
		addCharacterPanel(pnl);
		
		pnl = new ComponentPanel(0, height, width / 3, -200);
		addSwitchCharacterPanel(pnl);
		
		pnl = new ComponentPanel(width / 3, 0, width / 3, height / 2);
		addDefenseTraitsPanel(pnl);
		
		pnl = new ComponentPanel(width / 3, height / 2, width / 3, height / 2);
		addInfluencerPanel(pnl);
		
		pnl = new ComponentPanel(width / 3 * 2, 0, width / 3, height - 200);
		addLobbyPanel(pnl, 20);
		
		pnl = new ComponentPanel(width / 3 * 2, height, width / 3, -200);
		addGameButtonsPanel(pnl);
	}
	private final void addCharacterPanel(ComponentPanel pnl) {
		// Turns out we created a new GuiComponent for this one!
		pnl.addComponent(charPreview = new GuiCharacterPreview(15, 15, (float) pnl.getWidth() - 15, (float) pnl.getHeight() - 15, (entity = new EntityDummy("CharPrev", 0, 0).changeType(BCEntity.Type.WARRIOR))));
		Postman.getInstance().unregisterListener(entity);
		pnl.appendToComponents(this.components);
	}
	private final void addSwitchCharacterPanel(ComponentPanel pnl) {
		// switch char is 2 btns and a label
		// There's a lot of bs magic numbers here. but it works. so play with them if you'd like.
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 5 - 20, (float) pnl.getHeight() / 4 * 1 - 22, 40, 40, "←", new Runnable() {
			public void run() {
				ScreenMenuLobby.this.selectedCharacter = MathUtil.wrap(0, ScreenMenuLobby.this.selectedCharacter - 1, 3);
				updateCharPreview();
			}
		}));
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 5 * 4 - 20, (float) pnl.getHeight() / 4 * 1 - 22, 40, 40, "→", new Runnable() {
			public void run() {
				ScreenMenuLobby.this.selectedCharacter = MathUtil.wrap(0, ScreenMenuLobby.this.selectedCharacter + 1, 3);
				updateCharPreview();
			}
		}));
		pnl.addComponent(characterLabel = new GuiLabel((float) pnl.getWidth() / 2, (float) pnl.getHeight() / 4 * 1 - 2, "Warrior").align(GuiLabel.ALIGN_CENTRE));
		
		pnl.addComponent(new GuiLabel((float) pnl.getWidth() / 5 * 1 - 25, (float) pnl.getHeight() / 4 * 3 - 12, "Red").align(GuiLabel.ALIGN_HORIZONTAL_RIGHT, GuiLabel.ALIGN_VERTICAL_CENTRE));
		pnl.addComponent(redSpinner = new GuiNumberSpinner((float) pnl.getWidth() / 5 * 1 - 20, (float) pnl.getHeight() / 4 * 3 - 72, 40, 120, pnl.getComponents()).setMinMax(0, 255).setNumber(entity.getColor().getRed()));
		
		pnl.addComponent(new GuiLabel((float) pnl.getWidth() / 2 - 25, (float) pnl.getHeight() / 4 * 3 - 12, "Green").align(GuiLabel.ALIGN_HORIZONTAL_RIGHT, GuiLabel.ALIGN_VERTICAL_CENTRE));
		pnl.addComponent(greenSpinner = new GuiNumberSpinner((float) pnl.getWidth() / 2 - 20, (float) pnl.getHeight() / 4 * 3 - 72, 40, 120, pnl.getComponents()).setMinMax(0, 255).setNumber(entity.getColor().getGreen()));
		
		pnl.addComponent(new GuiLabel((float) pnl.getWidth() / 5 * 4 - 25, (float) pnl.getHeight() / 4 * 3 - 12, "Blue").align(GuiLabel.ALIGN_HORIZONTAL_RIGHT, GuiLabel.ALIGN_VERTICAL_CENTRE));
		pnl.addComponent(blueSpinner = new GuiNumberSpinner((float) pnl.getWidth() / 5 * 4 - 20, (float) pnl.getHeight() / 4 * 3 - 72, 40, 120, pnl.getComponents()).setMinMax(0, 255).setNumber(entity.getColor().getBlue()));
		
		pnl.appendToComponents(this.components);
	}
	private final void addDefenseTraitsPanel(ComponentPanel pnl) {
		// defense traits is 3 labels and 3 number spinners plus a "points remaining" label
		pnl.addComponent(defRemainLbl = new GuiLabel((float) pnl.getWidth() / 2, (float) pnl.getHeight() / 5 * 1, "Defense Points Remaining: " + defPointRemain).align(GuiLabel.ALIGN_HORIZONTAL_CENTRE, GuiLabel.ALIGN_VERTICAL_BOTTOM));
		
		pnl.addComponent(new GuiLabel((float) pnl.getWidth() / 2 - 20, (float) pnl.getHeight() / 5 * 2, "Warrior Defense:").align(GuiLabel.ALIGN_HORIZONTAL_RIGHT, GuiLabel.ALIGN_VERTICAL_CENTRE));
		pnl.addComponent(warDefSpinner = new GuiNumberSpinner((float) pnl.getWidth() / 2 - 15, (float) pnl.getHeight() / 5 * 2 - 20, 120, 40, pnl.getComponents()));
		
		pnl.addComponent(new GuiLabel((float) pnl.getWidth() / 2 - 20, (float) pnl.getHeight() / 5 * 3, "Mage Defense:").align(GuiLabel.ALIGN_HORIZONTAL_RIGHT, GuiLabel.ALIGN_VERTICAL_CENTRE));
		pnl.addComponent(magDefSpinner = new GuiNumberSpinner((float) pnl.getWidth() / 2 - 15, (float) pnl.getHeight() / 5 * 3 - 20, 120, 40, pnl.getComponents()));
		
		pnl.addComponent(new GuiLabel((float) pnl.getWidth() / 2 - 20, (float) pnl.getHeight() / 5 * 4, "Archer Defense:").align(GuiLabel.ALIGN_HORIZONTAL_RIGHT, GuiLabel.ALIGN_VERTICAL_CENTRE));
		pnl.addComponent(arcDefSpinner = new GuiNumberSpinner((float) pnl.getWidth() / 2 - 15, (float) pnl.getHeight() / 5 * 4 - 20, 120, 40, pnl.getComponents()));
		
		pnl.appendToComponents(this.components);
	}
	private final void addInfluencerPanel(ComponentPanel pnl) {
		// AI Influencer will probably be a special GuiComponent that returns the coords of the point on a 2D plane.
		pnl.addComponent(new GuiLabel((float) pnl.getWidth() / 2, (float) pnl.getHeight() / 2, "Coming Soon!").align(GuiLabel.ALIGN_CENTRE).adjustFont(Font.ITALIC));
		pnl.appendToComponents(this.components);
	}
	private final void addLobbyPanel(ComponentPanel pnl, int playerCount) {
		// Lobby is a GuiList w/ coloured names for readiness - could become list selector w/ buttons?
		pnl.addComponent(new GuiList(0, 0, (float) pnl.getWidth(), (float) pnl.getHeight(), playerCount));
		pnl.appendToComponents(this.components);
	}
	private final void addGameButtonsPanel(ComponentPanel pnl) {
		// invite is a button which produces an overlay screen
		// ready makes the player's status to ready (green name in lobby)
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 2 - 100, (float) pnl.getHeight() / 4 * 1 - 22, 200, 45, "Invite To Game", new Runnable() {
			public void run() {
				// TODO: Show invite overlay.
			}
		}));
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 2 - 100, (float) pnl.getHeight() / 4 * 2 - 22, 200, 45, "Ready To BATTLE", new Runnable() {
			public void run() {
				// TODO: Start the game.
				// Send Ready packet to the server
				// once all players are ready, initiate countdown in the middle of the screen.
				// once countdown complete, set screen to screengame, and wait for server instructions.
			}
		}));
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 2 - 75, (float) pnl.getHeight() / 4 * 3 - 22, 150, 45, "Back", new Runnable() {
			public void run() {
				// confirmation overlay
				Screen.screenBack();
			}
		}));
		pnl.appendToComponents(this.components);
	}
	public ScreenMenuLobby setHost(boolean isHost) {
		this.isHost = isHost;
		return this;
	}
	
	public void updateCharPreview() {
		charPreview.changeCharacter(selectedCharacter);
		characterLabel.setString(0, entity.getType().toString());
	}
	
	public void tick() {
		// change colour according to RGB spinners,
		Color c = entity.getColor();
		if(c.getRed() != redSpinner.getNumber() || c.getGreen() != greenSpinner.getNumber() || c.getBlue() != blueSpinner.getNumber()) {
			entity.setColor(new Color(redSpinner.getNumber(), greenSpinner.getNumber(), blueSpinner.getNumber()));
		}
		
		defPointRemain = BCEntity.DEFENSE_MAX - warDefSpinner.getNumber() - magDefSpinner.getNumber() - arcDefSpinner.getNumber();
		warDefSpinner.setMinMax(0, defPointRemain + warDefSpinner.getNumber());
		magDefSpinner.setMinMax(0, defPointRemain + magDefSpinner.getNumber());
		arcDefSpinner.setMinMax(0, defPointRemain + arcDefSpinner.getNumber());
		defRemainLbl.setString(0, "Defense Points Remaining: " + defPointRemain);
	}
	public void render(Graphics2D g2d) {
	}
	public void renderBackground(Graphics2D g2d) {
		int width = BattleCell.getMainGame().getDisplay().getWidth();
		int height = BattleCell.getMainGame().getDisplay().getHeight();
		g2d.setColor(Color.BLUE);
		g2d.fillRect(0, 0, width, height);
	}
	
	public boolean isHost() {
		return isHost;
	}
}
