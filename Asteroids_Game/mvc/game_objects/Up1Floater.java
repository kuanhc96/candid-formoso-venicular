package _08final.mvc.model;

import java.awt.*;

public class Up1Floater extends NewShipFloater {
    public Up1Floater() {
        super();
        setColor(Color.GREEN);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(getColor());
        g.fillOval((int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()), 2 * getRadius(), 2 * getRadius());
        g.drawString("+1LP", (int) (getCenter().getX() - getRadius()), (int) (getCenter().getY() - getRadius()));
    }
}
