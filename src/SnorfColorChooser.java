package snorf;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;

public class SnorfColorChooser extends JDialog implements ChangeListener {
	
	// components...
	private JColorChooser fgChooser;
	private JColorChooser bgChooser;
	private JButton okButton;
	private JButton cancelButton;
	private JCheckBox previewButton;
	
	// need access to snorf (to change colors)...
	private Snorf snorf;
	
	// a bunch of actions...
	class OkAction extends AbstractAction {
        public OkAction() { super("ok"); }
        public void actionPerformed(ActionEvent e) { ok(); }
    };
    
    class CancelAction extends AbstractAction {
        public CancelAction() { super("cancel"); }
        public void actionPerformed(ActionEvent e) { cancel(); }
    };

    // some helper functions for reading/writing the preferences file
    
	public static String colorToHex(Color color) {		
		String red = Integer.toHexString(color.getRed());
		String green = Integer.toHexString(color.getGreen());
		String blue = Integer.toHexString(color.getBlue());
		if (red.length() == 1)   { red = "0"+red;     }
		if (green.length() == 1) { green = "0"+green; }
		if (blue.length() == 1)  { blue = "0"+blue;   }
		return (red+green+blue);
	}
	
	public static Color hexToColor(String hexString) {
		int red = Integer.parseInt(hexString.substring(0,2),16);
		int green = Integer.parseInt(hexString.substring(2,4),16);
		int blue = Integer.parseInt(hexString.substring(4,6),16);
		try {
			Color color = new Color(red,green,blue);
			return color;
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public SnorfColorChooser(Snorf snorf) {
		this.snorf = snorf;
		
		// make modal...
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		// setup components...
		fgChooser = createColorChooser(snorf.fgColor,"foreground color");
		bgChooser = createColorChooser(snorf.bgColor,"background color");						
		
		okButton = new JButton(new OkAction());
		cancelButton = new JButton(new CancelAction());
		previewButton = new JCheckBox("preview");
		previewButton.setSelected(true);

		JPanel chooserPane = new JPanel();
		chooserPane.setLayout(new BoxLayout(chooserPane, BoxLayout.LINE_AXIS));
		chooserPane.add(Box.createHorizontalGlue());
		chooserPane.add(fgChooser);
		chooserPane.add(bgChooser);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(previewButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(okButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);

		Container contentPane = getContentPane();
		contentPane.add(chooserPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);		
		pack();
		
		// anonymous closing event (cancels changes)...
		addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                cancel();
            }
        });
	}
	
	// create custom colorChooser...
	public JColorChooser createColorChooser(Color color, String title) {
		JColorChooser chooser = new JColorChooser(color);
		chooser.setPreviewPanel(new JPanel());
		chooser.setBorder(BorderFactory.createTitledBorder(title));
		AbstractColorChooserPanel[] oldPanels = chooser.getChooserPanels();
		for (int i=0; i<oldPanels.length; i++) {
	        String panelName = oldPanels[i].getClass().getName();
	        if (panelName.contains("DefaultSwatchChooserPanel") || panelName.contains("DefaultRGBChooserPanel")) {	        	
	        	chooser.removeChooserPanel(oldPanels[i]);
	        }
	    }	
		chooser.getSelectionModel().addChangeListener(this);
		return chooser;
	}
		
	public void stateChanged(ChangeEvent e) {		
		if (previewButton.isSelected()) {			
			snorf.setColors(fgChooser.getColor(),bgChooser.getColor(),false);
		}
	}
		
	public void ok() {
		snorf.setColors(fgChooser.getColor(), bgChooser.getColor(),true);
		setVisible(false);
	}
	
	public void cancel() {
		snorf.setColors(snorf.fgColor,snorf.bgColor,false);
		setVisible(false);
	}
}
