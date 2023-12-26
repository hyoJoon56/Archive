
import java.awt.Color;
import java.awt.Rectangle;


public class BallAndBlockModel {
	private BallAndBlockView v;
	static boolean isGameFinished;
	private Ball ball;
	private Bar bar;
	private int score;
	private Block [][] blocks;
	private BallAndBlockController c;

	public BallAndBlockModel(BallAndBlockView v, BallAndBlockController c) {
		this.v = v;
		ball = v.getBall();
		bar = v.getBar();
		score = v.getScore();
		blocks = v.getBlocks();
		this.c = c;
	}
	private boolean checkIntersectionBar(int x, int r, int w, int h) {
		double y = Math.sqrt(r*r-(x-ball.getCenter().getX())*(x-ball.getCenter().getX()))+ ball.getCenter().getY(); 
		if (bar.getX()-v.BALL_WIDTH <=x&& bar.getX()+v.BALL_WIDTH/2+w>=x) {
			if(bar.getY()-r == y) {
				return true;
			}
		}
		return false;
	}
	private boolean checkIntersectionBall(int x, int y, int r, int w, int h, int i, int j) {
		double k = Math.sqrt(r*r-(x-ball.getCenter().getX())*(x-ball.getCenter().getX()))+  ball.getCenter().getY();
		if (blocks[i][j].getX()-v.BALL_WIDTH  <= x && blocks[i][j].getX()+w+v.BALL_WIDTH/2>=x) {
			if (blocks[i][j].getY()-v.BALL_HEIGHT/2 <= k && blocks[i][j].getY()+h+v.BALL_HEIGHT/2>k) {
				return true;
			}
		}
		return false;
	}
	public void checkCollision(int dir) {
		if(dir == 0) { // up-right
			if(ball.getY()<0) {
				//ball.movement(1);
				c.setDirection(1);
			}
			else if(ball.getX()>v.WINDOW_WIDTH-v.BALL_WIDTH) {
				//ball.movement(2);
				c.setDirection(2);
			}

		}
		else if (dir ==1) { // Down-right
			if(ball.getY() > v.WINDOW_HEIGHT-v.BALL_HEIGHT*2) {
				c.setDirection(0);
			}
			if(ball.getX()>v.WINDOW_WIDTH-v.BALL_WIDTH) {
				c.setDirection(3);
			}
			//Bar
			if (ball.getBottomCenter().y >= bar.getY()) {
				// ball.getX(),ball.getY(),ball.getWidth(),ball.getHeight()), new Rectangle(bar.getX(), bar.getY(),bar.getWidth(),bar.getHeight()))
				if(checkIntersectionBar(ball.getX(), v.BALL_WIDTH/2, v.BAR_WIDTH, v.BAR_HEIGHT)) {
					c.setDirection(0);
				}
			}
			if(ball.getY() >  v.WINDOW_HEIGHT-v.BALL_HEIGHT-v.BALL_HEIGHT) {
				if (c.isSurvivalMode()) {
					v.setGameFinished(true);
					v.repaint();
					return;
				}
				c.setDirection(0);
				ball.setX(v.WINDOW_WIDTH/2 - v.BALL_WIDTH/2); 
				ball.setY(v.WINDOW_HEIGHT/2 - v.BALL_HEIGHT/2);
				v.setScore(0);
			}
		}
		else if (dir == 2) { // Up-Left
			if(ball.getY()< 0) {
				c.setDirection(3);
			}
			if(ball.getX() < 0) {
				c.setDirection(0);
			}

		}
		else if (dir ==3) { // Down-Left
			if(ball.getY() >  v.WINDOW_HEIGHT-v.BALL_HEIGHT-v.BALL_HEIGHT) {
				if (c.isSurvivalMode()) {
					v.setGameFinished(true);
					v.repaint();
					return;
				}
				c.setDirection(0);
				ball.setX(v.WINDOW_WIDTH/2 - v.BALL_WIDTH/2); 
				ball.setY(v.WINDOW_HEIGHT/2 - v.BALL_HEIGHT/2);
				v.setScore(0);
			}
			if(ball.getX()<0) {
				c.setDirection(1);
			}

			if (ball.getBottomCenter().y >= bar.getY()) {
				if(checkIntersectionBar(ball.getX(), v.BALL_WIDTH/2, v.BAR_WIDTH, v.BAR_HEIGHT)) {
					c.setDirection(2);
				}
			}

		}
		v.repaint();
	}
	public void checkCollisionBlock(int dir) {
		boolean collide = false;
		int index1 = 0;
		int index2 = 0;
		for (int i=0; i<v.BLOCK_ROWS; i++) {
			for(int j=0; j<v.BLOCK_COLUMNS; j++) {
				Block block = blocks[i][j];
				if(block.isHidden() == true) {
					continue;
				}
				if (block.isHidden() == false) {
					if (dir == 0) {
						if(this.checkIntersectionBall(ball.getX(), ball.getY(),v.BALL_WIDTH/2, v.BLOCK_WIDTH, v.BLOCK_HEIGHT, i, j)) {
							if(ball.getTopCenter().getX()>block.getX() && ball.getTopCenter().getX() <= block.getX()+v.BLOCK_WIDTH) {

								c.setDirection(1);
								System.out.println("up-right to down-right");
							}
							//else if (ball.getTopCenter().getX() >= block.getX()-v.BALL_WIDTH && ball.getTopCenter().getX() <= block.getX() ) {
							//	if (ball.getRightCenter().getY()<=block.getY()+v.BAR_HEIGHT && ball.getRightCenter().getY() >= block.getY()) {
							//		c.setDirection(3);
							//		System.out.println("up-right to down-left");
							//	}
							//}
							else {
								if(ball.getRightCenter().getX() > block.getX()+v.BLOCK_WIDTH) {
									c.setDirection(1);
									System.out.println("up-right to down-right");
								}
								else {
									c.setDirection(2);
									System.out.println("up-right to up-left");
								}
							}
							index1 = i;
							index2 = j;
							collide = true;

							scoreCheck(block.getColor());
							break;

						}
					}
					else if (dir ==1) {
						if(this.checkIntersectionBall(ball.getX(), ball.getY(),v.BALL_WIDTH/2, v.BLOCK_WIDTH, v.BLOCK_HEIGHT, i, j)) {
							if(ball.getBottomCenter().getX()>block.getX() && ball.getBottomCenter().getX() <= block.getX()+v.BLOCK_WIDTH) {

								c.setDirection(0);
								System.out.println("down-right to up-right");
							}
							else {
								if(ball.getRightCenter().getX() > block.getX()+v.BLOCK_WIDTH) {
									c.setDirection(0);
									System.out.println("down-right to up-right");
								}
								else {
									c.setDirection(3);
									System.out.println("down-right to down-left");
								}
							}
							index1 = i;
							index2 = j;
							collide = true;
							scoreCheck(block.getColor());

							break;

						}
					}
					else if (dir == 2) {
						if(this.checkIntersectionBall(ball.getX(), ball.getY(),v.BALL_WIDTH/2, v.BLOCK_WIDTH, v.BLOCK_HEIGHT, i, j)) {
							if(ball.getTopCenter().getX()>block.getX() && ball.getTopCenter().getX() <= block.getX()+v.BLOCK_WIDTH) {

								c.setDirection(3);
								System.out.println("up-left to down-left");
							}
							else {
								if(ball.getLeftCenter().getX() > block.getX()) {
									c.setDirection(3);
									System.out.println("up-left to down-left");
								}
								else {
								c.setDirection(0);
								System.out.println("up-left to up-right");
								}
							}
							index1 = i;
							index2 = j;
							collide = true;
							scoreCheck(block.getColor());

							break;
						}
					}
					else if (dir==3) {
						if(this.checkIntersectionBall(ball.getX(), ball.getY(),v.BALL_WIDTH/2, v.BLOCK_WIDTH, v.BLOCK_HEIGHT, i, j)) {
							if(ball.getBottomCenter().getX()>block.getX() && ball.getBottomCenter().getX() <= block.getX()+v.BLOCK_WIDTH) {

								c.setDirection(2);
								System.out.println("down-left to up-left");
							}
							else {
								if(ball.getLeftCenter().getX() > block.getX()) {
									c.setDirection(2);
									System.out.println("down-left to up-left");
								}
								c.setDirection(1);
								System.out.println("down-left to down-right");
							}
							index1 = i;
							index2 = j;
							collide = true;
							scoreCheck(block.getColor());

							break;
						}
					}
				}
			}
		}
		if (collide) {
			v.getBlocks()[index1][index2].setHidden(true);
		}
	}
	public void scoreCheck(int color) {
		if(color == 0) {
			v.setScore(v.getScore()+5);
		}
		else if(color == 1) {
			v.setScore(v.getScore()+15);
		}
		else if(color == 2) {
			v.setScore(v.getScore()+30);
		}
		else if(color == 3) {
			v.setScore(v.getScore()+45);
		}
		else if(color == 4) {
			v.setScore(v.getScore()+60);
		}
	}
	public void isGameFinish() {
		int count = 0;
		for (int i=0; i<v.BLOCK_ROWS; i++) {
			for(int j=0; j<v.BLOCK_COLUMNS; j++) {
				Block block = blocks[i][j];
				if (block.isHidden()) {
					count++;
				}
			}
		}
		if(count == v.BLOCK_ROWS*v.BLOCK_COLUMNS) {
			//timer.stop();
			v.setGameFinished(true);
		}
	}
}


