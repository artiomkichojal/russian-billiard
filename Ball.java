import java.awt.Image;

import javax.swing.ImageIcon;

public class Ball extends Circle{
	// private double x, y;
	double x_r, y_r; // bewegungs richtung
	private Image image;
	boolean visible;
	public static int RADIUS;
	private boolean moving = true;

	double energy;
	double speed;
	boolean canMove = false;
	private String ballColor;
	private String name;
	double mass = 1;

	/**
	 * x,y ist Mittelpunkt
	 * 
	 * @param x
	 * @param y
	 * @param ballColor
	 */
	public Ball(int x, int y, String ballColor, String name) {
		super(x, y, 0);
		this.setBallColor(ballColor);
		// System.out.println(pooltable.getWidth());
		if (ballColor.toLowerCase().equals("white")) {
			ImageIcon ii = new ImageIcon(getClass().getResource("ballWhite.png"));
			image = ii.getImage();
		} else {
			ImageIcon ii = new ImageIcon(getClass().getResource("ballYellow.png"));
			image = ii.getImage();
		}
		visible = true;
		x_r = 0;
		y_r = 0;
		Ball.RADIUS = image.getHeight(null) / 2;
		radius = Ball.RADIUS;
		energy = 5;
		speed = 1;
		this.name = name;
		this.mass = 1;

	}

	public String getBallColor() {
		return ballColor;
	}

	public void setBallColor(String ballColor) {
		this.ballColor = ballColor;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Ball b = (Ball) obj;
		return this.name.equals(b.name);
	}

	public Image getImage() {
		return image;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setCannMove(boolean par) {
		canMove = par;
	}

	public boolean getCanMove() {
		return canMove;
	}

	public void moveRichtung() {
		// rechtem rand
		if ((this.x >= (PoolTable.WALL_RIGHT_INNER- Ball.RADIUS))) {
			x_r = -1 * x_r;
			energy *= 0.98;
		}
		// kollision mit oberem
		if (this.y <= (PoolTable.WALL_UP_INNER + Ball.RADIUS)) {
			y_r = -1 * y_r;
			energy *= 0.98;
		}
		// linkem rand
		if ((this.x <= (PoolTable.WALL_LEFT_INNER + Ball.RADIUS))) {
			x_r = -1 * x_r;
			energy *= 0.98;
		}
		// kollision mit unterem
		if ((this.y >= (PoolTable.WALL_DOWN_INNER - Ball.RADIUS))) {
			y_r = -1 * y_r;
			energy *= 0.98;
		}
		
		if (energy > 0) {
			this.x += x_r * energy;
			this.y += y_r * energy;
		}
		
		else if ((x_r * energy) <= 0.0001 && (y_r * energy) <= 0.0001) {
			canMove = false;
		} 
		energy -= 0.005;
		
	}

	public Circle getBounds() {
		return new Circle((int) x, (int) y, Ball.RADIUS);
	}

	/**
	 * @return the moving
	 */
	public boolean isMoving() {
		return moving;
	}


	public double getX_r() {
		return x_r;
	}

	public void setX_r(double x_r) {
		this.x_r = x_r;
	}

	public double getY_r() {
		return y_r;
	}

	public void setY_r(double y_r) {
		this.y_r = y_r;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}
}
