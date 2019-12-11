package _08final.mvc.model;

import _08final.sounds.Sound;

import java.util.ArrayList;
import java.util.List;


public class CommandCenter {

	private long gameTime;

	private int nNumFalcon;
	private int nLevel;
	private long lScore;
	private Falcon falShip;
	private boolean bPlaying;
	private boolean bPaused;
	private long levelEpochTime;
	private long gameEpochTime;
	private boolean gameInitiated;
	private boolean gameTimedOut;
	private boolean gameKilled;
	private boolean gameCleared;


	// These ArrayLists with capacities set
	private List<Movable> movDebris = new ArrayList<Movable>(300);
	private List<Movable> movFriends = new ArrayList<Movable>(100);
	private List<Movable> movFoes = new ArrayList<Movable>(200);
	private List<Movable> movFloaters = new ArrayList<Movable>(50);

	private GameOpsList opsList = new GameOpsList();


	private static CommandCenter instance = null;

	// Constructor made private - static Utility class only
	private CommandCenter() {}


	public static CommandCenter getInstance(){
		if (instance == null){
			instance = new CommandCenter();
		}
		return instance;
	}


	public  void initGame(){
		setLevel(0);
		setScore(0);
		setNumFalcons(4);
		spawnFalcon(true);
		this.levelEpochTime = System.currentTimeMillis();
		this.gameEpochTime = System.currentTimeMillis();
		this.gameTime = 40000;
		this.gameInitiated = false;
		this.gameCleared = false;
		this.gameKilled = false;
		this.gameTimedOut = false;
		this.gameTimedOut = false;
		CommandCenter.getInstance().setGameInitiated(true);
//		this.elapsedTimeCounter = 0;
	}
	
	// The parameter is true if this is for the beginning of the game, otherwise false
	// When you spawn a new falcon, you need to decrement its number
	public  void spawnFalcon(boolean bFirst) {
		if (getNumFalcons() != 0) {
			falShip = new Falcon();
			opsList.enqueue(falShip, CollisionOp.Operation.ADD);
			if (!bFirst) {
				setNumFalcons(getNumFalcons() - 1);
			}
		} else {
			setGameKilled(true);
			setGameTimedOut(false);
		}
		
		Sound.playSound("shipspawn.wav");

	}

	public GameOpsList getOpsList() {
		return opsList;
	}

	public void setOpsList(GameOpsList opsList) {
		this.opsList = opsList;
	}

	public  void clearAll(){
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();
	}

	public  boolean isPlaying() {
		return bPlaying;
	}

	public  void setPlaying(boolean bPlaying) {
		this.bPlaying = bPlaying;
	}

	public  boolean isPaused() {
		return bPaused;
	}

	public  void setPaused(boolean bPaused) {
		this.bPaused = bPaused;
	}
	
	public  boolean isGameOver() {		//if the number of falcons is zero, then game over
		if (getNumFalcons() == 0) {
			return true;
		}
		return false;
	}

	public  int getLevel() {
		return nLevel;
	}

	public   long getScore() {
		return lScore;
	}

	public  void setScore(long lParam) {
		lScore = lParam;
	}

	public  void setLevel(int n) {
		nLevel = n;
	}

	public  int getNumFalcons() {
		return nNumFalcon;
	}

	public  void setNumFalcons(int nParam) {
		nNumFalcon = nParam;
	}
	
	public  Falcon getFalcon(){
		return falShip;
	}
	
	public  void setFalcon(Falcon falParam){
		falShip = falParam;
	}

	public  List<Movable> getMovDebris() {
		return movDebris;
	}



	public  List<Movable> getMovFriends() {
		return movFriends;
	}



	public  List<Movable> getMovFoes() {
		return movFoes;
	}


	public  List<Movable> getMovFloaters() {
		return movFloaters;
	}

	public void resetTimer() {
		levelEpochTime = System.currentTimeMillis();

	}

	public long getElapsedTime() {




		return System.currentTimeMillis() - levelEpochTime ;

	}

	public long getTotalElapsedTime() {
		return System.currentTimeMillis() - gameEpochTime;
	}

	public long getGameTime() {
		return gameTime;
	}

	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}

	public boolean getGameInitiated() {
		return gameInitiated;
	}

	public void setGameInitiated(boolean gameInitiated) {
		this.gameInitiated = gameInitiated;
	}

	public boolean getGameTimedOut() {
		return gameTimedOut;
	}

	public void setGameTimedOut(boolean gameTimedOut) {
		this.gameTimedOut = gameTimedOut;
	}

	public boolean getGameKilled() {
		return gameKilled;
	}

	public void setGameKilled(boolean gameKilled) {
		this.gameKilled = gameKilled;
	}

	public boolean getGameCleared() {
		return gameCleared;
	}

	public void setGameCleared(boolean gameCleared) {
		this.gameCleared = gameCleared;
	}

	public long getGameEpochTime(){
		return gameEpochTime;
	}


}
