package _08final.mvc.model;

import java.awt.*;

public class ShieldFloater extends NewShipFloater {
    public ShieldFloater() {
        super();
        setColor(Color.BLUE);

    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(getColor());
        g.fillOval((int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()), 2 * getRadius(), 2 * getRadius());
        g.drawString("Shield", (int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()));
    }

}
