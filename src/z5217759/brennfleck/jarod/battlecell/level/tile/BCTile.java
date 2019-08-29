package z5217759.brennfleck.jarod.battlecell.level.tile;

import com.thebrenny.jumg.level.tiles.Tile;

public class BCTile extends Tile {
	public BCTile(Type type) {
		super(type.name, type.id, type.mapX, type.mapY);
		this.setSolid(type.isSolid);
	}
	
	public static Tile getTile(Type type) {
		return Tile.getTile(type.id);
	}
	
	public static enum Type {
		DIRT(0, "dirt", 0, 0, false),
		GRASS(1, "grass", 1, 0, false),
		STONE(2, "stone", 0, 1, true);
		
		public final int id;
		public final String name;
		public final int mapX;
		public final int mapY;
		public final boolean isSolid;
		
		private Type(int id, String name, int mapX, int mapY, boolean isSolid) {
			this.id = id;
			this.name = name;
			this.mapX = mapX;
			this.mapY = mapY;
			this.isSolid = isSolid;
		}
	}
}
