import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
//Owned by Tony
// TODO: Auto-generated Javadoc
//Owned by Tony Wang
/**
 * The Class Elevator.
 *
 * @author This class will represent an elevator, and will contain
 * configuration information (capacity, speed, etc) as well
 * as state information - such as stopped, direction, and count
 * of passengers targetting each floor...
 */
public class Elevator {
	
	/** The Constant STOP. */
	// Elevator State Variables
	final static int STOP = 0;
	
	/** The Constant MVTOFLR. */
	final static int MVTOFLR = 1;
	
	/** The Constant OPENDR. */
	final static int OPENDR = 2;
	
	/** The Constant OFFLD. */
	final static int OFFLD = 3;
	
	/** The Constant BOARD. */
	final static int BOARD = 4;
	
	/** The Constant CLOSEDR. */
	final static int CLOSEDR = 5;
	
	/** The Constant MV1FLR. */
	final static int MV1FLR = 6;
	
	/** The Constant DOWN. */
	final static int DOWN = -1;
	
	/** The Constant UP. */
	final static int UP = 1;

	/** The capacity. */
	// Default configuration parameters - these will be read from a file....
	private int capacity = 15;
	
	/** The ticks per floor. */
	private int ticksPerFloor = 5;
	
	/** The ticks door open close. */
	private int ticksDoorOpenClose = 2;  
	
	/** The pass per tick. */
	private int passPerTick = 3;
	
	//State Variables
	/** The curr state. */
	// track the elevator state
	private int currState;
	
	/** The prev state. */
	private int prevState;
	
	/** The prev floor. */
	// track what floor you are on, and where you came from
	private int prevFloor;
	
	/** The curr floor. */
	private int currFloor;
	
	/** The direction. */
	// direction 1 = up, -1 = down
	private int direction;
	// timeInState is reset on state entry, used to determine if state is finished
	/** The time in state. */
	// or if floor has changed...
	private int timeInState;
	
	/** The door state. */
	// used to track where the the door is in OPENDR and CLOSEDR states 
	private int doorState;
	
	/** The num passengers. */
	// number of passengers on the elevator
	private int numPassengers;	
	
	/** list of passengers . */
	private ArrayList<Passengers> passengersList = new ArrayList<Passengers>();
	// when exiting the STOP ==> MVTOFLR, the floor to moveTo and the direction to go in once you
	/** The move to floor. */
	// get there...
	private int moveToFloor;
	
	/** The move to floor dir. */
	private int moveToFloorDir;
	
	/**  table of delay values, initialized with construction of elevator. */
	private int[] delayTable;
	
	/** The elevator full. */
	private boolean elevatorFull;


	/**
	 * Instantiates a new elevator.
	 *
	 * @param capacity the capacity
	 * @param floorTicks the floor ticks
	 * @param doorTicks the door ticks
	 * @param tickPassengers the tick passengers
	 */
	// You need to update this constructor to configure the elevator and set any additional state as necessary.
	public Elevator (int capacity, int floorTicks, int doorTicks, int tickPassengers) {		
		this.prevState = STOP;
		this.currState = STOP;
		this.currFloor = 0;
		this.capacity = capacity;
		this.ticksPerFloor = floorTicks;
		this.ticksDoorOpenClose = doorTicks;
		this.passPerTick = tickPassengers;
		this.direction = 1;
		
		this.initDelayTable();
	}
	
	/**
	 * Update curr state.
	 *
	 * @param state the state
	 */
	public void updateCurrState(int state) {
		timeInState++;
		prevState = currState;
		//System.out.println("UPDATING CURRSTATE. TIME: " + timeInState + ". NEW STATE: " + state + ", CURRSTATE: " + currState);
		if (state != currState) {
			//System.out.println("h");
			currState = state;
			timeInState = 1;
		}
	}
	
	/**
	 * Adds the passengers.
	 *
	 * @param p the p
	 * @return true, if successful
	 */
	public boolean addPassengers(Passengers p) {
		if (numPassengers + p.getNumber() > capacity) return false;
		else {
			passengersList.add(p);
			numPassengers += p.getNumber();
			return true;
		}
	}
	
