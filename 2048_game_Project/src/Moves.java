public abstract class Moves {
	private Board Given;
	private int point;
	private int [][] valueOnly;

	Moves(Board a){
		Given = a;
	}
	Moves(int [][] vv){
		valueOnly = vv;
	}

	public abstract Board moveBlocks();
	public abstract int[][] countForZero(int a, int b);
	public abstract int combineTwoBlocks(int [][] aa, int c);
	public abstract int[][] moveBlocks(int [][] bb);

	public int getPoint() {
		return point;
	}


	public void setPoint(int point) {
		this.point = point;
	}
	public Board getGiven() {
		return Given;
	}
	public int[][] getValueOnly() {
		return valueOnly;
	}
	public void setGiven(Board given) {
		Given = given;
	}

}
