package z5217759.brennfleck.jarod.battlecell.level.tile;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thebrenny.jumg.level.tiles.TileEntity;
import com.thebrenny.jumg.util.Images;

import z5217759.brennfleck.jarod.battlecell.level.BattleCellLevel;

public abstract class BCTileEntity extends TileEntity {
	private final Type type;
	protected final int maxMeta;
	protected int lastMeta = -1;
	protected int meta = 0;
	
	public BCTileEntity(int x, int y, Type type) {
		super("genUID:" + type.name, type.id, x, y, type.mapCoords[0].x, type.mapCoords[0].y);
		this.type = type;
		this.maxMeta = type.mapCoords.length;
	}
	
	public BattleCellLevel getLevel() {
		return (BattleCellLevel) super.getLevel();
	}
	
	public BufferedImage getRawImage() {
		if(this.image == null) {
			this.image = Images.getSubImage(TileEntity.TILE_MAP, TileEntity.TILE_SIZE, type.mapCoords[meta].x, type.mapCoords[meta].y);
		}
		return super.getRawImage();
	}
	
	public int getMeta() {
		return this.meta;
	}
	
	public int setMeta(int meta) {
		this.lastMeta = this.meta;
		this.meta = meta;
		this.image = null;
		return this.lastMeta;
	}
	
	public static enum Type {
		BOX(0, "box", new Point(1, 1));
		
		public final int id;
		public final String name;
		public final Point[] mapCoords;
		
		private Type(int id, String name, Point ... mapCoords) {
			this.id = id;
			this.name = name;
			this.mapCoords = mapCoords;
		}
		
	}
	
	static {
		TileEntity.TILE_MAP = Images.getImage("tile_map");
	}
}
