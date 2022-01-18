package scrabble.model;

import java.util.ArrayList;

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
    	
    	// WHAT THIS DOES AT THIS POINT: 
    	// USES TWO ABOVE METHODS TO GET WORDS FROM TILES IN HAND E.G. ADAF AFAF AFFA...
    	// FROM THESE METHODS WE GET A LIST FOR THIS METHOD
    	// THIS METHOD GOES THROUGH THE LIST TO FIND WORD THAT EXISTS
    	// THEN IF THE COMPUTER GOES FIRST IT PLAYS THIS WORD ON TILE H8 HORIZONTALY
    	// IF NOT THE METHOD CHECKS IF THE WORD WAS ALREADY PLAYED
    	// IF NOT THE METHOD TAKES ALL WORDS PLAYED ON THE BOARD AND CHECKS IF THE GENERATED WORD IS A SUBSTRING OF IT (THIS SHOULD BE REPLACED WITH FOR CYCLE BECAUSE
    	// SUBSTRING CHECKS IF ALL LETTERS ARE PRESENT, WE NEED JUST ONE)
    	// THEN THE METHOD CHECKS IF THE ALREADY PLAYED WORD WAS PLAYED HORIZONTALLY
    	// THEN IT CHECKS WHETHER THE LAST LETTER OF THE GENERATED WORD IS THE FIRST LETTER OF THE ALREADY PLAYED WORD
    	// IF YES AND THERE IS SPACE FOR IT THE GENERATED WORD IS ADDED BEFORE THE PLAYED WORD WITH THEIR LAST AND FIRST LETTERS RESPECTIVELLY OVERLAPPING
    	// THE METHOD ALSO CHECKS WHETHER THE FIRST LETTER OF THE GENERATED WORD IS EQUAL TO LAST LETTER OF PLAYED WORD
    	// IF YES, SAME APPROACH, APPEND PLAYEDWORD
    	// IF NEITHER OF THESE IS TRUE THEN WE LOOK FOR MATCHING LETTER WITHIN THE ENTIRE STRING AND MAKE A NEW CHAR WITH THIS VALUE IF FOUND (COULD BE DONE EARLIER ALREADY)
    	// THEN WE FIND THE COORDINATE OF THIS LETTER ON THE BOARD, WHILE GETTING THE INDEX OF THE MATCHING LETTER WITHIN THE WORD STRING
    	// FROM THE WORD STRING INDEX WE KNOW HOW MUCH TO SUBTRACT TO GET THE CORRECT ROW SO THE MATCHING LETTERS OVERLAP
    	
    	// TO BE ADDED VERTICAL APPROACH IN THE SAME WAY
    	
    	// TO BE ADDED SWAPPING (HOWEVER PRETTY UNLIKELY THAT A COMPUTER WONT BE ABLE TO FIND A WORD TO CREATE)
    	
    	// THIS IS A STUPID STRATEGY, DOES NOT LOOK FOR WORDS WITH HIGHEST SCORES, IT ALWAYS TAKES FIRST WORD IT FINDS THAT ACTUALLY CAN BE PLACED
    	
    	/**Word to be played*/
    	ArrayList<String> listOfAllPermutations = new ArrayList<>();
    	
    	listOfAllPermutations = determineWord(formString(letterDeck));
    	
    	/**Cycle through generated list of words*/
    	for(String word : listOfAllPermutations) {
    		/**Suitable word found*/
    		if(checker.isValidWord(word) != null) {
    			/**If first turn play from center*/
    			if(board.getPlayedWords().isEmpty()) {
    				move = move + "WORD H8 H " + word;
    			}
    			/**Word was not already played*/
    			else if(!board.getPlayedWords().contains(word)) {
    				/**Cycle though words already on board*/
    				for(String playedWord : board.getPlayedWords()) {
    					/**Check if word is substring of a word already on board*/
    							if(playedWord.contains(word)) {
    								/**If the played word is horizontal*/
    								if(board.getWordDirectionMap().get(playedWord) == "H") {
    									/**If the last letter of the word is the first letter of the played word*/
    									if(word.charAt(word.length()-1) == playedWord.charAt(0)) {
    										/**If the new word can fit*/
    										if(board.convert(board.getWordCoordinateMap().get(playedWord))[1] > word.length()) {
    				
    										move = move + "WORD " + board.convert(board.convert(board.getWordCoordinateMap().get(playedWord))[0], board.convert(board.getWordCoordinateMap().get(playedWord))[1] - word.length() + 1) + " H " + word.toUpperCase();
    										}
    									}
    									/**If the first letter of the word is the last letter of the played word*/
    									else if(word.charAt(0) == playedWord.charAt(playedWord.length() - 1)) {
    										/**If the new word can fit*/
    										if(board.convert(board.getWordCoordinateMap().get(playedWord))[1] + word.length() <= 15) {
    					    				
    										move = move + "WORD " + board.convert(board.convert(board.getWordCoordinateMap().get(playedWord))[0], board.convert(board.getWordCoordinateMap().get(playedWord))[1] + word.length() - 1) + " H " + word.toUpperCase();
        									}
    									}
    									
    									/**Stores Matching letters*/
    									char matchingLetter = ' ';
    									
    									/**Finds matching letters*/
    									for(char wordLetters : word.toCharArray()) {
    										for(char letters : playedWord.toCharArray()) {
    											if(wordLetters == letters) {matchingLetter = letters;}
    										}
    									}
    									
    									/**If matching letter is not empty*/
    									if (matchingLetter != ' ') {
    										/**Get index of matching letter in playedWord*/
    										int indexMatchingLetterPlayedWord = playedWord.indexOf(matchingLetter);
    										/**Get index of matching letter in word*/
    										int indexMatchingLetterWord = word.indexOf(matchingLetter);
    										/**Get start coordinates of played word*/
    										int[] playedWordStartCoords = board.convert(board.getWordCoordinateMap().get(playedWord));
    										/**Get coordinates of matching letter inside of playedWord*/
    										String coordsOfMatchingLetter = board.convert(playedWordStartCoords[0], indexMatchingLetterPlayedWord);
    										
    										move = move + "WORD" + board.convert(board.convert(coordsOfMatchingLetter)[0] - indexMatchingLetterWord, indexMatchingLetterPlayedWord);
    									}
    								}
    									
    							
    								
    						
    							else {
    							
    							}
    					}
    				}
    			}
    		}
    	}
    	return move;
    }

}
