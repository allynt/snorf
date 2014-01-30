package snorf;

import javax.swing.*;
import java.io.*;

// custom fileChooser (for text files)...
public class SnorfFileChooser extends JFileChooser {
	
	private static File currentDirectory = null;
	private String extension = "txt";
	private String description = "text files (*.txt)";
	
	private Snorf snorf;
	
	public SnorfFileChooser(Snorf snorf) {
		this.snorf = snorf;
		
		if (currentDirectory == null) {
			this.setCWD(new File("."));
		}
		
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File file) {
				// all directories return true...
				if (file.isDirectory()) {
					return true;
				}

				String fileName = file.getName();
				int i = fileName.lastIndexOf('.');
				if (fileName.substring(i+1).toLowerCase().equals(extension)) {
					// if the string after the last "." equals "txt", return true
					return true;
				}
				else {
					// otherwise, return false
					return false;
				}
			}
			public String getDescription() {
				return description;
			}
		});
	}
	
	public void setCWD(File file) {
        try {
            currentDirectory = file.getCanonicalFile();

        }
        catch (IOException e) {
        	// should be System.err.println()?
            JOptionPane.showMessageDialog(null,e.getMessage(),Snorf.TITLE,JOptionPane.ERROR_MESSAGE);            
        }
    }
	
	public File showOpenDialog() {
		setCurrentDirectory(currentDirectory);
		if (super.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File file = getSelectedFile();
		if (file == null || !(file.isFile())) {
			return null;
		}

		setCWD(file);
		return file;
	}

	public File showSaveDialog() {
		if (snorf.currentFile != null) {
			setCurrentDirectory(snorf.currentFile);
		}
		else {
			setCurrentDirectory(currentDirectory);			
		}
		if (super.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File file = getSelectedFile();
		if (file == null /*|| !(file.isFile())*/) {
			return null;
		}
		if (file.isFile()) {
			if (JOptionPane.showConfirmDialog(this,"overwrite existing \"" + file.getName() + "\"?",this.getDialogTitle(),JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION) {
	              return null;
	          }
		}

		setCWD(file);
		return file;
	}

}
