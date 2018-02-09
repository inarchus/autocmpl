package autocmpl;

/*
 * class Candidate
 *  This class contains two data elements and implements comparable so that it can be sorted from greatest to least.  Otherwise, nothing to see here.  
 *  
 */

public class Candidate implements Comparable<Candidate>{
	
	String word = "";
	int confidence = 0;
	
	/*
	 * Default Constructor, does nothing right now.    
	 */
	public Candidate()
	{
		
	}
	
	public Candidate(Candidate c2)
	{
		this.word = new String(c2.word);
		this.confidence = c2.confidence;
	}
	
	public Candidate(String t_word, int t_confidence)
	{
		word = new String(t_word);
		confidence = t_confidence;
	}

	public void setWord(String t_word)
	{
		word = new String(t_word);
	}
	
	public void setConfidence(int t_confidence)
	{
		confidence = t_confidence;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public Integer getConfidence()
	{
		return new Integer(confidence);
	}

	/*
	 * This compareTo method will allow a sorting.  It ensures that the highest values end up at the top of any list so it doesn't have to be reversed.   
	 * 
	 */
	public int compareTo(Candidate c2) {
		if (this.getConfidence() > c2.getConfidence() )
		{
			return -1;
		}
		else if (this.getConfidence() < c2.getConfidence())
		{
			return 1;
		}
			 
		return 0;
	}
}
