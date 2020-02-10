package z5217759.brennfleck.jarod.battlecell.net.packets;

import com.thebrenny.jumg.net.Packet;

public class Packet001Disconnect extends Packet {
	private String name;
	private String reason;
	
	public Packet001Disconnect(String name, String reason) {
		super(PacketType.DISCONNECT.id);
		this.name = name;
		this.reason = reason;
	}
	public Packet001Disconnect(byte[] data) {
		super(PacketType.DISCONNECT.id);
		String[] newData = readData(data).split(Packet.DELIMITER);
		int index = 0;
		this.name = newData[index++];
		this.reason = newData[index++];
	}
	
	public String getName() {
		return name;
	}
	public String getReason() {
		return reason;
	}
	
	public Object[] getObjectsToSend() {
		return new Object[] {name, reason};
	}
}
