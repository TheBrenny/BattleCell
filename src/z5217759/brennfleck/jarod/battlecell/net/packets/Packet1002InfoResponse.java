package z5217759.brennfleck.jarod.battlecell.net.packets;

import com.thebrenny.jumg.net.Packet;
import com.thebrenny.jumg.util.Logger;

public class Packet1002InfoResponse extends Packet {
	public static final String CONNECTED_USERS = "connectedUsers";
	
	public static String RESPONSE_DELIMITER = ":nn:";
	
	private String[][] response;
	
	public Packet1002InfoResponse(String[][] response) {
		super(PacketType.INFO_RESPONSE.id);
		this.response = response;
	}
	public Packet1002InfoResponse(byte[] data) {
		super(PacketType.INFO_RESPONSE.id);
		String[] newData = readData(data).split(Packet.DELIMITER);
		this.response = new String[newData.length][2];
		for(int index = 0; index < response.length; index++) this.response[index] = newData[index].split(RESPONSE_DELIMITER, 2);
	}
	
	public String getResponse(String request) {
		if(request == null) return null;
		
		for(int i = 0; i < response.length; i++) {
			if(response[i][0].equals(request)) return response[i][1];
		}
		
		return null;
	}
	
	public Object[] getObjectsToSend() {
		Object[] objects = new Object[response.length];
		for(int i = 0; i < response.length; i++) {
			objects[i] = response[i][0] + RESPONSE_DELIMITER + response[i][1];
		}
		return objects;
	}
	
	public static String buildResponse(String request, String ... response) {
		String ret = request + RESPONSE_DELIMITER;
		
		if(response.length == 0) {
			Logger.log("Request [" + request + "] hasn't got a meaningful response!");
			ret += null;
		}
		
		for(int i = 0; i < response.length; i++) ret += response[i] + (i < response.length - 1 ? RESPONSE_DELIMITER : "");
		
		return ret;
	}
}
