package z5217759.brennfleck.jarod.battlecell.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.thebrenny.jumg.entities.messaging.Postman;
import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenMenu;
import com.thebrenny.jumg.gui.components.ComponentPanel;
import com.thebrenny.jumg.gui.components.GuiButton;
import com.thebrenny.jumg.gui.components.GuiLabel;
import com.thebrenny.jumg.gui.components.GuiList;
import com.thebrenny.jumg.gui.components.GuiList.GuiListItem;
import com.thebrenny.jumg.gui.components.GuiNumberSpinner;
import com.thebrenny.jumg.net.GameClient;
import com.thebrenny.jumg.net.GameServer;
import com.thebrenny.jumg.util.Logger;
import com.thebrenny.jumg.util.MathUtil;
import com.thebrenny.jumg.util.TimeUtil;

import z5217759.brennfleck.jarod.battlecell.BattleCell;
import z5217759.brennfleck.jarod.battlecell.entities.BCEntity;
import z5217759.brennfleck.jarod.battlecell.entities.EntityDummy;
import z5217759.brennfleck.jarod.battlecell.gui.components.GuiCharacterPreview;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet001Disconnect;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet002Ready;

public class ScreenMenuLobby extends ScreenMenu {
	private boolean isHost = false;
	private boolean isReady = false;
	private String hostName = "";
	private EntityDummy entity;
	private EntityDummy entityTwo; // TODO: when we spin between three
	private EntityDummy entityThree;
	private ArrayList<NameToDeleteEntry> namesToDelete;
	
	private GuiLabel characterLabel;
	private GuiCharacterPreview charPreview;
	private GuiList lobbyNames;
	private GuiLabel defRemainLbl;
	private GuiNumberSpinner warDefSpinner;
	private GuiNumberSpinner magDefSpinner;
	private GuiNumberSpinner arcDefSpinner;
	private GuiNumberSpinner redSpinner;
	private GuiNumberSpinner greenSpinner;
	private GuiNumberSpinner blueSpinner;
	
	private Color charColor = Color.BLACK;
	private byte selectedCharacter = 0;
	private int defPointRemain = BCEntity.DEFENSE_MAX;
	private int defenseWarrior = 0;
	private int defenseMagician = 0;
	private int defenseArcher = 0;
	
