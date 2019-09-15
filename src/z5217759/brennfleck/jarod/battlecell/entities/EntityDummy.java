package z5217759.brennfleck.jarod.battlecell.entities;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class EntityDummy extends BCEntity {
    public EntityDummy(String name, float x, float y) {
        super(name, x, y, Type.DUMMY);
        setCounterTrigger(new int[] {20, 40});
    }
    public void update() {
        //this.setLocation(KeyBindings.MOUSE_POINT);
    }
    
    public void setLocation(Point2D location) {
        this.boundingBox.x = (float) location.getX();
        this.boundingBox.y = (float) location.getY();
    }
    
    public BufferedImage getRawImage() {
        if(this.image == null) this.image = this.getBaseImage();
        return this.image;
    }
    public void onAnimationStateChanged(AnimationState state) {
    }
    public void attack(BCEntity target) {
    }
}
