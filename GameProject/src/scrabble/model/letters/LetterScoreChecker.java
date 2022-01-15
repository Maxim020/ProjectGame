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
		
		this.scoreMap.put('a', 1);
		this.scoreMap.put('b', 3);
		this.scoreMap.put('c', 3);
		this.scoreMap.put('d', 2);
		this.scoreMap.put('e', 1);
		this.scoreMap.put('f', 4);
		this.scoreMap.put('g', 2);
		this.scoreMap.put('h', 4);
		this.scoreMap.put('i', 1);
		this.scoreMap.put('j', 8);
		this.scoreMap.put('k', 5);
		this.scoreMap.put('l', 1);
		this.scoreMap.put('m', 3);
		this.scoreMap.put('n', 1);
		this.scoreMap.put('o', 1);
		this.scoreMap.put('p', 3);
		this.scoreMap.put('q', 10);
		this.scoreMap.put('r', 1);
		this.scoreMap.put('s', 1);
		this.scoreMap.put('t', 1);
		this.scoreMap.put('u', 1);
		this.scoreMap.put('v', 4);
		this.scoreMap.put('w', 4);
		this.scoreMap.put('x', 8);
		this.scoreMap.put('y', 4);
		this.scoreMap.put('z', 10);
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
