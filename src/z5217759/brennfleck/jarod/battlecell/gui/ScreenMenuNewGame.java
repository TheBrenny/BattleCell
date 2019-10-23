package z5217759.brennfleck.jarod.battlecell.gui;

import java.awt.Graphics2D;

import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.gui.ScreenMenu;
import com.thebrenny.jumg.gui.components.GuiButton;
import com.thebrenny.jumg.gui.components.GuiLabel;
import com.thebrenny.jumg.gui.components.GuiTextBox;
import com.thebrenny.jumg.net.Packet;
import com.thebrenny.jumg.util.Images;
import com.thebrenny.jumg.util.Logger;

import z5217759.brennfleck.jarod.battlecell.BattleCell;
import z5217759.brennfleck.jarod.battlecell.net.BCClient;
import z5217759.brennfleck.jarod.battlecell.net.BCServer;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet000Connect;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet1001InfoRequest;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet1002InfoResponse;
import z5217759.brennfleck.jarod.battlecell.net.packets.PacketType;

public class ScreenMenuNewGame extends ScreenMenu {
	private GuiTextBox serverBox;
	private GuiButton joinBtn;
	
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
				BCServer server = (BCServer) new BCServer(BattleCell.getMainGame().getUsername()).setConnectionPacket(PacketType.CONNECT.id);
				server.start();
				joinServerMethod(server.getLANSrcAddress() + ":" + server.getSrcPort());
				//GameServer.ServerInfo info = server.getServerInfo(); // This will change to the actual game server object!
				//Screen.screenForward(new ScreenMenuLobby(BattleCell.getMainGame().getUsername()).setHost(true));
			}
		}));
		addComponent(serverBox = new GuiTextBox(width / 2 - 125, height / 12 * 8, 190, 50, "IP Address"));
		addComponent(joinBtn = new GuiButton(width / 2 + 75, height / 12 * 8, 50, 50, "Join", new Runnable() {
			public void run() {
				joinServerMethod(null);
			}
		}));
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
	
	@SuppressWarnings("static-access")
	public void joinServerMethod(String server) {
		try {
			joinBtn.setEnabled(false);
			server = server == null ? serverBox.getString() : server;
			String port = BCServer.DEFAULT_PORT + "";
			if(server.contains(":")) {
				String[] parts = server.split(":");
				server = parts[0];
				port = parts[1];
			}
			BCClient client = (BCClient) new BCClient(server, port).getInstance();
			client.setDestination(server, Integer.parseInt(port));
			client.start();
			
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(500); // Don't win the race!

						if(client.testConnection()) { // Thread Blocking!
							// If the server exists we want to shoot the connection request packet and join the game!
							String username = BattleCell.getMainGame().getUsername();
							String[] connectedUsers = null;
							
							// Send 000Connect
							Packet p = new Packet000Connect(username);
							client.sendPacket(p);
							p = new Packet000Connect(client.grabPacket(p.generatePacketHeader()).getData());
							if(!((Packet000Connect) p).getName().startsWith(username)) throw new Exception("The username from the response [" + ((Packet000Connect) p).getName() + "] didn't match your name [" + username + "]! Could've been a racing issue?");
							// TODO: the above line shouldn't be an if - what if we end up in a race situation and the second packet we receive is our connection packet?
							username = ((Packet000Connect) p).getName();
							BattleCell.getMainGame().setUsername(username);
							
							// Send 1001InfoRequest
							p = new Packet1001InfoRequest(new String[] {Packet1002InfoResponse.CONNECTED_USERS});
							client.sendPacket(p);
							
							// Recv 1002InfoResponse
							p = new Packet1002InfoResponse(new String[][] {}); // makes it easier in the next line...
							p = new Packet1002InfoResponse(client.grabPacket(p.generatePacketHeader()).getData());
							connectedUsers = ((Packet1002InfoResponse) p).getResponse(Packet1002InfoResponse.CONNECTED_USERS).split(Packet1002InfoResponse.RESPONSE_DELIMITER);
							
							// change screens
							ScreenMenuLobby lobby = new ScreenMenuLobby(connectedUsers);
							Screen.screenForward(lobby);
						} else {
							throw new Exception("The server doesn't exist!");
						}
					} catch(Exception e) {
						Logger.log("Oh no! Something went wrong!");
						e.printStackTrace();
					}
					joinBtn.setEnabled(true);
				}
			}).start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
	}
	public void render(Graphics2D g2d) {
	}
}
