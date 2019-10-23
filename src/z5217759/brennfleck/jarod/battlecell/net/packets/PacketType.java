package z5217759.brennfleck.jarod.battlecell.net.packets;

import com.thebrenny.jumg.net.Packet;

public enum PacketType {
	CONNECT("000", Packet000Connect.class),
	DISCONNECT("001", Packet001Disconnect.class),
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