	public ScreenMenuLobby(String host, String ... names) {
		this.hostName = host;
		// this game data becomes passed game data.
		
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
		namesToDelete = new ArrayList<NameToDeleteEntry>();
		
		for(String name : names) addPlayer(name);
		
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
				ScreenMenuLobby.this.selectedCharacter = (byte) MathUtil.wrap(0, ScreenMenuLobby.this.selectedCharacter - 1, 3);
				updateCharPreview();
			}
		}));
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 5 * 4 - 20, (float) pnl.getHeight() / 4 * 1 - 22, 40, 40, "→", new Runnable() {
			public void run() {
				ScreenMenuLobby.this.selectedCharacter = (byte) MathUtil.wrap(0, ScreenMenuLobby.this.selectedCharacter + 1, 3);
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
		pnl.addComponent(lobbyNames = new GuiList(0, 0, (float) pnl.getWidth(), (float) pnl.getHeight(), playerCount));
		pnl.appendToComponents(this.components);
	}
	private final void addGameButtonsPanel(ComponentPanel pnl) {
		// invite is a button which produces an overlay screen
		// ready makes the player's status to ready (green name in lobby)
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 2 - 100, (float) pnl.getHeight() / 4 * 1 - 22, 200, 45, "Invite To Game", new Runnable() {
			public void run() {
				// TODO: Show invite overlay.
			}
		}).setEnabled(false));
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 2 - 100, (float) pnl.getHeight() / 4 * 2 - 22, 200, 45, "Ready To BATTLE", new Runnable() {
			public void run() {
				// TODO: Start the game.
				// Send Ready packet to the server
				// once all players are ready, initiate countdown in the middle of the screen.
				// once countdown complete, set screen to screengame, and wait for server instructions.
				readyingUpChangesButtons();
				GameClient gc = GameClient.getInstance();

				if(gc != null) {
					Packet002Ready packet = new Packet002Ready(BattleCell.getMainGame().getUsername(), isReady, selectedCharacter, charColor.getRGB(), defenseWarrior, defenseMagician, defenseArcher);
				} else {
					Logger.log("WHAT THE HECK!? YOU SHOULDN'T BE HERE WITHOUT A GAME CLIENT INSTANCE!");
				}
			}
		}));
		pnl.addComponent(new GuiButton((float) pnl.getWidth() / 2 - 75, (float) pnl.getHeight() / 4 * 3 - 22, 150, 45, "Back", new Runnable() {
			public void run() {
				// confirmation overlay
				exitLobby(null);
			}
		}));
		pnl.appendToComponents(this.components);
	}
	public ScreenMenuLobby setHost(boolean isHost) {
		this.isHost = isHost;
		return this;
	}
	public void readyingUpChangesButtons() {
		isReady = !isReady;

	}

	public void updateCharPreview() {
		charPreview.changeCharacter(selectedCharacter);
		characterLabel.setString(0, entity.getType().toString());
	}
	
	public void tick() {
		// change colour according to RGB spinners,
		if( //@formatter:off
			charColor.getRed() != redSpinner.getNumber() ||
			charColor.getGreen() != greenSpinner.getNumber() ||
			charColor.getBlue() != blueSpinner.getNumber()
			//@formatter:on
		) {
			entity.setColor(charColor = new Color(redSpinner.getNumber(), greenSpinner.getNumber(), blueSpinner.getNumber()));
		}
		
		int defPointOld = defPointRemain;
		defPointRemain = BCEntity.DEFENSE_MAX - warDefSpinner.getNumber() - magDefSpinner.getNumber() - arcDefSpinner.getNumber();
		if(defPointRemain != defPointOld) {
			defRemainLbl.setString(0, "Defense Points Remaining: " + defPointRemain);
			warDefSpinner.setMinMax(0, defPointRemain + warDefSpinner.getNumber());
			magDefSpinner.setMinMax(0, defPointRemain + magDefSpinner.getNumber());
			arcDefSpinner.setMinMax(0, defPointRemain + arcDefSpinner.getNumber());
		}
		
		NameToDeleteEntry[] ntd = namesToDelete.toArray(new NameToDeleteEntry[namesToDelete.size()]);
		for(NameToDeleteEntry ntde : ntd) {
			if(ntde.hasExpired()) removePlayerEntry(ntde);
		}
	}
	
	public void render(Graphics2D g2d) {
	}
	public void renderBackground(Graphics2D g2d) {
		int width = BattleCell.getMainGame().getDisplay().getWidth();
		int height = BattleCell.getMainGame().getDisplay().getHeight();
		g2d.setColor(Color.BLUE);
		g2d.fillRect(0, 0, width, height);
	}
	
	public void addPlayer(String name) {
		lobbyNames.addToList(name);
	}
	public void removePlayer(String name, String reason) {
		if(name != null && name.equals(hostName)) {
			exitLobby("Server closed");
		} else {
			int i = lobbyNames.indexOf(name);
			if(i != -1) {
				GuiListItem item = lobbyNames.getItem(i);
				namesToDelete.add(new NameToDeleteEntry(item, name, reason, TimeUtil.getEpoch()));
			}
		}
	}
	private void removePlayerEntry(NameToDeleteEntry ntde) {
		namesToDelete.remove(ntde);
		lobbyNames.removeItem(ntde.getItem());
	}
	
	public boolean isHost() {
		return isHost;
	}
	
	public void exitLobby(String reason) {
		GameClient gc = GameClient.getInstance();
		GameServer gs = GameServer.getInstance();
		
		if(gs != null) {
			gs.disconnectAllConnections();
			gs.stop();
		}
		if(gc != null) {
			if(gs == null) gc.sendPacket(new Packet001Disconnect(BattleCell.getMainGame().getUsername(), reason));
			gc.stop();
		}
		
		Screen.screenBack();
		if(reason != null) Screen.screenForward(new ScreenMenuServerConnectionLost(reason));
	}
	
	private class NameToDeleteEntry {
		public static final long TIME_TO_LIVE = 3000;
		private GuiListItem item;
		private String name;
		private String reason;
		private long timestamp;
		
		public NameToDeleteEntry(GuiListItem item, String name, String reason, long timestamp) {
			this.item = item;
			this.item.setString(0, name + " disconnected" + (reason != null ? ": " + reason : ""));
			this.name = name;
			this.reason = reason;
			this.timestamp = timestamp;
		}
		
		public GuiListItem getItem() {
			return item;
		}
		public String getName() {
			return name;
		}
		public String getReason() {
			return reason;
		}
		public long getTimestamp() {
			return timestamp;
		}
		
		public boolean hasExpired() {
			return TimeUtil.getEpoch() - timestamp >= TIME_TO_LIVE;
		}
	}
}
