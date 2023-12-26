import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class BuildingFSMBasicTest.
 */
@TestMethodOrder(OrderAnnotation.class)
class BuildingFSMBasicTest {
	
	/** The c. */
	private ElevatorSimController c;
	
	/** The b. */
	private Building b;
	
	/** The fio. */
	private MyFileIO fio = new MyFileIO();
	
	/** The debug. */
	private static boolean DEBUG = false;
	
	/** The os. */
	private static String os = null;
	
	/** The java home. */
	private static String javaHome = null;

	/**
	 * Update sim config CSV.
	 *
	 * @param fname the fname
	 */
	private void updateSimConfigCSV(String fname) {
		File fh = fio.getFileHandle("ElevatorSimConfig.csv");
		String line = "";
		ArrayList<String> fileData = new ArrayList<>();
		try {
			BufferedReader br = fio.openBufferedReader(fh);
			while ( (line = br.readLine())!=null) {
				if (line.matches("passCSV.*")) 
					fileData.add("passCSV,"+fname);
				else
					fileData.add(line);
			}
			fio.closeFile(br);
			BufferedWriter bw = fio.openBufferedWriter(fh);
			for (String l : fileData)
				bw.write(l+"\n");
			fio.closeFile(bw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Copy test file.
	 *
	 * @param fname the fname
	 */
	private void copyTestFile(String fname) {
		File ifh = fio.getFileHandle("test_data/"+fname);
		File ofh = fio.getFileHandle(fname);
		Path src = Paths.get(ifh.getPath());
		Path dest = Paths.get(ofh.getPath());
		try {
			Files.copy(src, dest,StandardCopyOption.REPLACE_EXISTING);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		updateSimConfigCSV(fname);
	}
	
	/**
	 * Delete test CSV.
	 *
	 * @param fname the fname
	 */
	private void deleteTestCSV(String fname) {
		MyFileIO fio = new MyFileIO();
		File ifh = fio.getFileHandle(fname);
		ifh.delete();
		ifh = fio.getFileHandle(fname.replaceAll(".csv", "PassData.csv"));
		ifh.delete();
	}

	/**
	 * Process cmp elevator output.
	 *
	 * @param proc the proc
	 * @param results the results
	 * @return true, if successful
	 */
	private boolean processCmpElevatorOutput(Process proc, ArrayList<String> results) {
		String line = "";
		boolean pass = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		try {
			while ((line = br.readLine())!=null) {
				results.add(line);
				System.out.println(line);
				if (line.contains("FAILED")) pass = false;
			}
			br.close();		
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return pass;
	}
	
	/**
	 * Prints the manual cmp elevator instructions.
	 *
	 * @param fh the fh
	 */
	private void printManualCmpElevatorInstructions(File fh) {
		System.out.println("ERROR: cmpElevator failed to run - you will need to run manually.");
		System.out.println("       1) cd to your project directory in the terminal.");
		System.out.println("       2) java -jar cmpElevator.jar "+fh.getName().replaceAll(".cmp", ".log"));	
	}
	
	/**
	 * Process cmp elevator error.
	 *
	 * @param proc the proc
	 * @param results the results
	 * @param fh the fh
	 * @return true, if successful
	 */
	private boolean processCmpElevatorError(Process proc, ArrayList<String> results, File fh) {
		String line = "";
		boolean pass = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		try {
			while ((line = br.readLine())!=null) {
				results.add(line);
				System.out.println(line);
				pass = false;
			}
			br.close();		
			printManualCmpElevatorInstructions(fh);
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return pass;
	}
	
	/**
	 * Execute cmp elevator.
	 *
	 * @param fh the fh
	 * @param cmd the cmd
	 * @return true, if successful
	 */
	private boolean executeCmpElevator(File fh,String cmd) {
		boolean pass = true;
		ArrayList<String> cmpResults = new ArrayList<String>();
		if (javaHome == null) {
			printManualCmpElevatorInstructions(fh);
			fail();
		}
		cmd = javaHome+"/"+cmd;
		String[] execCmpElevator = cmd.split("\\s+");
		try {
			Process proc = new ProcessBuilder(execCmpElevator).start();
			proc.waitFor();
			pass = pass && processCmpElevatorOutput(proc,cmpResults);
			if (cmpResults.isEmpty()) 
				pass = pass && processCmpElevatorError(proc,cmpResults,fh);
			
			if (!cmpResults.isEmpty()) {
				BufferedWriter bw = fio.openBufferedWriter(fh);
				for (int i = 0; i < cmpResults.size() ; i++) {
					bw.write(cmpResults.get(i)+"\n");
				}
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    return(pass);	
	}

	
    /**
     * Gets the operating system.
     *
     * @return the operating system
     */
    private static String getOperatingSystem() {
    	os = System.getProperty("os.name");
    	return os;
    }

    /**
     * Gets the java home.
     *
     * @return the java home
     */
    private static void getJavaHome() {
    	File fh = null;
    	javaHome = System.getProperty("java.home").replaceAll("jre","bin");
		if (DEBUG) System.out.println("JavaHome: "+javaHome);
		if (os.matches(".*Windows.*")) {
			fh = new File(javaHome+"/java.exe");
		} else {
			fh = new File(javaHome+"/java");			
		}
		if (!fh.exists()) 
			javaHome = null;
    }

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Running on: "+getOperatingSystem());
		getJavaHome();
		File ifh = new File("ElevatorSimConfig.csv");
		File ofh = new File("ElevatorSimConfig.save");
		Path src = Paths.get(ifh.getPath());
		Path dest = Paths.get(ofh.getPath());
		Files.copy(src, dest,StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		File ifh = new File("ElevatorSimConfig.save");
		File ofh = new File("ElevatorSimConfig.csv");
		Path src = Paths.get(ifh.getPath());
		Path dest = Paths.get(ofh.getPath());
		Files.copy(src, dest,StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}
	
	/**
	 * Test basic check passengers.
	 */
	@Test
	@Order(1)
	//@Disabled
	void testBasicCheckPassengers() {
		String test = "basicCheckPassengers";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 10;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar cmpElevator.jar --ckCalls "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Test call on floor up 1.
	 */
	@Test
	@Order(2)
	//@Disabled
	void testCallOnFloorUp1() {
		String test = "basicCallOnFloorUp1";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 26;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Test call on floor up 1 b.
	 */
	@Test
	@Order(3)
	//@Disabled
	void testCallOnFloorUp1b() {
		String test = "basicCallOnFloorUp1b";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 31;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Test call on floor up 1 c.
	 */
	@Test
	@Order(4)
	//@Disabled
	void testCallOnFloorUp1c() {
		String test = "basicCallOnFloorUp1c";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 46;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}
	
	/**
	 * Testbasic board offld 1.
	 */
	@Test
	@Order(5)
	//@Disabled
	void testbasicBoardOffld1() {
		String test = "basicBoardOffld1";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 34;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic board offld 2.
	 */
	@Test
	@Order(6)
	//@Disabled
	void testbasicBoardOffld2() {
		String test = "basicBoardOffld2";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 34;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic board offld 3.
	 */
	@Test
	@Order(7)
	//@Disabled
	void testbasicBoardOffld3() {
		String test = "basicBoardOffld3";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 63;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic board offld 4.
	 */
	@Test
	@Order(8)
	//@Disabled
	void testbasicBoardOffld4() {
		String test = "basicBoardOffld4";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 59;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic mv 1 F chg dir 1.
	 */
	@Test
	@Order(9)
	//@Disabled
	void testbasicMv1FChgDir1() {
		String test = "basicMv1FChgDir1";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 66;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic mv 1 F chg dir 2.
	 */
	@Test
	@Order(10)
	//@Disabled
	void testbasicMv1FChgDir2() {
		String test = "basicMv1FChgDir2";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 111;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic cl dr chg dir 1.
	 */
	@Test
	@Order(11)
	//@Disabled
	void testbasicClDrChgDir1() {
		String test = "basicClDrChgDir1";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 76;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic cl dr chg dir 2.
	 */
	@Test
	@Order(12)
	//@Disabled
	void testbasicClDrChgDir2() {
		String test = "basicClDrChgDir2";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 106;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic offld chg dir 1.
	 */
	@Test
	@Order(13)
	//@Disabled
	void testbasicOffldChgDir1() {
		String test = "basicOffldChgDir1";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 76;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic offld chg dir 2.
	 */
	@Test
	@Order(14)
	//@Disabled
	void testbasicOffldChgDir2() {
		String test = "basicOffldChgDir2";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 62;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic offld chg dir 3.
	 */
	@Test
	@Order(15)
	//@Disabled
	void testbasicOffldChgDir3() {
		String test = "basicOffldChgDir3";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 111;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}

	/**
	 * Testbasic offld chg dir 4.
	 */
	@Test
	@Order(16)
	//@Disabled
	void testbasicOffldChgDir4() {
		String test = "basicOffldChgDir4";
		System.out.println("\n\nExecuting Test: "+test+".csv");
		copyTestFile(test+".csv");
		c = new ElevatorSimController(null);
		b = c.getBuilding();
		b.enableLogging();
	    int i;
		for (i = 0; i < 97;i++) c.stepSim();
		b.closeLogs(i);
		deleteTestCSV(test+".csv");
		File fh = fio.getFileHandle(test+".cmp");
		String cmd = "java -jar ./cmpElevator.jar "+test+".log";
		assertTrue(executeCmpElevator(fh,cmd));
	}


}
