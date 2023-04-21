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

import static java.lang.Math.*;

public class DroneServer {

	static private List<Map<String, Double>> positions = new ArrayList<>();
	
	public static void main(String[] args) {
		
        final int PUERTO = 20064;
		 
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

		try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");
			sc = servidor.accept();
			System.out.println("Connected to client...\n");
            
            while (true) {
				System.out.println("Waiting for client...\n");
                out = new DataOutputStream(sc.getOutputStream());
        		in = new DataInputStream(sc.getInputStream());

				in.readUTF();
				System.out.println("Message from client received. \n");
        		
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


				String json = " {\r\n"
					            + "        \"Actitud\": \"Attitude:pitch="+currentPosition.get("pitch")+",yaw="+currentPosition.get("yaw")+",roll="+currentPosition.get("roll")+"\",\r\n"
				        	    + "        \"Elevacion\": \"" + currentPosition.get("alt") + "\",\r\n"
				       	   	    + "        \"Localización Sistema Global\": \"LocationGlobal:lat=" + currentPosition.get("lat") + ",lon=" + currentPosition.get("lon") + ",alt=" + currentPosition.get("alt") + "\",\r\n"
				                + "        \"Tiempo\": \"" + currentPosition.get("ms") + "\"\r\n"
				                + "}";
        		
				System.out.println();
				System.out.println(json);
				System.out.println();
				out.writeUTF(json);
				System.out.println("Data sent to client. \n");
            }
        } catch (IOException ex) {
            Logger.getLogger(DroneServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
