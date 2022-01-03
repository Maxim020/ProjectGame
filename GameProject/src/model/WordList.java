package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordList {
	
	//Attributes
	private ArrayList<String> wordsList;
	//Constructor
	/**
	 * Constructor for the WordList class
	 * When WordList object is initialized, it's attribute wordsList(ArrayList<String>) is filled with words from the Collins Scrabble Words (2019) text file.
	 * Source: https://boardgames.stackexchange.com/questions/38366/latest-collins-scrabble-words-list-in-text-file
	 * Property of Collins
	 * @requires Correct text file with path "src/model/Collins Scrabble Words (2019).txt"
	 * @exception FileNotFoundException, IOException
	 * @ensures this.wordsList.isEmpty() = false;
	 * @author Maxim
	 */
	public WordList() {
		String line;
		try {
			BufferedReader buffReader = new BufferedReader(new FileReader("src/model/Collins Scrabble Words (2019).txt"));
			
			try {
				while((line = buffReader.readLine()) != null) {
					this.wordsList.add(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<String> getWordsList(){
		return wordsList;
	}
	
	/**
	 * Checks the list of words for word given in the argument. Returns true if found, else false.
	 * @param String word
	 * @return true || false
	 * @author Maxim
	 * !!NOTE!! - Might want to implements a better searching algorithm, we will see during testing
	 */
	public boolean isPresent(String word) {
		return wordsList.contains(word);
	}
}
