package snorf;

import java.io.*;
import javax.swing.*;

// utility class to make FileIO easier...
public class SnorfFileIO {
	
	static public String read(File file) {
		StringBuffer contents = new StringBuffer();
        BufferedReader input = null;

        try {
            input = new BufferedReader(new FileReader(file));
            String line = null; //not declared within while loop
            while (( line = input.readLine()) != null){
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),Snorf.TITLE,JOptionPane.ERROR_MESSAGE);            
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),Snorf.TITLE,JOptionPane.ERROR_MESSAGE);            
        }
        finally {        	
        	try {
                if (input!= null) {
                    //flush and close both "input" and its underlying FileReader
                    input.close();
                }
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null,e.getMessage(),Snorf.TITLE,JOptionPane.ERROR_MESSAGE);            
            }            
        }
        return contents.toString();
    }
	
	static public void write(File file, String contents) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(contents);
			output.close();
		}
		catch (IOException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),Snorf.TITLE,JOptionPane.ERROR_MESSAGE);            	        
		}
	}
}
