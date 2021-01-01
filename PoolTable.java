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

	public static int height = 571;
	public static int width = 894;
	public static int X_ABSTAND;
	public static int Y_ABSTAND;
	ArrayList<Ball> balls;
	boolean allowedToMove = false;

	public PoolTable() {
		balls = new ArrayList<Ball>();
		setFocusable(true);
		addKeyListener(new TAdapter());
		ImageIcon ii = new ImageIcon(getClass().getResource("table.png"));
		table = ii.getImage();
		X_ABSTAND = (width - table.getWidth(null)) / 2;
		Y_ABSTAND = (height - table.getHeight(null)) / 2;
		mainBall = new Ball(width / 2 + 100, height / 2 - 24 + 100, "white",
				"main");
		secondBall = new Ball(width / 2 + 100, height / 2 - 24, "yellow",
				"second");

		balls.add(mainBall);
		balls.add(secondBall);
		for (int i = 1; i <= 10; i++) {

			Ball b = new Ball(width / 2 + 100 - 25*i, height / 2 - 24, "yellow",
					String.valueOf(i));
			balls.add(b);
		}
		System.out.println("x " + mainBall.getX() + " y " + mainBall.getY());
		System.out
				.println("x " + secondBall.getX() + " y " + secondBall.getY());
		System.out.println("ende: " + (width - X_ABSTAND - 20) + " "
				+ (height - Y_ABSTAND - 20));

		setDoubleBuffered(true);
		timer = new Timer(10, this);
		timer.start();
		addMouseListener(this);

	}

	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(table, X_ABSTAND, Y_ABSTAND, null);

		// draw balls
//		g2d.drawImage(mainBall.getImage(), (int) mainBall.getX() - 12,
//				(int) mainBall.getY() - 12, null);
//		g2d.drawImage(secondBall.getImage(), (int) secondBall.getX() - 12,
//				(int) secondBall.getY() - 12, null);
		for (Ball ball : balls) {
			g2d.drawImage(ball.getImage(), (int) ball.getX() - 12,
					(int) ball.getY() - 12, null);
		}
		g2d.fillOval((int) mainBall.getX() - 6, (int) mainBall.getY() - 6, 12,
				12);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// if(mainBall.getBounds().hasCollision(secondBall.getBounds())){
		// System.out.println("Kol");
		// int xx = (int) (secondBall.getX() - mainBall.getX());
		// int yy = (int) (secondBall.getY() - mainBall.getY() );
		// double laenge = Math.sqrt(xx*xx + yy*yy);
		//
		// secondBall.setX_r((double)xx/laenge);
		// secondBall.setY_r((double)yy/laenge);
		//
		// secondBall.setEnergy(mainBall.getEnergy());
		// mainBall.setEnergy(mainBall.getEnergy()/2);
		//
		// secondBall.setCannMove(true);
		// }

		
		for (int i = 0; i < balls.size(); i++) {
			Ball firstBall = balls.get(i);
			boolean kollision = false;
			for (int j = 0; j < balls.size(); j++) {
				Ball secBall = balls.get(j);
				if (firstBall.getBounds().hasCollision(secBall.getBounds()) && 
						!firstBall.equals(secBall)) {
					int xx = (int) (secBall.getX() - firstBall.getX());
					int yy = (int) (secBall.getY() - firstBall.getY());
					double laenge = Math.sqrt(xx * xx + yy * yy);
					secBall.setX_r((double) xx / laenge);
					secBall.setY_r((double) yy / laenge);

					firstBall.setX_r(-secBall.getY_r());
					firstBall.setY_r(secBall.getX_r());
					secBall.setEnergy(firstBall.getEnergy());

					secBall.setCannMove(true);
					secBall.moveRichtung();
					kollision = true;
					System.out.println(firstBall.getName() + " " + secBall.getName());
				}
				if (kollision) {
					
					firstBall.setEnergy(firstBall.getEnergy() / 5);
					firstBall.moveRichtung();
				}
			}
			
			
		}

		for (Ball actBall : balls) {
			if (actBall.getCanMove()) {
				actBall.moveRichtung();
			}
		}

		repaint();

	}

	/**
	 * @return the x_ABSTAND
	 */
	public int getX_ABSTAND() {
		return X_ABSTAND;
	}

	/**
	 * @return the y_ABSTAND
	 */
	public int getY_ABSTAND() {
		return Y_ABSTAND;
	}

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

		double maus_loc_x = e.getX();
		double maus_loc_y = e.getY();

		int xx = (int) (mainBall.getX() - maus_loc_x);
		int yy = (int) (mainBall.getY() - maus_loc_y);
		double laenge = Math.sqrt(xx * xx + yy * yy);

		mainBall.setX_r((double) xx / laenge);
		mainBall.setY_r((double) yy / laenge);
		System.out.println("xr " + mainBall.getX_r());
		System.out.println("yr " + mainBall.getY_r());
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

}
