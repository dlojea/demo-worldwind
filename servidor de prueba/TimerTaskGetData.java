import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.TimerTask;

import org.json.*;

public class TimerTaskGetData extends TimerTask{
	AppFrame appFrame;

	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	
	int i;

	public TimerTaskGetData(AppFrame appFrame) {
		this.appFrame = appFrame;
		this.i = 0;
	}

	@Override
	public void run() {
		try {
			this.socket = new Socket("localhost", appFrame.getSocket());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.in = new DataInputStream(socket.getInputStream());
			
			out.writeInt(i); //le mando al servidor la posicion de la ruta
			
			String mensaje = in.readUTF();
			System.out.println(mensaje);
	
			JSONObject json = new JSONObject(mensaje);
			System.out.println("\n");
			System.out.println(json);
			
			Vista v = new Vista(json, this.appFrame.getPosicionCamara());

	 		appFrame.updateView(v);
			i++;

	    } catch (Exception exception) {
			System.out.println("\nERROR: " + exception.getMessage());
		}
	}
}
