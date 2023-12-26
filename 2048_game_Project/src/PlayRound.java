
public class PlayRound {
	public static final char UP = 'w';
	public static final char DOWN = 's';
	public static final char RIGHT = 'd';
	public static final char LEFT = 'a';

	private Board b;
	private Board c;
	private int score;
	private boolean gameOver;
	private boolean possibleMove;

	PlayRound (){
		b = new Board(3);
		gameOver = false;
		possibleMove = false;
		score = 0;
		System.out.println("Welcome to Infinite Version!");
		System.out.println("Here is your 3 x 3 boards!");


		while(gameOver == false) {
			c = new Board(b);
			System.out.println(c.outcome());
			if (checkGOver()==true) {
				System.out.println("Game Over! Your Final Score: " + score);
				break;
			}
			System.out.println("Score: "+score+"\n");
			char input = getInput();
			while (possibleMove==false) {
				char realInput = possibleMove(input);
				input = realInput;
			}
			possibleMove = false;
			moveB(input);
			b=c;
			possibleMove = false;

		}
	}
	PlayRound (int k){
		b = new Board(3);
		gameOver = false;
		possibleMove = false;
		score = 0;
		System.out.println("Welcome to 2048 Version!");
		System.out.println("Here is your 3 x 3 boards!");


		while(gameOver == false) {
			c = new Board(b);
			System.out.println(c.outcome());
			if(check2048() == true){
				System.out.println("Game Over! You won the game!" + "\n" +"Your Final Score:" + score);
			}
			if (checkGOver()==true) {
				System.out.println("Game Over! Your Final Score: " + score);
				break;
			}
			System.out.println("Score: "+score+"\n");
			char input = getInput();
			while (possibleMove==false) {
				char realInput = possibleMove(input);
				input = realInput;
			}
			possibleMove = false;
			moveB(input);
			b=c;
			possibleMove = false;

		}
	}

	public char getInput() {
		boolean incorrectInput  = true;
		char correctInput = 'c';
		System.out.println("Move the blocks by using 'w''a''s''d'");
		System.out.println("w: move up, a: move left. s: move down. d: move right");
		while (incorrectInput) {
			correctInput = TextIO.getlnChar();
			if (correctInput == UP || correctInput == DOWN || correctInput == RIGHT || correctInput == LEFT) {
				break;
			}
			else {
				System.out.println("Wrong input! You must choose one from only these four letters!");
			}
		}
		return correctInput;
	}
	public char getInput(char k) {
		System.out.println("Not Possible to Move in this direction right now"+"\n" +"Try different direction");
		boolean incorrectInput  = true;
		while (incorrectInput) {
			char changeInput = TextIO.getlnChar();
			if (changeInput !=k) {
				if (changeInput == UP || changeInput == DOWN || changeInput == RIGHT || changeInput == LEFT) {
					k = changeInput;
					break;
				}
			}
			System.out.println("Wrong input! You must choose one input that can move those blocks!");
		}
		return k;
	}


	public char possibleMove(char a) {
		CheckMoves vv = new CheckMoves(this.c.getvalueBoard(),this.c.getsizeBoard());
		if (a == UP) {
			if (vv.checkPossibleMove(0)==false) {
				return a = getInput(a);
			}
			this.possibleMove = true;
		}
		if (a == DOWN) {
			if (vv.checkPossibleMove(1)==false) {
				return a = getInput(a);
			}
			this.possibleMove = true;
		}
		if (a == LEFT) {
			if (vv.checkPossibleMove(2)==false) {
				return a = getInput(a);
			}
			this.possibleMove = true;
		}
		if (a== RIGHT) {
			if (vv.checkPossibleMove(3)==false) {
				return a = getInput(a);
			}
			this.possibleMove = true;
		}
		return a;
	}

	public void moveB(char input) {
		if (input == UP) {
			Moves u = new MoveUp(c);
			u.moveBlocks();
			score = score + u.getPoint();
		}
		if (input == DOWN) {
			Moves u = new MoveDown(c);
			u.moveBlocks();
			score = score + u.getPoint();
		}
		if (input == LEFT) {
			Moves u = new MoveLeft(c);
			u.moveBlocks();
			score = score + u.getPoint();
		}
		if (input == RIGHT) {
			Moves u = new MoveRight(c);
			u.moveBlocks();
			score = score + u.getPoint();
		}
	}

	public boolean checkGOver() {
		CheckMoves gg = new CheckMoves(this.c.getvalueBoard(),this.c.getsizeBoard());
		return gg.checkGameOver(c.getsizeBoard());
	}

	public boolean check2048() {
		for (int i=0; i< this.c.getsizeBoard(); i++) {
			for (int j=0; j<this.c.getsizeBoard(); j++) {
				if (c.getvalueBoard()[i][j]==2048) {
					return true;
				}
			}
		}
		return false;
	}


}
