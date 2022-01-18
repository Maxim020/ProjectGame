package scrabble.model;

import java.util.ArrayList;

import scrabble.model.ComputerPlayer;
import scrabble.model.letters.LetterDeck;
import scrabble.model.words.ScrabbleWordChecker;

public class NaiveStrategy implements Strategy{
	
	/**
	 * Forms a starting String to be made permutations of
	 * @param LetterDeck letterDeck
	 * @requires letterDeck != null
	 * @return String s
	 * @author Maxim
	 */
	public String formString(LetterDeck letterDeck) {
		String s = "";
		for(int i = 0; i < letterDeck.getLettersInDeck().size(); i++) {
			s = s + letterDeck.getLettersInDeck().get(i);
		}
		return s;
	}
	
	/**
	 * Recursively makes permutations from given string and adds them to the list.
	 * The method calls itself until the String "" is added to the list.
	 * @param String s
	 * @requires s != null
	 * @ensures return of word contained in the scrabble dictionary
	 * @return ArrayList<String> Res
	 * @author Maxim
	 */
	public ArrayList<String> determineWord(String s) {
		
		if (s.length() == 0) {
			 
            ArrayList<String> empty = new ArrayList<>();
            empty.add("");
            return empty;
        }
		
		char ch = s.charAt(0);
		
		String substring = s.substring(1);
		
		ArrayList<String> prevResult = determineWord(substring);
		
		ArrayList<String> Res = new ArrayList<>();

        for (String val : prevResult) {
            for (int i = 0; i <= val.length(); i++) {
                Res.add(val.substring(0, i) + ch + val.substring(i));
            }
        }
		
        return Res;
	}
	

    @Override
    /**
     * returns a String in form of "WORD {coordinate} {direction} {word}"
     * @param Board board
     * @param LetterDeck letterDeck
     * @param ScrabbleWordChecker checker
     * @requires board != null && letterDeck != null && checker != null
     * @ensures Return of a valid command for the processMove() method
     * @return String move
     * @author Maxim
     */
    public String determineMove(Board board, LetterDeck letterDeck, ScrabbleWordChecker checker) {
    	
    	String move = "WORD ";
    	
    	//Determine coordinate - To be implemented
    	
    	//Determine direction - To be implemented
    	
    	/**Word to be played*/
    	ArrayList<String> listOfAllPermutations = new ArrayList<>();
    	
    	listOfAllPermutations = determineWord(formString(letterDeck));
    	
    	for(String word : listOfAllPermutations) {
    		if(checker.isValidWord(word) != null) {
    			move = move + " " + word.toUpperCase();
    		}
    	}
    	return move;
    }

}
