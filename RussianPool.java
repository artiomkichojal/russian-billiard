import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RussianPool extends JFrame{
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static int WINDOW_WIDTH = 0;
	public static int WINDOW_HEIGHT = 0;

	public RussianPool() {
		setPlatformLookAndFell();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//setSize(900,600); //30 die obere leiste
		WINDOW_WIDTH = (int)dim.getWidth();
		WINDOW_HEIGHT = (int)dim.getHeight();
		System.out.println("WINDOW_WIDTH " + WINDOW_WIDTH + " WINDOW_HEIGHT " + WINDOW_HEIGHT);
		add(new PoolTable());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("Pool");
		setResizable(true);
		
		setVisible(true);
		
	}
	private void setPlatformLookAndFell() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Math.toDegrees(Math.acos(-0.5*Math.sqrt(3))));
		System.out.println(Math.cos(45));
		new RussianPool();

	}

}
