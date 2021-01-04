import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PoolTable extends JPanel implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Timer timer;
	Image table;
	Ball mainBall;
	Ball secondBall;
	boolean colided = true;

	// public static int height = 571;
	// public static int width = 894;
	// public static int X_ABSTAND;
	// public static int Y_ABSTAND;
	ArrayList<Ball> balls;
	boolean allowedToMove = false;

	public static double WALL_UP_OUTER = 0;
	public static double WALL_LEFT_OUTER = 0;
	public static double WALL_RIGHT_OUTER = 0;
	public static double WALL_DOWN_OUTER = 0;

	public static double WALL_UP_INNER = 0;
	public static double WALL_LEFT_INNER = 0;
	public static double WALL_RIGHT_INNER = 0;
	public static double WALL_DOWN_INNER = 0;

	public static final double WALL_WIDTH = 20;

	private boolean isTageting = false;

	double maus_loc_x = 0;
	double maus_loc_y = 0;

	public PoolTable() {
		setFocusable(true);
		addKeyListener(new TAdapter());
		balls = new ArrayList<Ball>();

		ImageIcon ii = new ImageIcon(getClass().getResource("table.png"));
		table = ii.getImage();
		System.out.println("table size " + table.getWidth(null) + " " + table.getHeight(null));
		setWallOuterCoordinates();
		setWallInnerCoordinates();
		spawnBalls();

		setDoubleBuffered(true);
		timer = new Timer(1, this);
		timer.start();
		addMouseListener(this);

	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(table, (int) WALL_LEFT_OUTER, (int) WALL_UP_OUTER, null);

		for (Ball ball : balls) {
			g2d.drawImage(ball.getImage(), (int) ball.getX() - Ball.RADIUS, (int) ball.getY() - Ball.RADIUS, null);
		}
		g2d.fillOval((int) mainBall.getX() - Ball.RADIUS / 2, (int) mainBall.getY() - Ball.RADIUS / 2, Ball.RADIUS,
				Ball.RADIUS);

		g2d.drawLine((int) WALL_LEFT_INNER, (int) WALL_UP_INNER, (int) WALL_RIGHT_INNER, (int) WALL_DOWN_INNER);
		g2d.drawLine((int) WALL_LEFT_INNER, (int) ((WALL_DOWN_INNER + WALL_UP_INNER) / 2), (int) WALL_RIGHT_INNER,
				(int) ((WALL_DOWN_INNER + WALL_UP_INNER) / 2));

		if (isTageting) {
			// zweiter punkt muss über die geradengleichung g = p1 + t * (p2 - p1) ermittelt
			// werden
			g2d.drawLine((int) maus_loc_x, (int) maus_loc_y,
					(int) maus_loc_x + ((int) mainBall.getX() - (int) maus_loc_x) * 10,
					(int) maus_loc_y + ((int) mainBall.getY() - (int) maus_loc_y) * 10);
		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (int i = 0; i < balls.size(); i++) {
			Ball firstBall = balls.get(i);
			boolean kollision = false;
			for (int j = 0; j < balls.size(); j++) {
				Ball secBall = balls.get(j);

				if (!firstBall.equals(secBall) && firstBall.hasCollision(secBall)) {
					// firstBall.hasCollision(secBall);
					System.out.println("Collisiion of " + firstBall.getName() + " " + secBall.getName());
					System.out.println("collision point first " + firstBall.collisionPoint.getX() + " "
							+ firstBall.collisionPoint.getY());
					System.out.println(
							"collision point sec " + secBall.collisionPoint.x + " " + secBall.collisionPoint.y);

					double normalVectorX = (secBall.getX() - firstBall.getX());
					double normalVectorY = (secBall.getY() - firstBall.getY());
					double laenge = Math.sqrt(normalVectorX * normalVectorX + normalVectorY * normalVectorY);

					// Normale normiert (laenge 1)
					double normalVectorNormiertX = normalVectorX / laenge;
					double normalVectorNormiertY = normalVectorY / laenge;

					System.out.println("normalvektor " + normalVectorX + " " + normalVectorY);

					// Tangente
					double tangentenVectorX = -normalVectorNormiertY;
					double tangentenVectorY = normalVectorNormiertX;

					// Dot Product Tangent
					double dpTan1 = firstBall.x_r * tangentenVectorX + firstBall.y_r * tangentenVectorY;
					double dpTan2 = secBall.x_r * tangentenVectorX + secBall.y_r * tangentenVectorY;

					// Dot Product Normal
					double dpNorm1 = firstBall.x_r * normalVectorNormiertX + firstBall.y_r * normalVectorNormiertY;
					double dpNorm2 = secBall.x_r * normalVectorNormiertX + secBall.y_r * normalVectorNormiertY;

					// Conservation of Momentum (Energieerhaltung)
					double m1 = (dpNorm1 * (firstBall.mass - secBall.mass) + 2.0 * secBall.mass * dpNorm2)
							/ (firstBall.mass + secBall.mass);
					double m2 = (dpNorm2 * (secBall.mass - firstBall.mass) + 2 * firstBall.mass * dpNorm1)
							/ (firstBall.mass + secBall.mass);


					firstBall.setX_r(tangentenVectorX * dpTan1 + normalVectorNormiertX * m1);
					firstBall.setY_r(tangentenVectorY * dpTan1 + normalVectorNormiertY * m1);

					secBall.setX_r(tangentenVectorX * dpTan2 + normalVectorNormiertX * m2);
					secBall.setY_r(tangentenVectorY * dpTan2 + normalVectorNormiertY * m2);

					// this.setNewVelocityVector(firstBall, secBall);
					// this.setNewVelocityVector(secBall, firstBall);

					// double xx = (secBall.getX() - firstBall.getX());
					// double yy = (secBall.getY() - firstBall.getY());
					// double laenge = Math.sqrt(xx * xx + yy * yy);
					// System.out.println("laenge von sec " + laenge);

					// secBall.setX_r((double) xx / laenge);
					// secBall.setY_r((double) yy / laenge);

					// firstBall.setX_r(-secBall.getY_r());
					// firstBall.setY_r(secBall.getX_r());

					// firstBall.setEnergy(firstBall.getEnergy() * 0.9);
					// secBall.setEnergy(firstBall.getEnergy());
					secBall.setCannMove(true);
					secBall.moveRichtung();

					firstBall.setCannMove(true);
					firstBall.moveRichtung();

					kollision = true;

				}
			}

		}

		for (Ball actBall : balls) {
			if (actBall.getCanMove()) {
				actBall.moveRichtung();
				// if (actBall.getName().equals("main")) {
				// System.out.println(actBall.getX()+ " " + actBall.getY());
				// //actBall.moveRichtung();
				// }
			}
		}

		repaint();

	}

	private void setNewVelocityVector(Ball firstBall, Ball secBall) {
		double normalVectorX = (secBall.getX() - firstBall.getX());
		double normalVectorY = (secBall.getY() - firstBall.getY());
		double laenge = Math.sqrt(normalVectorX * normalVectorX + normalVectorY * normalVectorY);

		double normalVectorNormiertX = (1 / laenge) * normalVectorX;
		double normalVectorNormiertY = (1 / laenge) * normalVectorY;

		System.out.println("normalvektor " + normalVectorX + " " + normalVectorY);

		double v1oX = firstBall.x_r - normalVectorNormiertX * (normalVectorNormiertX * firstBall.x_r);
		double v1oY = firstBall.y_r - normalVectorNormiertY * (normalVectorNormiertY * firstBall.y_r);

		double v2pX = normalVectorNormiertX * (normalVectorNormiertX * secBall.x_r);
		double v2pY = normalVectorNormiertY * (normalVectorNormiertY * secBall.y_r);

		

		double vNewX = v1oX + v2pX;
		double vNewY = v1oY + v2pY;
		System.out.println("old vel " + firstBall.getX_r() + " " + firstBall.getY_r());
		System.out.println("new vel " + vNewX + " " + vNewY);
		firstBall.setX_r(vNewX);
		firstBall.setY_r(vNewY);

	}

	public Point getNormalVector(Ball first, Ball second) {
		return new Point((int) (second.getX() - first.getX()), (int) (second.getY() - first.getY()));
	}

	// getCollisionPoint(Ball ball1, Ball ball2) {

	// }

	private void setWallOuterCoordinates() {
		PoolTable.WALL_LEFT_OUTER = (RussianPool.WINDOW_WIDTH - table.getWidth(null)) / 2;
		PoolTable.WALL_UP_OUTER = (RussianPool.WINDOW_HEIGHT - table.getHeight(null)) / 2;
		PoolTable.WALL_RIGHT_OUTER = PoolTable.WALL_LEFT_OUTER + table.getWidth(null);
		PoolTable.WALL_DOWN_OUTER = PoolTable.WALL_UP_OUTER + table.getHeight(null);
	}

	private void setWallInnerCoordinates() {
		PoolTable.WALL_LEFT_INNER = PoolTable.WALL_LEFT_OUTER + WALL_WIDTH;
		PoolTable.WALL_UP_INNER = PoolTable.WALL_UP_OUTER + WALL_WIDTH;
		PoolTable.WALL_RIGHT_INNER = PoolTable.WALL_RIGHT_OUTER - WALL_WIDTH;
		PoolTable.WALL_DOWN_INNER = PoolTable.WALL_DOWN_OUTER - WALL_WIDTH;
	}

	private void spawnBalls() {
		mainBall = new Ball((int) WALL_LEFT_INNER + 100, (int) WALL_UP_INNER - Ball.RADIUS * 2 + 100, "white", "main");
		secondBall = new Ball((int) WALL_LEFT_INNER + 100, (int) WALL_UP_INNER + Ball.RADIUS * 2, "yellow", "second");

		balls.add(mainBall);
		balls.add(secondBall);
		for (int i = 1; i <= 3; i++) {

			Ball b = new Ball((int) WALL_RIGHT_INNER - 100 - 30 * i, (int) ((WALL_DOWN_INNER + WALL_UP_INNER) / 2),
					"yellow", String.valueOf(i));
			balls.add(b);
		}
		System.out.println("x " + mainBall.getX() + " y " + mainBall.getY());
		System.out.println("x " + secondBall.getX() + " y " + secondBall.getY());
	}
	// getWALL_UP_INNER() {
	// return RussianPool.WINDOW_HEIGHT - RussianPool.WINDOW_WIDTH +
	// table.getWidth(null)) / 2
	// }

	private class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_SPACE) {
				// allowedToMove = !allowedToMove;
				mainBall.setCannMove(true);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		isTageting = true;
		this.maus_loc_x = e.getX();
		this.maus_loc_y = e.getY();

		int xx = (int) (mainBall.getX() - this.maus_loc_x);
		int yy = (int) (mainBall.getY() - this.maus_loc_y);
		double laenge = Math.sqrt(xx * xx + yy * yy);

		mainBall.setX_r((double) xx / laenge);
		mainBall.setY_r((double) yy / laenge);
		System.out.println("xr " + mainBall.getX_r());
		System.out.println("yr " + mainBall.getY_r());
		System.out.println("Mouse Position " + this.maus_loc_x + "  " + this.maus_loc_y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("HAllo");
		isTageting = false;

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
