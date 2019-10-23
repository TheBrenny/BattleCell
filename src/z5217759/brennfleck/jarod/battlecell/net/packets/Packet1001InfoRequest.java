package z5217759.brennfleck.jarod.battlecell.net.packets;

import com.thebrenny.jumg.net.Packet;

public class Packet1001InfoRequest extends Packet {
	public String[] request;
	
	public Packet1001InfoRequest(String[] request) {
		super(PacketType.INFO_REQUEST.id);
		this.request = request;
	}
	public Packet1001InfoRequest(byte[] data) {
		super(PacketType.INFO_REQUEST.id);
		String[] newData = readData(data).split(Packet.DELIMITER);
		this.request = new String[newData.length];
		for(int index = 0; index < newData.length; index++) this.request[index] = newData[index];
	}
	
	public String[] getRequest() {
		return this.request;
	}
	
	public Object[] getObjectsToSend() {
		return request;
	}
}
