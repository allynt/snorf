package snorf;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class SnorfFindPane extends JPanel {
	
	// components...
    protected JTextField findField;
    protected JButton nextButton;
    protected JButton previousButton;
    protected JButton allButton;
    protected JCheckBox caseButton;
    protected JButton hideButton;
    protected JLabel errorLabel;      
    protected JLabel findLabel;
    protected JPanel buttonPane;
    protected JPanel findPane;
    
    private Highlighter highlighter;
    private DefaultHighlighter.DefaultHighlightPainter painter;
    private JTextPane textPane;
    public int pos;
    
    // constants...
    private static final int NEXT = 0;
    private static final int PREVIOUS = 1;
    private static final int ALL = 2;
    
    private static final String ERROR = "phrase not found  ";
    private static final String NO_ERROR = "                  ";
    
    // a bunch of actions...
    class NextAction extends AbstractAction {
        public NextAction() { super("next", Snorf.nextIcon); }
        public void actionPerformed(ActionEvent e) { find(NEXT); }
    };
    
    class PreviousAction extends AbstractAction {
        public PreviousAction() { super("previous", Snorf.previousIcon); }
        public void actionPerformed(ActionEvent e) { find(PREVIOUS); }
    };

    class AllAction extends AbstractAction {
        public AllAction() { super("all"); }
        public void actionPerformed(ActionEvent e) { find(ALL); }
    };

    class HideAction extends AbstractAction {
        public HideAction() { super("", Snorf.hideIcon); }
        public void actionPerformed(ActionEvent e) { hidePane(); }
    };

 
	public SnorfFindPane(JTextPane textPane) {		
		this.textPane = textPane;
		highlighter = textPane.getHighlighter();		
		
		String fontName = textPane.getFont().getName();
		
		// setup components...
	    findField = new JTextField();
	    findField.setFont(new Font(fontName,Font.PLAIN,12));
	    findPane = new JPanel();
	    findPane.setLayout(new BoxLayout(findPane, BoxLayout.LINE_AXIS));
	    findPane.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));//t,l,b,r
	    findPane.add(Box.createHorizontalGlue());	 
	    findLabel = new JLabel("find: ");
	    findLabel.setFont(new Font(fontName,Font.PLAIN,12));           
	    findPane.add(findLabel);
	    findPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    findPane.add(findField);
	    findPane.add(Box.createRigidArea(new Dimension(20, 0)));
	    
	    nextButton = new JButton(new NextAction());
	    nextButton.setFont(new Font(fontName,Font.PLAIN,12));
	    previousButton = new JButton(new PreviousAction());
	    previousButton.setFont(new Font(fontName,Font.PLAIN,12));
	    allButton = new JButton(new AllAction());
	    allButton.setFont(new Font(fontName,Font.PLAIN,12));
	    caseButton = new JCheckBox("match case");
	    caseButton.setFont(new Font(fontName,Font.PLAIN,12));
	    buttonPane = new JPanel();
	    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));//t,l,b,r
	    buttonPane.add(Box.createHorizontalGlue());
	    buttonPane.add(nextButton);
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    buttonPane.add(previousButton);
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	    buttonPane.add(allButton);
	    buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
	    buttonPane.add(caseButton);
	    
	    hideButton = new JButton(new HideAction());
	        

		errorLabel = new JLabel(NO_ERROR);
	    errorLabel.setFont(new Font(fontName,Font.ITALIC,12));

	    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	    add(Box.createHorizontalGlue());
	    add(findPane);
	    buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        add(buttonPane);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        add(errorLabel);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        add(hideButton);
        
        // anonymous keyListener (reset if findField changes)...
        findField.addKeyListener( new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                errorLabel.setText(NO_ERROR);
                pos = -1;
            }
        });      
	}
	
	public void setColors(Color fgColor, Color bgColor) {

		Color highlightColor = bgColor;
		for(int i=0; i<12; i++) { // this should be sufficient
			highlightColor = highlightColor.brighter();
		}		
		painter = new DefaultHighlighter.DefaultHighlightPainter(highlightColor);
		
		findLabel.setForeground(fgColor);
		findLabel.setBackground(bgColor);
		findField.setForeground(fgColor);
		findField.setBackground(bgColor);
		findPane.setForeground(fgColor);
		findPane.setBackground(bgColor);		
	    nextButton.setForeground(fgColor);
	    nextButton.setBackground(bgColor);
	    previousButton.setForeground(fgColor);
	    previousButton.setBackground(bgColor);
	    allButton.setForeground(fgColor);
	    allButton.setBackground(bgColor);
	    caseButton.setForeground(fgColor);
	    caseButton.setBackground(bgColor);
	    hideButton.setForeground(fgColor);
	    hideButton.setBackground(bgColor);
	    buttonPane.setForeground(fgColor);
	    buttonPane.setBackground(bgColor);
	    errorLabel.setForeground(highlightColor);//fgColor);
	    errorLabel.setBackground(bgColor);		
		this.setBackground(bgColor);
		this.setForeground(fgColor);	        
	}
	
	public void find(int type) {
		errorLabel.setText(NO_ERROR);
		highlighter.removeAllHighlights();

		// get search terms...
		String find = findField.getText();
		String text = textPane.getText();
		if (text.isEmpty() || find.isEmpty()) {
			errorLabel.setText(ERROR);
			return;
		}
		if (!(caseButton.isSelected())) {
			find = find.toLowerCase();
			text = text.toLowerCase();
		}

		int i;
		if (pos == -1) {
			pos = textPane.getCaretPosition();
		}
		int length = find.length();
		switch(type) {
			case NEXT:
				i = text.indexOf(find,pos);
				if (i == -1) {
					errorLabel.setText(ERROR);
				}
				else {
					highlight(i,length);
					pos = i+length;
				}
				break;
			case PREVIOUS:
				i = text.lastIndexOf(find,pos);
				if (i == -1) {
					errorLabel.setText(ERROR);
				}
				else {
					highlight(i,length);
					pos = i-1;
				}
				break;
			case ALL:
				i = text.indexOf(find,0);
				if (i == -1) {
					errorLabel.setText(ERROR);
				}
				else {
					do {
						highlight(i,length);
						i = text.indexOf(find,i+length);
					}while (i != -1);
				}
				break;
		}
	}
	
	public void highlight(int i, int length) {	
		try {
			// highlight term...		
			highlighter.addHighlight(i,i+length,painter);
			// move scrollbar to position...
			textPane.setCaretPosition(i);
			textPane.getCaret().setSelectionVisible(true);
		}
		catch(BadLocationException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void hidePane() {
		highlighter.removeAllHighlights();
		setVisible(false);
	}
	
	public void showPane() {
		errorLabel.setText(NO_ERROR);
		setVisible(true);
		findField.grabFocus();
		pos = -1;
	}
}
