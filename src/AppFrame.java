import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.render.ContourLine;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.view.firstperson.BasicFlyView;
import gov.nasa.worldwind.view.firstperson.FlyToFlyViewAnimator;
import gov.nasa.worldwindx.examples.LayerPanel;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.util.PropertyAccessor;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.view.firstperson.FlyToFlyViewAnimator.FlyToElevationAnimator;
import gov.nasa.worldwind.animation.Animator;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import gov.nasa.worldwind.animation.DoubleAnimator;
import gov.nasa.worldwind.animation.SmoothInterpolator;
import gov.nasa.worldwind.animation.ScheduledInterpolator;
import gov.nasa.worldwind.animation.MoveToDoubleAnimator;
import gov.nasa.worldwind.animation.MoveToPositionAnimator;
import gov.nasa.worldwind.animation.AngleAnimator;
import gov.nasa.worldwind.animation.RotateToAngleAnimator;

import org.json.JSONObject;

import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.ScreenImage;
import gov.nasa.worldwind.render.Size;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.render.Offset;
import java.awt.Point;

import javax.swing.JFrame;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.WebcamResolution;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.Dimension;


public class AppFrame extends JFrame {

	private WorldWindow wwd;
    private LayerPanel layerPanel;
    private JDesktopPane desktop;
    private MapFrame mapFrame;
    private VideoFrame videoFrame;
    
    private double posicionCamara;
    private int socket = 20064;
    private Timer timer;

    private int imageDelayFrames = 1;
    private Queue<Vista> viewBuffer;

    private boolean firstUpdateView = true;

    //public JSONObject 

    public AppFrame() {
        this.posicionCamara = 0;
        this.initialize();
        this.viewBuffer = new LinkedBlockingQueue<Vista>();
    }

