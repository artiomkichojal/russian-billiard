import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class RussianPool extends JFrame{
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public RussianPool() {
		setPlatformLookAndFell();
		add(new PoolTable());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900,600); //30 die obere leiste
		setLocationRelativeTo(null);
		setTitle("Pool");
		setResizable(false);
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
