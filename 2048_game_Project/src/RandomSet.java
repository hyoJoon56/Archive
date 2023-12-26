public class RandomSet {
	private int blocks;

	public RandomSet() {
		int [] RandNum = new int[10]; //generate the new number 2 and 4...
		for (int i=0; i<RandNum.length; i++) { //it's about 80% chance to have 2 and 20% to get 4
			if (i<=7) {
				RandNum[i]=2;
				continue;
			}
			RandNum[i]=4;
		}
		blocks = RandNum[(int)(Math.random()*9)];
	}
	public RandomSet(int size) {
		int [] RandNum = new int[size]; 
		for (int i=0; i<RandNum.length; i++) { //generate random location of X or Y
			RandNum[i]= i;
		}
		blocks = RandNum[(int)(Math.random()*(size))];

	}

	public int getBlocks() {
		return blocks;
	}

	public String toString() {
		return ""+blocks;
	}
}
