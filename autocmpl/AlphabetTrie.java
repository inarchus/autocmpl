package autocmpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class AlphabetTrie extends Thread {
	AlphabetTrieNode root = null;
	HashMap<Character, Integer> trie_map;
	Vector<Character> key_vector;

	MemoResultNode memorized_trie = null; 
	int default_depth = 2;
	
	final int ALPHABET_SIZE = 26;
	final int SPECIAL_CHARS = 3;
	
	
	public AlphabetTrie()
	{
		// initialize the root node
		root = new AlphabetTrieNode(ALPHABET_SIZE + SPECIAL_CHARS);
		root.init(ALPHABET_SIZE + SPECIAL_CHARS);
		
		trie_map = new HashMap<Character, Integer> ();
		// initialize a lookup table based on the characters.  
		for(int i = 0; i < ALPHABET_SIZE; i++)
		{
			trie_map.put( new Character((char) ('a' + i)), new Integer( i ) );
		}
		trie_map.put('-',ALPHABET_SIZE);
		trie_map.put('_', ALPHABET_SIZE + 1);
		trie_map.put('\'', ALPHABET_SIZE + 2);		
		
		key_vector = new Vector<Character>();
		key_vector.addAll(trie_map.keySet());	
		
		// construct the memoization tree.  default depth = 2 means that we'll have null, single and double characters memoized so recomputation isn't done all the time.  
		memorized_trie = new MemoResultNode(ALPHABET_SIZE + SPECIAL_CHARS, default_depth);
	}
	
	/*
	 * Method: getWordList
	 * Returns: Vector<Candidate>, sorted by 'relevance,' truncated to max_size
	 * 
	 * First, and very importantly, it proceeds to the end of the word.  Then it simply searches 
	 */
	public Vector<Candidate> getWordList(String initial_fragment, int max_size)
	{
		AlphabetTrieNode current_node = root;
		MemoResultNode memo_node = memorized_trie;
		
		Vector<Candidate> results = null;
				
		initial_fragment = initial_fragment.toLowerCase();
		
		if (initial_fragment.length() <= default_depth)
		{
			for(int s = 0; s < Math.min(initial_fragment.length(), default_depth); s++)
			{
				if(memo_node.getSuccessors() != null && memo_node.getSuccessors()[trie_map.get(initial_fragment.toCharArray()[s]).intValue()] != null)
				{
					memo_node = memo_node.getSuccessors()[trie_map.get(initial_fragment.toCharArray()[s]).intValue()];
				}
			}
			if (memo_node != null && memo_node.getResults() != null)
			{
				results = memo_node.getResults();
			}
		}
		
		if(results == null)
		{
			for(int i = 0; i < initial_fragment.length(); i++)
			{
				if(current_node.getSuccessors() != null && current_node.getSuccessors()[trie_map.get(initial_fragment.toCharArray()[i]).intValue()] != null)
				{
					current_node = current_node.getSuccessors()[trie_map.get(initial_fragment.toCharArray()[i]).intValue()];
				}
			}
			
			results = searchSubTrie(current_node, initial_fragment, max_size);
			if(initial_fragment.length() <= default_depth && memo_node != null && results != null)
			{
				memo_node.setResults(results);	
			}
			
		}		
		return results;
	}
	
	/*
	 * This is the recursive element of the trie search.  The initial fragment will contain the partially completed string, max_size ensures that we only generate max_size elements 
	 * 		in each function.  
	 * 
	 */
	private Vector<Candidate> searchSubTrie(AlphabetTrieNode start_node, String initial_fragment, int max_size)
	{
		Vector<Candidate> results = new Vector<Candidate> ();
		
		if(start_node.getFrequency() > 0)
		{
			results.add(new Candidate(new String(initial_fragment), start_node.getFrequency()));
		}
		
		Iterator<Character> itr = key_vector.iterator();

		String c;
		while(itr.hasNext())
		{
			c = itr.next().toString();
			
			if(start_node.getSuccessors() != null && start_node.getSuccessors()[trie_map.get(c.toCharArray()[0]).intValue()] != null)
			{
				results.addAll(searchSubTrie(start_node.getSuccessors()[trie_map.get(c.toCharArray()[0]).intValue()], new String(initial_fragment) + c, max_size));
			}
		}
		
		// Use the collections sort, merge sort is N lg(N) so should be good enough.  
		if(results != null && results.size() > 0)
		{
			Collections.sort(results);
			if(results.size() > 10)
			{
				results = new Vector<Candidate>(results.subList(0, 10));	
			}						
		}
		
		return results;
	}
	
	/*
	 * Adds a word to the trie structure.  It will increment the frequencies as it descends and reset the memoization to force recomputation.  
	 * 
	 */
	public void addWord(String word)
	{
		AlphabetTrieNode current_node = root;
				
		word = word.toLowerCase();
		
		if(word != null)
		{
			for(int i = 0; i < word.length(); i++)
			{
				if (current_node.getSuccessors() == null || current_node.getSuccessors()[trie_map.get(word.toCharArray()[i]).intValue()] == null)
				{
					current_node.init(ALPHABET_SIZE + SPECIAL_CHARS);
				}
								
				current_node = current_node.getSuccessors()[trie_map.get(word.toCharArray()[i]).intValue()];
				current_node.incrementTotalFrequency();
				
				
				if (i == word.length() - 1)
				{
					current_node.incrementFrequency();
				}
			}
			if(memorized_trie.getResults() != null)
			{
				memorized_trie.reset();	
			}			
		}		
	}
	
	public int getMemoDepth()
	{
		return default_depth;
	}
	/*
	 * Sets the depth of memoization, meaning the number of characters memoized.  By default it is set to 2, so empty string '' 
	 * 		as well as 'a', 't' are memoized, as well as any two letter combinations.  
	 * 		Note: 5 is set arbitrarily, but appx. 30^5 = 24,300,000 which is starting to get big.  Hundreds of megabytes in memoization is probably too much.
	 */
	public void setMemoDepth(int t_depth)
	{
		  
		if(t_depth >= 0 && t_depth <= 5)
		{
			default_depth = t_depth;
		}
	}
	
	/*
	 * Right now this doesn't actually recompute the memoization, it just resets it, but in the future, we could recompute the memoization to improve runtime.   
	 * 
	 */
	public void recomputeMemoization()
	{
		memorized_trie.reset();	
	}
}
