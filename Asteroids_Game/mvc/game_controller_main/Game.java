package _08final.mvc.controller;

import _08final.ShockWave;
import _08final.mvc.model.*;
import _08final.mvc.view.GamePanel;
import _08final.sounds.Sound;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener {

	// ===============================================
	// FIELDS
	// ===============================================

	public static final Dimension DIM = new Dimension(1100, 900); //the dimension of the game.
	private GamePanel gmpPanel;
	public static Random R = new Random();
	public final static int ANI_DELAY = 45; // milliseconds between screen
											// updates (animation)
	private Thread thrAnim;
	private int nLevel = 1;
	private int nTick = 0;
	private long pauseTime;

	private boolean bMuted = true;
	

	private final int PAUSE = 80, // p key
			QUIT = 81, // q key
			LEFT = 37, // rotate left; left arrow
			RIGHT = 39, // rotate right; right arrow
			UP = 38, // thrust; up arrow
			START = 83, // s key
			FIRE = 32, // space key
			MUTE = 77, // m-key mute

	// for possible future use
	HYPER = 68, 					// d key
	SHIELD = 65, 				// a key arrow
	// NUM_ENTER = 10, 				// hyp
	 SPECIAL = 70; 					// fire special weapon;  F key

	private Clip clpThrust;
	private Clip clpMusicBackground;

	private static final int SPAWN_NEW_SHIP_FLOATER = 1200;
	private static final int BIG_ASTEROID_SCORE = 1;
	private static final int MEDIUM_ASTEROID_SCORE = 5;
	private static final int SMALL_ASTEROID_SCORE = 10;
	private static final int UFO_SCORE = 20;
	private static final int FLOATER_SCORE = 5;

	// ===============================================
	// ==CONSTRUCTOR
	// ===============================================

	public Game() {

		gmpPanel = new GamePanel(DIM);
		gmpPanel.addKeyListener(this);
		clpThrust = Sound.clipForLoopFactory("whitenoise.wav");
		clpMusicBackground = Sound.clipForLoopFactory("music-background.wav");
	

	}

	// ===============================================
	// ==METHODS
	// ===============================================

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() { // uses the Event dispatch thread from Java 5 (refactored)
					public void run() {
						try {
							Game game = new Game(); // construct itself
							game.fireUpAnimThread();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void fireUpAnimThread() { // called initially
		if (thrAnim == null) {
			thrAnim = new Thread(this); // pass the thread a runnable object (this)
			thrAnim.start();
		}
	}

	// implements runnable - must have run method
	public void run() {

		// lower this thread's priority; let the "main" aka 'Event Dispatch'
		// thread do what it needs to do first
		thrAnim.setPriority(Thread.MIN_PRIORITY);

		// and get the current time
		long lStartTime = System.currentTimeMillis();

		// this thread animates the scene
		while (Thread.currentThread() == thrAnim) {
			tick();
			spawnNewShipFloater();
			gmpPanel.update(gmpPanel.getGraphics()); // update takes the graphics context we must 
														// surround the sleep() in a try/catch block
														// this simply controls delay time between 
														// the frames of the animation

			//this might be a good place to check for collisions
			checkCollisions();
			//this might be a god place to check if the level is clear (no more foes)
			//if the level is clear then spawn some big asteroids -- the number of asteroids 
			//should increase with the level. 
			checkNewLevel();
			checkTimeElapsed();

			try {
				// The total amount of time is guaranteed to be at least ANI_DELAY long.  If processing (update) 
				// between frames takes longer than ANI_DELAY, then the difference between lStartTime - 
				// System.currentTimeMillis() will be negative, then zero will be the sleep time
				lStartTime += ANI_DELAY;
				Thread.sleep(Math.max(0,
						lStartTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// just skip this frame -- no big deal
				continue;
			}
		} // end while
	} // end run

	private void checkCollisions() {

		

		Point pntFriendCenter, pntFoeCenter;
		int nFriendRadiux, nFoeRadiux;

		for (Movable movFriend : CommandCenter.getInstance().getMovFriends()) {
			for (Movable movFoe : CommandCenter.getInstance().getMovFoes()) {

				pntFriendCenter = movFriend.getCenter();
				pntFoeCenter = movFoe.getCenter();
				nFriendRadiux = movFriend.getRadius();
				nFoeRadiux = movFoe.getRadius();

				//detect collision
				if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {

					//falcon
					if ((movFriend instanceof Falcon) ) {
						if (!CommandCenter.getInstance().getFalcon().getUpgraded()) {
							if (!CommandCenter.getInstance().getFalcon().getProtected()) {
								CommandCenter.getInstance().getOpsList().enqueue(movFriend, CollisionOp.Operation.REMOVE);
								for (int deg = 0; deg < 360; deg += 20) {
									CommandCenter.getInstance().getOpsList().enqueue(new FalconDebris(CommandCenter.getInstance().getFalcon(), deg), CollisionOp.Operation.ADD);
								}
								CommandCenter.getInstance().spawnFalcon(false);
							}
						}
					}
					//not the falcon
					else {
						if (movFriend instanceof Cruise) {
							CommandCenter.getInstance().getOpsList().enqueue(movFriend, CollisionOp.Operation.REMOVE);
							CommandCenter.getInstance().getOpsList().enqueue(new ShockWave((Cruise) movFriend), CollisionOp.Operation.ADD);
						} else if (!(movFriend instanceof ShockWave)) {
							CommandCenter.getInstance().getOpsList().enqueue(movFriend, CollisionOp.Operation.REMOVE);
						}

					}//end else
					//kill the foe and if asteroid, then spawn new asteroids
					killFoe(movFoe);
					Sound.playSound("kapow.wav");

				}//end if 
			}//end inner for
		}//end outer for


		//check for collisions between falcon and floaters
		if (CommandCenter.getInstance().getFalcon() != null){
			Point pntFalCenter = CommandCenter.getInstance().getFalcon().getCenter();
			int nFalRadiux = CommandCenter.getInstance().getFalcon().getRadius();
			Point pntFloaterCenter;
			int nFloaterRadiux;
			
			for (Movable movFloater : CommandCenter.getInstance().getMovFloaters()) {
				pntFloaterCenter = movFloater.getCenter();
				nFloaterRadiux = movFloater.getRadius();
	
				//detect collision
				if (pntFalCenter.distance(pntFloaterCenter) < (nFalRadiux + nFloaterRadiux)) {
					CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + FLOATER_SCORE);
					CommandCenter.getInstance().getFalcon().setColor(((NewShipFloater) movFloater).getColor());
					CommandCenter.getInstance().getFalcon().setFadeValue(0);
					CommandCenter.getInstance().getFalcon().setUpgraded(true);
					CommandCenter.getInstance().getFalcon().setbFirst(true);
					CommandCenter.getInstance().getOpsList().enqueue(movFloater, CollisionOp.Operation.REMOVE);
					if (movFloater instanceof Up1Floater) {
						CommandCenter.getInstance().setNumFalcons(CommandCenter.getInstance().getNumFalcons() + 1);
					} else if (movFloater instanceof BloomingShotsFloater) {
						CommandCenter.getInstance().getFalcon().setBloomingShots(CommandCenter.getInstance().getFalcon().getBloomingShots() + 15);
					} else if (movFloater instanceof CruiseShotsFloater) {
						CommandCenter.getInstance().getFalcon().setCruiseShots(CommandCenter.getInstance().getFalcon().getCruiseShots() + 10);
					} else if (movFloater instanceof ShieldFloater) {
						CommandCenter.getInstance().getFalcon().setnShield(CommandCenter.getInstance().getFalcon().getnShield() + 1);
					} else if (movFloater instanceof HyperFLoater) {
						CommandCenter.getInstance().getFalcon().setnShield(CommandCenter.getInstance().getFalcon().getnHyper() + 1);
					}
					Sound.playSound("pacman_eatghost.wav");
	
				}//end if 
			}//end inner for
		}//end if not null
		


		//we are dequeu ing the opsList and performing operations in serial to avoid mutating the movable arraylists while iterating them above
		while(!CommandCenter.getInstance().getOpsList().isEmpty()){
			CollisionOp cop =  CommandCenter.getInstance().getOpsList().dequeue();
			Movable mov = cop.getMovable();
			CollisionOp.Operation operation = cop.getOperation();

			switch (mov.getTeam()){
				case FOE:
					if (operation == CollisionOp.Operation.ADD){
						CommandCenter.getInstance().getMovFoes().add(mov);
					} else {
						CommandCenter.getInstance().getMovFoes().remove(mov);
					}

					break;
				case FRIEND:
					if (operation == CollisionOp.Operation.ADD){
						CommandCenter.getInstance().getMovFriends().add(mov);
					} else {
						CommandCenter.getInstance().getMovFriends().remove(mov);
					}
					break;

				case FLOATER:
					if (operation == CollisionOp.Operation.ADD){
						CommandCenter.getInstance().getMovFloaters().add(mov);
					} else {
						CommandCenter.getInstance().getMovFloaters().remove(mov);
					}
					break;

				case DEBRIS:
					if (operation == CollisionOp.Operation.ADD){
						CommandCenter.getInstance().getMovDebris().add(mov);
					} else {
						CommandCenter.getInstance().getMovDebris().remove(mov);
					}
					break;


			}

		}
		//a request to the JVM is made every frame to garbage collect, however, the JVM will choose when and how to do this
		System.gc();
		
	}//end meth

	@SuppressWarnings("checkstyle:LineLength")
	private void killFoe(Movable movFoe) {
	    if (movFoe instanceof StrongAsteroid) {
	        if (((StrongAsteroid) movFoe).toExplode()) {
                //we know this is a StrongAsteroid, so we can cast without threat of ClassCastException
                StrongAsteroid astExploded = (StrongAsteroid) movFoe;
                //big asteroid
                if (astExploded.getSize() == 0){
                    //spawn two medium Asteroids
                    CommandCenter.getInstance().getOpsList().enqueue(new StrongAsteroid(astExploded), CollisionOp.Operation.ADD);
                    CommandCenter.getInstance().getOpsList().enqueue(new StrongAsteroid(astExploded), CollisionOp.Operation.ADD);
                    CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + BIG_ASTEROID_SCORE * 2);
                }
                //medium size aseroid exploded
                else if (astExploded.getSize() == 1){
                    //spawn three small Asteroids
                    CommandCenter.getInstance().getOpsList().enqueue(new StrongAsteroid(astExploded), CollisionOp.Operation.ADD);
                    CommandCenter.getInstance().getOpsList().enqueue(new StrongAsteroid(astExploded), CollisionOp.Operation.ADD);
                    CommandCenter.getInstance().getOpsList().enqueue(new StrongAsteroid(astExploded), CollisionOp.Operation.ADD);
                    CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + MEDIUM_ASTEROID_SCORE * 2);

                } else if (astExploded.getSize() == 2) { // when small asteroids exploded
                    for (int deg = 0; deg < 360; deg += 20) {
                        CommandCenter.getInstance().getOpsList().enqueue(new Debris(astExploded, deg), CollisionOp.Operation.ADD);
                    }
                    CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + SMALL_ASTEROID_SCORE * 2);
                }
				CommandCenter.getInstance().getOpsList().enqueue(movFoe, CollisionOp.Operation.REMOVE);
			} else {
	        	((StrongAsteroid) movFoe).setHits(((StrongAsteroid) movFoe).getHits() + 1);
			}

        } else if (movFoe instanceof Asteroid){
			//we know this is an Asteroid, so we can cast without threat of ClassCastException
			Asteroid astExploded = (Asteroid)movFoe;
			//big asteroid 
			if(astExploded.getSize() == 0){
				//spawn two medium Asteroids
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(astExploded), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(astExploded), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + BIG_ASTEROID_SCORE);
			} 
			//medium size aseroid exploded
			else if(astExploded.getSize() == 1){
				//spawn three small Asteroids
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(astExploded), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(astExploded), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(astExploded), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + MEDIUM_ASTEROID_SCORE);

			} else if (astExploded.getSize() == 2) { // when small asteroids exploded
				for (int deg = 0; deg < 360; deg += 20) {
					CommandCenter.getInstance().getOpsList().enqueue(new Debris(astExploded, deg), CollisionOp.Operation.ADD);
				}
				CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + SMALL_ASTEROID_SCORE);
			}
			CommandCenter.getInstance().getOpsList().enqueue(movFoe, CollisionOp.Operation.REMOVE);
		} else if (movFoe instanceof UFO){
	    	UFO ufoHit = (UFO) movFoe;
	    	if (((UFO) movFoe).toExplode()) {
				for (int deg = 0; deg < 360; deg += 20) {
					CommandCenter.getInstance().getOpsList().enqueue(new Debris(ufoHit, deg), CollisionOp.Operation.ADD);
				}
				CommandCenter.getInstance().getOpsList().enqueue(ufoHit, CollisionOp.Operation.REMOVE);
				CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + UFO_SCORE);
			} else {
	    		ufoHit.setHits(ufoHit.getHits() + 1);
			}
		}

		//remove the original Foe

	}

	//some methods for timing events in the game,
	//such as the appearance of UFOs, floaters (power-ups), etc. 
	public void tick() {
		if (nTick == Integer.MAX_VALUE)
			nTick = 0;
		else
			nTick++;
	}

	public int getTick() {
		return nTick;
	}

	private void spawnNewShipFloater() {
		//make the appearance of power-up dependent upon ticks and levels
		//the higher the level the more frequent the appearance
		if (getTick() % (SPAWN_NEW_SHIP_FLOATER - nLevel * 7) == 0) {
			Random r = new Random();
			int temp = r.nextInt(100);
			//CommandCenter.getInstance().getMovFloaters().enqueue(new NewShipFloater());
			if (temp < 20) {
				CommandCenter.getInstance().getOpsList().enqueue(new BloomingShotsFloater(), CollisionOp.Operation.ADD);
			} else if (temp < 40) {
				CommandCenter.getInstance().getOpsList().enqueue(new Up1Floater(), CollisionOp.Operation.ADD);

			} else if (temp < 60) {
				CommandCenter.getInstance().getOpsList().enqueue(new CruiseShotsFloater(), CollisionOp.Operation.ADD);
			} else if (temp < 80) {
				CommandCenter.getInstance().getOpsList().enqueue(new ShieldFloater(), CollisionOp.Operation.ADD);
			} else {
				CommandCenter.getInstance().getOpsList().enqueue(new HyperFLoater(), CollisionOp.Operation.ADD);

			}
		}
	}

	// Called when user presses 's'
	private void startGame() {
		CommandCenter.getInstance().clearAll();
		CommandCenter.getInstance().initGame();
		CommandCenter.getInstance().setLevel(0);
		CommandCenter.getInstance().setPlaying(true);
		CommandCenter.getInstance().setPaused(false);
		//if (!bMuted)
		   // clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
	}

	//this method spawns new asteroids
	private void spawnAsteroids(int nNum) {
		for (int nC = 0; nC < nNum; nC++) {
			//Asteroids with size of zero are big
			if (CommandCenter.getInstance().getLevel() < 1) {
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(0), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(0), CollisionOp.Operation.ADD);
			} else if (CommandCenter.getInstance().getLevel() < 3) {
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(0), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().getOpsList().enqueue(new StrongAsteroid(0), CollisionOp.Operation.ADD);
			} else if (CommandCenter.getInstance().getLevel() < 4){
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(0), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().getOpsList().enqueue(new UFO(), CollisionOp.Operation.ADD);
			} else if (CommandCenter.getInstance().getLevel() < 5) {
				CommandCenter.getInstance().getOpsList().enqueue(new StrongAsteroid(0), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().getOpsList().enqueue(new UFO(), CollisionOp.Operation.ADD);
			} else { // game Cleared
				CommandCenter.getInstance().setNumFalcons(0);
				CommandCenter.getInstance().setGameCleared(true);
				CommandCenter.getInstance().getOpsList().enqueue(new Asteroid(0), CollisionOp.Operation.ADD);
				CommandCenter.getInstance().setPlaying(false);
			}


			// CommandCenter.getInstance().getOpsList().enqueue(new UFO(), CollisionOp.Operation.ADD);
		}
	}
	
	
	private boolean isLevelClear(){
		//if there are no more Asteroids on the screen
		boolean bAsteroidFree = true;
		for (Movable movFoe : CommandCenter.getInstance().getMovFoes()) {

			if (movFoe instanceof Asteroid || movFoe instanceof UFO){
				bAsteroidFree = false;
				break;
			}
		}
		return bAsteroidFree;

		
	}
	
	private void checkNewLevel(){
		
		if (isLevelClear()){
			if (CommandCenter.getInstance().getFalcon() !=null)
				CommandCenter.getInstance().getFalcon().setProtected(true);

			spawnAsteroids(CommandCenter.getInstance().getLevel() + 1);
			CommandCenter.getInstance().setLevel(CommandCenter.getInstance().getLevel() + 1);
			CommandCenter.getInstance().resetTimer();

			if (CommandCenter.getInstance().getLevel() > 1) {
				CommandCenter.getInstance().getFalcon().setbFirst(true);
			}
			if (CommandCenter.getInstance().getLevel() % 3 == 0) {
				CommandCenter.getInstance().setGameTime(CommandCenter.getInstance().getGameTime() + 15000);
				CommandCenter.getInstance().setNumFalcons(CommandCenter.getInstance().getNumFalcons() + 2);
			}

		}
	}
	
	private void checkTimeElapsed() {
		if (CommandCenter.getInstance().getElapsedTime() >= CommandCenter.getInstance().getGameTime()) {
			CommandCenter.getInstance().setNumFalcons(0);
			CommandCenter.getInstance().setGameTimedOut(true);
		}
	}
	

	// Varargs for stopping looping-music-clips
	private static void stopLoopingSounds(Clip... clpClips) {
		for (Clip clp : clpClips) {
			clp.stop();
		}
	}

	// ===============================================
	// KEYLISTENER METHODS
	// ===============================================

	@Override
	public void keyPressed(KeyEvent e) {
		Falcon fal = CommandCenter.getInstance().getFalcon();
		int nKey = e.getKeyCode();
		// System.out.println(nKey);

		if (nKey == START && !CommandCenter.getInstance().isPlaying())
			startGame();

		if (fal != null) {

			switch (nKey) {
			case PAUSE:
				CommandCenter.getInstance().setPaused(!CommandCenter.getInstance().isPaused());
				if (CommandCenter.getInstance().isPaused()) {
					stopLoopingSounds(clpMusicBackground, clpThrust);

				}
				else
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case QUIT:
				System.exit(0);
				break;
			case UP:
				fal.thrustOn();
				if (!CommandCenter.getInstance().isPaused())
					clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case LEFT:
				fal.rotateLeft();
				break;
			case RIGHT:
				fal.rotateRight();
				break;

			// possible future use
			// case KILL:
			// case NUM_ENTER:

			default:
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Falcon fal = CommandCenter.getInstance().getFalcon();
		int nKey = e.getKeyCode();
		 System.out.println(nKey);

		if (fal != null) {
			switch (nKey) {
			case FIRE:
				CommandCenter.getInstance().getOpsList().enqueue(new Bullet(fal), CollisionOp.Operation.ADD);
				Sound.playSound("laser.wav");
				break;
				
			//special is a special weapon, current it just fires the cruise missile. 
			case SPECIAL:
				if (CommandCenter.getInstance().getFalcon().getBloomingShots() > 0) {
					for (int deg = 0; deg < 360; deg += 20) {
						CommandCenter.getInstance().getOpsList().enqueue(new Bullet(fal, deg), CollisionOp.Operation.ADD);
					}
					CommandCenter.getInstance().getFalcon().setBloomingShots(CommandCenter.getInstance().getFalcon().getBloomingShots() - 1);
				} else if (CommandCenter.getInstance().getFalcon().getCruiseShots() > 0) {
					CommandCenter.getInstance().getOpsList().enqueue(new Cruise(fal), CollisionOp.Operation.ADD);
					CommandCenter.getInstance().getFalcon().setCruiseShots(CommandCenter.getInstance().getFalcon().getCruiseShots() - 1);
				}
				//Sound.playSound("laser.wav");
				break;
				
			case LEFT:
				fal.stopRotating();
				break;
			case RIGHT:
				fal.stopRotating();
				break;
			case UP:
				fal.thrustOff();
				clpThrust.stop();
				break;
				
			case MUTE:
				if (!bMuted){
					stopLoopingSounds(clpMusicBackground);
					bMuted = !bMuted;
				} 
				else {
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
					bMuted = !bMuted;
				}
				break;

			case SHIELD:
				if (CommandCenter.getInstance().getFalcon().getnShield() > 0) {
					CommandCenter.getInstance().getFalcon().setbFirst(false);
					CommandCenter.getInstance().getFalcon().setProtected(true);
					CommandCenter.getInstance().getFalcon().setnShield(CommandCenter.getInstance().getFalcon().getnShield() - 1) ;
				}
				break;

			case HYPER:
				if (CommandCenter.getInstance().getFalcon().getnHyper() > 0) {
					Random rx = new Random();
					Random ry = new Random();
					int hyperX = rx.nextInt(DIM.width);
					int hyperY = ry.nextInt(DIM.height);
					CommandCenter.getInstance().getFalcon().setCenter(new Point(hyperX, hyperY));
					CommandCenter.getInstance().getFalcon().setbFirst(true);
					CommandCenter.getInstance().getFalcon().setProtected(true);
					CommandCenter.getInstance().getFalcon().setnHyper(CommandCenter.getInstance().getFalcon().getnHyper() - 1) ;
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	// Just need it b/c of KeyListener implementation
	public void keyTyped(KeyEvent e) {
	}

}


