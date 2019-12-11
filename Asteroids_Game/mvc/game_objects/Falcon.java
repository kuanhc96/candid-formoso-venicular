package _08final.mvc.model;

import _08final.mvc.controller.Game;

import java.awt.*;
import java.util.ArrayList;


public class Falcon extends Sprite {

	// ==============================================================
	// FIELDS 
	// ==============================================================
	
	private final double THRUST = .65;

	final int DEGREE_STEP = 7;
	
	private boolean bShield = false;
	private boolean bFlame = false;
	private boolean bProtected; //for fade in and out
	
	private boolean bThrusting = false;
	private boolean bTurningRight = false;
	private boolean bTurningLeft = false;
	
	private int nShield;
	private int nHyper;
	private boolean upgraded;
			
	private final double[] FLAME = { 23 * Math.PI / 24 + Math.PI / 2,
			Math.PI + Math.PI / 2, 25 * Math.PI / 24 + Math.PI / 2 };

	private int[] nXFlames = new int[FLAME.length];
	private int[] nYFlames = new int[FLAME.length];

	private Point[] pntFlames = new Point[FLAME.length];

	private int bloomingShots;

	private int cruiseShots;

	private boolean bFirst;

	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public Falcon() {
		super();
		setTeam(Team.FRIEND);
		ArrayList<Point> pntCs = new ArrayList<Point>();
		
		// Robert Alef's awesome falcon design
		pntCs.add(new Point(0,9));
		pntCs.add(new Point(-1, 6));
		pntCs.add(new Point(-1,3));
		pntCs.add(new Point(-4, 1));
		pntCs.add(new Point(4,1));
		pntCs.add(new Point(-4,1));

		pntCs.add(new Point(-4, -2));
		pntCs.add(new Point(-1, -2));
		pntCs.add(new Point(-1, -9));
		pntCs.add(new Point(-1, -2));
		pntCs.add(new Point(-4, -2));

		pntCs.add(new Point(-10, -8));
		pntCs.add(new Point(-5, -9));
		pntCs.add(new Point(-7, -11));
		pntCs.add(new Point(-4, -11));
		pntCs.add(new Point(-2, -9));
		pntCs.add(new Point(-2, -10));
		pntCs.add(new Point(-1, -10));
		pntCs.add(new Point(-1, -9));
		pntCs.add(new Point(1, -9));
		pntCs.add(new Point(1, -10));
		pntCs.add(new Point(2, -10));
		pntCs.add(new Point(2, -9));
		pntCs.add(new Point(4, -11));
		pntCs.add(new Point(7, -11));
		pntCs.add(new Point(5, -9));
		pntCs.add(new Point(10, -8));
		pntCs.add(new Point(4, -2));

		pntCs.add(new Point(1, -2));
		pntCs.add(new Point(1, -9));
		pntCs.add(new Point(1, -2));
		pntCs.add(new Point(4,-2));


		pntCs.add(new Point(4, 1));
		pntCs.add(new Point(1, 3));
		pntCs.add(new Point(1,6));
		pntCs.add(new Point(0,9));

		assignPolarPoints(pntCs);

		setColor(Color.white);
		
		//put falcon in the middle.
		setCenter(new Point(Game.DIM.width / 2, Game.DIM.height / 2));
		
		//with random orientation
		setOrientation(Game.R.nextInt(360));
		
		//this is the size of the falcon
		setRadius(35);

		//these are falcon specific
		setProtected(true);
		setFadeValue(0);
		bloomingShots = 10;
		cruiseShots = 5;
		nShield = 1;
		bShield = false;
		bFirst = true;
		nHyper = 1;
		upgraded = false;
	}
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================
	@Override
	public void move() {
		super.move();
		if (bThrusting) {
			bFlame = true;
			double dAdjustX = Math.cos(Math.toRadians(getOrientation()))
					* THRUST;
			double dAdjustY = Math.sin(Math.toRadians(getOrientation()))
					* THRUST;
			setDeltaX(getDeltaX() + dAdjustX);
			setDeltaY(getDeltaY() + dAdjustY);
		}
		if (bTurningLeft) {

			if (getOrientation() <= 0 && bTurningLeft) {
				setOrientation(360);
			}
			setOrientation(getOrientation() - DEGREE_STEP);
		} 
		if (bTurningRight) {
			if (getOrientation() >= 360 && bTurningRight) {
				setOrientation(0);
			}
			setOrientation(getOrientation() + DEGREE_STEP);
		}


		//implementing the fadeInOut functionality - added by Dmitriy
		if (getProtected()) {
			if (bFirst) {
				setFadeValue(getFadeValue() + 3);
			} else {
				setFadeValue(getFadeValue() + 1);
			}
		}

		if (getUpgraded() && getFadeValue() < 255) {
			setFadeValue((getFadeValue() + 3));
		}

		if (getFadeValue() == 255) {
			setUpgraded(false);
			setProtected(false);
			setbFirst(false);
		}



	} //end move

