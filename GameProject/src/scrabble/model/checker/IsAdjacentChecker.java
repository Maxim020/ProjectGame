package scrabble.model.checker;

import scrabble.model.Board;

public class IsAdjacentChecker {
	
	private Board board;
	
	public IsAdjacentChecker(Board board) {
		this.board = board;
	}
	
	/**
	 * Checks if there is a letter present on at least one of the fields adjacent to the placed word.
	 * @param coordinate
	 * @param direction
	 * @param word
	 * @requires coordinate == {A-O + 1-15} && direction == "h"||"v" && word != null
	 * @ensures return of true or false
	 * @return true||false
	 * @author Maxim
	 */
	public boolean isAdjacent(String coordinate, String direction, String word) {
		
		int[] coords = board.convert(coordinate);
		
		for(int i = 0; i < board.getPlayedWords().size(); i++) {
			if(word.contains(board.getPlayedWords().get(i))) {
				return true;
			}
		}
		
		if(direction.equalsIgnoreCase("H")) {
		
			if(board.isFieldValid(coords[0], coords[1] - 1) && !board.isFieldEmpty(coords[0], coords[1] - 1)) {
				return true;
			}
		
			if(board.isFieldValid(coords[0], coords[1] + word.length()) && !board.isFieldEmpty(coords[0], coords[1] + word.length())) {
				return true;
			}
			
			for(int i = 0; i < word.length(); i++) {
				if(board.isFieldValid(coords[0] - 1, coords[1] + i) && !board.isFieldEmpty(coords[0] - 1, coords[1] + i)) {
					return true;
				}
			}
			
			for(int i = 0; i < word.length(); i++) {
				if(board.isFieldValid(coords[0] + 1, coords[1] + i) && !board.isFieldEmpty(coords[0] + 1, coords[1] + i)) {
					return true;
				}
			}
		
		}
		
		else if(direction.equalsIgnoreCase("V")) {
			
			if(board.isFieldValid(coords[0] - 1, coords[1]) && !board.isFieldEmpty(coords[0] - 1, coords[1])) {
				return true;
			}
		
			if(board.isFieldValid(coords[0] + word.length(), coords[1]) && !board.isFieldEmpty(coords[0]  + word.length(), coords[1])) {
				return true;
			}
			
			for(int i = 0; i < word.length(); i++) {
				if(board.isFieldValid(coords[0] + i, coords[1] - 1) && !board.isFieldEmpty(coords[0] + i, coords[1] - 1)) {
					return true;
				}
			}
			
			for(int i = 0; i < word.length(); i++) {
				if(board.isFieldValid(coords[0] + i, coords[1] + 1) && !board.isFieldEmpty(coords[0] + i, coords[1] + 1)) {
					return true;
				}
			}
		
		}
		
		return false;
		
	}

}
