package snorf;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Snorf extends JPanel implements MouseListener {
	public static final String TITLE = "snorf";
	public static final String USAGE = "snorf [-p <propertiesFile>] [<filename>]";

	// some icons...
	public static ImageIcon logoIcon = new ImageIcon(Snorf.class.getResource("logo.png"));
	public static ImageIcon nextIcon = new ImageIcon(Snorf.class.getResource("next.png"));
	public static ImageIcon previousIcon = new ImageIcon(Snorf.class.getResource("previous.png"));
	public static ImageIcon hideIcon = new ImageIcon(Snorf.class.getResource("hide.png"));

	// need access to the main JFrame (for minimize event)...
	private static JFrame frame;

	// components...
	private JMenu menu;
	private SnorfTextPane textPane;
	private SnorfFindPane findPane;
	private SnorfStatusPane statusPane;

	// look & feel (most of these can be set in .snorfrc)...
	protected static Properties properties = new Properties();
	protected static String propertiesFileName = ".snorfrc";
	protected Color fgColor = Color.GREEN;
	protected Color bgColor = Color.BLACK;
	protected double marginPct = 1.0;
	protected boolean resizeable = false;
	protected int fontSize = 12;
	protected String fontName;

	protected File currentFile = null;
	protected static File initialFile = null;

	// a bunch of actions...
	class NewAction extends AbstractAction {
        public NewAction() { super("new document"); }
        public void actionPerformed(ActionEvent e) { newDoc(); }
    };

	class OpenAction extends AbstractAction {
        public OpenAction() { super("open document"); }
        public void actionPerformed(ActionEvent e) { openDoc(); }
    };

	class SaveAction extends AbstractAction {
        public SaveAction() { super("save document"); }
        public void actionPerformed(ActionEvent e) { saveDoc(); }
    };

	class SaveAsAction extends AbstractAction {
        public SaveAsAction() { super("save document as..."); }
        public void actionPerformed(ActionEvent e) { saveDocAs(); }
    };

    class FindAction extends AbstractAction {
        public FindAction() { super("find selection"); }
        public void actionPerformed(ActionEvent e) { find(); }
    };

    class ColorAction extends AbstractAction {
        public ColorAction() { super("update colors"); }
        public void actionPerformed(ActionEvent e) { color(); }
    };

    class UpdateAction extends AbstractAction {
        public UpdateAction() { super("update statistics"); }
        public void actionPerformed(ActionEvent e) { update(); }
    };

    class MinimizeAction extends AbstractAction {
        public MinimizeAction() { super("minimize editor"); }
        public void actionPerformed(ActionEvent e) { minimize(); }
    };

    class QuitAction extends AbstractAction {
        public QuitAction() { super("quit editor"); }
        public void actionPerformed(ActionEvent e) { quit(); }
    };

	class HelpAction extends AbstractAction {
        public HelpAction() { super("help"); }
        public void actionPerformed(ActionEvent e) { help(); }
    };

    public Snorf(JFrame frame) {
    	Snorf.frame = frame;
    	currentFile = null;

    	try {
    		// try reading the configuration file...
    		properties.load(new FileInputStream(propertiesFileName));
    		marginPct = Double.parseDouble(properties.getProperty("marginPct",Double.toString(marginPct)));
    		fontSize = Integer.parseInt(properties.getProperty("fontSize",Integer.toString(fontSize)));
    		resizeable = Boolean.parseBoolean(properties.getProperty("resizeable",Boolean.toString(resizeable)));
    		fgColor = SnorfColorChooser.hexToColor(properties.getProperty("fgColor"));
    		if (fgColor == null) fgColor = Color.GREEN;
    		bgColor = SnorfColorChooser.hexToColor(properties.getProperty("bgColor"));
    		if (bgColor == null) bgColor = Color.BLACK;
    	}
    	catch (IOException e) {
    		System.err.println("error reading property file; " + e.getMessage());
    	}

    	// anonymous inner class for close event (triggered if resizeable=true)
    	frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                quit();
            }
    	}
        );

        frame.setUndecorated(!resizeable);
        frame.setTitle(TITLE);
        frame.setIconImage(logoIcon.getImage());
	fontName = getFont().getName();

	// setup textPane...
        textPane = new SnorfTextPane(this);
        textPane.setColors(fgColor,bgColor);
        textPane.getTextPane().addMouseListener(this);

        // setup findPane...
        findPane = new SnorfFindPane(textPane.getTextPane());
        findPane.setColors(fgColor,bgColor);
        findPane.hidePane();

        // setup statusPane...
        statusPane = new SnorfStatusPane(textPane.getTextPane());
        statusPane.setColors(fgColor,bgColor);

        JPanel bottomPane = new JPanel();
        bottomPane.setLayout(new BoxLayout(bottomPane,BoxLayout.PAGE_AXIS));
        bottomPane.add(Box.createVerticalGlue());
        bottomPane.add(findPane);
        bottomPane.add(Box.createRigidArea(new Dimension(0,2)));
        bottomPane.add(statusPane);

        setLayout(new BorderLayout());
        add(textPane,BorderLayout.CENTER);
        add(bottomPane,BorderLayout.SOUTH);

        // setup menu...
        menu = new JMenu();
        menu.add(new NewAction());
        menu.add(new OpenAction());
        menu.add(new SaveAction());
        menu.add(new SaveAsAction());
        menu.addSeparator();
        menu.add(new FindAction());
        menu.addSeparator();
        menu.add(new ColorAction());
        menu.add(new UpdateAction());
        menu.addSeparator();
        menu.add(new MinimizeAction());
        menu.add(new QuitAction());
        menu.addSeparator();
        menu.add(new HelpAction());

        // try opening the initialFile...
        if (initialFile != null) {
        	currentFile = initialFile;
    		statusPane.setFileName(currentFile.getName());
    		String text = SnorfFileIO.read(currentFile);
    		textPane.getTextPane().setText(text);
    		initialFile = null; // be sure to reset it (since it's static)
        }
    }

    public SnorfTextPane getTextPane() {
    	return textPane;
    }

    public SnorfFindPane getFindPane() {
    	return findPane;
    }

    public SnorfStatusPane getStatusPane() {
    	return statusPane;
    }

    // mouse events (show popup menu)...
    public void mouseExited(MouseEvent event)   {}
    public void mouseEntered(MouseEvent event)  {}
    public void mouseClicked(MouseEvent event)  {}
    public void mouseReleased(MouseEvent event) {}
    public void mousePressed(MouseEvent event)  {
    	if (event.isPopupTrigger()) {
        	menu.getPopupMenu().show(this,event.getX(),event.getY());
    	}
    }

    // change look & feel...
    public void setColors(Color fgColor, Color bgColor, boolean overwrite) {
    	textPane.setColors(fgColor,bgColor);
    	statusPane.setColors(fgColor,bgColor);
    	findPane.setColors(fgColor,bgColor);
    	if (overwrite) {
    		this.fgColor = fgColor;
    		this.bgColor = bgColor;
    	}
    }

    // a bunch of actions...

    public void newDoc() {
    	if (!(textPane.getTextPane().getText().isEmpty())) {
    		if (JOptionPane.showConfirmDialog(this,"Save current document?",TITLE,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
    			if (!saveDoc()) return;
    		}
    	}
    	currentFile = null;
    	statusPane.setFileName("");
    	textPane.getTextPane().setText("");
    }

    public void openDoc() {
    	if (!(textPane.getTextPane().getText().isEmpty())) {
    		if (JOptionPane.showConfirmDialog(this,"Save current document?",TITLE,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
    			if (!saveDoc()) return;
    		}
    	}
    	SnorfFileChooser fileChooser = new SnorfFileChooser(this);
    	File file = fileChooser.showOpenDialog();
    	if (file != null) {
    		currentFile = file;
    		statusPane.setFileName(currentFile.getName());
    		String text = SnorfFileIO.read(file);
    		textPane.getTextPane().setText(text);
    	}
    }

    public boolean saveDoc() {
    	if (currentFile == null) {
    		return saveDocAs();
    	}
    	else {
    		String text = textPane.getTextPane().getText();
    		SnorfFileIO.write(currentFile,text);
    		statusPane.setFileName(currentFile.getName());
    		return true;
    	}
    }

    public boolean saveDocAs() {
    	SnorfFileChooser fileChooser = new SnorfFileChooser(this);
    	File file = fileChooser.showSaveDialog();
        if (file != null) {
        	currentFile = file;
        	return saveDoc();
        }
        return false;
    }

    public void find() {
    	findPane.showPane();
    }

    public void color() {
    	SnorfColorChooser colorChooser = new SnorfColorChooser(this);
    	colorChooser.setVisible(true);
    }

    public void update() {
    	statusPane.count();
    }

    public void minimize() {
    	frame.setState(Frame.ICONIFIED);
    }

    public void help() {
    	SnorfHelpDialog helpDialog = new SnorfHelpDialog();
    	Dimension frameSize = frame.getSize();
    	helpDialog.setSize(frameSize.width/2,frameSize.height/2);
    	helpDialog.setVisible(true);
    }

    public void quit() {
    	if (!(textPane.getTextPane().getText().isEmpty())) {
    		if (JOptionPane.showConfirmDialog(this,"Save current document?",TITLE,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
    			if (!saveDoc()) return;
    		}
    	}
    	try {
    		// try (over)writing the configuration file...
      		properties.setProperty("fgColor",SnorfColorChooser.colorToHex(fgColor));
      		properties.setProperty("bgColor",SnorfColorChooser.colorToHex(bgColor));
    		properties.setProperty("marginPct",Double.toString(this.marginPct));
    		properties.setProperty("fontSize",Integer.toString(fontSize));
    		properties.setProperty("resizeable",Boolean.toString(resizeable));
            properties.store(new FileOutputStream(propertiesFileName), null);
        }
    	catch (IOException e) {
    		System.err.println(e.getMessage());
        }
    	frame.setVisible(false);
    	System.exit(0);
    }

    // create the GUI
    private static void createAndShowGUI() {
    	JFrame frame = new JFrame();
    	frame.getContentPane().add(new Snorf(frame));
        frame.pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

    	String arg;
    	int i = 0;
    	while (i < args.length) {
    		arg = args[i++];
    		if (arg.equals("-p")) {
    			propertiesFileName = args[i++];
    			continue;
    		}
    		initialFile = new File(arg);
    	}

    	if (initialFile != null && ! initialFile.exists()) {
    		System.err.println("error: '" + initialFile.getAbsolutePath() + "' is not found; aborting.");
    	}

    	else {
    		SwingUtilities.invokeLater(new Runnable() {
    			public void run() {
    				createAndShowGUI();
    			}
    		});
    	}
    }
}
