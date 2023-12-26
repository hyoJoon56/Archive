

public class BallAndBlackGameMain {
	public static void main(String[] args) {
		BallAndBlockView view = new BallAndBlockView();
		new BallAndBlockController (view);
		view.requestFocusInWindow();
	}
}
