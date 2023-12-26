//Author: Jimin Park

//import java.util.ListIterator;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

// TODO: Auto-generated Javadoc
/**
 * The Class ElevatorSimulation.
 */
// The GUI
public class ElevatorSimulation extends Application {

/** The controller. */
//Variables that maintain system
	private ElevatorSimController controller;
	
	/** The curr floor. */
	private int currFloor;
	
	/** The curr floor in ctrl. */
	private int currFloorInCtrl;
	
	/** The passengers. */
	private int passengers; // # of passengers on elevator
	
	/** The time. */
	private int time; // time to be displayed
	
	/** The t. */
	private Timeline t = new Timeline(new KeyFrame(Duration.millis(100), ae -> controller.stepSim()));
	
	/** The state. */
	private int state; // state of the elevator

/** The num floors. */
//final variables
	private final int NUM_FLOORS;
	
	/** The num elevators. */
	private final int NUM_ELEVATORS;
	
	/** The stop. */
	private final int STOP = Elevator.STOP; // int represent label for each state
	
	/** The mvtoflr. */
	private final int MVTOFLR = Elevator.MVTOFLR;
	
	/** The opendr. */
	private final int OPENDR = Elevator.OPENDR;
	
	/** The offld. */
	private final int OFFLD = Elevator.OFFLD;
	
	/** The board. */
	private final int BOARD = Elevator.BOARD;
	
	/** The closedr. */
	private final int CLOSEDR = Elevator.CLOSEDR;
	
	/** The mv1flr. */
	private final int MV1FLR = Elevator.MV1FLR;

/** The border pane. */
//Panes, Scenes
	private BorderPane borderPane = new BorderPane();
	
	/** The grid. */
	private GridPane grid = new GridPane();
	
	/** The main scene. */
	private Scene mainScene;
	
	/** The choose N cycles pane. */
	private BorderPane chooseNCyclesPane = new BorderPane();
	
	/** The choose N cycles. */
	private Scene chooseNCycles = new Scene(chooseNCyclesPane);

/** The buttons. */
//GUI Elements
	VBox buttons = new VBox();
	
	/** The bottom buttons. */
	HBox bottomButtons = new HBox();
	
	/** The speed buttons. */
	HBox speedButtons = new HBox();
	
	/** The step. */
	// buttons
	private Button step = new Button("Step");
	
	/** The step N cycles. */
	private Button stepNCycles = new Button("Step N Cycles");
	
	/** The step N cycles start. */
	private Button stepNCyclesStart = new Button("Start");
	
	/** The run. */
	private Button run = new Button("Run");
	
	/** The log. */
	private Button log = new Button("Log");
	
	/** The fastest. */
	private Button fastest = new Button("Fastest");
	
	/** The fast. */
	private Button fast = new Button ("Fast");
	
	/** The slow. */
	private Button slow = new Button("Slow");
	
	/** The slowest. */
	private Button slowest = new Button("Slowest");
	
	/** The time label. */
	// labels
	private Label timeLabel = new Label();
	
	/** The cycle ct label. */
	private Label cycleCtLabel = new Label();
	
	/** The up labels. */
	private ArrayList<Label> upLabels = new ArrayList<Label>();
	
	/** The down labels. */
	private ArrayList<Label> downLabels = new ArrayList<Label>();
	
	/** The pass label. */
	private Label passLabel;
	
	/** The elevator. */
	// Elevator & Shape
	StackPane elevator;
	
	/** The closed elevator. */
	StackPane closedElevator = new StackPane(new Rectangle(50, 50, Color.GRAY));
	
	/** The opened elevator. */
	StackPane openedElevator = new StackPane(new Rectangle(50, 50, Color.DARKGRAY), new Rectangle(40, 50, Color.BEIGE));
	
	/** The elevator loc. */
	int[] elevatorLoc = { 1, 1 }; // col, row (y,x)
	
	/** The state shape. */
	Polygon stateShape;
	
	/** The stop. */
	Polygon stop = new Polygon();
	
	/** The up. */
	Polygon up = new Polygon();
	
	/** The down. */
	Polygon down = new Polygon();
	
	/** The offld. */
	Polygon offld = new Polygon();
	
	/** The board. */
	Polygon board = new Polygon();
	
	/** The wait. */
	Polygon wait = new Polygon();
	
