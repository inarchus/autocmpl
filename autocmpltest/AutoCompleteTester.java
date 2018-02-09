package autocmpltest;

//import java.util.Scanner;
//import autocmpl.*;

/*
 * This methods only purpose is to open a TestFrame.  If we want text input, we have to re-enabled the commented sections.  
 * However, this does contain the main entry point for the program.  
 */

public class AutoCompleteTester {

	public static void main(String[] args) {
		new TestFrame();
		
		/*
		Scanner s = new Scanner(System.in);
		String input_string = s.nextLine();
		
		AutocompleteProvider acp = new AutocompleteProvider(); 
		
		while(input_string != null && !input_string.equals("quit"))
		{
			acp.train(input_string);			
			input_string = s.nextLine();
		}
		*/
	}

}
