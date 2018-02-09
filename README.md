# autocmpl
Autocompletion Java Project for Asymmetrik


Instructions for Compilation or use:
The main method is in AutoCompleteTester.java, so presumably one would compile this file.  Java version 8u162 was used to build this project so if any problems arise, update to that version.  However, I don't believe any of the newest Java functionality was used, so it should work on most versions of Java after 1.5.  
	
If you don't want to use the tester and just want to use the internal code, import the autocmpl package which contains all of the autocompletion code except for the testing window and main function.  
	
The testing window and code is contained in the autocmpltest package just so that it can be separated out from the other code, if not desired.  
	
I've included some text files for testing purposes.  

AutocompletionProvider and Candidate classes are implemented in a package "autocmpl."  
1. In this package, there are five classes:
a) AutocompletionProvider - Handles the outward interface as specified in the description.  One could run the code directly from there using the interface that was described.  
Implements train(String passage)
Vector<Candidate> getWords(String fragment) - returns null if no results occur
int getMaxListSize()
void setMaxListSize(int t_max_list_size) - resets max list size and forces recomputation of any saved results
b) Candidate - This class is implemented in the way described as well.  There's nothing really hidden under the hood here except that it implements Comparable for sorting.  
String getWord()
Integer getConfidence()
c) AlphabetTrie	- This is where the majority of the work is done naturally.  There are functions:
Vector<Candidate> getWordList(String initial_fragment, int max_size) - this feeds the get words function in the AutocompletionProvider.  It scans through the "trie" structure until it finds the proper path (i.e. words with that initial fragment exist in the original corpus.)  If nothing exists, it returns null.  
				
This function also takes care of the "memoization" of the initial two characters results to speed up lookup after the first time the data set is loaded.  
void addWord(String word)
This will add a specific word from the training passage.  We use regex to separate words from "String passage" and send them to void recomputeMemoization() - doesn't recompute the memoization yet, simply resets the memo-tree and forces recomputation as future lookups are done.  
A future addition would be a separate thread that would actually recompute the results while not interfering with operation of the program.  
void setMemoDepth(int t_depth) - sets the memo-depth (i.e. the number of characters memoized) A hard limit at 5 is set.  
public int getMemoDepth() - returns the memoization depth (i.e. 2 means any two character expression is memoized).  
d) AlphabetTrieNode
AlphabetTrieNode(int t_alphabet_size)
AlphabetTrieNode [] getSuccessors()
void init(int t_alphabet_size) - in order to prevent overuse of memory, we don't allocate new trie nodes until necessary (i.e. not in the constructor)  
void setFrequency(int new_frequency) - really never used in the code, there for completeness and future use
int incrementFrequency()
int getFrequency()
