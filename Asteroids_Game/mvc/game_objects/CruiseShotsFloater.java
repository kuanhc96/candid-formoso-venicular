package _08final.mvc.model;

import java.awt.*;

public class CruiseShotsFloater extends NewShipFloater {
    public CruiseShotsFloater() {
        super();
        setColor(Color.RED.brighter());
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(getColor());
        g.fillOval((int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()), 2 * getRadius(), 2 * getRadius());
        g.drawString("+10 Cruise Shots", (int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()));
    }
}
