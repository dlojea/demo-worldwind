import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class DroneServer {
	
	public static void main(String[] args) {
		
        final int PUERTO = 5000;
		 
        ServerSocket servidor = null;
        Socket sc = null;
        DataOutputStream out;
        DataInputStream in;
        List<JSONObject> ruta = null;
        
        try {
        	ruta = hacerRuta(42.29, -8.79, 500, 0, 2000);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");
            
            while (true) {
                sc = servidor.accept();
                out = new DataOutputStream(sc.getOutputStream());
        		in = new DataInputStream(sc.getInputStream());
        		
        		int i = in.readInt();
        		
                System.out.println(ruta.get(i).toString());
                out.writeUTF(ruta.get(i).toString());
                i++;
                
            }
        } catch (IOException ex) {
            Logger.getLogger(DroneServer.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
	
	public static List<JSONObject> hacerRuta(double latitud, double longitud, double altitud, double angulo, double distancia) throws JSONException{
		List<JSONObject> listaCoordenadas = new ArrayList<>();
		
		double R = 6371000; 
		double PI = 3.1415926535;
		double RADIANS = PI/180;
		double DEGREES = 180/PI;
		double radbear  = angulo * RADIANS;
		
		double paso = 50; //distancia que avanza el dron con cada petición
		
		for (int i = 0; i <= distancia; i += paso) {
			double lat2;
		    double lon2;

		    double lat1 = latitud * RADIANS;
		    double lon1 = longitud * RADIANS;
		    
		    lat2 = Math.asin( Math.sin(lat1)*Math.cos(paso / R) +
		              Math.cos(lat1)*Math.sin(paso/R)*Math.cos(radbear) );
		    lon2 = lon1 + Math.atan2(Math.sin(radbear)*Math.sin(paso / R)*Math.cos(lat1),
		                     Math.cos(paso/R)-Math.sin(lat1)*Math.sin(lat2));
		    
		    latitud = lat2 * DEGREES;
		    longitud = lon2 * DEGREES;
		      
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");  
			LocalDateTime now = LocalDateTime.now();  
			
			JSONObject json = new JSONObject("{\r\n"
		            + "        \"Actitud\": \"Attitude:pitch=45,yaw=" + radbear + ",roll=0\",\r\n"
	        	    + "        \"Elevacion\": \"" + altitud + "\",\r\n"
	       	   	    + "        \"Localización Sistema Global\": \"LocationGlobal:lat=" + latitud + ",lon=" + longitud + ",alt=" + altitud + "\",\r\n"
                    + "        \"Tiempo\": \"" + now + "\"\r\n"
                    + "    }");
			listaCoordenadas.add(json);
		}
		
		return listaCoordenadas;
		
	}
}
