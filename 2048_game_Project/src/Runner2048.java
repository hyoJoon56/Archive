
public class Runner2048 {
	public static void main(String[] args) {
		boolean beginner = true;
		boolean finishReading = false;
		System.out.println("Welcome to 2048 Mini Version!");
		while(beginner){
			System.out.println("If you want to know the basic rule, please type 'yes'."+"\n"+ "If you don't want to see the basic rule, please type 'no'.");
			beginner = TextIO.getlnBoolean();
			if (beginner) {
				System.out.println("1. 2048 Mini is played on a gray 3×3 grid, with numbered tiles that slide when a player moves them by 'w', 'a', 's', 'd' keys.");
				System.out.println("2. Every turn, a new tile will randomly appear in an empty spot on the board with a value of either 2 or 4");
				System.out.println("3. Tiles slide as far as possible in the chosen direction until they are stopped by either another tile or the edge of the grid.");
				System.out.println("4. If two tiles of the same number collide while moving, they will merge into a tile with the total value of the two tiles that collided");
				System.out.println("5. The resulting tile cannot merge with another tile again in the SAME move");
				System.out.println("6. If a move causes three consecutive tiles of the same value to slide together, only the two tiles farthest along the direction of motion will combine. ");
				System.out.println("7. The user's score starts at zero, and is increased whenever two tiles combine, by the value of the new tile.");
				System.out.println("8. When the player has no legal moves (there are no empty spaces and no adjacent tiles with the same value), the game ends.");
				while(finishReading == false) {
					System.out.println("Type 'Yes' if you finished reading these basic rules!");
					finishReading = TextIO.getlnBoolean();
				}

			}
		} 
		int input = 0;
		System.out.println("We have Two version for this game");
		System.out.println("1: Infinite Version-After reaching the 2048 tile, you can continue to play (beyond the 2048 tile) to reach higher scores.");
		System.out.println("2: 2048 Version- When you reache the 2048 tile, you won the game!");
		System.out.println("Please typer '1' or '2' to choose the version");
		while(input!= 1 || input !=2) {
			input = TextIO.getlnInt();
			if (input == 1 || input == 2){
				break;
			}
			System.out.println("You must type either number '1' or number '2' to play this game!");
		}

		if (input==1) {
			PlayRound start = new PlayRound();
		}
		if (input==2) {
			PlayRound startS = new PlayRound(2);
		}
	}
}
