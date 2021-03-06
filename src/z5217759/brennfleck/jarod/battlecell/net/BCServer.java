package z5217759.brennfleck.jarod.battlecell.net;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.HashMap;

import com.thebrenny.jumg.net.GameServer;
import com.thebrenny.jumg.net.Packet;
import com.thebrenny.jumg.util.StringUtil;

import z5217759.brennfleck.jarod.battlecell.entities.NetEntity;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet000Connect;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet001Disconnect;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet002Ready;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet1001InfoRequest;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet1002InfoResponse;
import z5217759.brennfleck.jarod.battlecell.net.packets.PacketType;

public class BCServer extends GameServer {
	private HashMap<String, NetEntity> connectedPlayers;
	
	public BCServer(String host) {
		super(host);
		connectedPlayers = new HashMap<String, NetEntity>();
	}
	
	public void handlePacket(String message, InetAddress address, int port) {
		PacketType packet = PacketType.getPacketType(Packet.retrievePacketID(message));
		
		if(packet == null) return;
		
		Packet p = null;
		try {
			p = packet.clazz.getConstructor(byte[].class).newInstance(message.getBytes());
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		if(p == null) return; // Log the error, beyond the exception?
		
		switch(packet) {
		case CONNECT:
			handleConnect((Packet000Connect) p, address, port);
			break;
		case DISCONNECT:
			handleDisconnect((Packet001Disconnect) p, address, port);
			break;
		case READY:
			handleReady((Packet002Ready) p, address, port);
			break;
		case INFO_REQUEST:
			handleInfoRequest((Packet1001InfoRequest) p, address, port);
			break;
		case INFO_RESPONSE: // don't do anything?
			break;
		}
	}
	
	public void handleConnect(Packet000Connect p, InetAddress address, int port) {
		// if not lobby screen, ignore
		// Screen ss = Screen.getCurrentScreen();
		// if(!(ss instanceof ScreenMenuLobby)) return;
		
		// add to lobby screen and send packet to all clients
		if(addConnection(address, port)) {
			String theName = p.getName();
			while(connectedPlayers.containsKey(theName)) theName += "'s fam";
			p = new Packet000Connect(theName);
			connectedPlayers.put(theName, null);
			sendDataToAll(p);
		}
	}
	public void handleDisconnect(Packet001Disconnect p, InetAddress address, int port) {
		if(removeConnection(address, port)) {
			connectedPlayers.remove(p.getName());
			sendDataToAll(p);
		}
	}
	public void handleReady(Packet002Ready p, InetAddress address, int port) {
		NetEntity e = null;
		if(p.isReady()) e = new NetEntity(p.getName(), 0, 0, p.getCharacter(), p.getColor(), p.getWarDef(), p.getMagDef(), p.getArcDef());
		connectedPlayers.put(p.getName(), e);
	}
	public void handleInfoRequest(Packet1001InfoRequest p, InetAddress address, int port) {
		// switch for what the request is then respond with data to the client.
		Packet1002InfoResponse packResponse = null;
		String[] request = p.getRequest();
		String[][] response = new String[request.length][2];
		
		for(int i = 0; i < request.length; i++) {
			response[i][0] = request[i];
			
			switch(request[i]) {
			case Packet1002InfoResponse.CONNECTED_USERS:
				String[] names = connectedPlayers.keySet().toArray(new String[0]);
				response[i][1] = StringUtil.join(names, Packet1002InfoResponse.RESPONSE_DELIMITER);
				break;
			case Packet1002InfoResponse.SERVER_HOST:
				response[i][1] = this.getServerInfo().getHost();
				break;
			}
		}
		
		packResponse = new Packet1002InfoResponse(response);
		
		sendPacket(packResponse, address, port);
	}
	
	public void disconnectAllConnections() {
		Packet001Disconnect p = new Packet001Disconnect(this.getServerInfo().getHost(), "Closed the server.");
		sendDataToAll(p);
	}
}
