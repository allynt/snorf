package snorf;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SnorfHelpDialog extends JDialog {
	
	private JButton okButton;
	private JEditorPane textPane;
	
	// the help message...
	private static String helpText = 
		"<html>" +
		"<h2>snorf</h2>a simple text editor" +
		"<p>This is a minimal text editor with just the features I want, and <i>no crap</i>.</p>" +
		"<p>It lets me concentrate on writing with no distractions; It provides a full-screen window with lots of calming blank space; It doesn't try to deal with icky formatting, it just deals with raw text. " +
		"<p>The only features it bothers with are opening &amp; saving files, updating the color scheme, resizing the font, tracking the word count, etc., and searching for text.</p>" +
		"<p>The latest look and feel is automatically saved in a configuration file (.snorfrc)" +
		"<p>commands are as follows:" +
		"<ul>" +
		"<li><b>ctrl-mouse wheel</b>: increases/decreases the text size" +
		"<li><b>ctrl-up/down</b>: increases/decreases the text size" +
		"<li><b>mouse click on statusBar</b>: update statistics (word count, etc.) in statusBar" +
		"<li><b>rt. mouse button</b>: brings up the context menu which offers the following options<br>" +
		"(some of which also have keyboard shortcuts)" +
		"<ul>" +
		"<li><b>ctrl-N</b>: new document" +
		"<li><b>ctrl-O</b>: open document" +
		"<li><b>ctrl-S</b>: save document" +
		"<li>save document as..." +
		"<li><b>ctrl-F</b>: display find panel" +
		"<li>change the color scheme" +
		"<li><b>ctrl-U</b>: update statistics (word count, etc.) in statusBar" +
		"<li><b>esc</b>: minimizes the application" +				
		"<li><b>ctrl-H</b>: display this help page" +
		"<li><b>ctrl-Q</b>: quit application" +
		"</ul>" +
		"<li><b>ctrl-C</b>: copy" +
		"<li><b>ctrl-X</b>: cut" +		
		"<li><b>ctrl-V</b>: paste" +
		"</ul>" +
		"<p>usage is: <u>" + translate(Snorf.USAGE) + "</u></p>" +
		"<p style='text-align: right; padding: 10px; margin: 10px'><i>enjoy!</i></p>" +
		"</html>";
	
	class OkAction extends AbstractAction {
        public OkAction() { super("ok"); }
        public void actionPerformed(ActionEvent e) { ok(); }
    };
       
	public SnorfHelpDialog() {

		setIconImage(Snorf.logoIcon.getImage());
		setTitle(Snorf.TITLE);
		setModalityType(ModalityType.APPLICATION_MODAL);
						
		textPane = new JEditorPane("text/html",helpText);
		textPane.setEditable(false);
		
		okButton = new JButton(new OkAction());
		
		JScrollPane scrollPane = new JScrollPane(textPane);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setWheelScrollingEnabled(true);

		Container contentPane = getContentPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(okButton, BorderLayout.SOUTH);		
		pack();		
		
		addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                ok();
            }
        });
		
		// make sure the text is scrolled to the top...
		textPane.setCaretPosition(0);
	}
		
	public void ok() {
		setVisible(false);
	}
	
	public void setColors(Color fgColor, Color bgColor) {
		textPane.setForeground(fgColor);
		textPane.setBackground(bgColor);
		okButton.setForeground(fgColor);
		okButton.setBackground(bgColor);
	}
	
	protected static String translate(String source) {
		// format a string for HTML...
		String target = source;
		return target.replace("<","&lt;").replace(">","&gt;");		
	}
}
