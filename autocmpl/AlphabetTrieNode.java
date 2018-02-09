package autocmpl;

/**
 * This AlphabetTrieNode class contains a total_frequency (the number of words passing through the current node, mainly for 'future use').  
 * 
 *   Primarily it includes the successor 'trie' nodes and the "terminal" frequency of the current node, meaning the number of times this appears as the last character of
 *   	a word in the corpus.  
 */
public class AlphabetTrieNode {
	
	AlphabetTrieNode [] successors = null;
	// total frequency represents the total number of words passing through this node
	int total_frequency = 0;
	// represents the number of times for which this node is terminal (thus is an 'actual' word).   
	int frequency = 0;
	
	static int alphabet_size = 0;
	
	public AlphabetTrieNode(int t_alphabet_size)
	{
		alphabet_size = t_alphabet_size; 
	}
	
	public AlphabetTrieNode [] getSuccessors()
	{
		return successors;
	}
	
	public void init(int t_alphabet_size)
	{
		successors = new AlphabetTrieNode [alphabet_size];
		for (int i = 0; i < alphabet_size; i++)
		{
			successors[i] = new AlphabetTrieNode(alphabet_size);
		}
	}
	
	public void setFrequency(int new_frequency)
	{
		if( new_frequency >= 0)
		{
			frequency = new_frequency;
			
		}
	}
	
	public int incrementFrequency()
	{
		total_frequency++;
		frequency ++;
		return frequency;
	}
	
	public int getFrequency()
	{
		return frequency;
	}
	
	public void setTotalFrequency(int new_total_frequency)
	{
		if( new_total_frequency >= 0)
		{
			total_frequency = new_total_frequency;
			
		}
	}
	
	public int incrementTotalFrequency()
	{
		total_frequency ++;
		
		return total_frequency;
	}
	
	public int getTotalFrequency()
	{
		return total_frequency;
	}
}
