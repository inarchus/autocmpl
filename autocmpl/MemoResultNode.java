package autocmpl;

import java.util.Vector;

public class MemoResultNode {
	
	Vector<Candidate> results = null;
	MemoResultNode [] successor_results = null;
	
	int alphabet_size = 1;
	
	public MemoResultNode(int c_alphabet_size, int depth)
	{
		alphabet_size = c_alphabet_size;
		if(depth > 0)
		{
			successor_results = new MemoResultNode[alphabet_size];
			for(int i = 0; i < alphabet_size; i++)
			{
				successor_results[i] = new MemoResultNode(alphabet_size, depth - 1);
			}				
		}
	}
	
	/*
	 * Make a copy of each string in the array.  
	 * 
	 */
	
	public void reset()
	{
		results = null;
		if(successor_results != null)
		{
			for(int i = 0; i < alphabet_size; i++)
			{
				successor_results[i].reset();
			}					
		}
	}
	
	public MemoResultNode [] getSuccessors()
	{
		return successor_results;
	}
	
	public void setResults(Vector<Candidate> t_results)
	{
		if(t_results.size() > 0)
		{
			results = new Vector<Candidate>(t_results.size());
			for(int i = 0; i < t_results.size(); i++)
			{
				results.add(new Candidate(t_results.elementAt(i)));
			}
		}		
	}
	
	public Vector<Candidate > getResults()
	{
		return results;
	}
	
	
}
