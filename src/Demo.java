import javax.swing.*;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;

public class Demo {
    
    public static void main(String[] args) {
    	Configuration.setValue(AVKey.INITIAL_LATITUDE, 42.29);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -8.7376);
        //Configuration.setValue(AVKey.INITIAL_LATITUDE, 42.24);
        //Configuration.setValue(AVKey.INITIAL_LONGITUDE, -6.79);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 2500);
        Configuration.setValue(AVKey.INITIAL_PITCH, 45);
    	
    	System.setProperty("gov.nasa.worldwind.app.config.document", "config/worldwind.xml");
    	AppFrame frame = new AppFrame();
        frame.setTitle("WorldWind Application");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
        });
    }
}