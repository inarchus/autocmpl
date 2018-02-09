# autocmpl
Autocompletion Java Project for Asymmetrik

Additional Documentation is in Readme.txt

To rebuild the code, download autocmpl and autocmpltest and compile the AutocompleteTester.java file which contains main.  
The autocmpl package is independent from the testing package and can be linked to another tester.  

The TestCorpora directory contains a few tests that I used.  There's the text from a Washington Post article, Romeo and Juliet, Moby Dick, and a book by Tolstoy (the last two from gutenberg).  
When running the larger tests, the training method takes a few seconds to update, but when after it does, typing text into the query box will show predicted completions of that text as well as their frequencies.  There are only 10 shown at a time, and they represent the highest frequency predictions.  

If you use my testing window, I find that Java doesn't seem to like input more than about 600-700 kb into a JTextArea, so I have included a way to load files and read from them to load additional testing data that is larger.  

Allowable words were determined using regex, "[a-zA-Z]+( [-][a-zA-Z]+)*('[a-zA-Z]+)?" . 

This should allow 'words' like "here-or-there" or "her's", but it only allows one apostrophe, though it permits underscores like this_or_that.  It insists that the characters all be English alphabetic and considers upper and lower case as the same in the algorithm.  

In order to avoid the edit boxes hanging, searches and file inputs are in separate Threads.  
