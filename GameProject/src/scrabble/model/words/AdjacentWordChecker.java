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
	 * @requires word != null && 0 >= row < 15 && 0 >= column < 15
	 * @ensures true or false return
	 * @return true || false
	 * @author Maxim
	 */
	public boolean checkHorizontalWordAdjacency(String word, int column, int row) {
		
		int i = 0;
		
		boolean flag = true;
		
		while(i != word.toCharArray().length) {
			
			String adjacentWord = "";
			String firstLetter = "";
			boolean flag2 = false;
			ArrayList<Character> charList = new ArrayList<>();
			int j = 1;
			int k = 1;
			
			if(board.isFieldValid(row - 1, column + i) && board.isFieldEmpty(row - 1, column + i)) {
				
				while(board.getTile(row + k, column + i) != ' ') {
					charList.add(board.getTile(row + k, column + i));
					k = k + 1;
				}
				
				charList.add(0, board.getTile(row, column + i));
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				if(adjacentWord.equals(firstLetter + board.getTile(row, column + i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			else if(board.isFieldValid(row - 1, column + i) && !board.isFieldEmpty(row - 1, column + i) && board.isFieldValid(row - 1, column + i) && !board.isFieldEmpty(row - 1, column + i)) {
				
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
		
		int l = 1;
		int m = 1;
		ArrayList<Character> charList1 = new ArrayList<>();
		ArrayList<Character> charList2 = new ArrayList<>();
		
		while(board.isFieldValid(row, column - l) && !board.isFieldEmpty(row, column - l)) {
			charList1.add(board.getTile(row, column - l));
			l = l + 1;
		}
		
		if(!charList1.isEmpty()) {
			
			String s = word;
		
			for(int o = 0; o < charList1.size(); o++) {
				s = charList1.get(o)  + s;
			}
		
			if(wordChecker.isValidWord(s) == null) {
				flag = false;
			}
		}
		
		while(board.isFieldValid(row, column + word.length() - 1 + m) && !board.isFieldEmpty(row, column + word.length() - 1 + m)) {
			charList2.add(board.getTile(row, column + word.length() - 1 + m));
			m = m + 1;
		}
		
			if(!charList2.isEmpty()) {
				
				String s = word;
		
				for(int p = 0; p < charList2.size(); p++) {
					s = s + charList2.get(p);
				}
		
			if(wordChecker.isValidWord(s) == null) {
				flag = false;
			}
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
			
			if(board.isFieldValid(row + i, column - 1) && board.isFieldEmpty(row + i, column - 1)) {
				
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
			
			else if (board.isFieldValid(row + i, column - 1) && !board.isFieldEmpty(row + i, column - 1) && board.isFieldValid(row + i, column + 1) && !board.isFieldEmpty(row + i, column + 1)) {
				
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
		
		int l = 1;
		int m = 1;
		ArrayList<Character> charList1 = new ArrayList<>();
		ArrayList<Character> charList2 = new ArrayList<>();
		
		while(board.isFieldValid(row - l, column) && !board.isFieldEmpty(row - l, column)) {
			charList1.add(board.getTile(row - l, column));
			l = l + 1;
		}
		
		if(!charList1.isEmpty()) {
			
			String s = word;
		
			for(int o = 0; o < charList1.size(); o++) {
				s = charList1.get(o)  + s;
			}
		
			if(wordChecker.isValidWord(s) == null) {
				flag = false;
			}
		}
		
		while(board.isFieldValid(row  + word.length() - 1 + m , column) && !board.isFieldEmpty(row  + word.length() - 1 + m, column)) {
			charList2.add(board.getTile(row  + word.length() - 1 + m , column));
			m = m + 1;
		}
		
			if(!charList2.isEmpty()) {
				
				String s = word;
		
				for(int p = 0; p < charList2.size(); p++) {
					s = s + charList2.get(p);
				}
		
			if(wordChecker.isValidWord(s) == null) {
				flag = false;
			}
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
			return checkHorizontalWordAdjacency(word, coords[1], coords[0]);
		}
		else {
			return checkVerticalWordAdjacency(word, coords[1], coords[0]);
		}
		
	}
	
	
}
