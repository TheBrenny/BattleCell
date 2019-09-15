package z5217759.brennfleck.jarod.battlecell.entities;

import java.awt.image.BufferedImage;

import com.thebrenny.jumg.util.Angle;
import com.thebrenny.jumg.util.MathUtil;

public class EntityMagicBall extends EntityProjectile {
    protected Angle spinAngle;
    private int spinCounter = 0;
    private int spinMax = 50;

    public EntityMagicBall(float x, float y, BCEntity owner, BCEntity target) {
        super(x, y, Type.MAGIC_BALL, owner, target);
        this.spinAngle = new Angle(0);
    }

    public void tick() {
        super.tick();
        spinCounter = MathUtil.wrap(0, spinCounter++, spinMax);
        if(spinCounter == 0) spinAngle.changeAngle(90);
    }
    public BufferedImage getRawImage() {
        return spinAngle.getRotation(super.getRawImage());
    }
}