	/** The choose N cycles lbl. */
	// NCycles
	private Label chooseNCyclesLbl = new Label("Choose the number of cycles: ");
	
	/** The choose N cycles field. */
	private TextField chooseNCyclesField = new TextField("Integer of cycles");
	
	/** The instructions. */
	private Label instructions = new Label("Select speed, then run");

	/**
	 * Instantiates a new elevator simulation.
	 */
	public ElevatorSimulation() {
		controller = new ElevatorSimController(this);
		NUM_FLOORS = controller.getNumFloors();
		NUM_ELEVATORS = controller.getNumElevators();
		currFloor = controller.getCurrentFloor();
		currFloorInCtrl = currFloor;
		setUpGuiElementsInConstructor();
	}

	/**
	 * Start. You need to design the GUI. Note that the test name should appear in
	 * the Title of the window!!
	 * 
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Elevator Simulation - " + controller.getTestfile());
		// main border pane
		bottomButtons.getChildren().add(step);
		bottomButtons.getChildren().add(stepNCycles);
		bottomButtons.getChildren().add(run);
		bottomButtons.getChildren().add(log);
		speedButtons.getChildren().add(fastest);
		speedButtons.getChildren().add(fast);
		speedButtons.getChildren().add(slow);
		speedButtons.getChildren().add(slowest);
		buttons.getChildren().add(instructions);
		buttons.getChildren().add(bottomButtons);
		buttons.getChildren().add(speedButtons);
		borderPane.setBottom(buttons);
		// set up grid pane
		setUpGrid();
		// nCycles pane
		stepNCycles.setOnAction(e -> primaryStage.setScene(chooseNCycles));
		stepNCyclesStart.setOnAction(e -> startAction(primaryStage));

		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	/**
	 * Updates GUI.
	 */
	public void updateGUI() {
		state = controller.getElevatorState();
		// Floor changed
		currFloorInCtrl = controller.getCurrentFloor();
		moveElevatorGraphic();
		changeStateShapeGraphic();
		currFloor = currFloorInCtrl;
		// FloorQueue changed: add or subtract number of people waiting
		floorQueueChanged();
		// time changed
		updateTimeGraphic();
	}

	/**
	 * Ends simulation.
	 */
	public void endSimulation() {
		t.stop();
	}

	/**
	 * Sets the up grid.
	 */
	private void setUpGrid() {
		mainScene = new Scene(borderPane, 350, (NUM_FLOORS + 1) * 70);
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setGridLinesVisible(false);
		borderPane.setCenter(grid);
		// make rows & columns
		for (int i = 0; i < NUM_FLOORS + 1; i++) {
			RowConstraints row = new RowConstraints(50);
			grid.getRowConstraints().add(row);
		}
		for (int i = 0; i < 6; i++) {
			ColumnConstraints col = new ColumnConstraints(50);
			grid.getColumnConstraints().add(col);
		}
		setUpGridLabels();
		// Add Elevator & State shape
		setElevatorLoc();
		elevator = closedElevator;
		stateShape = getStateShape();
		grid.add(elevator, elevatorLoc[0], elevatorLoc[1]);
		grid.add(stateShape, 0, elevatorLoc[1]);
		// Add Passengers
		passengers = 0;
		passLabel = new Label("" + passengers);
		grid.setHalignment(passLabel, HPos.CENTER);
		grid.setValignment(passLabel, VPos.CENTER);
		grid.add(passLabel, elevatorLoc[0], elevatorLoc[1]);
	}

	/**
	 * Sets the up grid labels.
	 */
	private void setUpGridLabels() {
		// Labels on top and side
		Text labelText = new Text();
		labelText.setText("Direction");
		labelText.setFont(Font.font("Verdana"));
		grid.add(labelText, 0, 0);
		labelText = new Text();
		labelText.setText("UP Pass.");
		labelText.setFont(Font.font("Verdana", 10));
		grid.add(labelText, 2, 0);
		labelText = new Text();
		labelText.setText("DOWN Pass.");
		labelText.setFont(Font.font("Verdana", 10));
		grid.add(labelText, 3, 0);
		timeLabel.setText("Time:\n" + time);
		timeLabel.setFont(Font.font("Verdana"));
		cycleCtLabel.setText("Cycle Ct:\n" + t.getCycleCount());
		cycleCtLabel.setFont(Font.font("Verdana", 10));
		grid.add(timeLabel, 5, 1);
		grid.add(cycleCtLabel, 5, 2);
		for (int i = NUM_FLOORS; i > 0; i--) {
			labelText = new Text("Floor " + Integer.toString(i));
			labelText.setFont(Font.font("Verdana"));
			grid.add(labelText, 4, NUM_FLOORS + 1 - i);
		}
	}

