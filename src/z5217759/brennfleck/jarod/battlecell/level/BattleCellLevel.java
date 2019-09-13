package z5217759.brennfleck.jarod.battlecell.level;

import com.thebrenny.jumg.level.Level;
import com.thebrenny.jumg.level.gen.GeneratorFromFile;

import z5217759.brennfleck.jarod.battlecell.BattleCell;
import z5217759.brennfleck.jarod.battlecell.level.tile.BCTile;

public class BattleCellLevel extends Level {
	public static final String MAP_LOCATION = BattleCell.getMainGame().getGameInfo().packageRoot() + ".level.maps";
	
	public BattleCellLevel() {
		super(new BattleCellGenerator());
	}
	
	public static class BattleCellGenerator extends GeneratorFromFile {
		public BattleCellGenerator() {
			super(MAP_LOCATION + ".main_map.bcmap", true, BCTile.getTile(BCTile.Type.STONE));
		}
	}
	public void expandMap(int x, int y) {
	}
}
