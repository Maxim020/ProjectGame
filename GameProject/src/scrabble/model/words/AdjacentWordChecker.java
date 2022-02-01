package scrabble.model.words;

import java.util.ArrayList;

import scrabble.model.player.PlayerList;
import scrabble.model.Board;
import scrabble.model.player.ComputerPlayer;

public class AdjacentWordChecker {
	
	private ScrabbleWordChecker wordChecker;
	private Board board;
	private WordScoreCounter scoreCounter;
	
	public AdjacentWordChecker(Board board) {
		this.board = board;
		wordChecker = new InMemoryScrabbleWordChecker();
		scoreCounter = new WordScoreCounter(board);
	}
	
	/**
	 * Takes a word, it's start coordinates, and checks if there are letters above and below it.
	 * If there are letters found, they are formed into a word and this word is checked if it is present in the dictionary.
	 * If the method does not return false, all vertically adjacent words are correct.
	 * @param  word
	 * @param  row
	 * @param  column
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
			int score = 0;
			
			if((board.isFieldValid(row - 1, column + i) && board.isFieldEmpty(row - 1, column + i)) || board.isFieldValid(row - 1, column + i) == false) {
				
				while(board.isFieldValid(row + k, column + i) && board.getTile(row + k, column + i) != ' ') {
					charList.add(board.getTile(row + k, column + i));
					k = k + 1;
				}
				
				charList.add(0, word.charAt(i));
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				if(adjacentWord.equals(firstLetter + word.charAt(i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			else if(board.isFieldValid(row - 1, column + i) && !board.isFieldEmpty(row - 1, column + i) && board.isFieldValid(row + 1, column + i) && !board.isFieldEmpty(row + 1, column + i)) {
				
				while(board.isFieldValid(row + j, column + i) && board.getTile(row + j, column + i) != ' ') {
					charList.add(board.getTile(row + j, column + i));
					j = j + 1;
				}
				
				charList.add(0, word.charAt(i));
				
				while(board.isFieldValid(row - k, column + i) && board.getTile(row - k, column + i) != ' ') {
					charList.add(0,board.getTile(row - k, column + i));
					k = k + 1;
				}
				
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				if(adjacentWord.equals(firstLetter + word.charAt(i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			else if((board.isFieldValid(row + 1, column + i) && board.isFieldEmpty(row + 1, column + i)) || board.isFieldValid(row + 1, column + i) == false) {
				
				charList.add(0, word.charAt(i));
				
				while(board.isFieldValid(row - k, column + i) && board.getTile(row - k, column + i) != ' ') {
					charList.add(0,board.getTile(row - k, column + i));
					k = k + 1;
				}
				
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				if(adjacentWord.equals(firstLetter + word.charAt(i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
			}
			
			if(flag != false && !(PlayerList.getInstance().getCurrentPlayer() instanceof ComputerPlayer)) {
				if(board.isFieldValid(row, column + i) && board.isFieldEmpty(row, column + i)) {
					if((board.isFieldValid(row - 1, column + i) && !board.isFieldEmpty(row - 1, column + i)) || (board.isFieldValid(row + 1, column + i) && !board.isFieldEmpty(row + 1, column + i))) {
						if(!adjacentWord.equals(firstLetter)) {
							if(board.isFieldValid(row - k + 1, column + i)) {
							score = scoreCounter.getTotalWordScoreVertical(adjacentWord, row - k + 1, column + i);
							PlayerList.getInstance().getCurrentPlayer().addToScore(score);
							}
						}
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
	 * If the method does not return false, all horizontally adjacent words are correct.
	 * @param  word
	 * @param  row
	 * @param  column
	 * @requires word != null && 0 > row < 15 && 0 > column < 15
	 * @ensures true or false return
	 * @return true || false
	 * @author Maxim
	 */
	
	public boolean checkVerticalWordAdjacency(String word, int column, int row) {
		
		int i = 0;
		boolean flag = true;
		
		while(i != word.toCharArray().length) {
			
			String adjacentWord = "";
			String firstLetter = "";
			ArrayList<Character> charList = new ArrayList<>();
			boolean flag2 = false;
			int j = 1;
			int k = 1;
			int score = 0;
			
			
			if((board.isFieldValid(row + i, column - 1) && board.isFieldEmpty(row + i, column - 1)) || board.isFieldValid(row + i, column - 1) == false) {
				
				while(board.isFieldValid(row + i, column + k) && board.getTile(row + i, column + k) != ' ') {
					charList.add(board.getTile(row + i, column + k));
					k = k + 1;
				}
				
				charList.add(0, word.charAt(i));
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				if(adjacentWord.equals(firstLetter + word.charAt(i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			else if (board.isFieldValid(row + i, column - 1) && !board.isFieldEmpty(row + i, column - 1) && board.isFieldValid(row + i, column + 1) && !board.isFieldEmpty(row + i, column + 1)) {
				
				while(board.isFieldValid(row + i, column + j) && board.getTile(row + i, column + j) != ' ') {
					charList.add(board.getTile(row + i, column + j));
					j = j + 1;
				}
				
				charList.add(0, word.charAt(i));
				
				while(board.isFieldValid(row + i, column - k) && board.getTile(row + i, column - k) != ' ') {
					charList.add(0,board.getTile(row + i, column - k));
					k = k + 1;
				}
				
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				if(adjacentWord.equals(firstLetter + word.charAt(i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
				
			}
			
			else if((board.isFieldValid(row + i, column + 1) && board.isFieldEmpty(row + i, column + 1)) || board.isFieldValid(row + i, column + 1) == false) {
				charList.add(0, word.charAt(i));
				
				while(board.isFieldValid(row + i, column - k) && board.getTile(row + i, column - k) != ' ') {
					charList.add(0,board.getTile(row + i, column - k));
					k = k + 1;
				}
				
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWord = adjacentWord + charList.get(l);
				}
				
				if(adjacentWord.equals(firstLetter + word.charAt(i))) {
					flag2 = true;
				}
				
				if(flag2 == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						flag = false;
					}
				}
			}
			
			
			
			if(flag != false && !(PlayerList.getInstance().getCurrentPlayer() instanceof ComputerPlayer) ) {
				if(board.isFieldValid(row + i, column) && board.isFieldEmpty(row + i, column)) {
					if((board.isFieldValid(row + i, column - 1) && !board.isFieldEmpty(row + i, column - 1)) || (board.isFieldValid(row + i, column + 1) && !board.isFieldEmpty(row + i, column + 1))) {
						if(!adjacentWord.equals(firstLetter)) {
							if(board.isFieldValid(row + i, column - k + 1))
							score = scoreCounter.getTotalWordScoreHorizontal(adjacentWord, row + i, column - k + 1);
							PlayerList.getInstance().getCurrentPlayer().addToScore(score);
						}
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
	 * @param  coordinate
	 * @param  direction
	 * @param  word
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