	/**
	 * Action that is performed when "Start" button is clicked.
	 *
	 * @param primaryStage the primary stage
	 */
	private void startAction(Stage primaryStage) { // action when "Start" is clicked
		int i = -1;
		String textField = chooseNCyclesField.getText();
		String tester = textField;
		tester = tester.replaceAll("[0-9]+", "");
		if (tester.length() != 0) {
			chooseNCyclesField.clear();
			chooseNCyclesField.setText("INVALID INPUT");
			return;
		} else
			i = Integer.parseInt(textField);
		t.setCycleCount(i);
		updateCycleCntGraphic();
		primaryStage.setScene(mainScene);
		t.play();
	}

	/**
	 * Action that is performed when "Run" button is clicked.
	 */
	private void runAction() {
		t.setCycleCount(Timeline.INDEFINITE);
		t.play();
	}

	/**
	 * Update cycle count graphic.
	 */
	private void updateCycleCntGraphic() {
		grid.getChildren().remove(cycleCtLabel);
		cycleCtLabel.setText("Cycle Ct:\n" + t.getCycleCount());
		grid.add(cycleCtLabel, 5, 2);
	}

	/**
	 * Update time graphic.
	 */
	private void updateTimeGraphic() {
		time++;
		grid.getChildren().remove(timeLabel);
		timeLabel.setText("Time:\n" + time);
		grid.add(timeLabel, 5, 1);
	}

	/**
	 * Translates currFloor to a coordinate on "grid". Only the row number (also
	 * known as elevatorLoc[1]) changes.)
	 */
	private void setElevatorLoc() {
		elevatorLoc[1] = NUM_FLOORS - currFloorInCtrl;
	}

	/**
	 * Gets the right elevator state based on the Controller.
	 *
	 * @return the proper elevator
	 */
	private StackPane getElevatorState() {
		if (state == OPENDR)
			return openedElevator;
		else if (state == CLOSEDR)
			return closedElevator;
		return elevator;
	}

	/**
	 * Move elevator graphic.
	 */
	private void moveElevatorGraphic() {
		// System.out.println("old) elevator & gui match:
		// "+(grid.getRowIndex(elevator)==elevatorLoc[1]));
		grid.getChildren().remove(elevator);
		grid.getChildren().remove(passLabel);
		setElevatorLoc();
		elevator = getElevatorState();
		if (elevator == null)
			elevator = closedElevator;
		grid.add(elevator, elevatorLoc[0], elevatorLoc[1]);
		// System.out.println("new) elevator & gui match:
		// "+(grid.getRowIndex(elevator)==elevatorLoc[1]));
		// update number of passengers
		passengers = controller.getElevatorPassengerCount();
		passLabel = new Label("" + passengers);
		grid.setHalignment(passLabel, HPos.CENTER);
		grid.setValignment(passLabel, VPos.CENTER);
		grid.add(passLabel, elevatorLoc[0], elevatorLoc[1]);
	}

	/**
	 * Change state shape graphic.
	 */
	private void changeStateShapeGraphic() {
		grid.getChildren().remove(stateShape);
		stateShape = getStateShape();
		grid.add(stateShape, 0, elevatorLoc[1]);
	}

	/**
	 * Gets the state shape.
	 *
	 * @return the state shape
	 */
	private Polygon getStateShape() {
		Polygon output = new Polygon();
		if (state == STOP)
			output = stop;
		else if ((currFloorInCtrl - currFloor) < 0) {
			if (state == MVTOFLR) {
				down.setFill(Color.GREEN);
			} else if (state == MV1FLR) {
				down.setFill(Color.BLUE);
			}
			output = down;
		} else if ((currFloorInCtrl - currFloor) > 0) {
			if (state == MVTOFLR)
				up.setFill(Color.GREEN);
			else if (state == MV1FLR)
				up.setFill(Color.BLUE);
			output = up;
		} else if ((currFloorInCtrl - currFloor) == 0) {
			if (state == OFFLD) {
				offld.setFill(Color.YELLOW);
				output = offld;
			}
			else if (state == BOARD) {
				board.setFill(Color.YELLOW);
				output = board;
			}
			else if (state == OPENDR || state == CLOSEDR) {
				wait.setFill(Color.GRAY);
				output = wait;
			}
			else {
				output = wait;
			}
		}
		return output;
	}