    private void initialize() {

        this.desktop = new JDesktopPane();
    	this.getContentPane().add(desktop, BorderLayout.CENTER);
		this.initializeWW();
        
        // WEBCAM TEST
        for (Webcam webcam : Webcam.getWebcams()) {
			System.out.println("Webcam detected: " + webcam.getName());
		}

        Webcam webcam = Webcam.getWebcams().get(1);
        
        webcam.setCustomViewSizes(new Dimension[] { WebcamResolution.FHD.getSize() }); // register custom size
        webcam.setViewSize(WebcamResolution.FHD.getSize()); // set custom size
        webcam.open(); // open camera device

		WebcamPanel panelwc = new WebcamPanel(webcam);
        /*
		panelwc.setFPSDisplayed(true);
		panelwc.setDisplayDebugInfo(true);
		panelwc.setImageSizeDisplayed(true);
		panelwc.setMirrored(true);

		JFrame window = new JFrame("Test webcam panel");
		window.add(panelwc);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
        */

        byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
        System.out.println("Bytes length: " + bytes.length);

        InputStream is = new ByteArrayInputStream(bytes);

        try {
            BufferedImage newBi = ImageIO.read(is);

            final ScreenImage screenImage = new ScreenImage();

            screenImage.setImageSource(newBi);
            screenImage.setSize(new Size(Size.MAINTAIN_ASPECT_RATIO, -1, null, Size.EXPLICIT_DIMENSION, 1.0, AVKey.FRACTION));
            screenImage.setScreenLocation(new Point(0, 0));
            screenImage.setScreenOffset(Offset.CENTER);
            screenImage.setOpacity(1);

            RenderableLayer layer = new RenderableLayer();
            layer.setName("Drone Camera");
            layer.addRenderable(screenImage);

            System.out.println(this.wwd);

            this.wwd.getModel();

            this.wwd.getModel().getLayers();

            this.wwd.getModel().getLayers().add(layer);
        
            WorldWindow wwd = this.wwd;

            new Thread(new Runnable() {
                public void run() {
                  try {
                    while (true) {
                      byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
                      InputStream is = new ByteArrayInputStream(bytes);
                      BufferedImage newBi = ImageIO.read(is);
                      screenImage.setImageSource(newBi);
                      wwd.redrawNow();
                      Thread.sleep(1000/30);
                    }
                  } catch (Exception e) { }
                }
              }).start();

            this.layerPanel = new LayerPanel(this.wwd);
            this.dibujarLineasDeNivel();
            
            StatusBar statusBar = new StatusBar();
            this.add(statusBar, BorderLayout.PAGE_END);
            statusBar.setEventSource(this.wwd);
            
            JMenuBar menubar = new JMenuBar();
            
            JMenu menuCapas = new JMenu("Capas");
            menuCapas.add(this.layerPanel);
            menubar.add(menuCapas);
            
            JMenu menuOpciones = new JMenu("Opciones");
            JMenuItem itemAngulo = new JMenuItem("Cambiar �ngulo de la c�mara");
            menuOpciones.add(itemAngulo);
            itemAngulo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostrarOpciones();	        	
                }
            });
            menubar.add(menuOpciones);
            
            
            JPanel panel = new JPanel();
            panel.setBackground(new Color(0,0,0,.0f));
            panel.setBounds(0,0,250,250);
            panel.add(this.panelTransparencia(screenImage));
            panel.add(this.panelSincronizacion());
            
            this.desktop.add(panel, JLayeredPane.MODAL_LAYER);

            this.mapFrame = mostrarMapa();
            this.videoFrame = mostrarVideo();

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTaskGetData(this), 0, 33);
            
            JMenu menuDrones = new JMenu("Cambiar dron");
            JMenuItem itemDron1 = new JMenuItem("Dron 1");
            JMenuItem itemDron2 = new JMenuItem("Dron 2");
            menuDrones.add(itemDron1);
            menuDrones.add(itemDron2);
            itemDron1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    socket = 20064;
                    cambiarDron();
                }
            });
            itemDron2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    socket = 20065;
                    cambiarDron();
                }
            });

            menubar.add(menuDrones);

            this.setJMenuBar(menubar);
        } catch (IOException exc) {
            System.err.println("Error al leer imagen webcam");
        }
    }
    
    private void initializeWW() {
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
    }

    private MapFrame mostrarMapa() {
    	MapFrame frame = new MapFrame();
    	frame.setTitle("Mapa");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(this);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
        });
        
        return frame;
    }

    private VideoFrame mostrarVideo() {
    	VideoFrame frame = new VideoFrame();
    	frame.setTitle("Video");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(this);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
        });
        
        return frame;
    }
    
    public void updateView(Vista v) {

        if (firstUpdateView) {
            BasicFlyView view = (BasicFlyView) this.wwd.getView();

            v.setPosicionCamara(this.posicionCamara);
        
            view.setEyePosition(v.getPosition());
            view.setHeading(v.getYaw());
            view.setPitch(v.getPitch());
            view.setRoll(v.getRoll());

            firstUpdateView = false;

            this.wwd.redraw();

            return;
        }

        this.viewBuffer.add(v); //Almacenamos vista en buffer

        videoFrame.updateView(v);

        while(this.viewBuffer.size() > this.imageDelayFrames) {
            this.viewBuffer.remove();
        }

        if (this.viewBuffer.size() == this.imageDelayFrames) {
            v = this.viewBuffer.remove();

            BasicFlyView view = (BasicFlyView) this.wwd.getView();
    
            // SISTEMA DE INTERPOLACIÓN CON ANIMATOR
            FlyToFlyViewAnimator animator =
                            FlyToFlyViewAnimator.createFlyToFlyViewAnimator(view,
                                view.getEyePosition(), v.getPosition(),
                                view.getHeading(), v.getYaw(),
                                view.getPitch(), v.getPitch(),
                                view.getRoll(), v.getRoll(),
                                view.getEyePosition().getElevation(), v.getPosition().getElevation(),
                                500, WorldWind.ABSOLUTE);
            
            List<Animator> animators = new LinkedList<Animator>();
    
            for (Animator anim : animator.getAnimators()) {
                if (anim instanceof FlyToFlyViewAnimator.FlyToElevationAnimator) {
                    FlyToFlyViewAnimator.FlyToElevationAnimator ftfAnim = (FlyToFlyViewAnimator.FlyToElevationAnimator) anim;
                    animators.add(new MoveToDoubleAnimator(ftfAnim.getEnd(), 0.998, ftfAnim.getPropertyAccessor()));
                }
                else if (anim instanceof FlyToFlyViewAnimator.OnSurfacePositionAnimator) { 
                    FlyToFlyViewAnimator.OnSurfacePositionAnimator ftfAnim = (FlyToFlyViewAnimator.OnSurfacePositionAnimator) anim;
                    animators.add(new MoveToPositionAnimator(ftfAnim.getBegin(), ftfAnim.getEnd(), 0.998, ftfAnim.getPropertyAccessor()));
                }
                else if (anim instanceof AngleAnimator) {
                    AngleAnimator ftfAnim = (AngleAnimator) anim;
                    animators.add(new RotateToAngleAnimator(ftfAnim.getBegin(), ftfAnim.getEnd(), 0.998, ftfAnim.getPropertyAccessor()));
                }
                else {
                    animators.add(anim);
                }
            }
    
            animator.setAnimators(animators.toArray(new Animator[animators.size()]));
    
            view.addAnimator(animator);
            animator.start();
            view.firePropertyChange(AVKey.VIEW, null, view);
            
            mapFrame.updateMarker(v);

            this.wwd.redraw();
        }
    }
    
    private void dibujarLineasDeNivel() {
        RenderableLayer layer = new RenderableLayer();
        layer.setName("Curvas de nivel");
        layer.setPickEnabled(false);
        //layer.setMaxActiveAltitude(3000);
        
        this.wwd.getModel().getLayers().add(layer);

        //insertBeforeLayerName(this.wwd, layer, "Br�jula");

        int alturaMax = 2100;
        int incremento = 25;

        List<Color> escalaColor = hacerEscalaColor(alturaMax, incremento);
        int i = 0;

        for (int elevation = 0; elevation <= alturaMax; elevation += incremento) {
            ContourLine cl = new ContourLine(elevation);
            //cl.setColor(new Color(1f, 1f, 1f));
            cl.setColor(escalaColor.get(i));

            layer.addRenderable(cl);
            i++;
        }
    }
    
    private List<Color> hacerEscalaColor(int alturaMax, int incremento) {
    	List<Color> escalaColor = new ArrayList<>();
    	float verde = 0;
    	float rojo = 0;
    	float azul = 255;
    	float paso = 255 / ((alturaMax/incremento) / 5);

    	while (verde < 255) {
    		verde += paso;
    		if (verde > 255) { 
    			verde = 255; 
    		}
    		escalaColor.add(new Color(rojo/255,verde/255,azul/255));
    	}

    	while (azul > 0) {
    		azul -= paso;
    		if (azul < 0) { 
    			azul = 0; 
    		}
    		escalaColor.add(new Color(rojo/255,verde/255,azul/255));
    	}

    	while (rojo < 255) {
    		rojo += paso;
    		if (rojo > 255) { 
    			rojo = 255; 
    		}
    		escalaColor.add(new Color(rojo/255,verde/255,azul/255));
    	}

    	while (verde > 0) {
    		verde -= paso;
    		if (verde < 0) { 
    			verde = 0; 
    		}
    		escalaColor.add(new Color(rojo/255,verde/255,azul/255));
    	}

    	while (verde < 255 && azul < 255) {
    		verde += paso;
    		azul += paso;
    		if (verde > 255 || azul > 255) { 
    			verde = 255; 
    			azul = 255;
    		}
    		escalaColor.add(new Color(rojo/255,verde/255,azul/255));
    	}

    	return escalaColor;
    }
    
    private JPanel panelTransparencia(ScreenImage screenImage) {
    	 JPanel controlPanel = new JPanel();
    	 
         TitledBorder tb = new TitledBorder("Transparencia");
         tb.setTitleColor(new Color(255,255,255));
         controlPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), tb));
         controlPanel.setBackground(new Color(0,0,0,.0f));
         
         JSlider slider = new JSlider(0, 100, 50);
         screenImage.setOpacity((double) 0.5);
         slider.setBackground(getForeground()); 
         slider.addChangeListener((ChangeEvent event) -> {
            //this.wwd.getModel().getLayers().getLayerByName("Imagen detallada").setOpacity((double) slider.getValue()/100);
            //this.wwd.getModel().getLayers().getLayerByName("Drone Camera").setOpacity((double) slider.getValue()/100);
            screenImage.setOpacity((double) slider.getValue()/100);
            this.wwd.redraw();
         });
         
         controlPanel.add(slider);
         return controlPanel;
    }

    private JPanel panelSincronizacion() {
        JPanel controlPanel = new JPanel();
        
        TitledBorder tb = new TitledBorder("Sincronizar retardo imagen");
        tb.setTitleColor(new Color(255,255,255));
        controlPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), tb));
        controlPanel.setBackground(new Color(0,0,0,.0f));
        
        JSlider slider = new JSlider(1, 10, 1);
        slider.setBackground(getForeground()); 
        slider.addChangeListener((ChangeEvent event) -> {
           //this.wwd.getModel().getLayers().getLayerByName("Imagen detallada").setOpacity((double) slider.getValue()/100);
           //this.wwd.getModel().getLayers().getLayerByName("Drone Camera").setOpacity((double) slider.getValue()/100);
           //screenImage.setOpacity((double) slider.getValue()/100);
           //this.wwd.redraw();
           imageDelayFrames = (int) slider.getValue();
        });
        
        controlPanel.add(slider);
        return controlPanel;
   }
    
    private void mostrarOpciones() {
    	JFrame frame = new OpcionesFrame(this);
    	frame.setTitle("Opciones");
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
        });
    }

    private void cambiarDron() {
    	timer.cancel();
    	timer = new Timer();
    	timer.scheduleAtFixedRate(new TimerTaskGetData(this), 0, 33);
    }
    
    public double getPosicionCamara() {
    	return this.posicionCamara;
    }
    
    public void setPosicionCamara(double posicionCamara) {
    	this.posicionCamara = posicionCamara;
    }

    public int getSocket() {
    	return this.socket;
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
