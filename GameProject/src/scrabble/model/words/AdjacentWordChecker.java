package scrabble.model.words;

import java.util.ArrayList;
import java.util.Collections;

import scrabble.model.Board;

public class AdjacentWordChecker {
	
	//Attributes
	private ScrabbleWordChecker wordChecker;
	private Board board;
	
	//Constructor
	public AdjacentWordChecker(Board board) {
		this.board = board;
		wordChecker = new InMemoryScrabbleWordChecker();
	}
	
	//Methods
	/**
	 * Takes a word, it's start coordinates, and checks if there are letters above and below it.
	 * If there are letters found, they are formed into a word and this word is checked if it is present in the dictionary.
	 * If the method does not return false, all vertically adjacent words are correct.
	 * @param String word
	 * @param int row
	 * @param int column
	 * @requires word != null && 0 > row < 15 && 0 > column < 15
	 * @ensures true or false return
	 * @return true || false
	 * @author Maxim
	 */
	public boolean checkHorizontalWordAdjacency(String word, int row, int column) {
		
		int i = 0;
		
		boolean flag = true;
		
		while(i != word.toCharArray().length) {
			
			String adjacentWord = "";
			String firstLetter = "";
			boolean flag2 = false;
			ArrayList<Character> charList = new ArrayList<>();
			int j = 1;
			int k = 1;
			
			if(board.isFieldEmpty(row - 1, column + i)) {
				
				while(board.getTile(row + k, column + i) != ' ') {
					charList.add(board.getTile(row + k, column + i));
					k = k + 1;
				}
				
				charList.add(0, board.getTile(row, column + i));
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				System.out.println(adjacentWord);
				
				if(adjacentWord.equals(firstLetter + board.getTile(row, column + i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			else {
				
				while(board.getTile(row + j, column + i) != ' ') {
					charList.add(board.getTile(row + j, column + i));
					j = j + 1;
				}
				
				charList.add(0, board.getTile(row, column + i));
				
				while(board.getTile(row - k, column + i) != ' ') {
					charList.add(0,board.getTile(row - k, column + i));
					k = k + 1;
				}
				
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				System.out.println("Here");
				System.out.println(adjacentWord);
				
				
				if(adjacentWord.equals(firstLetter + board.getTile(row, column + i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			i = i + 1;
		}
		
		return flag;
	}
	
	/**
	 * Takes a word, it's start coordinates, and checks if there are letters right and left of it.
	 * If there are letters found, they are formed into a word and this word is checked if it is present in the dictionary.
	 * If the method does not return false, all vertically adjacent words are correct.
	 * @param String word
	 * @param int row
	 * @param int column
	 * @requires word != null && 0 > row < 15 && 0 > column < 15
	 * @ensures true or false return
	 * @return true || false
	 * @author Maxim
	 */
	
	public boolean checkVerticalWordAdjacency(String word, int row, int column) {
		
		int i = 0;
		boolean flag = true;
		
		while(i != word.toCharArray().length) {
			
			String adjacentWord = "";
			String firstLetter = "";
			ArrayList<Character> charList = new ArrayList<>();
			boolean flag2 = false;
			int j = 1;
			int k = 1;
			
			if(board.isFieldEmpty(row + i, column - 1)) {
				
				while(board.getTile(row + i, column + k) != ' ') {
					charList.add(board.getTile(row + i, column + k));
					k = k + 1;
				}
				
				charList.add(0, board.getTile(row + i, column));
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				
				if(adjacentWord.equals(firstLetter + board.getTile(row + i, column))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			else {
				
				while(board.getTile(row + i, column + j) != ' ') {
					charList.add(board.getTile(row + i, column + j));
					j = j + 1;
				}
				
				charList.add(0, board.getTile(row + i, column));
				
				while(board.getTile(row + i, column - k) != ' ') {
					charList.add(0,board.getTile(row + i, column - k));
					k = k + 1;
				}
				
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				
				if(adjacentWord.equals(firstLetter + board.getTile(row + i, column))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			i = i + 1;
		}
		return flag;
	}
	
	/**
	 * Adapted method for (coordinate, direction, word) format.
	 * Combines Horizontal and Vertical adjacent word checkers.
	 * Calls one or the other depending on direction.
	 * @param String coordinate
	 * @param String direction
	 * @param String word
	 * @requires coordinate == {A-O + 1-15} && direction == "h"||"v" && word != null
	 * @ensures return of true or false
	 * @return true || false
	 */
	public boolean areAdjacentWordsValid(String coordinate, String direction, String word) {
		
		int [] coords = board.convert(coordinate);
		
		if(direction.equalsIgnoreCase("H")) {
			return checkHorizontalWordAdjacency(word, coords[0], coords[1]);
		}
		else {
			return checkVerticalWordAdjacency(word, coords[0], coords[1]);
		}
		
	}
	
	
}
