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

// import org.json.JSONException;
// import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DroneServer {

	static private List<Map<String, Double>> positions = new ArrayList<>();
	
	public static void main(String[] args) {
		
        final int PUERTO = 5000;
		 
        ServerSocket servidor = null;
        Socket sc = null;
        DataOutputStream out;
        DataInputStream in;
        // List<JSONObject> ruta = null;

		try {
			File pathFile = new File("./demo.path");
			Scanner pathReader = new Scanner(pathFile);

			int currentLine = 0;

			while (pathReader.hasNextLine()) {
				String data = pathReader.nextLine();
				currentLine++;

				if (currentLine > 1) {
					System.out.println(data);
					String[] dataValues = data.split(" ");

					Map<String, Double>  positionData = new HashMap<>();
					positionData.put("ms",Double.parseDouble(dataValues[0]));

					positionData.put("lat",Double.parseDouble(dataValues[1]));
					positionData.put("lon",Double.parseDouble(dataValues[2]));
					positionData.put("alt",Double.parseDouble(dataValues[3]));

					positionData.put("pitch",Double.parseDouble(dataValues[4]));
					positionData.put("yaw",Double.parseDouble(dataValues[5]));
					positionData.put("roll",Double.parseDouble(dataValues[6]));

					positions.add(positionData);
				}
			}
			pathReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		System.out.println(positions);

		System.out.println("===");
		System.out.println("===\n");

		long initialMillis = System.currentTimeMillis();

		long millisOffset = 0;

		while(true) {
			long currentMillis = System.currentTimeMillis() - initialMillis;
			System.out.print("[");
			System.out.print(currentMillis);
			System.out.print(" ms since start]");
			System.out.println("");

			Map<String, Double> previousPosition = null;
			Map<String, Double> nextPosition = null;

			long correctedMillis = currentMillis - millisOffset;

			for(Map<String, Double> position : positions) {
				if (position.get("ms") <= correctedMillis) {
					previousPosition = position;
				}
				else if (nextPosition == null) {
					nextPosition = position;
					break;
				}
			}

			Map<String, Double> currentPosition = new HashMap<>();

			if (nextPosition == null) {
				millisOffset += correctedMillis;
				currentPosition = previousPosition;
			}
			else {
				double nextWeight = (correctedMillis - previousPosition.get("ms")) / (nextPosition.get("ms") - previousPosition.get("ms"));
				double previousWeight = 1.0 - nextWeight;

				currentPosition.put("ms", 0.0+currentMillis);

				currentPosition.put("lat", previousWeight*previousPosition.get("lat") + nextWeight*nextPosition.get("lat"));
				currentPosition.put("lon", previousWeight*previousPosition.get("lon") + nextWeight*nextPosition.get("lon"));
				currentPosition.put("alt", previousWeight*previousPosition.get("alt") + nextWeight*nextPosition.get("alt"));

				currentPosition.put("yaw", previousWeight*previousPosition.get("yaw") + nextWeight*nextPosition.get("yaw"));
				currentPosition.put("pitch", previousWeight*previousPosition.get("pitch") + nextWeight*nextPosition.get("pitch"));
				currentPosition.put("roll", previousWeight*previousPosition.get("roll") + nextWeight*nextPosition.get("roll"));
			}

			System.out.println(currentPosition);
			System.out.println("");

			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}	
		}

		/*
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
 		*/
    }
	
	// public static List<JSONObject> hacerRuta(double latitud, double longitud, double altitud, double angulo, double distancia) throws JSONException{
	// 	List<JSONObject> listaCoordenadas = new ArrayList<>();
		
	// 	double R = 6371000; 
	// 	double PI = 3.1415926535;
	// 	double RADIANS = PI/180;
	// 	double DEGREES = 180/PI;
	// 	double radbear  = angulo * RADIANS;
		
	// 	double paso = 50; //distancia que avanza el dron con cada petición
		
	// 	for (int i = 0; i <= distancia; i += paso) {
	// 		double lat2;
	// 	    double lon2;

	// 	    double lat1 = latitud * RADIANS;
	// 	    double lon1 = longitud * RADIANS;
		    
	// 	    lat2 = Math.asin( Math.sin(lat1)*Math.cos(paso / R) +
	// 	              Math.cos(lat1)*Math.sin(paso/R)*Math.cos(radbear) );
	// 	    lon2 = lon1 + Math.atan2(Math.sin(radbear)*Math.sin(paso / R)*Math.cos(lat1),
	// 	                     Math.cos(paso/R)-Math.sin(lat1)*Math.sin(lat2));
		    
	// 	    latitud = lat2 * DEGREES;
	// 	    longitud = lon2 * DEGREES;
		      
	// 		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");  
	// 		LocalDateTime now = LocalDateTime.now();  
			
	// 		JSONObject json = new JSONObject("{\r\n"
	// 	            + "        \"Actitud\": \"Attitude:pitch=45,yaw=" + radbear + ",roll=0\",\r\n"
	//         	    + "        \"Elevacion\": \"" + altitud + "\",\r\n"
	//        	   	    + "        \"Localización Sistema Global\": \"LocationGlobal:lat=" + latitud + ",lon=" + longitud + ",alt=" + altitud + "\",\r\n"
    //                 + "        \"Tiempo\": \"" + now + "\"\r\n"
    //                 + "    }");
	// 		listaCoordenadas.add(json);
	// 	}
		
	// 	return listaCoordenadas;
		
	// }
}
