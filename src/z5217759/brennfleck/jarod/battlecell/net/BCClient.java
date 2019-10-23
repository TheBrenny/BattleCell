package z5217759.brennfleck.jarod.battlecell.net;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;

import com.thebrenny.jumg.gui.Screen;
import com.thebrenny.jumg.net.GameClient;
import com.thebrenny.jumg.net.Packet;
import com.thebrenny.jumg.util.Logger;
import com.thebrenny.jumg.util.StringUtil;

import z5217759.brennfleck.jarod.battlecell.gui.ScreenMenuLobby;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet000Connect;
import z5217759.brennfleck.jarod.battlecell.net.packets.Packet001Disconnect;
import z5217759.brennfleck.jarod.battlecell.net.packets.PacketType;

public class BCClient extends GameClient {
	public BCClient(String serverIPAddress, String serverPort) {
		super(serverIPAddress, Integer.parseInt(serverPort));
	}
	public BCClient(String serverIPAddress, int serverPort) {
		super(serverIPAddress, serverPort);
	}
	
	public void handlePacket(String message, InetAddress address, int port) {
		if(!getDestAddress().equals(address) || getDestPort() != port) {
			Logger.log(StringUtil.insert("This packet didn't come from the server: [{}:{}] != [{}:{}]", getDestAddress(), getDestPort(), address, port));
			return;
		}
		
		PacketType packet = PacketType.getPacketType(Packet.retrievePacketID(message));
		
		if(packet == null) return;
		
		Packet p = null;
		try {
			p = packet.clazz.getConstructor(byte[].class).newInstance(message.getBytes());
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return;
		}
		
		if(p == null) return; // Log the error, beyond the exception?
		
		switch(packet) {
		case CONNECT:
			handleConnect((Packet000Connect) p, address, port);
			break;
		case DISCONNECT:
			handleDisconnect((Packet001Disconnect) p, address, port);
			break;
		case INFO_REQUEST: // don't do anything?
			break;
		case INFO_RESPONSE: // Don't do anything, because this is a response so it should be grabbed using grabPacket immediately after the request was sent.
			break;
		}
	}
	
	private void handleConnect(Packet000Connect p, InetAddress address, int port) {
		// TODO: Add to lobby screen.
		Screen ss = Screen.getCurrentScreen();
		if(!(ss instanceof ScreenMenuLobby)) return;
		
		ScreenMenuLobby s = (ScreenMenuLobby) ss;
		
		s.addPlayer(p.getName());
	}
	private void handleDisconnect(Packet001Disconnect p, InetAddress address, int port) {
		// This can happen on any screen at any time!
		Screen ss = Screen.getCurrentScreen();
		if(!(ss instanceof ScreenMenuLobby)) return;
		
		ScreenMenuLobby s = (ScreenMenuLobby) ss;
		
		s.removePlayer(p.getName(), p.getReason());
	}
}
