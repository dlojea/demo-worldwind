/*
 * Copyright 2006-2009, 2017, 2020 United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 * 
 * The NASA World Wind Java (WWJ) platform is licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * NASA World Wind Java (WWJ) also contains the following 3rd party Open Source
 * software:
 * 
 *     Jackson Parser â€“ Licensed under Apache 2.0
 *     GDAL â€“ Licensed under MIT
 *     JOGL â€“ Licensed under  Berkeley Software Distribution (BSD)
 *     Gluegen â€“ Licensed under Berkeley Software Distribution (BSD)
 * 
 * A complete listing of 3rd Party software notices and licenses included in
 * NASA World Wind Java (WWJ)  can be found in the WorldWindJava-v2.2 3rd-party
 * notices and licenses PDF found in code directory.
 */

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.view.firstperson.BasicFlyView;

import java.awt.BorderLayout;

import javax.swing.*;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Demo extends JFrame {
	
	private WorldWindowGLCanvas wwd;
	
    public Demo() throws JSONException {
        wwd = new WorldWindowGLCanvas();
        wwd.setPreferredSize(new java.awt.Dimension(1000, 800));
        getContentPane().add(wwd, java.awt.BorderLayout.CENTER);
        wwd.setModel(new BasicModel());
        
        StatusBar statusBar = new StatusBar();
        add(statusBar, BorderLayout.PAGE_END);
        statusBar.setEventSource(wwd);
        
        BasicFlyView flyView = new BasicFlyView();
        wwd.setView(flyView);
        
        JSONObject json = new JSONObject(" {\r\n"
				+ "        \"Actitud\": \"Attitude:pitch=-0.4009615182876587,yaw=1.6788901090621948,roll=0.0008944717119447887\",\r\n"
				+ "        \"Elevacion\": \"61.610000000000014\",\r\n"
				+ "        \"GPS\": \"GPSInfo:fix=6,num_sat=10\",\r\n"
				+ "        \"Localización Sistema Global\": \"LocationGlobal:lat=42.2905979,lon=-8.794522,alt=361.61\",\r\n"
				+ "        \"Localización Sistema Global Relativo a Home\": \"LocationGlobalRelative:lat=42.2905979,lon=-8.794522,alt=145.499\",\r\n"
				+ "        \"Localización Sistema Local\": \"LocationLocal:north=797.6734008789062,east=-439.1247253417969,down=-145.49925231933594\",\r\n"
				+ "        \"Tiempo\": \"2022-04-01 10:01:47.266832\"\r\n"
				+ "    }");
        
        Vista v = new Vista(json);
        
        updateView(v);
    }
    
    public void updateView(Vista v) {
    	BasicFlyView view = (BasicFlyView) wwd.getView();
        
        view.setEyePosition(v.getPosition());
        view.setHeading(v.getYaw());
        view.setPitch(v.getPitch());
        view.setRoll(v.getRoll());
        
        wwd.redraw();
    }
    
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
				try {
					JFrame frame = new Demo();
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                frame.pack();
	                frame.setVisible(true);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}