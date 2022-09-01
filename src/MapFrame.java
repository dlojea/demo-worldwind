import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JFrame;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;

public class MapFrame extends JFrame {
	
	private WorldWindow wwd;
	private MarkerLayer markerLayer;

	

	public MapFrame() {
		this.initialize();
	}
	
	private void initialize() {
		this.wwd = new WorldWindowGLJPanel();
		this.wwd.setModel(new BasicModel());
		this.wwd.getModel().setGlobe(new EarthFlat());
		//Layer compass = wwd.getModel().getLayers().getLayerByName("Br�jula");
		//this.wwd.getModel().getLayers().remove(compass);
		this.getContentPane().add((Component) this.wwd, java.awt.BorderLayout.CENTER);
		
		this.markerLayer = new MarkerLayer();
		this.markerLayer.setOverrideMarkerElevation(true);
		this.markerLayer.setKeepSeparated(false);
		this.markerLayer.setElevation(1000d);
        this.wwd.getModel().getLayers().add(markerLayer);
        
	}
	
	public void updateMarker(Vista v) {
		ArrayList<Marker> markers = new ArrayList<Marker>();
		
		BasicMarkerAttributes attr = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.ORIENTED_CONE_LINE, 0.7);
		Marker marker = new BasicMarker(v.getPosition(), attr);
		marker.setHeading(v.getYaw());
		marker.setPitch(v.getPitch());
		marker.setRoll(v.getRoll());
		
		markers.add(marker);
		this.markerLayer.setMarkers(markers);
		//this.wwd.getView().setEyePosition(v.getPosition());
		
		this.wwd.redraw();
	}
}
