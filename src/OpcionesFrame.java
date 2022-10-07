import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

public class OpcionesFrame extends JFrame {
	
	private AppFrame appFrame;
	
	public OpcionesFrame(AppFrame appFrame) {
		this.appFrame = appFrame;
		this.initialize();
	}
	
	private void initialize() {
        JPanel controlPanel = new JPanel();
        
        TitledBorder tb = new TitledBorder("Ángulo de la cámara");
        tb.setTitleColor(new Color(0,0,0));
        controlPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), tb));
        controlPanel.setBackground(new Color(0,0,0,.0f));
        
        JTextField textAngulo = new JTextField(Double.toString(appFrame.getPosicionCamara()));
        textAngulo.setPreferredSize(new Dimension(200,24));
        textAngulo.setToolTipText("Introduce el ángulo de la cámara, entre 0 y 90°");
        controlPanel.add(textAngulo);
        
        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	try {
	        		Double doubleAngulo = Double.parseDouble(textAngulo.getText());
	        		if (doubleAngulo < 0 || doubleAngulo > 90) {
	        			throw new Exception();
	        		}
	        		appFrame.setPosicionCamara(doubleAngulo);
		        	cerrarFrame();
	    		} catch (Exception e1) {
	    			JOptionPane.showMessageDialog(null, "Introduce un valor entre 0 y 90");
	    		}
	        }
        });
        controlPanel.add(botonGuardar);
        
        this.add(controlPanel);
	}
	
	private void cerrarFrame() {
		this.dispose();
	}
}
