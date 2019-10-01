package z5217759.brennfleck.jarod.battlecell.gui.components;

import java.awt.image.BufferedImage;

import com.thebrenny.jumg.gui.components.GuiImageLabel;
import com.thebrenny.jumg.util.Images;

import z5217759.brennfleck.jarod.battlecell.entities.BCEntity;
import z5217759.brennfleck.jarod.battlecell.entities.EntityDummy;

public class GuiCharacterPreview extends GuiImageLabel {
	private static final long serialVersionUID = 1L;
	protected BCEntity entity;
	
	public GuiCharacterPreview(float x, float y, float width, float height, BCEntity entity) {
		super(x, y, width, height, entity.getImage());
		this.entity = entity;
	}
	
	public void tick() {
		super.tick();
		entity.updateCounters();
		// only ticks when the mouse moves! :( fix this!
	}
	
	public GuiCharacterPreview changeCharacter(int charID) {
		if(!(entity instanceof EntityDummy)) return this;
		return changeCharacter(BCEntity.Type.getTypeByID(charID));
	}
	public GuiCharacterPreview changeCharacter(BCEntity.Type type) {
		if(entity instanceof EntityDummy) ((EntityDummy) entity).changeType(type);
		return this;
	}
	
	public BufferedImage getImage() {
		// 4, 1, 23, 31 are the x, y, w, h for the selection of JUST the character. No unnecessary transparent pixels.
		return Images.getResizedImage(entity.getImage().getSubimage(4, 1, 23, 31), (int) getWidth(), (int) getHeight());
	}
}
