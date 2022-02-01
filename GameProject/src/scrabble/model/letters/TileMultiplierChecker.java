package scrabble.model.letters;

import java.util.HashMap;

import scrabble.model.Board.FieldType;

public class TileMultiplierChecker {
	
	private HashMap<FieldType, Integer> letterMultiplierMap;
	private HashMap<FieldType, Integer> wordMultiplierMap;
	
	/**
	 * Initializes a map with int multipliers for each type of tile
	 * @author Maxim
	 */
	public TileMultiplierChecker(){
		
		letterMultiplierMap = new HashMap<>();
		wordMultiplierMap = new HashMap<>();
		
		letterMultiplierMap.put(FieldType.TRIPLE_LETTER_SCORE, 3);
		letterMultiplierMap.put(FieldType.DOUBLE_LETTER_SCORE, 2);
		wordMultiplierMap.put(FieldType.TRIPLE_WORD_SCORE, 3);
		wordMultiplierMap.put(FieldType.DOUBLE_WORD_SCORE, 2);
	}
	
	/**
	 * Returns an int value which represents the letter multiplier for tile given in the argument
	 * @param  ft
	 * @return int multiplier
	 * @author Maxim
	 */
	public int letterMultiplierChecker(FieldType ft) {
		return letterMultiplierMap.get(ft);
	}
	
	/**
	 * Returns an int value which represents the word multiplier for tile given in the argument
	 * @param  ft
	 * @return int multiplier
	 * @author Maxim
	 */
	public int wordMultiplierChecker(FieldType ft) {
		return wordMultiplierMap.get(ft);
	}

}
