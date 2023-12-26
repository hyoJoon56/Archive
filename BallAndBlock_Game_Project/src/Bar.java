
import java.awt.Color;

public class Bar {
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	
	public Bar(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	public void movement(int barX) {
		if (x< barX) {
			x += 5;
		}
		else if(x> barX) {
			x -= 5;
		}
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
