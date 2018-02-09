package autocmpltest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;

import autocmpl.AutocompleteProvider;

public class TrainingThread extends Thread {

	JLabel notification_label;
	File text_file;
	AutocompleteProvider acp = null;
	
	public TrainingThread(JLabel t_notification, File t_text_file, AutocompleteProvider t_abt )
	{
		acp = t_abt;
		notification_label = t_notification;
		text_file = t_text_file;
	}
	
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(text_file));
			
			String text_line = reader.readLine();
			while(text_line != null)
			{
				acp.train(text_line);
				text_line = reader.readLine();							
			}
			notification_label.setText("Training Data Entered from " + text_file.getName());
			reader.close();
			
		} 
		catch (FileNotFoundException e) 
		{
		} 
		catch (IOException e) 
		{
		}
	} 

}
