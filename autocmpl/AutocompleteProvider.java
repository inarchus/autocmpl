package autocmpl;

import java.util.*;
import java.util.regex.*;

/**
 * class AutocompleteProvider
 * 
 * 
 */

public class AutocompleteProvider {
	
	AlphabetTrie autocomplete_trie = new AlphabetTrie();
	AlphabetTrieNode current_place = null;

	int max_list_size = 10; 
	
    /*
     * Method: getWords
     * Arguments: String fragment - represents a partial word
     * Returns: Vector<Candidate> containing a list of candidate classes with frequencies of words, or some modification of frequency to denote a preference rating.  
     * 			We set a maximum-list-size to limit the size of the returned lists.  
     * This method is described and required by the interface specification. All it does is call the trie's getWordList function.   
     * 
     */
	
	public Vector<Candidate> getWords(String fragment)
	{
		return autocomplete_trie.getWordList(fragment, max_list_size);
	}
	
    /*
     * Method: train
     * Arguments: A single string containing a "passage"/corpus of text
     * Returns: (void)
     * 
     * This method is described and required by the interface specification.
     * Use Regex to extract legal expressions as words.  It will allow multiple hyphenation, but then only one ' character.  
     * Legal word examples: hello, Axiom, five-by-five, I've, computers, etc.  
     * "Words" I've chosen to reject: I've's (anything with more than one internal apostrophe), --this-- (because it doesn't start with a letter.  we'll take the this substring) 
     * 		Mr. Hastings' pen (it will only take the Hastings part, so we don't grab quotation apostrophes.  
     */
	public void train(String passage)
	{
		Pattern word_pattern = Pattern.compile("[a-zA-Z]+([-_][a-zA-Z]+)*('[a-zA-Z]+)?");
		Matcher word_matcher = word_pattern.matcher(passage);
		String current_word;
		
		while(word_matcher.find())
		{
			current_word = word_matcher.group();
			autocomplete_trie.addWord(current_word);
		}
	}
	
	public int getMaxListSize()
	{
		return max_list_size;
	}
	
	public void setMaxListSize(int t_max_list_size)
	{
		max_list_size = t_max_list_size;
		autocomplete_trie.recomputeMemoization();
	}
}
