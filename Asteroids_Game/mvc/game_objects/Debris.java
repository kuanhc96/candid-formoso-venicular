package _08final.mvc.model;

import java.awt.*;

public class Debris extends Sprite {
    protected static final double DISTANCE_FROM_PREV = 3.5;
    protected static final int RAD = 100;

    // private Point center;
    private double angle;
    private int size;

    public Debris(Sprite itemExploded, double angle) {
        super();
        this.angle = angle;
        setTeam(Team.DEBRIS);
        setExpire(20);
        setRadius(RAD / itemExploded.getRadius());
        Point explosionLocation = itemExploded.getCenter();
        this.size =	itemExploded.getRadius() + 1;
        setDeltaX(DISTANCE_FROM_PREV * Math.cos(Math.toRadians(angle)));
        setDeltaY(DISTANCE_FROM_PREV * Math.sin(Math.toRadians(angle)));
        setCenter(new Point((int) explosionLocation.getX(), (int) explosionLocation.getY()));
        setColor(Color.YELLOW);
    }

    @Override
    public void move() {
        super.move();
        if (getExpire() == 0) {
            CommandCenter.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
        } else {
            setExpire(getExpire() - 1);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillOval((int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()), 2 * getRadius(), 2 * getRadius());
    }


    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
