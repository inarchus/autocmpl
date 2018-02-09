package autocmpltest;

import autocmpl.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;

import javax.swing.border.SoftBevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

//import java.util.Vector; Moved to SearchThread
//import java.util.regex.*;

/*
 * This TestFrame class serves as a relatively easy way to enter training and query data and get results in a readable way.  
 * 
 * Basically this thing is two edit boxes and a button.  The file menu can load a file into the training data, or exit.  
 * 
 */

public class TestFrame extends JFrame implements WindowListener, ActionListener, CaretListener {
	JTextArea  training_field = null;
	JTextArea  query_field = null;
	JButton trainButton = null;
	AutocompleteProvider acp = null;
	JMenu main_menu;
	JMenuBar menu_bar;
	JMenuItem max_size_item;
	JMenuItem exit_item;
	JMenuItem load_item;
	//JProgressBar progress_loading;
	JList options_display;
	JLabel bottom_info;
	// used to prevent bad timing in searches from redrawing list with old data.  
	long current_priority = 0;
	long future_priority = 0;
	
	public TestFrame()
	{
		acp = new AutocompleteProvider();
		addWindowListener(this);

		setTitle("Autocompletion Tester");
		
		training_field = new JTextArea ();
		training_field.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		query_field = new JTextArea();
		query_field.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		trainButton = new JButton("Input Training Data");

		
		options_display = new JList();
		JScrollPane options_scroller = new JScrollPane(options_display);
		
		training_field.setLineWrap(true);
		training_field.setWrapStyleWord(true);
		
		
		query_field.setLineWrap(true);
		query_field.setWrapStyleWord(true);
		
		//progress_loading = new JProgressBar();
		
		JScrollPane query_scroll = new JScrollPane(query_field);
		JScrollPane training_scroll = new JScrollPane(training_field);
		
		setLayout( new GridBagLayout() );
		GridBagConstraints gbc = new GridBagConstraints();
		
		query_field.addCaretListener(this);
		trainButton.addActionListener(this);

		JLabel training_scroll_label = new JLabel("Enter the training data here as plain text (not more than 500kb, use file load for larger input):");
		JLabel query_scroll_label = new JLabel("Enter the query data here as plain text:");
		bottom_info = new JLabel ("No Data Loaded Yet");
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 5;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		add(training_scroll_label, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 4;
		gbc.gridwidth = 5;
		gbc.weightx = 0.75;
		gbc.weighty = 0.75;
				
		add(training_scroll, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridheight = 1;
		gbc.gridwidth = 5;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
				
		add(query_scroll_label, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridheight = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		
		add(query_scroll, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 4;
		gbc.gridy = 6;
		gbc.gridheight = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.2;
		gbc.weighty = 0.5;
		
		add(options_scroller, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 9;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		gbc.weightx = 0.25;
		gbc.weighty = 0.25;
		
		add(trainButton, gbc);

		/*
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0.45;
		gbc.weighty = 0.15;
		
		add(progress_loading, gbc);
		progress_loading.setValue(0);
		 */
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.gridheight = 1;
		gbc.gridwidth = 5;
		gbc.weightx = 0.5;
		gbc.weighty = 0.15;
		
		add(bottom_info , gbc);
		
		setWindowsLookAndFeel();	
		setSize(750, 750);
		
		menu_bar = new JMenuBar();
		
		max_size_item = new JMenuItem("Set new maximum list size");
		
		load_item = new JMenuItem("Load");
		load_item.setMnemonic(KeyEvent.VK_K);
		exit_item = new JMenuItem("Exit");
		exit_item.setMnemonic(KeyEvent.VK_X);
		
		main_menu = new JMenu("File");
		main_menu.setMnemonic(KeyEvent.VK_F);
		
		main_menu.add(load_item);
		main_menu.add(exit_item);
		
		menu_bar.add(main_menu);
		
		exit_item.addActionListener(this);
		load_item.addActionListener(this);
		
		setJMenuBar(menu_bar);

		setVisible(true);
	}
	/*
	 * caretListener
	 * 
	 * 
	 */
	public void caretUpdate(CaretEvent arg0) {
		String current_text = query_field.getText();
		future_priority++;
		SearchThread st = new SearchThread(acp, current_text, options_display, query_field, this, future_priority);
		st.start();
	}

	/*
	 * Action Listener for Button Presses mostly
	 * 
	 * 
	 */
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == trainButton)
		{
			acp.train(training_field.getText());
			bottom_info.setText("Training Data Entered from training edit box.  ");
		}
		else if (event.getSource() == exit_item)
		{
			this.dispose();
		}
		else if (event.getSource() == load_item)
		{
			JFileChooser fc = new JFileChooser();
			int r = fc.showOpenDialog(this);
			if (r == JFileChooser.APPROVE_OPTION)
			{
				File text_file = fc.getSelectedFile();
				if(text_file.exists() && text_file.canRead())
				{
					TrainingThread load_file_and_train = new TrainingThread(bottom_info, text_file, acp);
					load_file_and_train.start();
				}
			}
			else if (event.getSource() == max_size_item)
			{
				// add this to change maximum item count.  
			}
			 
		}
	}


	public void windowActivated(WindowEvent arg0) {
		
	}


	public void windowClosed(WindowEvent arg0) {
		// sometimes its this one.  
		System.exit(0);
		
	}


	public void windowClosing(WindowEvent arg0) {
		// sometimes its this one.  
		System.exit(0);
	}


	public void windowDeactivated(WindowEvent arg0) {
		
		
	}


	public void windowDeiconified(WindowEvent arg0) {
		
		
	}


	public void windowIconified(WindowEvent arg0) {
		
		
	}


	public void windowOpened(WindowEvent arg0) {
		
		
	}
	
	private void setWindowsLookAndFeel()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
			//this.pack();
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	    }
	    catch (ClassNotFoundException e) {
	    }
	    catch (InstantiationException e) {
	    }
	    catch (IllegalAccessException e) {
	    }
	}
	
	public void setPriority(long new_priority)
	{
		current_priority = new_priority;
	}
	public long getPriority()
	{
		return current_priority ;
	}
	/**
	 * Automated Eclipse serial UID 
	 */
	private static final long serialVersionUID = -8530308476615714012L;
	
}
