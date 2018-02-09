package autocmpltest;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;

import autocmpl.AutocompleteProvider;
import autocmpl.Candidate;

public class SearchThread extends Thread {
	String current_text = null;
	AutocompleteProvider acp = null;
	JList options_display = null;
	JTextArea query_field = null;
	TestFrame our_frame = null;
	long priority = 0;
	
	public SearchThread(AutocompleteProvider t_acp, String t_current_word, JList t_options_display, JTextArea t_query_field, TestFrame t_our_frame, long t_priority)
	{
		current_text = t_current_word;
		acp = t_acp;
		options_display = t_options_display;
		query_field = t_query_field;
		priority = t_priority;
		our_frame = t_our_frame;
	}
	public void run()
	{
		if(acp != null && query_field != null && query_field != null)
		{
			String current_word = "";
			Pattern word_pattern = Pattern.compile("[a-zA-Z]+([-_][a-zA-Z]+)*('[a-zA-Z]+)?");
			Matcher word_matcher = word_pattern.matcher(current_text);
			while(word_matcher.find())
			{
				current_word = word_matcher.group();
			}
			Vector<Candidate> candidates = acp.getWords(current_word);
			String tooltip_text = "<html>";
			DefaultListModel dlm = new DefaultListModel();
			for(int i = 0; i < candidates.size(); i++)
			{
				dlm.addElement(new String(candidates.elementAt(i).getWord() + " " + candidates.elementAt(i).getConfidence().toString()));
				tooltip_text += candidates.elementAt(i).getWord() + " " + candidates.elementAt(i).getConfidence() + "<br>"; 
			}
			
			
			if(candidates.size() == 0)
			{
				tooltip_text += "No Suggestions";
			}
			tooltip_text += "</html>";
			if (our_frame.getPriority() <= priority)
			{
				options_display.setModel(dlm);
				query_field.setToolTipText(tooltip_text);
				our_frame.setPriority(priority);
			}
		}

	}

}
