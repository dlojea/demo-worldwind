import javax.swing.*;

public class Demo extends JFrame {
    
    public static void main(String[] args) {
    	System.setProperty("gov.nasa.worldwind.app.config.document", "config/worldwind.xml");
    	AppFrame frame = new AppFrame();
        frame.setTitle("WorldWind Application");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});
    }
}