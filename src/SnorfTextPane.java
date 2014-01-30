package snorf;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SnorfTextPane extends JPanel implements KeyListener, MouseWheelListener, CaretListener {
	
	// components...
	protected JTextPane textPane;
	protected JScrollPane scrollPane;
		
	protected Snorf snorf;
	
	private boolean isControlPressed = false;
	private int marginSize = 100;
	
	public SnorfTextPane(Snorf snorf) {
		this.snorf = snorf;
		
		setLayout(new BorderLayout());        
	    
		textPane = new JTextPane();
		textPane.setFont(new Font(snorf.fontName,Font.PLAIN,snorf.fontSize));
		
		int lrInset = (int)Math.round(marginSize * snorf.marginPct);
		int tbInset = (int)Math.round(marginSize * snorf.marginPct);
		textPane.setMargin(new Insets(tbInset,lrInset,tbInset,lrInset)); // t,l,b,r
		
		textPane.addKeyListener(this);
		textPane.addMouseWheelListener(this);
		textPane.addCaretListener(this);
		
		scrollPane = new JScrollPane(textPane);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setWheelScrollingEnabled(true);
	    scrollPane.addMouseWheelListener(this);  
	    	    
	    add(scrollPane,BorderLayout.CENTER);		
	}
	
	public JTextPane getTextPane() {
		return textPane;
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void incrementFont(int increment) {
		int newFontSize = (snorf.fontSize + increment);
		if (newFontSize > 0) {
			snorf.fontSize = newFontSize;
			textPane.setFont(new Font(snorf.fontName,Font.PLAIN,snorf.fontSize));
		}	
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {		
		int notches = e.getWheelRotation();
		if (isControlPressed) {
			// modify the font...
			incrementFont(notches);
		}
		else {
			// move the scrollbar...
			JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
			scrollBar.setValue(scrollBar.getValue()+20*notches);
		}
	}
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_CONTROL) {
			isControlPressed = true;
		}
		else if (code == KeyEvent.VK_ESCAPE) {
			// minimize
			snorf.minimize();
		}
		else if (isControlPressed) {
			switch (code) {
				case KeyEvent.VK_S:
					// save
					snorf.saveDoc();
					break;					
				case KeyEvent.VK_O:
					// open
					snorf.openDoc();
					break;
				case KeyEvent.VK_F:
					// find
					snorf.find();			
					break;
				case KeyEvent.VK_H:
					// help
					snorf.help();
					break;
				case KeyEvent.VK_U:
					// update count
					snorf.update();
					break;
				case KeyEvent.VK_Q:
					// quit
					snorf.quit();
					break;
				case KeyEvent.VK_DOWN:
					// increase font
					incrementFont(+1);						
					return;	// return immediately; don't toggle isControlPressed
				case KeyEvent.VK_UP:
					// decrease font
					incrementFont(-1);
					return;	// return immediately; don't toggle isControlPressed	
			}
			isControlPressed = false;
		}
	}
	    
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			isControlPressed = false;
		}    	
	}

	public void keyTyped(KeyEvent e) {	}
	
	public void caretUpdate(CaretEvent e) {
		snorf.getFindPane().pos = -1;
	}
	
	public void setColors(Color fgColor, Color bgColor) {
		textPane.setBackground(bgColor);
		textPane.setForeground(fgColor);
		textPane.setCaretColor(fgColor);
	}
}
