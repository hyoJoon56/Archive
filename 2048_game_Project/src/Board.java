public class Board {
	private String [][] squareBoard;
	private int sizeBoard;
	private int [][] valueBoard;
	private boolean [][] usedSpaces;


	public Board(int size) {
		squareBoard = new String [size][size];
		sizeBoard = squareBoard.length;
		valueBoard = new int [sizeBoard][sizeBoard];
		for(int i = 0; i < squareBoard.length; i++) {
			for(int j = 0; j < squareBoard[0].length; j++) {
				squareBoard[i][j] = "";
			}
		}
		usedSpaces = new boolean [sizeBoard][sizeBoard];
		for(int i = 0; i < squareBoard.length; i++) {
			for (int j =0 ; j<squareBoard.length; j++) {
				if (this.squareBoard[i][j]== "") {
					usedSpaces[i][j] = false;
					continue;

				}
				usedSpaces[i][j] = true;
			}
		}
		int count = 2;
		while (count>0) {
			RandomSet n = new RandomSet();
			RandomSet row = new RandomSet(squareBoard.length);
			RandomSet column = new RandomSet(squareBoard[0].length);
			if (usedSpaces[row.getBlocks()][column.getBlocks()]==false) {
				valueBoard[row.getBlocks()][column.getBlocks()] = n.getBlocks();
				squareBoard[row.getBlocks()][column.getBlocks()] = n.toString();
				usedSpaces[row.getBlocks()][column.getBlocks()]= true;
				count--;
			}
		}

	}

	public Board (Board AfterFirst) {
		squareBoard = AfterFirst.squareBoard;
		sizeBoard = squareBoard.length;
		valueBoard = new int [sizeBoard][sizeBoard];
		for(int i = 0; i < squareBoard.length; i++) {
			for(int j = 0; j < squareBoard[0].length; j++) {
				squareBoard[i][j] = AfterFirst.getsquareBoard()[i][j];
				//
				//
				//
				valueBoard[i][j] = AfterFirst.getvalueBoard()[i][j];
			}
		}
		usedSpaces = new boolean [sizeBoard][sizeBoard];
		for(int i = 0; i < squareBoard.length; i++) {
			for (int j =0 ; j<squareBoard.length; j++) {
				if (this.squareBoard[i][j]== "") {
					usedSpaces[i][j] = false;
					continue;

				}
				usedSpaces[i][j] = true;
			}
		}
		int count = 1;
		while (count>0) {
			RandomSet n = new RandomSet();
			RandomSet row = new RandomSet(squareBoard.length);
			RandomSet column = new RandomSet(squareBoard.length);
			if (usedSpaces[row.getBlocks()][column.getBlocks()]==false) {
				valueBoard[row.getBlocks()][column.getBlocks()] = n.getBlocks();
				squareBoard[row.getBlocks()][column.getBlocks()] = n.toString();
				usedSpaces[row.getBlocks()][column.getBlocks()]= true;
				count--;
			}
		}
	}


	public String outcome() {
		String message = "";
		for(int i = 0; i < squareBoard.length; i++) {
			for (int j =0 ; j<squareBoard.length; j++) {
				message = message +"["+ String.format("%5s", squareBoard[i][j])+"     "+"]"; 
			}
			message = message + "\n";
		}
		return message;
	}


	public void convertsquareBoardIntovalueBoard(Board b) {
		for (int i=0; i<b.getsizeBoard(); i++) {
			for(int k=0; k<b.getsizeBoard(); k++) {
				if (b.getvalueBoard()[i][k]!=0) {
					b.getsquareBoard()[i][k] = b.getvalueBoard()[i][k]+"";
				}
				else {
					b.getsquareBoard()[i][k] = "";
				}
			}
		}
	}


	public int getsizeBoard() {
		return sizeBoard;
	}
	public int[][] getvalueBoard() {
		return valueBoard;
	}

	public String[][] getsquareBoard() {
		return squareBoard;
	}
	public boolean[][] getusedSpaces() {
		return usedSpaces;
	}


}
