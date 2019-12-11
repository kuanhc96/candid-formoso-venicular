package _08final;

import _08final.mvc.model.CollisionOp;
import _08final.mvc.model.CommandCenter;
import _08final.mvc.model.Cruise;
import _08final.mvc.model.Sprite;

import java.awt.*;

public class ShockWave extends Sprite {
    private int expansionFactor;
    public ShockWave(Cruise cruiseExploded) {
        super();
        this.expansionFactor = 1;
        setRadius(1);
        setTeam(Team.FRIEND);
        setExpire(30);
        Point explosionLocation = cruiseExploded.getCenter();
        setCenter(new Point((int) explosionLocation.getX(), (int) explosionLocation.getY()));
        setColor(Color.RED);
    }

    @Override
    public void move() {
        super.move();
        if (getExpire() == 0) {
            CommandCenter.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
        } else {
            setExpire(getExpire() - 1);
            expansionFactor+=8;
            setRadius(expansionFactor);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.drawOval((int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()), 2 * getRadius(), 2 * getRadius());
    }
}
