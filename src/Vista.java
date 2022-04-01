import org.json.JSONException;
import org.json.JSONObject;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;

public class Vista {
    	private Position position; //coordenadas y altura
 		private Angle yaw; //orientacion
 		private Angle pitch; //inclinacion
 		private Angle roll; //rotacion
 		
 		public Vista(Position position, Angle yaw, Angle pitch, Angle roll) {
 			this.position = position; 
 			this.yaw = yaw;
 			this.pitch = pitch;
 			this.roll = roll;
 		}
 		
 		public Vista(JSONObject json) throws JSONException {
 			String actitud = json.getString("Actitud");
 	    	String[] actitudArray = actitud.split(",");
 	    	String[] pitch = actitudArray[0].split("=");
 	    	String[] yaw = actitudArray[1].split("=");
 	    	String[] roll = actitudArray[2].split("=");
 	    	
 	    	this.yaw = Angle.fromDegrees(Double.parseDouble(yaw[1]));
 	    	//Creo que worldwind define 0 como hacia abajo mientras que en el simulador 0 es paralelo al plano, le añado 90° por ahora
 	    	this.pitch = Angle.fromDegrees(Double.parseDouble(pitch[1]) + 90);
 	    	this.roll = Angle.fromDegrees(Double.parseDouble(roll[1]));
 	    	
 	    	String lsg = json.getString("Localización Sistema Global");
 	    	String[] lsgArray = lsg.split(",");
 	    	String[] latitud = lsgArray[0].split("=");
 	    	String[] longitud = lsgArray[1].split("=");
 	    	String[] altitud = lsgArray[2].split("=");
 	    	
 	    	this.position = new Position(new LatLon(
 	    			Angle.fromDegrees(Double.parseDouble(latitud[1])), 
 	    			Angle.fromDegrees(Double.parseDouble(longitud[1]))),
 	    			Double.parseDouble(altitud[1]));
 		}

 		public Position getPosition() {
 			return position;
 		}
 		
 		public Angle getYaw() {
 			return yaw;
 		}
 		
 		public Angle getPitch() {
 			return pitch;
 		}

 		public Angle getRoll() {
 			return roll;
 		}
 	}
