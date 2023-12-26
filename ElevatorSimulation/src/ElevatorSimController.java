import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import myfileio.MyFileIO;


// TODO: Auto-generated Javadoc
//Owned by Tony Wang
/**
 * The Class ElevatorSimController.
 */
// TODO: Auto-generated Javadoc
public class ElevatorSimController {
	
	/** The gui. */
	private ElevatorSimulation gui;
	
	/** The building. */
	private Building building;
	
	/** The fio. */
	private MyFileIO fio;


	/** The num floors. */
	private final int NUM_FLOORS;
	
	/** The num elevators. */
	private final int NUM_ELEVATORS;
	
	/** The num floors. */
	private int numFloors;
	
	/** The num elevators. */
	private int numElevators;
	
	/** The capacity. */
	private int capacity;
	
	/** The floor ticks. */
	private int floorTicks;
	
	/** The door ticks. */
	private int doorTicks;
	
	/** The tick passengers. */
	private int tickPassengers;
	
	/** The testfile. */
	private String testfile = "basicOffldChgDir4.csv";
	
	/** The logfile. */
	private String logfile;
	
	/** The new arrivals array. */
	private int[][] newArrivalsArray;
	
	/** The step cnt. */
	private int stepCnt = 0;
	
	/** The end sim. */
	private boolean endSim = false;
		
	/**
	 * Instantiates a new elevator sim controller. 
	 * Reads the configuration file to configure the building and
	 * the elevator characteristics and also select the test
	 * to run. Reads the passenger data for the test to run to
	 * initialize the passenger queue in building...
	 *
	 * @param gui the gui
	 */
	public ElevatorSimController(ElevatorSimulation gui) {	
		this.gui = gui;
		fio = new MyFileIO();
		// IMPORTANT: DO NOT CHANGE THE NEXT LINE!!!
		configSimulation("ElevatorSimConfig.csv");
		NUM_FLOORS = numFloors;
		NUM_ELEVATORS = numElevators;
		logfile = testfile.replaceAll(".csv", ".log");
		building = new Building(NUM_FLOORS,NUM_ELEVATORS,logfile);
		initializePassengerData(testfile);
		
		building.configElevators(capacity, floorTicks, doorTicks, tickPassengers);
		// YOU still need to configure the elevators in the building here....
		
	}
	
	//TODO: Write methods to update the GUI display
	//      Needs to cover the Elevator state, Elevator passengers
	//      and queues for each floor, as well as the current time
		
	/**
	 * Gets the elevator state.
	 *
	 * @return the elevator state
	 */
	public int getElevatorState() {
		return building.getElevatorState();
	}
	
	/**
	 * Gets the elevator state array.
	 *
	 * @return the elevator state array
	 */
	public int[] getElevatorStateArray() {
		return building.getElevatorStateArray();
	}
	
	/**
	 * Gets the elevator direction.
	 *
	 * @return the elevator direction
	 */
	public int getElevatorDirection() {
		return building.getElevatorDirection();
	}
	
	/**
	 * Gets the elevator direction array.
	 *
	 * @return the elevator direction array
	 */
	public int[] getElevatorDirectionArray() {
		return building.getElevatorDirectionArray();
	}
	
	/**
	 * Gets the new arrivals array.
	 *
	 * @return the new arrivals array
	 */
	public int[][] getNewArrivalsArray() {
		return newArrivalsArray;
	}
	
	
	
