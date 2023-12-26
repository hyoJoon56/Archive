import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import myfileio.MyFileIO;
// TODO: Auto-generated Javadoc
//Owned by Joon Koo
/**
 * The Class Building.
 */
// TODO: Auto-generated Javadoc
public class Building {

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

	/** The Constant UP. */
	private final static int UP =1;

	/** The Constant DOWN. */
	private final static int DOWN = -1;

	/** The Constant LOGGER. */
	private final static Logger LOGGER = Logger.getLogger(Building.class.getName());

	/** The fh. */
	private FileHandler fh;

	/** The fio. */
	private MyFileIO fio;

	/** The pass data file. */
	private File passDataFile;

	/** The pass success. */
	// ArrayLists to store those passengers that arrived or gave up...
	private ArrayList<Passengers> passSuccess;

	/** The gave up. */
	private ArrayList<Passengers> gaveUp;

	/** The num floors. */
	// Elevator State Variables
	private final int NUM_FLOORS;

	/** The num elevators. */
	private final int NUM_ELEVATORS;

	/** The floors. */
	public Floor[] floors; // level 1 = floor[0]...

	/** The elevators. */
	private Elevator[] elevators;

	/** The elevator. */
	private Elevator elevator;

	/** The users. */
	private GenericQueue<Passengers> users;

	/** The total up. */
	private ArrayList<Passengers> totalUp = new ArrayList<Passengers>();

	/** The total down. */
	private ArrayList<Passengers> totalDown = new ArrayList<Passengers>();

	/** The passengers currently in the elevator. */
	private ArrayList<Passengers> passInelevator = new ArrayList<Passengers>();

	/** The passengers that have arrived. */
	private ArrayList<Passengers> passArrived = new ArrayList<Passengers>();

	/** The passengers that gave up. */
	private ArrayList<Passengers> passGaveUp = new ArrayList<Passengers>();

	/**  The most important group of passengers. */
	private Passengers priority;

	/** The delay. */
	private int delay;

	/**  amount of passengers going into the elevator. */
	private int boardPass;

	/** The skip passenger ID. */
	private int skipPassengerID = -1;

