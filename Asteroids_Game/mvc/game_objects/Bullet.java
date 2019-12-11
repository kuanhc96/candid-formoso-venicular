package _08final.mvc.model;

import java.awt.*;
import java.util.ArrayList;


public class Bullet extends Sprite {

	  private final double FIRE_POWER = 35.0;

	 private double angle;
	
public Bullet(Sprite fal){
		
		super();
	    setTeam(Team.FRIEND);
		
		//defined the points on a cartesean grid
		ArrayList<Point> pntCs = new ArrayList<Point>();
		
		pntCs.add(new Point(0,3)); //top point
		
		pntCs.add(new Point(1,-1));
		pntCs.add(new Point(0,-2));
		pntCs.add(new Point(-1,-1));

		assignPolarPoints(pntCs);

		//a bullet expires after 20 frames
	    setExpire( 20 );
	    setRadius(6);


	    //everything is relative to the falcon ship that fired the bullet
	    setDeltaX( fal.getDeltaX() +
	               Math.cos( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
	    setDeltaY( fal.getDeltaY() +
	               Math.sin( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
	    setCenter( fal.getCenter() );

	    //set the bullet orientation to the falcon (ship) orientation
	    setOrientation(fal.getOrientation());
		this.angle = 0;

	}

	public Bullet(Sprite fal, double deg) {
		this(fal);
		this.angle = deg;
		Point falconLocation = fal.getCenter();
		setDeltaX(Math.cos(Math.toRadians(angle)) * FIRE_POWER / 2);
		setDeltaY(Math.sin(Math.toRadians(angle)) * FIRE_POWER / 2);
		setCenter(new Point((int) falconLocation.getX(), (int) falconLocation.getY()));
	}

	@Override
	public void move(){

		super.move();

		if (getExpire() == 0)
			CommandCenter.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
		else
			setExpire(getExpire() - 1);

	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

}