	/**
	 * Config simulation. Reads the filename, and parses the
	 * parameters.
	 *
	 * @param filename the filename
	 */
	private void configSimulation(String filename) {
		File configFile = fio.getFileHandle(filename);
		try ( BufferedReader br = fio.openBufferedReader(configFile)) {
			String line;
			while ((line = br.readLine())!= null) {
				String[] values = line.split(",");
				if (values[0].equals("numFloors")) {
					numFloors = Integer.parseInt(values[1]);
				} else if (values[0].equals("numElevators")) {
					numElevators = Integer.parseInt(values[1]);
				} else if (values[0].equals("passCSV")) {
					testfile = values[1];
				} else if (values[0].equals("capacity")) {
					capacity = Integer.parseInt(values[1]);
				} else if (values[0].equals("floorTicks")) {
					floorTicks = Integer.parseInt(values[1]);
				} else if (values[0].equals("doorTicks")) {
					doorTicks = Integer.parseInt(values[1]);
				} else if (values[0].equals("tickPassengers")) {
					tickPassengers = Integer.parseInt(values[1]);
				}	
			}
			fio.closeFile(br);
		} catch (IOException e) { 
			System.err.println("Error in reading file: "+filename);
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize passenger data. Reads the supplied filename,
	 * and for each passenger group, identifies the pertinent information
	 * and adds it to the passengers queue in Building...
	 *
	 * @param filename the filename
	 */
	private void initializePassengerData(String filename) {
		int time=0, numPass=0,fromFloor=0, toFloor=0;
		boolean polite = true;
		int wait = 1000;
		boolean firstLine = true;
		File inputData = fio.getFileHandle(filename);
		try (BufferedReader br = fio.openBufferedReader(inputData)) {
			String line;
			while ((line = br.readLine())!= null) {
				if (firstLine) {
					firstLine = false;
					continue;
				}
			//	System.out.println("READING: " + line);
				String[] values = line.split(",");
				for (int i = 0; i < values.length; i++) {
					switch (i) {
						case 0 : time      = Integer.parseInt(values[i]); break;
						case 1 : numPass   = Integer.parseInt(values[i]); break;
						case 2 : fromFloor   = Integer.parseInt(values[i]); break;
						case 3 : toFloor  = Integer.parseInt(values[i]); break;
						case 4 : polite = "TRUE".equalsIgnoreCase(values[i]); break;
						case 5 : wait      = Integer.parseInt(values[i]); break;
					}
				}
				//  YOU need to write this code in Building.java
				building.addPassengersToQueue(time,numPass,  fromFloor,toFloor,polite,wait);	
			}
			fio.closeFile(br);
		} catch (IOException e) { 
			System.err.println("Error in reading file: "+filename);
			e.printStackTrace();
		}
	}	
	
	/**
	 * Enable logging. A pass-through from the GUI to building
	 */
	public void enableLogging() {
		building.enableLogging();
	}
	
	// TODO: Write any other helper methods that you may need to access data from the building...
	
	
 	/**
	 * Step sim. See the comments below for the functionality you
	 * must implement......
	 */
	public void stepSim() {
 		// DO NOT MOVE THIS - YOU MUST INCREMENT TIME FIRST!
		stepCnt++;
		//building.getUsers().isEmpty() == false && building.getElevatorState() != Elevator.STOP
		if (building.usersIsClear() && building.elevatorStopped()) {
			if (gui != null) gui.updateGUI();
			building.updateElevator(stepCnt);
			building.processPassengerData();
			if (gui != null && building.noUsers()) {
				building.closeLogs(stepCnt);
				gui.endSimulation();
			}
		} else {
			if (!building.usersIsClear()) newArrivalsArray = building.checkPassengers(stepCnt);
			building.updateElevator(stepCnt);
			if (gui != null) gui.updateGUI();
		}
		
		// TODO: Write the rest of this method
		// If simulation is not completed (not all passengers have been processed
		// or elevator(s) are not all in STOP state), then
		// 		1) check for arrival of any new passengers  building.checkNewArrivals(int time)
		// 		2) update the elevator building.updateElevator(int time)
		// 		3) update the GUI 
		//  else 
		//    	1) update the GUI
		//		2) close the logs
		//		3) process the passenger results
		//		4) send endSimulation to the GUI to stop ticks.
	}
	
	/**
	 * Gets the elevator passenger count.
	 *
	 * @return the elevator passenger count
	 */
	public int getElevatorPassengerCount() {
		return building.getElevatorPassengerCount();
	}
	
	/**
	 * Gets the elevator passenger count array.
	 *
	 * @return the elevator passenger count array
	 */
	public int[] getElevatorPassengerCountArray() {
		return building.getElevatorPassengerCountArray();
	}
	
	/**
	 * Gets the floor data.
	 *
	 * @return the floor data
	 */
	public String[][] getFloorData() {
		return building.getFloorData();
	}
	
	/**
	 * Gets the current floor.
	 *
	 * @return the current floor
	 */
	public int getCurrentFloor() {
		return building.getElevatorFloor();
	}
	
	/**
	 * Gets the test name.
	 *
	 * @return the test name
	 */
	public String getTestName() {
		return null;
	}	
	
	/**
	 * Gets the gui.
	 *
	 * @return the gui
	 */
	public ElevatorSimulation getGui() {
		return gui;
	}

	/**
	 * Sets the gui.
	 *
	 * @param gui the new gui
	 */
	public void setGui(ElevatorSimulation gui) {
		this.gui = gui;
	}

	/**
	 * Gets the building.
	 *
	 * @return the building
	 */
	Building getBuilding() {
		return building;
	}

	/**
	 * Sets the building.
	 *
	 * @param building the new building
	 */
	public void setBuilding(Building building) {
		this.building = building;
	}

	/**
	 * Gets the fio.
	 *
	 * @return the fio
	 */
	public MyFileIO getFio() {
		return fio;
	}

	/**
	 * Sets the fio.
	 *
	 * @param fio the new fio
	 */
	public void setFio(MyFileIO fio) {
		this.fio = fio;
	}

	/**
	 * Gets the num floors.
	 *
	 * @return the num floors
	 */
	public int getNumFloors() {
		return numFloors;
	}

	/**
	 * Sets the num floors.
	 *
	 * @param numFloors the new num floors
	 */
	public void setNumFloors(int numFloors) {
		this.numFloors = numFloors;
	}

	/**
	 * Gets the num elevators.
	 *
	 * @return the num elevators
	 */
	public int getNumElevators() {
		return numElevators;
	}

	/**
	 * Sets the num elevators.
	 *
	 * @param numElevators the new num elevators
	 */
	public void setNumElevators(int numElevators) {
		this.numElevators = numElevators;
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
	 * Gets the floor ticks.
	 *
	 * @return the floor ticks
	 */
	public int getFloorTicks() {
		return floorTicks;
	}

	/**
	 * Sets the floor ticks.
	 *
	 * @param floorTicks the new floor ticks
	 */
	public void setFloorTicks(int floorTicks) {
		this.floorTicks = floorTicks;
	}

	/**
	 * Gets the door ticks.
	 *
	 * @return the door ticks
	 */
	public int getDoorTicks() {
		return doorTicks;
	}

	/**
	 * Sets the door ticks.
	 *
	 * @param doorTicks the new door ticks
	 */
	public void setDoorTicks(int doorTicks) {
		this.doorTicks = doorTicks;
	}

	/**
	 * Gets the tick passengers.
	 *
	 * @return the tick passengers
	 */
	public int getTickPassengers() {
		return tickPassengers;
	}

	/**
	 * Sets the tick passengers.
	 *
	 * @param tickPassengers the new tick passengers
	 */
	public void setTickPassengers(int tickPassengers) {
		this.tickPassengers = tickPassengers;
	}

	/**
	 * Gets the testfile.
	 *
	 * @return the testfile
	 */
	public String getTestfile() {
		return testfile;
	}

	/**
	 * Sets the testfile.
	 *
	 * @param testfile the new testfile
	 */
	public void setTestfile(String testfile) {
		this.testfile = testfile;
	}

	/**
	 * Gets the logfile.
	 *
	 * @return the logfile
	 */
	public String getLogfile() {
		return logfile;
	}

	/**
	 * Sets the logfile.
	 *
	 * @param logfile the new logfile
	 */
	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}

	/**
	 * Gets the step cnt.
	 *
	 * @return the step cnt
	 */
	public int getStepCnt() {
		return stepCnt;
	}

	/**
	 * Sets the step cnt.
	 *
	 * @param stepCnt the new step cnt
	 */
	public void setStepCnt(int stepCnt) {
		this.stepCnt = stepCnt;
	}

	/**
	 * Checks if is end sim.
	 *
	 * @return true, if is end sim
	 */
	public boolean isEndSim() {
		return endSim;
	}

	/**
	 * Sets the end sim.
	 *
	 * @param endSim the new end sim
	 */
	public void setEndSim(boolean endSim) {
		this.endSim = endSim;
	}

	/**
	 * Gets the num floors.
	 *
	 * @return the num floors
	 */
	public int getNUM_FLOORS() {
		return NUM_FLOORS;
	}

	/**
	 * Gets the num elevators.
	 *
	 * @return the num elevators
	 */
	public int getNUM_ELEVATORS() {
		return NUM_ELEVATORS;
	}
}
