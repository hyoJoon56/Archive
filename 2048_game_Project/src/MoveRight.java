
public class MoveRight extends Moves {
	MoveRight(Board a){
		super(a);
	}
	MoveRight(int [][] aa){
		super(aa);
	}

	public Board moveBlocks() {
		Board c = super.getGiven();
		int totalScore = super.getPoint();
		int [][] aa = this.countForZero(c.getsizeBoard(), c.getsizeBoard());

		for(int j=0; j<c.getsizeBoard();j++) {
			this.moveToRight(c.getvalueBoard(), aa, j);
		}
		for(int j=0; j<c.getsizeBoard();j++) {
			totalScore = totalScore + this.combineTwoBlocks(c.getvalueBoard(), j);
		}
		int [][] bb = this.countForZero(c.getsizeBoard(), c.getsizeBoard(),super.getGiven().getvalueBoard());
		for(int j=0; j<c.getsizeBoard();j++) {
			this.moveToRight(c.getvalueBoard(), bb, j);
		}

		c.convertsquareBoardIntovalueBoard(c);
		super.setPoint(super.getPoint()+totalScore);
		return c;
	}
	public int [][] moveBlocks(int [][] vv) {
		int [][] aa = this.countForZero();
		for(int j=0; j<vv.length;j++) {
			this.moveToRight(vv, aa, j);
		}
		int [][] bb = this.countForZero(vv);
		for(int j=0; j<vv.length;j++) {
			this.moveToRight(vv, bb, j);
		}
		return aa;
	}






	public int [][] countForZero (int rows, int columns ){
		int [][] zz = new int [rows][columns];
		int count = 0;
		for (int i = 0; i<super.getGiven().getsizeBoard(); i++) {
			for (int j=0; j<super.getGiven().getsizeBoard();j++) {
				for (int k=j; k<super.getGiven().getsizeBoard(); k++) {
					if (super.getGiven().getvalueBoard()[i][k]==0) {
						count++;
					}
				}
				zz[i][j]=count;
				count=0;
			}
		}

		return zz;
	}
	public int [][] countForZero (int rows, int columns, int[][] vv ){
		int [][] zz = new int [rows][columns];
		int count = 0;
		for (int i = 0; i<super.getGiven().getsizeBoard(); i++) {
			for (int j=0; j<super.getGiven().getsizeBoard();j++) {
				for (int k=j; k<super.getGiven().getsizeBoard(); k++) {
					if (super.getGiven().getvalueBoard()[i][k]==0) {
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
			for (int j=0; j<super.getValueOnly()[0].length;j++) {
				for (int k=j; k<super.getValueOnly().length; k++) {
					if (super.getValueOnly()[i][k]==0) {
						count++;
					}
				}
				zz[i][j]=count;
				count=0;
			}
		}

		return zz;
	}
	public int [][] countForZero (int[][] vv ){
		int [][] zz = new int [vv.length][vv[0].length];
		int count = 0;
		for (int i = 0; i<vv.length; i++) {
			for (int j=0; j<vv.length;j++) {
				for (int k=j; k<vv.length; k++) {
					if (super.getValueOnly()[i][k]==0) {
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

	public void moveToRight (int[][] vv, int [][] dd, int c) {
		for (int j =vv.length-1; j>=0; j--) {
			if (dd[c][j]+j>=vv.length) {
				dd[c][j] = 0;
			}
			if (vv[c][j]==0) {
				continue;
			}
			vv[c][j+dd[c][j]] = vv[c][j];
			if (dd[c][j] != 0) {
				vv[c][j]=0;
			}
		}
	}
	public int combineTwoBlocks(int[][] gg, int d) {
		int constant = 0;
		int count = 0;
		int score = 0;
		for (int i=super.getGiven().getsizeBoard()-1; i>=0; i--) {
			if (count ==0) {
				constant = gg[d][i];
				count++;
				continue;
			}
			if (constant!=0 && gg[d][i]==constant) {
				gg[d][i+1] = 2*constant;
				gg[d][i]=0;
				score = 2*constant;
			}
			count = 0;
		}
		return score;
	}


	public int combineTwoBlocks(int i, int d) {
		int constant = super.getGiven().getvalueBoard()[i][d];
		int count = 0;
		if (d+1>super.getGiven().getsizeBoard()-1) {
			return count;
		}
		if (constant!=0 && super.getGiven().getvalueBoard()[i][d+1]==constant) {
			count = constant*2;
			super.setPoint(count);
		}

		return count;
	}
	public int combineTwoBlocks(int i, int d, int[][] vv) {
		int constant = super.getValueOnly()[i][d];
		int count = 0;
		if (d+1>super.getValueOnly().length-1) {
			return count;
		}
		if (constant!=0 && super.getValueOnly()[i][d+1]==constant) {
			count = constant*2;
		}

		return count;
	}
}



