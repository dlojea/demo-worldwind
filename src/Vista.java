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
 		
 		private double posicionCamara;
 		
 		public Vista(Position position, Angle yaw, Angle pitch, Angle roll) {
 			this.position = position; 
 			this.yaw = yaw;
 			this.pitch = pitch;
 			this.roll = roll;
 		}
 		
 		public Vista(JSONObject json) throws JSONException {
 			String actitud = json.getString("Actitud");
 	    	String[] actitudArray = actitud.split(",");
 	    	String pitch = actitudArray[0].split("=")[1];
 	    	String yaw = actitudArray[1].split("=")[1];
 	    	String roll = actitudArray[2].split("=")[1];
 	    	
 	    	this.yaw = Angle.fromRadians(Double.parseDouble(yaw));
 	    	this.pitch = Angle.fromRadians(Double.parseDouble(pitch) + Math.toRadians(this.posicionCamara));
 	    	this.roll = Angle.fromRadians(Double.parseDouble(roll));
 	    	
 	    	String lsg = json.getString("Localización Sistema Global");
 	    	String[] lsgArray = lsg.split(",");
 	    	String latitud = lsgArray[0].split("=")[1];
 	    	String longitud = lsgArray[1].split("=")[1];
 	    	String altitud = lsgArray[2].split("=")[1];
 	    	
 	    	this.position = new Position(
 	    		new LatLon(
    				Angle.fromDegrees(Double.parseDouble(latitud)), 
    				Angle.fromDegrees(Double.parseDouble(longitud))),
				Double.parseDouble(altitud));
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
 		
 		public void setPosicionCamara(double posicionCamara) {
 			this.posicionCamara = posicionCamara;
 		}

 	}