	/**
	 * Removes the passengers.
	 *
	 * @param index the index
	 * @return true, if successful
	 */
	public boolean removePassengers(int index) {
		if (numPassengers - passengersList.get(index).getNumber()<0) {
			return false;
		}
		else {
			numPassengers -= passengersList.get(index).getNumber();
			passengersList.remove(index);
		}
		return true;
	}

	/**
	 * deletes passengers.
	 *
	 * @return true, if successful
	 */
//	public boolean killPassengers() {
//		if (passengersList.size() < 1) return false;
//		else {
//			passengersList;
//			return true;
//		}
//	}
	
	/**
	 * Move elevator 1 floor according to direction
	 */
	public void move() {
		timeInState++;
		this.prevFloor = this.currFloor;
		//System.out.println("MOVE CALLED: " + timeInState + ", " + this.ticksPerFloor);
		if (this.timeInState % this.ticksPerFloor == 0) {
			this.currFloor += this.direction;
		}
	}
	
	/**
	 * Checks if is door open.
	 *
	 * @return true, if is door open
	 */
	public boolean isDoorOpen() {
		return this.doorState == ticksDoorOpenClose;
	}
	
	/**
	 * Checks if is door closed.
	 *
	 * @return true, if is door closed
	 */
	public boolean isDoorClosed() {
		return this.doorState == 0;
	}
	
	/**
	 * Open door.
	 */
	public void openDoor() {
		prevFloor = currFloor;
		this.doorState++;
	}
	
	/**
	 * Close door.
	 */
	public void closeDoor() {
		this.doorState--;
		if (doorState <0) {
			doorState = 0;
		}
	}
	
	/**
	 * Stop.
	 */
	public void stop() {
		this.timeInState++;
	}

	/**
	 * Gets the capacity.
	 *
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Sets the capacity.
	 *
	 * @param capacity the new capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Gets the ticks per floor.
	 *
	 * @return the ticks per floor
	 */
	public int getTicksPerFloor() {
		return ticksPerFloor;
	}

	/**
	 * Sets the ticks per floor.
	 *
	 * @param ticksPerFloor the new ticks per floor
	 */
	public void setTicksPerFloor(int ticksPerFloor) {
		this.ticksPerFloor = ticksPerFloor;
	}

	/**
	 * Gets the ticks door open close.
	 *
	 * @return the ticks door open close
	 */
	public int getTicksDoorOpenClose() {
		return ticksDoorOpenClose;
	}
	
	/**
	 * Gets the num passengers.
	 *
	 * @return the num passengers
	 */
	public int getNumPassengers() {
		return this.numPassengers;
	}

	/**
	 * Sets the ticks door open close.
	 *
	 * @param ticksDoorOpenClose the new ticks door open close
	 */
	public void setTicksDoorOpenClose(int ticksDoorOpenClose) {
		this.ticksDoorOpenClose = ticksDoorOpenClose;
	}

	/**
	 * Gets the pass per tick.
	 *
	 * @return the pass per tick
	 */
	public int getPassPerTick() {
		return passPerTick;
	}

	/**
	 * Sets the pass per tick.
	 *
	 * @param passPerTick the new pass per tick
	 */
	public void setPassPerTick(int passPerTick) {
		this.passPerTick = passPerTick;
	}

	/**
	 * Gets the curr state.
	 *
	 * @return the curr state
	 */
	public int getCurrState() {
		return currState;
	}

	/**
	 * Sets the curr state.
	 *
	 * @param currState the new curr state
	 */
	public void setCurrState(int currState) {
		this.currState = currState;
	}

	/**
	 * Gets the prev state.
	 *
	 * @return the prev state
	 */
	public int getPrevState() {
		return prevState;
	}

	/**
	 * Sets the prev state.
	 *
	 * @param prevState the new prev state
	 */
	public void setPrevState(int prevState) {
		this.prevState = prevState;
	}

