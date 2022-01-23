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
		
		this.scoreMap.put('A', 1); this.scoreMap.put('a', 0);
		this.scoreMap.put('B', 3); this.scoreMap.put('b', 0);
		this.scoreMap.put('C', 3); this.scoreMap.put('c', 0);
		this.scoreMap.put('D', 2); this.scoreMap.put('d', 0);
		this.scoreMap.put('E', 1); this.scoreMap.put('e', 0);
		this.scoreMap.put('F', 4); this.scoreMap.put('f', 0);
		this.scoreMap.put('G', 2); this.scoreMap.put('g', 0);
		this.scoreMap.put('H', 4); this.scoreMap.put('h', 0);
		this.scoreMap.put('I', 1); this.scoreMap.put('i', 0);
		this.scoreMap.put('J', 8); this.scoreMap.put('j', 0);
		this.scoreMap.put('K', 5); this.scoreMap.put('k', 0);
		this.scoreMap.put('L', 1); this.scoreMap.put('l', 0);
		this.scoreMap.put('M', 3); this.scoreMap.put('m', 0);
		this.scoreMap.put('N', 1); this.scoreMap.put('n', 0);
		this.scoreMap.put('O', 1); this.scoreMap.put('o', 0);
		this.scoreMap.put('P', 3); this.scoreMap.put('p', 0);
		this.scoreMap.put('Q', 10); this.scoreMap.put('q', 0);
		this.scoreMap.put('R', 1); this.scoreMap.put('r', 0);
		this.scoreMap.put('S', 1); this.scoreMap.put('s', 0);
		this.scoreMap.put('T', 1); this.scoreMap.put('t', 0);
		this.scoreMap.put('U', 1); this.scoreMap.put('u', 0);
		this.scoreMap.put('V', 4); this.scoreMap.put('v', 0);
		this.scoreMap.put('W', 4); this.scoreMap.put('w', 0);
		this.scoreMap.put('X', 8); this.scoreMap.put('x', 0);
		this.scoreMap.put('Y', 4); this.scoreMap.put('y', 0);
		this.scoreMap.put('Z', 10); this.scoreMap.put('z', 0);
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
