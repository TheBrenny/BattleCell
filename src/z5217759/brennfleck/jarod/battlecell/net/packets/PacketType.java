package z5217759.brennfleck.jarod.battlecell.net.packets;

import com.thebrenny.jumg.net.Packet;

/*
 * TODO:
 * - Change this into an Annotation rather than an enum. Have all the packet
 * identification found within the individual classes so development and
 * adjustments are easier.
 */
public enum PacketType {
	CONNECT("000", Packet000Connect.class),
	DISCONNECT("001", Packet001Disconnect.class),
	READY("002", Packet002Ready.class),
	START_GAME("003", Packet003StartGame.class),
	INFO_REQUEST("1001", Packet1001InfoRequest.class),
	INFO_RESPONSE("1002", Packet1002InfoResponse.class);
	
	public final String id;
	public final Class<? extends Packet> clazz;
	
	private PacketType(String id, Class<? extends Packet> clazz) {
		this.id = id;
		this.clazz = clazz;
	}
	
	public static PacketType getPacketType(String id) {
		for(PacketType p : PacketType.values()) {
			if(id.equals(p.id)) return p;
		}
		return null;
	}
}