	/**
	 * Gets the prev floor.
	 *
	 * @return the prev floor
	 */
	public int getPrevFloor() {
		return prevFloor;
	}

	/**
	 * Sets the prev floor.
	 *
	 * @param prevFloor the new prev floor
	 */
	public void setPrevFloor(int prevFloor) {
		this.prevFloor = prevFloor;
	}

	/**
	 * Gets the curr floor.
	 *
	 * @return the curr floor
	 */
	public int getCurrFloor() {
		return currFloor;
	}

	/**
	 * Sets the curr floor.
	 *
	 * @param currFloor the new curr floor
	 */
	public void setCurrFloor(int currFloor) {
		this.currFloor = currFloor;
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * there is an UP and a DOWN constant
	 * sets direction.
	 *
	 * @param direction the new direction
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	/**
	 * Change direction.
	 */
	public void changeDirection() {
		this.direction = -1 * direction;
	}

	/**
	 * Gets the time in state.
	 *
	 * @return the time in state
	 */
	public int getTimeInState() {
		return timeInState;
	}
	/**
	 * Gets the door state.
	 *
	 * @return the door state
	 */
	public int getDoorState() {
		return doorState;
	}

	/**
	 * Sets the door state.
	 *
	 * @param doorState the new door state
	 */
	public void setDoorState(int doorState) {
		this.doorState = doorState;
	}

	/**
	 * Gets the passengers.
	 *
	 * @return the passengers
	 */
	public int getPassengers() {
		return numPassengers;
	}

	/**
	 * Sets the passengers.
	 *
	 * @param passengers the new passengers
	 */
	public void setPassengers(int passengers) {
		this.numPassengers = passengers;
	}

	/**
	 * Gets the move to floor.
	 *
	 * @return the move to floor
	 */
	public int getMoveToFloor() {
		return moveToFloor;
	}

	/**
	 * Sets the move to floor.
	 *
	 * @param moveToFloor the new move to floor
	 */
	public void setMoveToFloor(int moveToFloor) {
		this.moveToFloor = moveToFloor;
	}

	/**
	 * Gets the move to floor dir.
	 *
	 * @return the move to floor dir
	 */
	public int getMoveToFloorDir() {
		return moveToFloorDir;
	}

	/**
	 * Sets the move to floor dir.
	 *
	 * @param moveToFloorDir the new move to floor dir
	 */
	public void setMoveToFloorDir(int moveToFloorDir) {
		this.moveToFloorDir = moveToFloorDir;
	}
	
	/**
	 * gets the list of passengers.
	 *
	 * @return the passengers list
	 */
	public ArrayList<Passengers> getPassengersList() {
		return passengersList;
	}
	
	
	/**
	 * if door is open.
	 *
	 * @return true, if successful
	 */
	public boolean doorOpen() {
		if (doorState == 2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * if door is closed.
	 *
	 * @return true, if successful
	 */
	public boolean doorClose() {
		if (doorState == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * delays .
	 *
	 * @param numOfPass the num of pass
	 * @return the int
	 */
	public int delay(int numOfPass) {
		return delayTable[numOfPass];
	}
	
	/**
	 * initializes delayTable.
	 */
	private void initDelayTable() {
		delayTable = new int[capacity+1];
		for (int i = 0; i <= capacity; i++) {
			delayTable[i] = (int)(Math.ceil((i * 1.0) / passPerTick));
		}
	}
	
	/**
	 * if floor has changed.
	 *
	 * @return true, if successful
	 */
	public boolean changeFloor() {
		if (currFloor != prevFloor) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if is elevator full.
	 *
	 * @return true, if is elevator full
	 */
	public boolean isElevatorFull() {
		return elevatorFull;
	}

	/**
	 * Sets the elevator full.
	 *
	 * @param elevatorFull the new elevator full
	 */
	public void setElevatorFull(boolean elevatorFull) {
		this.elevatorFull = elevatorFull;
	}

}