	/**
	 * Instantiates a new building.
	 *
	 * @param numFloors the num floors
	 * @param numElevators the num elevators	
	 * @param logfile the logfile
	 */
	public Building(int numFloors, int numElevators,String logfile) {
		Passengers.resetStaticID();
		NUM_FLOORS = numFloors; //6
		NUM_ELEVATORS = numElevators; //1
		elevators = new Elevator[NUM_ELEVATORS];
		users = new GenericQueue<Passengers>(1000);
		// Initialize the LOGGER - DO NOT CHANGE THIS!!!
		System.setProperty("java.util.logging.SimpleFormatter.format","%4$-7s %5$s%n");
		LOGGER.setLevel(Level.OFF);
		try {
			fh = new FileHandler(logfile);
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// passDataFile is where you will write all the results for those passengers who successfully
		// arrived at their destination and those who gave up...
		fio = new MyFileIO();
		passDataFile = fio.getFileHandle(logfile.replaceAll(".log","PassData.csv"));

		// create the floors and the elevator(s)
		// note that YOU will need to create and config each specific elevator...
		floors = new Floor[NUM_FLOORS];
		for (int i = 0; i < NUM_FLOORS; i++) {
			floors[i]= new Floor(10); 
		}
	}

	// DO NOT CHANGE ANYTHING BELOW THIS LINE:
	/**
	 * Update elevator - this is called AFTER time has been incremented.
	 * -  Logs any state changes, if the have occurred,
	 * -  Calls appropriate method based upon currState to perform
	 *    any actions and calculate next state..c.
	 *
	 * @param time the time
	 */
	// YOU WILL NEED TO CODE ANY MISSING METHODS IN THE APPROPRIATE CLASSES...
	public void updateElevator(int time) {
		for (Elevator lift: elevators) {
			if (elevatorStateChanged(lift))
				logElevatorStateChanged(time,lift.getPrevState(),lift.getCurrState(),lift.getPrevFloor(),lift.getCurrFloor());

			switch (lift.getCurrState()) {
			case Elevator.STOP: lift.updateCurrState(currStateStop(time,lift)); break;
			case Elevator.MVTOFLR: lift.updateCurrState(currStateMvToFlr(time,lift)); break;
			case Elevator.OPENDR: lift.updateCurrState(currStateOpenDr(time,lift)); break;
			case Elevator.OFFLD: lift.updateCurrState(currStateOffLd(time,lift)); break;
			case Elevator.BOARD: lift.updateCurrState(currStateBoard(time,lift)); break;
			case Elevator.CLOSEDR: lift.updateCurrState(currStateCloseDr(time,lift)); break;
			case Elevator.MV1FLR: lift.updateCurrState(currStateMv1Flr(time,lift)); break;

			}
		}
	}

	/**
	 * state method for Elevator.STOP state
	 *
	 * @param time the time
	 * @param lift the lift
	 * @return the int
	 */
	private int currStateStop(int time, Elevator lift) {
		elevator = lift;
		int up = floors[elevator.getCurrFloor()].getUpSize();
		int down = floors[elevator.getCurrFloor()].getDownSize();
		if (up > 0 && down == 0) {
			elevator.setDirection(UP);
			return OPENDR;
		} else if (down > 0 && up == 0) {
			elevator.setDirection(DOWN);
			return OPENDR;
		}else if (down>0 && up >0) {
			priority = callPrioritization();
			elevator.setDirection((priority.getIntendedDirection()==DOWN)? DOWN : UP);
			return OPENDR;
		} else if(upInputs()==true || downInputs()==true ) {
			priority = callPrioritization();
			elevator.setDirection((elevator.getCurrFloor() > priority.getFromFloor())? DOWN : UP);
			return MVTOFLR;
		} else {
			return STOP;
		}
	}
	
	/**
	 * Removes the multiple calls in the same floor. (assume those calls into one)
	 *
	 * @param people the people
	 * @return the int
	 */
	private int removeMultipleCallsInTheSameFloor(ArrayList<Passengers> people) {
		int count = people.size();
		if (count == 0) {
			return 0;
		}
		int frontCall = -1;
		for (int i = 0; i<count; i++) {
			int call = people.get(i).getFromFloor();
			if (call == frontCall) {
				count--;
			}
			else {
				frontCall = call;
			}
		}
		return count;
	}
	
	/**
	 * detect Opposite calls in the current floor.
	 *
	 * @param upPeople the up people
	 * @param downPeople the down people
	 * @param lift the lift
	 * @return true, if there are opposite calls
	 */
	private boolean oppositeCallsInTheCurrentFloor(ArrayList<Passengers> upPeople, ArrayList<Passengers> downPeople, Elevator lift) {
		int up = 0;
		int down = 0;
		for (int i = 0; i <upPeople.size();i++) {
			if (upPeople.get(i).getFromFloor()==lift.getCurrFloor()) {
				up++;
			}
		}
		for (int i = 0; i <downPeople.size();i++) {
			if (downPeople.get(i).getFromFloor()==lift.getCurrFloor()) {
				down++;
			}
		}
		if (up>0 && down>0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Recount based on the current floor. (modify upCount/downCount)
	 *
	 * @param people the people
	 * @param size the size
	 * @param direction the direction
	 * @param lift the lift
	 * @return the int
	 */
	private int recountBasedOnTheCurrentFloor(ArrayList<Passengers> people, int size, int direction, Elevator lift) {
		if (downInputs()== false || upInputs() == false) {
			return size;
		}
		if (oppositeCallsInTheCurrentFloor(totalUp, totalDown, lift) == false) {
			return size;
		}
		int count = size;
		if (direction == UP) {
			for (int i = 0; i<people.size(); i++) {
				if (people.get(i).getFromFloor()<lift.getCurrFloor()) {
					count--;
				}
			}
		}
		else {
			for (int i = 0; i<people.size(); i++) {
				if (people.get(i).getFromFloor()>lift.getCurrFloor()) {
					count--;
				}
			}
		}
		return count;
	}

	/**
	 * figures out who's the most important.
	 *
	 * @return the passengers
	 */
	private Passengers callPrioritization() {
		int upCount = removeMultipleCallsInTheSameFloor(totalUp);
		int downCount = removeMultipleCallsInTheSameFloor(totalDown);
		upCount = recountBasedOnTheCurrentFloor(totalUp, upCount, UP, elevator);
		downCount = recountBasedOnTheCurrentFloor(totalDown, downCount, DOWN, elevator);
		if (upCount>downCount) {
			return findLowestUp();
		}
		if (upCount<downCount) {
			return findHighestDown();
		}
		if (upCount == downCount) {
			if (Math.abs(findLowestUp().getFromFloor()-elevator.getCurrFloor())>Math.abs(findHighestDown().getFromFloor()-elevator.getCurrFloor())) {
				return findHighestDown();
			}
			else {
				return findLowestUp();			}
		}
		return findLowestUp();
	}

	/**
	 * find group who wants to go Up located in the lowest floor among all users.
	 *
	 * @return the passengers
	 */
	private Passengers findLowestUp() {
		Passengers passenger = floors[NUM_FLOORS-2].getHead(UP);
		for (int i = NUM_FLOORS-2; i>=0; i--) {
			if (floors[i].getHead(UP) != null) {
				passenger = floors[i].getHead(UP);
			}
		}
		return passenger;
	}

	/**
	 * find group who wants to go Down located in the highest floor among all users.
	 *
	 * @return the passengers
	 */
	private Passengers findHighestDown() {
		Passengers passenger = floors[1].getHead(DOWN);
		for (int i = 1; i < NUM_FLOORS; i++) {
			if (floors[i].getHead(DOWN)!= null) {
				passenger = floors[i].getHead(DOWN);
			}
		}
		return passenger;
	}

	/**
	 * Check there is up call that is not from the current floor.
	 *
	 * @return true, if there is a input like that
	 */
	private boolean upInputs() {
		for (int i =0; i<NUM_FLOORS-1; i++) {
			if (elevator.getCurrFloor() != i && floors[i].getUpSize() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check there is down call that is not from the current floor.
	 *
	 * @return true, if there is a input like that
	 */
	private boolean downInputs() {
		for (int i =1; i<NUM_FLOORS; i++) {
			if (elevator.getCurrFloor() != i && floors[i].getDownSize() > 0) {
				return true;
			}
		}
		return false;
	}


	/**
	 * state method for Elevator.MVTOFLR
	 *
	 * @param time (tick)
	 * @param lift (elevator)
	 * @return OPENDR or MVTOFLR
	 */
	private int currStateMvToFlr(int time, Elevator lift) {
		elevator = lift;
		elevator.move();
		if (elevator.getCurrFloor() == priority.getFromFloor()) {
			elevator.setDirection((priority.getFromFloor() > priority.getToFloor()) ? DOWN : UP);
			return OPENDR;
		}
		else {
			return MVTOFLR;
		}
	}

	/**
	 * state method for Elevator.OFFLD
	 *
	 * @param time (tick)
	 * @param lift (elevator)
	 * @return CLOSEDR or OFFLD
	 */
	private int currStateOffLd(int time, Elevator lift) {
		elevator = lift;
		Floor currFloor = floors[elevator.getCurrFloor()];
		if (elevator.getPrevState() != OFFLD) {
			offLd(time, elevator);
		}
		if (elevator.getTimeInState() >= delay) {
			delay = 0;
			if (callAndDirection()) {
				return BOARD;
			}
			if (passInelevator.size()== 0) {
				if (callInCurrentDirection(elevator) == false){
					if (elevator.getDirection() == UP && currFloor.getDownSize() >0) { //can't call callAndDirection() because they have opposite directions
						elevator.changeDirection();														//UP vs. getDownSize()
						return BOARD;																	//callAndDirection() only works at DOWN vs. getDownSize() or UP vs. getUpSize()
					}
					if (elevator.getDirection()==DOWN && currFloor.getUpSize()>0) { //can't call callAndDirection() because they have opposite directions
						elevator.changeDirection();
						return BOARD;
					}
				}
			}
			return CLOSEDR;
		} 
		else {

			return OFFLD;
		}
	}

	/**
	 * Call and direction.
	 * check that the elevator direction is same as the specific direction calls on the current floor
	 * @return true if that kind of calls exist. 
	 */
	private boolean callAndDirection() {
		if (elevator.getDirection() == UP && floors[elevator.getCurrFloor()].getUpSize() > 0) {
			return true;
		}
		if (elevator.getDirection() == DOWN && floors[elevator.getCurrFloor()].getDownSize() > 0) {
			return true;
		}
		return false;
	}



	/**
	 * Call from the other floor whose directions are same as the current direction.
	 *
	 * @param lift the lift
	 * @return true, if there is an input like that
	 */

	private boolean callInCurrentDirection(Elevator lift) {
		if (lift.getDirection() == UP) {
			for (int i = lift.getCurrFloor()+1; i<NUM_FLOORS; i++) {
				if (floors[i].getDownSize() > 0 || floors[i].getUpSize() > 0) {
					return true;
				}
			}
		} else {
			for (int i = lift.getCurrFloor()-1; i>= 0; i--) {
				if (floors[i].getDownSize() > 0 || floors[i].getUpSize() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * state method for Elevator.OPENDR
	 * 
	 * @param time the time
	 * @param lift the lift
	 * @return OFFLD, BOARD, CLOSEDR
	 */
	private int currStateOpenDr(int time, Elevator lift) {
		elevator = lift;
		elevator.openDoor();
		if (!elevator.doorOpen()){ 
			return OPENDR; 
		} else if (passInelevator.size() != 0 && exitFloor()==true) {
			return OFFLD;
		}

		if (callAndDirection()) {
			return BOARD;
		} else {
			return CLOSEDR;
		}
	}

	/**
	 * Exit floor.
	 *
	 * @return true, if there has a person to leave at the current floor
	 */
	private boolean exitFloor() {
		for (int i = 0; i < passInelevator.size(); i++) {
			if (passInelevator.get(i).getToFloor() == elevator.getCurrFloor()) {
				return true;
			}
		}
		return false;
	}






	/**
	 * helper method for Elevator.OFFLD
	 * count the exit number of the passengers on the current floor and remove the passengers in the elevator
	 * @param time the time
	 * @param lift the elevator
	 */
	private void offLd(int time, Elevator lift) {
		int exitNum = 0;
		for (int i = passInelevator.size() -1; i>=0; i--) {
			Passengers p = passInelevator.get(i);
			if (p.getToFloor() == lift.getCurrFloor()) {
				exitNum = exitNum + p.getNumber();
				p.setTimeArrived(time);
				passArrived.add(p);
				ArrayList <Passengers> dataSet = getTotalUpOrTotalDown(p.getIntendedDirection());
				for (int j = 0; j < dataSet.size(); j++) {
						if (dataSet.get(j).getId()==p.getId()) {
							this.removeDataInTotalUpOrTotalDown(p.getIntendedDirection(), j);
						}
				}
				logArrival(time, p.getNumber(), lift.getCurrFloor(),p.getId());
				lift.removePassengers(i);
				passInelevator.remove(i);
			}
		}
		delay = lift.delay(exitNum); 
	}
	/**
	 * state method for Elevator.BOARD
	 * 
	 * @param time the time
	 * @param lift the lift
	 * @return the int
	 */
	private int currStateBoard(int time, Elevator lift) {
		board(time,elevator);
		if (delay == 0) {
			lift.setElevatorFull(false);
			skipPassengerID = -1;
			return CLOSEDR;
		}
		if (elevator.getTimeInState() >= delay){ 
			delay = 0;
			skipPassengerID = -1;
			return CLOSEDR;
		}
		else {
			return BOARD;
		}
	}

	/**
	 * helper method for Elevator.BOARD
	 * adding passengers to elevator if there is a call on the current floor
	 * @param time the time
	 * @param lift the elevator
	 */	
	private void board(int time, Elevator lift) {
		if (lift.getDirection() == UP) {
			while (floors[lift.getCurrFloor()].getUpSize()>0) {
				if (addPassengersToElevator(time,lift,UP) == false) {
					break;
				}
			}
		} 
		else {
			while (floors[lift.getCurrFloor()].getDownSize()>0) {
				if (addPassengersToElevator(time,lift,DOWN) == false) {
					break;
				}
			}
		}
		delay = lift.delay(boardPass);
	}

	/**
	 * Adds the passengers to elevator.
	 *
	 * @param time the time
	 * @param lift the lift
	 * @param direction the direction
	 * @return true, if successful
	 */
	public boolean addPassengersToElevator(int time, Elevator lift ,int direction) {
		Passengers h = floors[lift.getCurrFloor()].getHead(direction);
		Floor currFloor = floors[lift.getCurrFloor()];
		if (time - h.getTime() > h.getWaitTime()) {
			passGaveUp.add(currFloor.removeHead(direction));
			ArrayList <Passengers> dataSet = getTotalUpOrTotalDown(h.getIntendedDirection());
				for (int j = 0; j < dataSet.size(); j++) {
					if (dataSet.get(j).getId()==h.getId()) {
						removeDataInTotalUpOrTotalDown(direction, j);
					}
				}
			logGiveUp(time, h.getNumber(), lift.getCurrFloor(), direction, h.getId());
		} 
		else if (totalPass() + h.getNumber() > lift.getCapacity()) {
			h.changeIntoPolite();
			if (skipPassengerID != h.getId()) {
				logSkip(time, h.getNumber(), lift.getCurrFloor(), direction, h.getId());
				skipPassengerID = h.getId();
			}
			return false;
		} 
		else {
			boardPass += h.getNumber();
			h.setBoardTime(time);
			passInelevator.add(currFloor.removeHead(direction));
			lift.addPassengers(h);
			logBoard(time, h.getNumber(), lift.getCurrFloor(), h.getIntendedDirection(), h.getId()); 
		}
		return true;
	}
	
	
	/**
	 * Gets the total up or total down.
	 *
	 * @param direction the direction
	 * @return the total up or total down
	 */
	private ArrayList<Passengers> getTotalUpOrTotalDown(int direction){
		if (direction == UP) {
			return totalUp;
		}
		return totalDown;
	}
	
	/**
	 * Removes the data in total up or total down.
	 *
	 * @param direction the direction
	 * @param index the index
	 */
	private void removeDataInTotalUpOrTotalDown(int direction, int index) {
		if (direction == UP) {
			totalUp.remove(index);
		}
		else {
			totalDown.remove(index);
		}
	}

	/**
	 * Total number of passengers in the elevator.
	 *
	 * @return int of numPassengers
	 */
	private int totalPass() {
		int count = 0;
		for (int i = 0; i< passInelevator.size(); i++) {
			count = count + passInelevator.get(i).getNumber();
		}
		return count;
	}




	/**
	 * state method for Elevator.CLOSEDR
	 *
	 * @param time the time
	 * @param lift the lift
	 * @return the int
	 */
	private int currStateCloseDr(int time, Elevator lift) { //must debug this 
		elevator = lift;
		boardPass = 0;
		elevator.closeDoor();
		Passengers h = floors[lift.getCurrFloor()].getHead(lift.getDirection());
		if (h != null && h.isPolite()== false) {
			h.changeIntoPolite();
			return OPENDR;
		}
		if (elevator.isDoorClosed()) {
			if (passInelevator.size()==0) {
				if (floors[elevator.getCurrFloor()].getDownSize() == 0 && floors[elevator.getCurrFloor()].getUpSize()==0) {
					if (upInputs() == false && downInputs() == false) {
						return STOP;
					}
					if (callInCurrentDirection(elevator)==true) {
						return MV1FLR;
					} else {
						elevator.changeDirection();
						return MV1FLR;
					}
				}
				if(elevator.getCurrFloor()+elevator.getDirection()>= this.NUM_FLOORS || elevator.getCurrFloor()+elevator.getDirection()<0) {
					elevator.changeDirection();
				}
				return MV1FLR;
			} else {
				return MV1FLR;
			}
		}
		return CLOSEDR;
	}


	/**
	 * state method for Elevator.MV1FLR
	 *
	 * @param time the time
	 * @param lift the lift
	 * @return the int
	 */
	private int currStateMv1Flr(int time, Elevator lift) {
		elevator = lift;
		elevator.move();
		if (elevator.changeFloor()) {
			if (exitFloor()) {
				return OPENDR;
			}

			if(callAndDirection()) {
				return OPENDR;
			}
			if (passInelevator.size()==0 && callInCurrentDirection(elevator)==false) {
				elevator.changeDirection();
				if(callAndDirection()) {
					return OPENDR;
				}
			}
		}
		return MV1FLR;
	}


	/**
	 * Process passenger data. Do NOT change this - it simply dumps the 
	 * collected passenger data for successful arrivals and give ups. These are
	 * assumed to be ArrayLists...
	 */
	public void processPassengerData() {
		if (users.size()==0) {
			return;
		}
		try {
			BufferedWriter out = fio.openBufferedWriter(passDataFile);
			out.write("ID,Number,From,To,WaitToBoard,TotalTime\n");
			for (Passengers p : passSuccess) {
				String str = p.getId()+","+p.getNumber()+","+(p.getFromFloor()+1)+","+(p.getToFloor()+1)+","+
						(p.getBoardTime() - p.getTime())+","+(p.getTimeArrived() - p.getTime())+"\n";
				out.write(str);
			}
			for (Passengers p : gaveUp) {
				String str = p.getId()+","+p.getNumber()+","+(p.getFromFloor()+1)+","+(p.getToFloor()+1)+","+
						p.getWaitTime()+",-1\n";
				out.write(str);
			}
			fio.closeFile(out);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Enable logging. Prints the initial configuration message.
	 * For testing, logging must be enabled BEFORE the run starts.
	 */
	public void enableLogging() {
		LOGGER.setLevel(Level.INFO);
		for (Elevator el:elevators) {
			logElevatorConfig(el.getCapacity(),el.getTicksPerFloor(), el.getTicksDoorOpenClose(), el.getPassPerTick(), el.getCurrState(), el.getCurrFloor());
		}


	}

	/**
	 * Close logs, and pause the timeline in the GUI.
	 *
	 * @param time the time
	 */
	public void closeLogs(int time) {
		if (LOGGER.getLevel() == Level.INFO) {
			logEndSimulation(time);
			fh.flush();
			fh.close();
		}
	}

	/**
	 * Prints the state.
	 *
	 * @param state the state
	 * @return the string
	 */
	private String printState(int state) {
		String str = "";

		switch (state) {
		case Elevator.STOP: 		str =  "STOP   "; break;
		case Elevator.MVTOFLR: 		str =  "MVTOFLR"; break;
		case Elevator.OPENDR:   	str =  "OPENDR "; break;
		case Elevator.CLOSEDR:		str =  "CLOSEDR"; break;
		case Elevator.BOARD:		str =  "BOARD  "; break;
		case Elevator.OFFLD:		str =  "OFFLD  "; break;
		case Elevator.MV1FLR:		str =  "MV1FLR "; break;
		default:					str =  "UNDEF  "; break;
		}
		return(str);
	}

	/**
	 * Gets the elevator state.
	 *
	 * @return the elevator state
	 */
	public int getElevatorState() {
		return elevator.getCurrState();
	}

	/**
	 * Gets the elevator state array.
	 *
	 * @return the elevator state array
	 */
	public int[] getElevatorStateArray() {
		int[] out = new int[elevators.length];
		for (int i = 0; i < elevators.length; i++) {
			out[i] = elevators[i].getCurrState();
		}
		return out;
	}

	/**
	 * Gets the elevator direction.
	 *
	 * @return the elevator direction
	 */
	public int getElevatorDirection() {
		return elevator.getDirection();
	}

	/**
	 * Gets the elevator direction array.
	 *
	 * @return the elevator direction array
	 */
	public int[] getElevatorDirectionArray() {
		int[] out = new int[elevators.length];
		for (int i = 0; i < elevators.length; i++) {
			out[i] = elevators[i].getDirection();
		}
		return out;
	}

	/**
	 * Log elevator config.
	 *
	 * @param capacity the capacity
	 * @param ticksPerFloor the ticks per floor
	 * @param ticksDoorOpenClose the ticks door open close
	 * @param passPerTick the pass per tick
	 * @param state the state
	 * @param floor the floor
	 */
	private void logElevatorConfig(int capacity, int ticksPerFloor, int ticksDoorOpenClose, int passPerTick, int state, int floor) {
		LOGGER.info("CONFIG:   Capacity="+capacity+"   Ticks-Floor="+ticksPerFloor+"   Ticks-Door="+ticksDoorOpenClose+
				"   Ticks-Passengers="+passPerTick+"   CurrState=" + (printState(state))+"   CurrFloor="+(floor+1));
	}

	/**
	 * Log elevator state changed.
	 *
	 * @param time the time
	 * @param prevState the prev state
	 * @param currState the curr state
	 * @param prevFloor the prev floor
	 * @param currFloor the curr floor
	 */
	private void logElevatorStateChanged(int time, int prevState, int currState, int prevFloor, int currFloor) {
		if (elevator.getCurrFloor()!=elevator.getPrevFloor() || elevator.getCurrState()!=elevator.getPrevState()) {
			LOGGER.info("Time="+time+"   Prev State: " + printState(prevState) + "   Curr State: "+printState(currState)
			+"   PrevFloor: "+(prevFloor+1) + "   CurrFloor: " + (currFloor+1));
		}
	}

	/**
	 * Log arrival.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param id the id
	 */
	private void logArrival(int time, int numPass, int floor,int id) {
		LOGGER.info("Time="+time+"   Arrived="+numPass+" Floor="+ (floor+1)
				+" passID=" + id);						
	}

	/**
	 * Log calls.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param dir the dir
	 * @param id the id
	 */
	private void logCalls(int time, int numPass, int floor, int dir, int id) {
		LOGGER.info("Time="+time+"   Called="+numPass+" Floor="+ (floor +1)
				+" Dir="+((dir>0)?"Up":"Down")+"   passID=" + id);
	}

	/**
	 * Log give up.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param dir the dir
	 * @param id the id
	 */
	private void logGiveUp(int time, int numPass, int floor, int dir, int id) {
		LOGGER.info("Time="+time+"   GaveUp="+numPass+" Floor="+ (floor+1) 
				+" Dir="+((dir>0)?"Up":"Down")+"   passID=" + id);				
	}

	/**
	 * Log skip.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param dir the dir
	 * @param id the id
	 */
	private void logSkip(int time, int numPass, int floor, int dir, int id) {
		LOGGER.info("Time="+time+"   Skip="+numPass+" Floor="+ (floor+1) 
				+" Dir="+((dir>0)?"Up":"Down")+"   passID=" + id);				
	}

	/**
	 * Log board.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param floor the floor
	 * @param dir the dir
	 * @param id the id
	 */
	private void logBoard(int time, int numPass, int floor, int dir, int id) {
		LOGGER.info("Time="+time+"   Board="+numPass+" Floor="+ (floor+1) 
				+" Dir="+((dir>0)?"Up":"Down")+"   passID=" + id);				
	}



	/**
	 * Log end simulation.
	 *
	 * @param time the time
	 */
	private void logEndSimulation(int time) {
		LOGGER.info("Time="+time+"   Detected End of Simulation");
	}


	/**
	 * Elevator state changed.
	 *
	 * @param lift the lift
	 * @return true, if successful
	 */
	public boolean elevatorStateChanged(Elevator lift) {
		return true;
	}

	/**
	 * Config elevators.
	 *
	 * @param capacity the capacity
	 * @param floorTicks the floor ticks
	 * @param doorTicks the door ticks
	 * @param tickPassengers the tick passengers
	 */
	public void configElevators(int capacity, int floorTicks, int doorTicks, int tickPassengers) {
		elevators = new Elevator [1];
		elevators[0] = new Elevator(capacity, floorTicks, doorTicks, tickPassengers);
		elevator = elevators[0];
	}

	/**
	 * check Users data is empty.
	 *
	 * @return true, if successful
	 */
	public boolean usersIsClear() {
		if (users.size()==0) {
			return true;
		}
		return false;
	}

	/**
	 * Adds the passengers to queue.
	 *
	 * @param time the time
	 * @param numPass the num pass
	 * @param fromFloor the from floor
	 * @param toFloor the to floor
	 * @param polite the polite
	 * @param wait the wait
	 */
	public void addPassengersToQueue(int time,int numPass,int fromFloor,int toFloor,boolean polite, int wait) {
		if(numPass>0) {
			users.add(new Passengers(time, numPass, fromFloor-1, toFloor-1, polite, wait));
		}
	}

	/**
	 * Check for new arrivals.
	 *
	 * @param time the time
	 * @return the int[][] of all requests for the gui
	 */
	public int[][] checkPassengers(int time) {
		ArrayList<Passengers> newArrivals = new ArrayList<Passengers>();
		Passengers p = users.peek();
		while (p.getTime() == time) {
			if (p.getFromFloor()>p.getToFloor()) {
				floors[p.getFromFloor()].addDownQueue(p);
				totalDown.add(p);
			}
			else {
				floors[p.getFromFloor()].addUpQueue(p);
				totalUp.add(p);
			}
			newArrivals.add(p);
			logCalls(time, p.getNumber(), p.getFromFloor(), p.getIntendedDirection(), p.getId()); 
			users.remove();
			if (users.size()==0){
				break;
			}
			p = users.peek();
		}
		int[][] arriveData = new int[newArrivals.size()][3];
		for (int i = 0; i < newArrivals.size(); i++) {
			Passengers e = newArrivals.get(i);
			arriveData[i] = new int [] {e.getFromFloor(), e.getNumber(), e.getIntendedDirection()};
		}
		return arriveData;
	}


	/**
	 * Gets the elevator passenger count.
	 *
	 * @return the elevator passenger count
	 */
	public int getElevatorPassengerCount() {
		return elevator.getNumPassengers();
	}

	/**
	 * Gets the elevator passenger count array.
	 *
	 * @return the elevator passenger count array
	 */
	public int[] getElevatorPassengerCountArray() {
		int[] out = new int[elevators.length];
		for (int i = 0; i < elevators.length; i++) {
			out[i] = elevators[i].getNumPassengers();
		}
		return out;
	}

	/**
	 * gets 2d array of strings that stores the number of passengers waiting at each floor.
	 *
	 * @return the floor data
	 */
	public String[][] getFloorData() {
		String[][] out = new String[2][NUM_FLOORS];
		for (int i = 0; i < NUM_FLOORS; i++) {
			out[0][i] = floors[i].getUpContents();
		}

		for (int i = 0; i < NUM_FLOORS; i++) {
			out[1][i] = floors[i].getDownContents();
		}
		return out;
	}

	/**
	 * Gets the elevator floor.
	 *
	 * @return the elevator current floor
	 */
	public int getElevatorFloor() {
		return elevator.getCurrFloor();
	}

	/**
	 * Elevator stopped.
	 *
	 * @return true, if it stopped
	 */
	public boolean elevatorStopped() {
		if(elevator.getPrevState()==STOP) {
			return true;
		}
		return false;
	}
	
	/**
	 * No users.
	 *
	 * @return true, if successful
	 */
	public boolean noUsers() {
		if (upInputs()==false && downInputs()==false) {
			return true;
		}
		return false;
	}
	
}