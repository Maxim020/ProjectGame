package scrabble.model.letters;

import java.util.HashMap;

public class LetterScoreChecker {
	
	//Attributes
	private HashMap<Character, Integer> scoreMap;
	
	//Constructor
	/**
	 * Fills the HashMap scoreMap with letters and their respective scores
	 * @author Maxim
	 */
	public LetterScoreChecker() {
		
		scoreMap = new HashMap<>();
		
		this.scoreMap.put('A', 1);
		this.scoreMap.put('B', 3);
		this.scoreMap.put('C', 3);
		this.scoreMap.put('D', 2);
		this.scoreMap.put('E', 1);
		this.scoreMap.put('F', 4);
		this.scoreMap.put('G', 2);
		this.scoreMap.put('H', 4);
		this.scoreMap.put('I', 1);
		this.scoreMap.put('J', 8);
		this.scoreMap.put('K', 5);
		this.scoreMap.put('L', 1);
		this.scoreMap.put('M', 3);
		this.scoreMap.put('N', 1);
		this.scoreMap.put('O', 1);
		this.scoreMap.put('P', 3);
		this.scoreMap.put('Q', 10);
		this.scoreMap.put('R', 1);
		this.scoreMap.put('S', 1);
		this.scoreMap.put('T', 1);
		this.scoreMap.put('U', 1);
		this.scoreMap.put('V', 4);
		this.scoreMap.put('W', 4);
		this.scoreMap.put('X', 8);
		this.scoreMap.put('Y', 4);
		this.scoreMap.put('Z', 10);
		this.scoreMap.put('*', 0);
	}
	
	//Methods
	/**
	 * This method returns an integer with score for the requested letter
	 * @param char letter
	 * @return int score
	 * @author Maxim
	 */
	public int scoreChecker(char letter) {
		return scoreMap.get(letter);
	}

}
