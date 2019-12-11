package _08final.mvc.model;

import java.awt.*;

public class HyperFLoater extends NewShipFloater {

    public HyperFLoater() {
        super();
        setColor(Color.ORANGE);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(getColor());
        g.fillOval((int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()), 2 * getRadius(), 2 * getRadius());
        g.drawString("Hyper", (int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()));
    }
}
