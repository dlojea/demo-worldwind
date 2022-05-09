import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.MenuSelectionManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

import org.json.JSONException;
import org.json.JSONObject;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.render.ContourLine;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.view.firstperson.BasicFlyView;
import gov.nasa.worldwindx.examples.LayerPanel;

public class AppFrame extends JFrame {

	private WorldWindow wwd;
    private LayerPanel layerPanel;
    private JDesktopPane desktop;

    public AppFrame() {
        this.initialize();
    }

    private void initialize() {
    	this.desktop = new JDesktopPane();
    	this.getContentPane().add(desktop, BorderLayout.CENTER);
		try {
			this.initializeWW();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        this.layerPanel = new LayerPanel(this.wwd);
        this.dibujarLineasDeNivel();
        
        StatusBar statusBar = new StatusBar();
        this.add(statusBar, BorderLayout.PAGE_END);
        statusBar.setEventSource(this.wwd);
        
        JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("Capas");
		menu.add(this.layerPanel);
		layerPanel.setFocusable(true);
		menubar.add(menu);
		this.setJMenuBar(menubar);
        
        JPanel panel = new JPanel();
		panel.setBackground(new Color(0,0,0,.0f));
		panel.setBounds(0,0,250,100);
		panel.add(this.panelTransparencia());
		
		this.desktop.add(panel, JLayeredPane.MODAL_LAYER);
    }
    
    private void initializeWW() throws JSONException {
    	this.wwd = new WorldWindowGLJPanel();
		this.wwd.setModel(new BasicModel());

		Component wwComp = (Component) this.wwd;
		this.desktop.add(wwComp, JLayeredPane.DEFAULT_LAYER);
		this.desktop.addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				wwComp.setSize(desktop.getSize());
			}
		});
		
		BasicFlyView flyView = new BasicFlyView();
        this.wwd.setView(flyView);
        
        Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTaskGetData(this), 0, 33);
    }
    
    public void updateView(Vista v) {
    	BasicFlyView view = (BasicFlyView) wwd.getView();
        
        view.setEyePosition(v.getPosition());
        view.setHeading(v.getYaw());
        view.setPitch(v.getPitch());
        view.setRoll(v.getRoll());
        
        wwd.redraw();
    }
    
    private void dibujarLineasDeNivel() {
        RenderableLayer layer = new RenderableLayer();
        layer.setName("Curvas de nivel");
        layer.setPickEnabled(false);
        layer.setMaxActiveAltitude(3000);

        insertBeforeLayerName(this.wwd, layer, "Brújula");

        for (int elevation = 0; elevation <= 3000; elevation += 50) {
            ContourLine cl = new ContourLine(elevation);
            cl.setColor(new Color(1f, 1f, 1f));

            layer.addRenderable(cl);
        }
    }
    
    private JPanel panelTransparencia() {
    	 JPanel controlPanel = new JPanel();
    	 
         TitledBorder tb = new TitledBorder("Transparencia");
         tb.setTitleColor(new Color(255,255,255));
         controlPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), tb));
         controlPanel.setBackground(new Color(0,0,0,.0f));
         
         final JSlider slider = new JSlider(0, 100, 100);
         slider.setBackground(getForeground());
         slider.addChangeListener((ChangeEvent event) -> {
        	 this.wwd.getModel().getLayers().getLayerByName("Imagen detallada").setOpacity((double) slider.getValue()/100);
        	 this.wwd.redraw();
         });
         
         controlPanel.add(slider);
         return controlPanel;
    }
    
    public void insertBeforeCompass(WorldWindow wwd, Layer layer) {
        // Insert the layer into the layer list just before the compass.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof CompassLayer) {
                compassPosition = layers.indexOf(l);
            }
        }
        layers.add(compassPosition, layer);
    }

    public void insertBeforePlacenames(WorldWindow wwd, Layer layer) {
        // Insert the layer into the layer list just before the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof PlaceNameLayer) {
                compassPosition = layers.indexOf(l);
            }
        }
        layers.add(compassPosition, layer);
    }

    public void insertAfterPlacenames(WorldWindow wwd, Layer layer) {
        // Insert the layer into the layer list just after the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof PlaceNameLayer) {
                compassPosition = layers.indexOf(l);
            }
        }
        layers.add(compassPosition + 1, layer);
    }

    public void insertBeforeLayerName(WorldWindow wwd, Layer layer, String targetName) {
        // Insert the layer into the layer list just before the target layer.
        int targetPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers) {
            if (l.getName().contains(targetName)) {
                targetPosition = layers.indexOf(l);
                break;
            }
        }
        layers.add(targetPosition, layer);
    }

}
