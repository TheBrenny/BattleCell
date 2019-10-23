package z5217759.brennfleck.jarod.battlecell.net.packets;

import com.thebrenny.jumg.net.Packet;

public class Packet000Connect extends Packet {
	private String name;
	
	public Packet000Connect(String name) {
		super(PacketType.CONNECT.id);
		this.name = name;
	}
	
	public Packet000Connect(byte[] data) {
		super(PacketType.CONNECT.id);
		String[] newData = readData(data).split(Packet.DELIMITER);
		int index = 0;
		this.name = newData[index++];
	}

	public String getName() {
		return name;
	}
	
	public Object[] getObjectsToSend() {
		return new Object[] {name};
	}
}
