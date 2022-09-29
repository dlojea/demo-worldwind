import java.awt.Component;
import java.util.ArrayList;

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
import gov.nasa.worldwind.View;
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
import gov.nasa.worldwind.animation.DoubleAnimator;
import gov.nasa.worldwind.animation.SmoothInterpolator;
import gov.nasa.worldwind.animation.ScheduledInterpolator;
import gov.nasa.worldwind.animation.MoveToDoubleAnimator;
import gov.nasa.worldwind.animation.MoveToPositionAnimator;
import gov.nasa.worldwind.animation.AngleAnimator;
import gov.nasa.worldwind.animation.RotateToAngleAnimator;

import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.ScreenImage;
import gov.nasa.worldwind.render.Size;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.render.Offset;
import java.awt.Point;

public class VideoFrame extends JFrame {
	
	private WorldWindow wwd;
	
	private JDesktopPane desktop;

    private boolean firstUpdateView = true;

	public VideoFrame() {
		this.initialize();
	}
	
	private void initialize() {
		this.desktop = new JDesktopPane();
    	this.getContentPane().add(desktop, BorderLayout.CENTER);

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
		//Layer compass = wwd.getModel().getLayers().getLayerByName("Br�jula");
		//this.wwd.getModel().getLayers().remove(compass);
		this.getContentPane().add((Component) this.wwd, java.awt.BorderLayout.CENTER);
	}
	
	public void updateView(Vista v) {
    	BasicFlyView view = (BasicFlyView) this.wwd.getView();
    	
        if (firstUpdateView) {
            view.setEyePosition(v.getPosition());
            view.setHeading(v.getYaw());
            view.setPitch(v.getPitch());
            view.setRoll(v.getRoll());

            firstUpdateView = false;

            this.wwd.redraw();

            return;
        }

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
        
        this.wwd.redraw();
    }
}
