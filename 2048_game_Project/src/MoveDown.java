public class MoveDown extends Moves {
	MoveDown(Board a){
		super(a);
	}
	MoveDown(int [][] aa){
		super(aa);
	}
	public Board moveBlocks() {
		Board c = super.getGiven();
		int totalScore = super.getPoint();
		int [][] aa = this.countForZero(c.getsizeBoard(), c.getsizeBoard());

		for(int j=0; j<c.getsizeBoard();j++) {
			this.moveToDown(c.getvalueBoard(), aa, j);
		}
		for(int j=0; j<c.getsizeBoard();j++) {
			totalScore = totalScore + this.combineTwoBlocks(c.getvalueBoard(), j);
		}
		int [][] bb = this.countForZero(c.getsizeBoard(), c.getsizeBoard(),super.getGiven().getvalueBoard());
		for(int j=0; j<c.getsizeBoard();j++) {
			this.moveToDown(c.getvalueBoard(), bb, j);
		}
		c.convertsquareBoardIntovalueBoard(c);
		super.setPoint(super.getPoint()+totalScore);
		return c;

	}
	public int [][] moveBlocks(int [][] vv) {
		int [][] aa = this.countForZero();
		for(int j=0; j<vv.length;j++) {
			this.moveToDown(vv, aa, j);
		}
		int [][] bb = this.countForZero(vv);
		for(int j=0; j<vv.length;j++) {
			this.moveToDown(vv, bb, j);
		}
		return aa;
	}





	public int [][] countForZero (int rows, int columns ){
		int [][] zz = new int [rows][columns];
		int count = 0;
		for (int i = 0; i<super.getGiven().getsizeBoard(); i++) {
			for (int j=0; j<super.getGiven().getsizeBoard();j++) {
				for (int k=i; k<super.getGiven().getsizeBoard(); k++) {
					if (super.getGiven().getvalueBoard()[k][j]==0) {
						count++;
					}
				}
				zz[i][j]=count;
				count=0;
			}
		}

		return zz;
	}
	public int [][] countForZero (int rows, int columns, int[][]vv ){
		int [][] zz = new int [rows][columns];
		int count = 0;
		for (int i = 0; i<super.getGiven().getsizeBoard(); i++) {
			for (int j=0; j<super.getGiven().getsizeBoard();j++) {
				for (int k=i; k<super.getGiven().getsizeBoard(); k++) {
					if (super.getGiven().getvalueBoard()[k][j]==0) {
						count++;
					}
				}
				if (this.combineTwoBlocks(i, j)>0) {
					vv[i][j]= this.combineTwoBlocks(i, j);
					count++;
				}
				zz[i][j]=count;
				count=0;
			}
		}

		return zz;
	}

	public int [][] countForZero (){
		int [][] zz = new int [super.getValueOnly().length][super.getValueOnly()[0].length];
		int count = 0;
		for (int i = 0; i<super.getValueOnly().length; i++) {
			for (int j=0; j<super.getValueOnly().length;j++) {
				for (int k=i; k<super.getValueOnly().length; k++) {
					if (super.getValueOnly()[k][j]==0) {
						count++;
					}
				}
				zz[i][j]=count;
				count=0;
			}
		}

		return zz;
	}
	public int [][] countForZero (int[][]vv ){
		int [][] zz = new int [vv.length][vv[0].length];
		int count = 0;
		for (int i = 0; i<vv.length; i++) {
			for (int j=0; j<vv.length;j++) {
				for (int k=i; k<vv.length; k++) {
					if (super.getValueOnly()[k][j]==0) {
						count++;
					}
				}
				if (this.combineTwoBlocks(i, j, vv)>0) {
					vv[i][j]= this.combineTwoBlocks(i, j, vv);
					count++;
				}
				zz[i][j]=count;
				count=0;
			}
		}

		return zz;
	}

	public void moveToDown (int[][] vv, int [][] dd, int c) {
		for (int j =vv.length-1; j>=0; j--) {
			if (dd[j][c]+j>=vv.length) {
				dd[j][c] = 0;
			}
			if (vv[j][c]==0) {
				continue;
			}
			vv[j+dd[j][c]][c] = vv[j][c];
			if (dd[j][c] != 0) {
				vv[j][c]=0;
			}
		}
	}
	public int combineTwoBlocks(int[][] gg, int d) {
		int constant = 0;
		int count = 0;
		int score = 0;
		for (int i=super.getGiven().getsizeBoard()-1; i>=0; i--) {
			if (count ==0) {
				constant = gg[i][d];
				count++;
				continue;
			}
			if (constant!=0 && gg[i][d]==constant) {
				gg[i+1][d] = 2*constant;
				gg[i][d]=0;
				score = 2*constant;
			}
			count = 0;
		}
		return score;
	}

	public int combineTwoBlocks(int i, int d) {
		int constant = super.getGiven().getvalueBoard()[i][d];
		int count = 0;
		if (i+1>super.getGiven().getsizeBoard()-1) {
			return count;
		}
		if (constant!=0 && super.getGiven().getvalueBoard()[i+1][d]==constant) {
			count = constant*2;
			super.setPoint(count);
		}

		return count;
	}
	public int combineTwoBlocks(int i, int d, int[][] vv) {
		int constant = super.getValueOnly()[i][d];
		int count = 0;
		if (i+1>super.getValueOnly().length-1) {
			return count;
		}
		if (constant!=0 && super.getValueOnly()[i+1][d]==constant) {
			count = constant*2;

		}

		return count;
	}



}
