package _08final.mvc.model;

import java.awt.*;

public class FalconDebris extends Debris {

    public FalconDebris(Falcon falconExploded, double angle) {
        super(falconExploded, angle); // doesn't do anything. Used just to create super class object.
        setColor(Color.RED);
    }


}
