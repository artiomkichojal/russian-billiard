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
		timer = new Timer(10, this);
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
			// zweiter punkt muss über die geradengleichung g = p1 + t * (p2 - p1) ermittelt werden
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
				if (firstBall.getBounds().hasCollision(secBall.getBounds()) && !firstBall.equals(secBall)) {
					double xx =  (secBall.getX() - firstBall.getX());
					double yy = (secBall.getY() - firstBall.getY());
					double laenge = Math.sqrt(xx * xx + yy * yy);
					System.out.println("laenge von sec " + laenge);



					secBall.setX_r((double) xx / laenge);
					secBall.setY_r((double) yy / laenge);

					firstBall.setX_r(-secBall.getY_r());
					firstBall.setY_r(secBall.getX_r());

					firstBall.setEnergy(firstBall.getEnergy() * 0.9);
					firstBall.moveRichtung();

					secBall.setEnergy(firstBall.getEnergy());
					secBall.setCannMove(true);
					secBall.moveRichtung();

					kollision = true;
					System.out.println(firstBall.getName() + " " + secBall.getName());

				}
				// if (kollision) {

				// 	firstBall.setEnergy(firstBall.getEnergy() / 5);
				// 	firstBall.moveRichtung();
				// }
			}

		}

		for (Ball actBall : balls) {
			if (actBall.getCanMove()) {
				actBall.moveRichtung();
			}
		}

		repaint();

	}

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
		for (int i = 1; i <= 10; i++) {

			Ball b = new Ball((int) WALL_RIGHT_INNER - 100 - 25 * i, (int) ((WALL_DOWN_INNER + WALL_UP_INNER) / 2),
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
