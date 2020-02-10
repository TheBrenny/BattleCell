package z5217759.brennfleck.jarod.battlecell.entities;

import java.awt.Color;

/*
 * This isn't a real entity, it's just a serialisable class that contains info
 * on a BCEntity.
 */
public class NetEntity {
	private String name;
	private float x;
	private float y;
	private byte character;
	private int color;
	private int warDef;
	private int magDef;
	private int arcDef;
	
	public NetEntity(BCEntity e) {
		this.name = e.getName();
		this.x = e.getTileX();
		this.y = e.getTileY();
		this.color = e.getColor().getRGB();
		this.character = (byte) e.getType().id;
		this.warDef = e.getWarriorDefense();
		this.magDef = e.getMagicianDefense();
		this.arcDef = e.getArcherDefense();
	}
	public NetEntity(String name, float x, float y, byte character, int color, int warDef, int magDef, int arcDef) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.character = character;
		this.color = color;
		this.warDef = warDef;
		this.magDef = magDef;
		this.arcDef = arcDef;
	}
	
	public String getName() {
		return name;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
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
	
	public BCEntity instantiate() {
		BCEntity entity;
		switch(BCEntity.Type.getTypeByID(character)) {
		case WARRIOR:
			entity = new EntityWarrior(name, x, y);
			break;
		case MAGICIAN:
			entity = new EntityMagician(name, x, y);
			break;
		case ARCHER:
			entity = new EntityArcher(name, x, y);
			break;
		default:
		case DUMMY:
			entity = new EntityDummy(name, x, y);
			break;
		}
		
		entity.setColor(new Color(color));
		entity.setDefenseWarrior(warDef).setDefenseMagician(magDef).setDefenseArcher(arcDef);
		
		return entity;
	}
	public String stringify() {
		return stringify(",");
	}
	public String stringify(String delimiter) {
		String ret = "";
		
		ret += name + delimiter;
		ret += x + delimiter;
		ret += y + delimiter;
		ret += character + delimiter;
		ret += color + delimiter;
		ret += warDef + delimiter;
		ret += magDef + delimiter;
		ret += arcDef + delimiter;
		
		return ret;
	}
}
