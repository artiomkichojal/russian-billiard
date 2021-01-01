import java.awt.Image;

import javax.swing.ImageIcon;

public class Ball {
	private double x, y;
	double x_r, y_r; // bewegungs richtung
	private Image image;
	boolean visible;
	int radius;
	private boolean moving = true;

	double energy;
	double speed;
	boolean canMove = false;
	private String ballColor;
	private String name;

	/**
	 * x,y ist Mittelpunkt
	 * 
	 * @param x
	 * @param y
	 * @param ballColor
	 */
	public Ball(int x, int y, String ballColor, String name) {
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
		this.x = x;
		this.y = y;
		x_r = 0;
		y_r = 0;
		radius = image.getHeight(null) / 2;
		energy = 5;
		speed = 1;
		this.name = name;

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

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
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
		if ((this.x >= (Constants.widthJpanel - Constants.x_Abstand - 20 - radius))) {
			x_r = -1 * x_r;
		}
		// kollision mit oberem
		if (this.y <= (Constants.y_Abstand + 20 + radius)) {
			y_r = -1 * y_r;
		}
		// linkem rand
		if ((this.x <= (Constants.x_Abstand + 20 + radius))) {
			x_r = -1 * x_r;
		}
		// kollision mit unterem
		if ((this.y >= (Constants.heightJpanel - Constants.y_Abstand - 20 - radius))) {
			y_r = -1 * y_r;
		}
		
		if (energy > 0) {
			this.x +=x_r*energy;
			this.y += y_r*energy;
		}
		else if (energy <= 0) {
			canMove = false;
			energy = 1;
		} 
		//System.out.println(ballColor + " " + energy);
		energy -= 0.001;
		
	}

	public Circle getBounds() {
		return new Circle((int) x, (int) y, radius);
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
