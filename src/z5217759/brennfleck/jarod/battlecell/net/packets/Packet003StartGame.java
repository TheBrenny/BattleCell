package z5217759.brennfleck.jarod.battlecell.net.packets;

import com.thebrenny.jumg.net.Packet;

import z5217759.brennfleck.jarod.battlecell.entities.NetEntity;

public class Packet003StartGame extends Packet {
	public static String DATA_DELIMITER = ":nn:";
	private NetEntity[] players;
	
	public Packet003StartGame(NetEntity[] players) {
		super(PacketType.START_GAME.id);
	}
	public Packet003StartGame(byte[] data) {
		super(PacketType.START_GAME.id);
		String[] newData = readData(data).split(Packet.DELIMITER);
		for(int index = 0; index < newData.length; index++) {
			players[index] = buildNetEntity(newData[index]);
		}
	}
	
	private NetEntity buildNetEntity(String blob) {
		String[] d = blob.split(DATA_DELIMITER);
		int i = 0;
		NetEntity e = new NetEntity(//@formatter:off
			d[i++],						// name
			Float.parseFloat(d[i++]),	// x
			Float.parseFloat(d[i++]),	// y
			Byte.parseByte(d[i++]),		// char
			Integer.parseInt(d[i++]),	// color
			Integer.parseInt(d[i++]),	// warDef
			Integer.parseInt(d[i++]),	// magDef
			Integer.parseInt(d[i++])	// arcDef
		//@formatter:on
		);
		return e;
	}
	
	public Object[] getObjectsToSend() {
		Object[] objects = new Object[players.length];
		for(int i = 0; i < players.length; i++) {
			objects[i] = players[i].stringify(DATA_DELIMITER);
		}
		return objects;
	}
}
