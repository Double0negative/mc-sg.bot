package org.mcsg.bot.skype.games;

public class HangmanGame {
  //http://screencloud.net/v/uIwG
	String[] sprite = new String[] {" ___ \n |	|\n O	|\n\\|/	|\n |	|\n/ \\ |",
			" ___ \n |	|\n O	|\n\\|/	|\n |	|\n/   |",
			" ___ \n |	|\n O	|\n\\|/	|\n |	|\n    |",
			" ___ \n |	|\n O	|\n\\|	|\n |	|\n    |",
			" ___ \n |	|\n O	|\n |	|\n |	|\n    |",
			" ___ \n |	|\n O	|\n 	|\n 	|\n    |",
			" ___ \n |	|\n 	|\n 	|\n 	|\n    |"
	};
	
	private String word;
	private String hint;
	private int guess;
	
	
	/*  
 ___                                                                    
 |	|
 O	|
\|/	|
 |	|
/ \ |  ----

_ _ _ _ _ _ _ _ _ _ _ _
	
    1  2  3  4  5  6  7
   --------------------------
   |   |   |   |   |   |   |   |
   |   |   | ◯ | █ |   |   |   |
   |   |   | █ | ◯ |   |   |   |
   |   |   | ◯ | ◯ | ◯ |   |   |
   |█  |   | █ | █ | █ |   |   |
   |◯  |   | ◯ | █ | ◯ |   |█  |
   --------------------------
*/
}