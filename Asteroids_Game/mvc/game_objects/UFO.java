package _08final.mvc.model;

import _08final.mvc.controller.Game;

import java.awt.*;
import java.util.Random;

public class UFO extends Sprite {

    private double falconAngle;
    private int hits;
    private Random rShoot;

    private static final int MAX_HITS = 5;
    public UFO() {
        super();
        setTeam(Team.FOE);
        Random rx = new Random();
        Random ry = new Random();
        setCenter(new Point(rx.nextInt(Game.DIM.width), ry.nextInt(Game.DIM.height)));
        setDeltaX(1);
        setDeltaY(1);
        setRadius(40);
        setOrientation(CommandCenter.getInstance().getFalcon().getOrientation());
        setExpire(Integer.MAX_VALUE);
        this.falconAngle = 0;
        rShoot = new Random();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius() / 4), getRadius() * 2, getRadius()/2);
        g.setColor(Color.WHITE);
        g.fillArc((int) (getCenter().getX() - getRadius() / 2), (int) (getCenter().getY() - getRadius() / 2), getRadius(),
                getRadius() - getRadius() / 4, 0,  180);
    }

    @Override
    public void move() {
        super.move();
        adjustAngle();
        int shoot = rShoot.nextInt(100);
        if (shoot <= 4) { // shoot every 5 seconds ?????
            shoot();
        }

    }

    public void adjustAngle() {

        this.falconAngle = Math.toDegrees(Math.atan((getCenter().getY() - CommandCenter.getInstance().getFalcon().getCenter().getY()) /
                (getCenter().getX() - CommandCenter.getInstance().getFalcon().getCenter().getX())));
        setOrientation((int) falconAngle);
    }

    public void shoot() {
        CommandCenter.getInstance().getOpsList().enqueue(new EnemyBullet(this), CollisionOp.Operation.ADD);
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public boolean toExplode() {
        return hits == MAX_HITS;
    }
}
