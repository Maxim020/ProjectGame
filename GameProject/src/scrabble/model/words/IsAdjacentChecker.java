package scrabble.model.words;

import scrabble.model.Board;

public class IsAdjacentChecker {
	
	private Board board;
	
	public IsAdjacentChecker(Board board) {
		this.board = board;
	}
	
	/**
	 * Checks if there is a letter present on all the field adjacent to the placed word.
	 * @param String coordinate
	 * @param String direction
	 * @param String word
	 * @requires coordinate == {A-O + 1-15} && direction == "h"||"v" && word != null
	 * @ensures return of true or false
	 * @return true||false
	 * @author Maxim
	 */
	public boolean isAdjacent(String coordinate, String direction, String word) {
		
		int[] coords = board.convert(coordinate);
		
		if(direction.equalsIgnoreCase("H")) {
		
			if(!board.isFieldEmpty(coords[0], coords[1] - 1)) {
				return true;
			}
		
			if(!board.isFieldEmpty(coords[0], coords[1] + word.length() + 1)) {
				return true;
			}
			
			for(int i = 0; i < word.length(); i++) {
				if(!board.isFieldEmpty(coords[0] - 1, coords[1] + i)) {
					return true;
				}
			}
			
			for(int i = 0; i < word.length(); i++) {
				if(!board.isFieldEmpty(coords[0] + 1, coords[1] + i)) {
					return true;
				}
			}
		
		}
		
		else if(direction.equalsIgnoreCase("V")) {
			
			if(!board.isFieldEmpty(coords[0] - 1, coords[1])) {
				return true;
			}
		
			if(!board.isFieldEmpty(coords[0]  + word.length() + 1, coords[1])) {
				return true;
			}
			
			for(int i = 0; i < word.length(); i++) {
				if(!board.isFieldEmpty(coords[0] + i, coords[1] - 1)) {
					return true;
				}
			}
			
			for(int i = 0; i < word.length(); i++) {
				if(!board.isFieldEmpty(coords[0] + i, coords[1] + 1)) {
					return true;
				}
			}
		
		}
		
		return false;
		
	}

}
