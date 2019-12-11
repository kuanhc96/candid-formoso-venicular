package _08final.mvc.model;

import java.awt.*;

public class StrongAsteroid extends Asteroid {
    private final int MAX_HITS = 3;
    private int hits;

    public StrongAsteroid(int nSize) {
        super(nSize);
        hits = 0;
    }

    public StrongAsteroid(StrongAsteroid asteroid) {
        super(asteroid);
        hits = 0;
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

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }

}
