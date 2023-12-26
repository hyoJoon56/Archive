
public class CheckMoves {
	private int [][] checkValues;
	private int [][] orignalValues;

	public CheckMoves(int [][] a, int k) {
		checkValues =  new int [k][k];
		for (int i=0; i<a.length; i++) {
			for(int j=0; j<a[0].length; j++) {
				checkValues[i][j] = a[i][j];
			}
		}
		orignalValues = a;
	}

	public Moves [] simulationSet(int [][] vv) {
		Moves []simulation = new Moves [4];
		simulation [0]= new MoveUp(vv);
		simulation [1]= new MoveDown(vv);
		simulation [2]= new MoveLeft(vv);
		simulation [3]= new MoveRight(vv);
		return simulation;
	}

	public boolean checkPossibleMove(int k) {
		int count = 0;
		Moves [] possibleMoveTest = this.simulationSet(checkValues);
		possibleMoveTest[k].moveBlocks(checkValues);
		for (int i=0; i<checkValues.length; i++) {
			for(int j=0; j<checkValues[0].length; j++) {
				if (checkValues[i][j] != orignalValues[i][j]) {
					count++;
				}
			}
		}
		if (count==0) {
			return false;
		}
		return true;
	}
	public boolean checkGameOver(int k) {
		int indicator = 0;
		boolean [] checkOver = new boolean [k+1];

		for (int i=0; i<=k; i++) {
			checkOver[i] = this.checkPossibleMove(i);
			if (checkOver[i]==true) {
				indicator++;
			}
		}
		if (indicator>0) {
			return false;
		}
		return true;
	}
}
