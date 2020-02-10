package z5217759.brennfleck.jarod.battlecell.net.packets;

import com.thebrenny.jumg.net.Packet;

public class Packet002Ready extends Packet {
	private String name;
	private boolean isReady;
	private byte character;
	private int color;
	private int warDef;
	private int magDef;
	private int arcDef;
	
	public Packet002Ready(String name, boolean isReady, byte character, int color, int warDef, int magDef, int arcDef) {
		super(PacketType.READY.id);
		this.name = name;
		this.isReady = isReady;
		this.character = character;
		this.color = color;
		this.warDef = warDef;
		this.magDef = magDef;
		this.arcDef = arcDef;
	}
	
	public Packet002Ready(byte[] data) {
		super(PacketType.READY.id);
		String[] newData = readData(data).split(Packet.DELIMITER);
		int index = 0;
		this.name = newData[index++];
		this.isReady = Boolean.parseBoolean(newData[index++]);
		this.character = Byte.parseByte(newData[index++]);
		this.color = Integer.parseInt(newData[index++]);
		this.warDef = Integer.parseInt(newData[index++]);
		this.magDef = Integer.parseInt(newData[index++]);
		this.arcDef = Integer.parseInt(newData[index++]);
	}
	
	public String getName() {
		return name;
	}
	public boolean isReady() {
		return isReady;
	}
	public byte getCharacter() {
		return character;
	}
	public int getColor() {
		return color;
	}
	public int getWarDef() {
		return warDef;
	}
	public int getMagDef() {
		return magDef;
	}
	public int getArcDef() {
		return arcDef;
	}
	
	public Object[] getObjectsToSend() {
		return new Object[] {name, isReady};
	}
}