	/**
	 * Sets the up gui elements in constructor.
	 */
	private void setUpGuiElementsInConstructor() {
		HBox nCyclesHBox = new HBox();
		nCyclesHBox.getChildren().add(chooseNCyclesLbl);
		nCyclesHBox.getChildren().add(chooseNCyclesField);
		nCyclesHBox.getChildren().add(stepNCyclesStart);
		chooseNCyclesPane.setCenter(nCyclesHBox);
		// set up state shape
		up.getPoints().addAll(new Double[] { 25.0, 0.0, 0.0, 25.0, 50.0, 25.0 });
		down.getPoints().addAll(new Double[] { 25.0, 25.0, 0.0, 0.0, 50.0, 0.0 });
		offld.getPoints().addAll(new Double[] { 0.0, 0.0, 0.0, 50.0, 30.0, 25.0 });
		board.getPoints().addAll(new Double[] { 30.0, 0.0, 30.0, 50.0, 0.0, 25.0 });
		stop.getPoints().addAll(new Double[] { 0.0, 0.0, 0.0, 25.0, 25.0, 25.0, 25.0, 0.0 });
		wait.getPoints().addAll(new Double[] { 0.0, 25.0, 25.0, 0.0, 50.0, 25.0, 25.0, 50.0 });
		// button Setup
		step.setOnAction(e -> controller.stepSim());
		run.setOnAction(e -> runAction());
		log.setOnAction(e -> controller.enableLogging()); // logs
		fastest.setOnAction(e ->  changeSpeed(5));
		fast.setOnAction(e ->  changeSpeed(100));
		slow.setOnAction(e ->  changeSpeed(250));
		slowest.setOnAction(e ->  changeSpeed(500));
		// Label setup
		for (int i = 0; i < NUM_FLOORS; i++) {
			upLabels.add(new Label());
			downLabels.add(new Label());
		}
	}
	
	/**
	 * Change speed.
	 *
	 * @param speed the speed
	 */
	private void changeSpeed(int speed) {
		t.pause();
		t = new Timeline(new KeyFrame(Duration.millis(speed), ae -> controller.stepSim()));
	}

	/**
	 * gets floor data converted to labels, delete all existing up/down labels in
	 * GUI, update & add up/down labels to GUI.
	 */
	private void floorQueueChanged() {
		convertFloorDataToLabels();
		// delete all existing up/down labels in GUI
		ObservableList<Node> childrens = grid.getChildren();
		ArrayList<Node> nodesToRemove = new ArrayList<Node>();
		for (Node node : childrens) {
			if (node instanceof Label && grid.getRowIndex(node) > 0
					&& (grid.getColumnIndex(node) == 2 || grid.getColumnIndex(node) == 3)) {
				nodesToRemove.add(node);
			}
		}
		for (int i = nodesToRemove.size() - 1; i >= 0; i--) {
			grid.getChildren().remove(nodesToRemove.get(i));
		}
		// update & add up/down labels to GUI
		for (int i = 0; i < NUM_FLOORS; i++) {
			grid.add(upLabels.get(i), 2, NUM_FLOORS - i);
			grid.add(downLabels.get(i), 3, NUM_FLOORS - i);
		}
	}

	/**
	 * Convert floor data to labels.
	 */
	private void convertFloorDataToLabels() {
		// get 2d array, put that into uplabel, downlabel
		String[][] floorData = controller.getFloorData(); // 1. up(0)/down(1) 2. passengers on that floor
		upLabels.clear();
		downLabels.clear();
		for (String pass : floorData[0]) {
			pass = pass.replace("queue: [", "");
			pass = pass.replace("]", "");
			// System.out.println("pass: "+pass);
			upLabels.add(new Label(pass));
		}
		for (String pass : floorData[1]) {
			pass = pass.replace("queue: [", "");
			pass = pass.replace("]", "");
			// System.out.println("pass: "+pass);
			downLabels.add(new Label(pass));
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

}
