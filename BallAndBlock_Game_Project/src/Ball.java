
import java.awt.Color;
import java.awt.Point;

public class Ball {
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	private BallAndBlockView v;
	private int ballSpeed;
	
	public Ball(int x, int y, int width, int height, Color color, BallAndBlockView v) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.v = v;
		ballSpeed = v.BALL_SPEED;
	}
	public Point getCenter() {
		return new Point(x+(v.BALL_WIDTH/2), y+ (v.BALL_HEIGHT/2));
	}
	public Point getBottomCenter() {
		return new Point(x+(v.BALL_WIDTH/2), y+ v.BALL_HEIGHT);
	}
	public Point getTopCenter() {
		return new Point (x+(v.BALL_WIDTH/2), y);
	}
	public Point getLeftCenter() {
		return new Point (x, y+ v.BALL_HEIGHT/2);
	}
	public Point getRightCenter() {
		return new Point (x+v.BALL_WIDTH, y+ v.BALL_HEIGHT/2);
	}
	public void movement(int dir) {
		if(dir == 0) { // up-right
			x += ballSpeed;
			y -= ballSpeed;
		}else if (dir ==1) { // Down-right
			x += ballSpeed;
			y += ballSpeed;
		}else if (dir == 2) { // Up-Left
			x -= ballSpeed;
			y -= ballSpeed;
		}else if (dir ==3) { // Down-Left
			x -= ballSpeed;
			y += ballSpeed;
		}
		v.repaint();
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}
	
}
