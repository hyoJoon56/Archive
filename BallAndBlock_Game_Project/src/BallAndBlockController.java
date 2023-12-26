
import java.awt.event.*;

import javax.swing.Timer;

public class BallAndBlockController implements FocusListener, MouseListener, KeyListener, ActionListener  {
	private BallAndBlockView v;
	private int direction;
	private int ballSpeed;
	private Timer timer;
	private int barX;
	private BallAndBlockModel m;
	private Ball ball;
	private Bar bar;
	private boolean survivalMode;
	public BallAndBlockController(BallAndBlockView v) {
		this.v = v;
		if((int) Math.round(Math.random())==1) {
			direction = 2;
		}
		else {
			direction = 0;
		}
		barX = v.WINDOW_WIDTH/2 - v.BAR_WIDTH/2;
		timer = new Timer(20, this);
		v.addKeyListener(this);
		v.addMouseListener(this);
		v.addFocusListener(this);
		v.requestFocusInWindow();
		m = new BallAndBlockModel(this.v, this);
		ball = v.getBall();
		bar = v.getBar();
		timer.start();
		survivalMode = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() != KeyEvent.VK_LEFT && e.getKeyCode() != KeyEvent.VK_RIGHT) {
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT && barX > 0) {
			barX -= 25;
			if(v.getBar().getX() <barX) {
				barX = v.getBar().getX() ;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT && barX<v.WINDOW_WIDTH-v.BAR_WIDTH) {
			barX += 25;
			if(v.getBar().getX()  > barX) {
				barX = v.getBar().getX() ;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ball.movement(direction);
		bar.movement(barX);
		m.checkCollision(direction); //wall bar
		m.checkCollisionBlock(direction); //blocks
		v.repaint(); //redraw

		//Game over
		m.isGameFinish();		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		timer.stop();
	}
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		timer.start();
		v.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getBarX() {
		return barX;
	}
	public void setBarX(int barX) {
		this.barX = barX;
	}
	public int getBallSpeed() {
		return ballSpeed;
	}
	public void setBallSpeed(int ballSpeed) {
		this.ballSpeed = ballSpeed;
	}
	public boolean isSurvivalMode() {
		return survivalMode;
	}

	
}
