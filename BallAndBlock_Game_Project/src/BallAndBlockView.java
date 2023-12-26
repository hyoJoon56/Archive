
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class BallAndBlockView extends JPanel {
	static final int BALL_WIDTH = 20;
	static final int BALL_HEIGHT = 20;
	static final int BLOCK_ROWS = 5;
	static final int BLOCK_COLUMNS = 10;
	static final int BLOCK_WIDTH = 40;
	static final int BLOCK_HEIGHT = 20;
	static final int BLOCK_GAP = 3;
	static final int BAR_WIDTH = 80;
	static final int BAR_HEIGHT = 20;
	static final int WINDOW_WIDTH = BLOCK_COLUMNS*40 + (BLOCK_GAP*BLOCK_COLUMNS) - BLOCK_GAP;
	static final int WINDOW_HEIGHT = 600;
	static final int BALL_SPEED = 5;
	
	private int blockWidth;
	private int speed;
	private int score;
	private int level;
	private Block[][] blocks;
	private boolean isGameFinished;
	private Ball ball;
	private Bar bar;
	public BallAndBlockView() {
		JFrame window = new JFrame ("Ball & Block Game");
		window.setVisible(false);
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation( (screensize.width - window.getWidth())/2,
				(screensize.height - window.getHeight())/2 );
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.add(this,BorderLayout.CENTER);
		this.setBackground(Color.BLACK);
		blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		for ( int i=0; i<BLOCK_ROWS; i++) {
			for (int j=0; j<BLOCK_COLUMNS; j++) {
				blocks[i][j] = new Block(BLOCK_WIDTH*j + BLOCK_GAP*j,100 + i*BLOCK_HEIGHT + BLOCK_GAP*i, BLOCK_WIDTH, BLOCK_HEIGHT, 4-i, false  );
			}
		}
		ball = new Ball(WINDOW_WIDTH/2 - BALL_WIDTH/2, WINDOW_HEIGHT/2 - BALL_HEIGHT/2, BALL_WIDTH,BALL_HEIGHT, Color.WHITE, this);
		bar = new Bar(WINDOW_WIDTH/2 - BAR_WIDTH/2, WINDOW_HEIGHT-100, BAR_WIDTH,BAR_HEIGHT,Color.RED); 
		level = 1;
		speed = 1;
		blockWidth =80;

	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		colorChooser(g2);
		strcuturDraw(g2);
	}
	private void colorChooser(Graphics2D g2) {
		for ( int i=0; i<BLOCK_ROWS; i++) {
			for (int j=0; j<BLOCK_COLUMNS; j++) {
				if(blocks[i][j].isHidden() == true) {
					continue;
				}
				if(blocks[i][j].getColor() == 0) {
					g2.setColor(Color.WHITE);
				}
				else if(blocks[i][j].getColor() == 1) {
					g2.setColor(Color.YELLOW);
				}
				else if(blocks[i][j].getColor() == 2) {
					g2.setColor(Color.BLUE);
				}
				else if(blocks[i][j].getColor() == 3) {
					g2.setColor(Color.MAGENTA);
				}
				else if(blocks[i][j].getColor() == 4) {
					g2.setColor(Color.ORANGE);
				}
				g2.fillRect(blocks[i][j].getX(), blocks[i][j].getY(), blocks[i][j].getWidth(), blocks[i][j].getHeight());
			}
		}
	}
	private void strcuturDraw(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
		if( isGameFinished) {
			gameOver(g2);
			return;
		}
		g2.drawString("score: "+score, WINDOW_WIDTH/2-30, 20);

		//draw Ball
		g2.setColor(ball.getColor());
		g2.fillOval(ball.getX(), ball.getY(), BALL_WIDTH, BALL_HEIGHT);

		//draw Bar
		g2.setColor(bar.getColor());
		g2.fillRect(bar.getX(), bar.getY(), BAR_WIDTH, BAR_HEIGHT);
	}
	public void gameOver(Graphics g2) {
		for ( int i=0; i<BLOCK_ROWS; i++) {
			for (int j=0; j<BLOCK_COLUMNS; j++) {
				blocks[i][j].setHidden(true);
			}


		}
		g2.drawString("Final score: "+score+"  Game Over!", WINDOW_WIDTH/2-70, 50);
	}

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Block[][] getBlocks() {
		return blocks;
	}
	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}
	public boolean isGameFinished() {
		return isGameFinished;
	}
	public void setGameFinished(boolean isGameFinished) {
		this.isGameFinished = isGameFinished;
	}
	public Ball getBall() {
		return ball;
	}
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	public Bar getBar() {
		return bar;
	}
	public void setBar(Bar bar) {
		this.bar = bar;
	}

}
