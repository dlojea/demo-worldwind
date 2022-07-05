import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

import org.json.JSONObject;

public class TimerTaskGetData extends TimerTask{
	AppFrame appFrame;
	double lat = 42.2905979;

	Socket socket;
	InputStream input;

	public TimerTaskGetData(AppFrame appFrame) {
		try {
			this.appFrame = appFrame;
			this.socket = new Socket("localhost", 20064);
			this.input = socket.getInputStream();
		} catch (Exception e) {}
	}

	@Override
	public void run() {
		//System.out.println("AAAAA");
    	byte[] data = new byte[8192];

		try {
			this.input.read(data);
	
			String jsonString = new String(data, StandardCharsets.UTF_8).replace("\\u00f3", "ó");
			jsonString = jsonString.substring(1, jsonString.length()-2);
	
			//System.out.println(jsonString);
	
			JSONObject json = new JSONObject(jsonString);
	
			//System.out.println("\n");
			//System.out.println(json);
	
			/*
			JSONObject json = new JSONObject(" {\r\n"
		            + "        \"Actitud\": \"Attitude:pitch=-0.4009615182876587,yaw=1.6788901090621948,roll=0.0008944717119447887\",\r\n"
	        	    + "        \"Elevacion\": \"61.610000000000014\",\r\n"
	    		    + "        \"GPS\": \"GPSInfo:fix=6,num_sat=10\",\r\n"
	       	   	    + "        \"Localización Sistema Global\": \"LocationGlobal:lat="+this.lat+",lon=-8.794522,alt=361.61\",\r\n"
	       	  	    + "        \"Localización Sistema Global Relativo a Home\": \"LocationGlobalRelative:lat=42.2905979,lon=-8.794522,alt=145.499\",\r\n"
	          	    + "        \"Localización Sistema Local\": \"LocationLocal:north=797.6734008789062,east=-439.1247253417969,down=-145.49925231933594\",\r\n"
                    + "        \"Tiempo\": \"2022-04-01 10:01:47.266832\"\r\n"
                    + "    }");
			*/
			
			Vista v = new Vista(json, this.appFrame.getPosicionCamara());
	
	 		appFrame.updateView(v);

	    } catch (Exception exception) {
			System.out.println("\nERROR: " + exception.getMessage());
		}
	}
}
