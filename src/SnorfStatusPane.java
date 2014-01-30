package snorf;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SnorfStatusPane extends JPanel implements MouseListener  {
	
	// components...
	protected JTextPane textPane;
    protected JLabel fileName;
    protected JLabel fileChars;
    protected JLabel fileWords;
    protected JLabel fileLines;
        
	public SnorfStatusPane(JTextPane textPane) {
		this.textPane = textPane;
		
	    setOpaque(true);
	    	    
	    // setup components...
        fileName = new JLabel("name: ");
        fileChars = new JLabel("chars: ");
        fileWords = new JLabel("words: ");
        fileLines = new JLabel("lines: ");
        
        String fontName = textPane.getFont().getName();
         
        fileName.setFont(new Font(fontName,Font.ITALIC,12));              
        fileChars.setFont(new Font(fontName,Font.ITALIC,12));        
        fileWords.setFont(new Font(fontName,Font.ITALIC,12));             
        fileLines.setFont(new Font(fontName,Font.ITALIC,12));
 
		setLayout(new GridLayout(1,4));
        add(fileName);
        add(fileChars);
        add(fileWords);
        add(fileLines);
        
        // add a mouseListener (update count when mouse clicks)...
        addMouseListener(this);
	}
	
	public void mouseExited(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mouseClicked(MouseEvent event) {}
	public void mouseReleased(MouseEvent event) {}
	public void mousePressed(MouseEvent event) {
		count();
	}
		
	public void setColors(Color fgColor, Color bgColor) {
		this.setBackground(bgColor);
		this.setForeground(fgColor);	        
		fileName.setBackground(bgColor);
		fileName.setForeground(fgColor);	              
		fileChars.setBackground(bgColor);
		fileChars.setForeground(fgColor);	        
		fileWords.setBackground(bgColor);
		fileWords.setForeground(fgColor);	     
		fileLines.setBackground(bgColor);
		fileLines.setForeground(fgColor);
	}
	
	public void count() {
    	String text = textPane.getText();    	
    	fileChars.setText("chars "+countChars(text));
    	fileWords.setText("words "+countWords(text));
    	fileLines.setText("lines "+countLines(text));
    }
	
	protected int countChars(String text) {
		char[] tokens = text.toCharArray();
		int nChars = 1;
		for(int i=0; i<tokens.length; i++) {
			if ((tokens[i] != ' ') && (tokens[i] != '\n')) nChars += 1;
		}
		return (nChars);
	}
	    
	protected int countWords(String text) {
		String[] tokens = text.split("\\s+");
		return tokens.length;
	}

	protected int countLines(String text) {
		String[] tokens = text.split("\\n+");
		return tokens.length;
	}
	
	public void setFileName(String name) {
		fileName.setText("name "+name);
	}
	
}
