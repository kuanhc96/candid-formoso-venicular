package _08final.mvc.model;

import java.awt.*;

public class BloomingShotsFloater extends NewShipFloater {
    public BloomingShotsFloater() {
        super();
        setColor(Color.CYAN);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(getColor());
        g.fillOval((int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()), 2 * getRadius(), 2 * getRadius());
        g.drawString("+15 Blooming Shots", (int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()));
    }
}
