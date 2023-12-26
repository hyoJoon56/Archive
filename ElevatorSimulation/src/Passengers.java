// TODO: Auto-generated Javadoc
/**
 * The Class Passengers.
 */
//Owned By Joon Koo
public class Passengers {
	
	/** The id. */
	private static int ID=0;
	// this will be initialized in the constructor so that it is unique for each
	/** The id. */
	// set of Passengers - and then increment the static ID
	private int id;	
	// These will come from the csv file, and should be initialized in the 
	/** The time. */
	// constructor.
	private int time;
	
	/** The number. */
	private int number;
	
	/** The from floor. */
	private int fromFloor;
	
	/** The to floor. */
	private int toFloor;
	
	/** The polite. */
	private boolean polite = true;
	
	/** The wait time. */
	private int waitTime;
	// These fields will be initialized during run time - boardTime is when the group
	// starts getting on the elevator, timeArrived is when the elevator starts offloading
	/** The board time. */
	// at the desired floor
	private int boardTime;
	
	/** The time arrived. */
	private int timeArrived;
	
	/** The intended direction. */
	private int intendedDirection = 0;
	
	/**
	 * Instantiates a new passengers.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param fromFloor the from floor
	 * @param toFloor the to floor
	 * @param polite the polite
	 * @param wait the wait
	 */
	public Passengers(int time,int numPass,int fromFloor,int toFloor,boolean polite, int wait) {
		this.time = time;
		this.number = numPass;
		this.fromFloor = fromFloor;
		this.toFloor =toFloor;
		this.polite = polite;
		this.waitTime = wait;
		this.id = ID;
		ID++;
		if (this.fromFloor > this.toFloor) intendedDirection = -1; //they want to go down
		else if (this.fromFloor < this.toFloor) intendedDirection = 1; //they want to go up 
	}
	
	/**
	 * resets static id.
	 * ONLY FOR DEBUGGING
	 */
	static void resetStaticID() {
		ID = 0;
	}
	
	/**
	 * Sets the wait time of passenger group.
	 *
	 * @param waitTime the new wait time
	 */
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * Sets the board time of passenger group.
	 *
	 * @param boardTime the new board time
	 */
	public void setBoardTime(int boardTime) {
		this.boardTime = boardTime;
	}
	

	/**
	 * Sets the arrival time of the passenger group.
	 *
	 * @param timeArrived the new time arrived
	 */
	public void setTimeArrived(int timeArrived) {
		this.timeArrived = timeArrived;
	}

	/**
	 * Gets the direction of the passenger group.
	 *
	 * @return its intended direction
	 */
	public int getIntendedDirection() {
		return this.intendedDirection;
	}
	
	/**
	 * Gets the passengers' uniqueid.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * get the time that passenger showed up on the floor.
	 *
	 * @return the time
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Gets the number of people in the passenger group.
	 *
	 * @return the number of people in the passenger group
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * get the passenger group's current floor.
	 *
	 * @return its current floor
	 */
	public int getFromFloor() {
		return fromFloor;
	}
	
	/**
	 * get the passenger group's target floor.
	 *
	 * @return its target floor
	 */
	public int getToFloor() {
		return toFloor;
	}
	
	/**
	 * Checks if is polite.
	 *
	 * @return true, if is polite
	 */
	public boolean isPolite() {
		return polite;
	}
	
	
	/**
	 * Gets the wait time.
	 *
	 * @return the wait time
	 */
	public int getWaitTime() {
		return waitTime;
	}
	
	/**
	 * Change into polite.
	 */
	public void changeIntoPolite() {
		this.polite = true;
	}
	
	/**
	 * Gets the board time of the passenger group.
	 *
	 * @return board time
	 */
	public int getBoardTime() {
		return boardTime;
	}
	
	/**
	 * Gets the arrival time of the passenger group.
	 *
	 * @return arrival time
	 */
	public int getTimeArrived() {
		return timeArrived;
	}
	
	// TODO: Write the constructor for this class
	//       Remember to appropriately adjust toFloor and fromFloor 
	//       from American to European numbering...
	
	// TODO: Write the getters and setters for this method
	
	/**
	 * To string.
	 *
	 * @return number of passengers in one group in String
	 */
	public String toString() {
		return this.getNumber() + "";
	}
	
}