	public void rotateLeft() {
		bTurningLeft = true;
	}

	public void rotateRight() {
		bTurningRight = true;
	}

	public void stopRotating() {
		bTurningRight = false;
		bTurningLeft = false;
	}

	public void thrustOn() {
		bThrusting = true;
	}

	public void thrustOff() {
		bThrusting = false;
		bFlame = false;
	}

	private int adjustColor(int nCol, int nAdj) {
		if (nCol - nAdj <= 0) {
			return 0;
		} else {
			return nCol - nAdj;
		}
	}

	@Override
	public void draw(Graphics g) {

		//does the fading at the beginning or after hyperspace
		Color colShip;
		if (getFadeValue() >= 255) {
			colShip = Color.white;
			setColor(Color.BLUE);
		} else {
			if (getColor() == Color.BLUE) { // Shield
				colShip = new Color(adjustColor(getFadeValue(), 200), adjustColor(
						getFadeValue(), 175), getFadeValue());
			} else if (getColor() == Color.RED) { // +10 cruise shots
				colShip = new Color(getFadeValue(), 0, 0);
			} else if (getColor() == Color.GREEN) { // 1UP
				System.out.println("r: " + adjustColor(getFadeValue(), 175) + " g: " + getFadeValue() + " b: " +  adjustColor(getFadeValue(), 200));
				colShip = new Color(adjustColor(
						getFadeValue(), 175), getFadeValue(), adjustColor(getFadeValue(), 200));
			} else if (getColor() == Color.CYAN) { // blooming shots
				System.out.println("r: " + 0 + " g: " + getFadeValue() + " b: " +  getFadeValue());
				colShip = new Color(0, getFadeValue(), getFadeValue());
			} else if (getColor() == Color.ORANGE) {
				colShip = new Color(getFadeValue(), adjustColor(
						getFadeValue(), 90), 0);
			} else {
				colShip = new Color(adjustColor(getFadeValue(), 200), adjustColor(
						getFadeValue(), 175), getFadeValue());
			}


		}



		//thrusting
		if (bFlame) {
			g.setColor(colShip);
			//the flame
			for (int nC = 0; nC < FLAME.length; nC++) {
				if (nC % 2 != 0) //odd
				{
					pntFlames[nC] = new Point((int) (getCenter().x + 2
							* getRadius()
							* Math.sin(Math.toRadians(getOrientation())
									+ FLAME[nC])), (int) (getCenter().y - 2
							* getRadius()
							* Math.cos(Math.toRadians(getOrientation())
									+ FLAME[nC])));

				} else //even
				{
					pntFlames[nC] = new Point((int) (getCenter().x + getRadius()
							* 1.1
							* Math.sin(Math.toRadians(getOrientation())
									+ FLAME[nC])),
							(int) (getCenter().y - getRadius()
									* 1.1
									* Math.cos(Math.toRadians(getOrientation())
											+ FLAME[nC])));

				} //end even/odd else

			} //end for loop

			for (int nC = 0; nC < FLAME.length; nC++) {
				nXFlames[nC] = pntFlames[nC].x;
				nYFlames[nC] = pntFlames[nC].y;

			} //end assign flame points

			//g.setColor( Color.white );
			g.fillPolygon(nXFlames, nYFlames, FLAME.length);

		} //end if flame

		drawShipWithColor(g, colShip);

	} //end draw()

	public void drawShipWithColor(Graphics g, Color col) {
		super.draw(g);
		g.setColor(col);
		g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
	}


	public void setProtected(boolean bParam) {
		if (bParam) {
			setFadeValue(0);
		}
		bProtected = bParam;
	}


	public boolean getProtected() {return bProtected;}

	public int getBloomingShots() {
		return bloomingShots;
	}

	public void setBloomingShots(int shots) {
		bloomingShots = shots;
	}

	public int getCruiseShots() {
		return cruiseShots;
	}

	public void setCruiseShots(int shots) {
		cruiseShots = shots;
	}

	public int getnShield() {
		return nShield;
	}

	public void setnShield(int nShield) {
		this.nShield = nShield;
	}

	public boolean getbFirst() {
		return bFirst;
	}

	public void setbFirst(boolean bFirst) {
		this.bFirst = bFirst;
	}

	public int getnHyper() {
		return nHyper;
	}

	public boolean getUpgraded() {
		return upgraded;
	}

	public void setUpgraded(boolean upgraded) {
		this.upgraded = upgraded;
	}

	public void setnHyper(int nHyper) {
		this.nHyper = nHyper;
	}

	
} //end class
