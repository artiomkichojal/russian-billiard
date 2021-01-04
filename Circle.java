import java.awt.Point;


public class Circle {
	double x;
	double y;
	double radius;
	Point collisionPoint;
	
	public Circle(double x,double y,double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		collisionPoint = new Point();
	}
	public boolean hasCollision(Circle circle){
		
	    double xDiff = x - circle.getX();
	    double yDiff = y - circle.getY();
//	    System.out.println("xdiff" + xDiff);
//	    System.out.println("ydiff" + yDiff);
	    double distance = Math.sqrt((Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));
//	    System.out.println("distance " + distance);
		collisionPoint = new Point((int)(x + radius), (int)(y + radius));
		circle.collisionPoint = new Point((int)(x + radius), (int)(y + radius)); 
	    boolean res = (distance - 2) <= (radius + circle.getRadius());
	    // if (res) {

	    // 	System.out.println("Kollision at " + collisionPoint.toString());
		// }
	    return res;
	}
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

}
