package scrabble.model.words;

import java.util.ArrayList;
import java.util.Collections;

import scrabble.model.Board;
import scrabble.model.exceptions.WordDoesNotExist;

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
	 * If the method does not throw an exception, all vertically adjacent words are correct.
	 * @return
	 */
	public void checkHorizontalWordAdjacency(String word, int row, int column) {
		
		int i = 0;
		
		while(i != word.toCharArray().length) {
			
			String adjacentWordReversed = "";
			String adjacentWord = "";
			String firstLetter = "";
			ArrayList<Character> charList = new ArrayList<>();
			boolean flag = false;
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
				System.out.println(firstLetter + board.getTile(row, column + i));
				
				if(adjacentWord.equals(firstLetter + board.getTile(row, column + i))) {
					flag = true;
				}
				
				if(flag == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						throw new WordDoesNotExist();
					}
				}
				
			}
			
			else {
				
				while(board.getTile(row + j, column + i) != ' ') {
					charList.add(board.getTile(row + j, column + i));
					j = j + 1;
				}
				
				Collections.reverse(charList);
				
				charList.add(0, board.getTile(row, column + i));
				
				while(board.getTile(row - k, column + i) != ' ') {
					charList.add(0,board.getTile(row - k, column + i));
					k = k + 1;
				}
				
				if(charList.get(1) == ' ') {
					charList.remove(1);
				}
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWordReversed = adjacentWordReversed + charList.get(l);
				}
				
				System.out.println(adjacentWordReversed);
				
				for(int f = 0; f < adjacentWordReversed.length(); f++) {
					char ch = adjacentWordReversed.charAt(f);
					adjacentWord = ch + adjacentWord;
				}
				
				System.out.println(adjacentWord);
				System.out.println(firstLetter + board.getTile(row, column + i));
				
				if(adjacentWord.equals(firstLetter + board.getTile(row, column + i))) {
					flag = true;
				}
				
				if(flag == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						throw new WordDoesNotExist();
					}
				}
				
			}
			
			i = i + 1;
		}
		
	}
	
public void checkVerticalWordAdjacency(String word, int row, int column) {
		
		int i = 0;
		int j = 1;
		int k = 1;
		
		
		while(i != word.toCharArray().length) {
			
			String adjacentWord = "";
			String firstLetter = "";
			ArrayList<Character> charList = new ArrayList<>();
			boolean flag = false;
			
			while(board.getTile(row + j, column) != ' ') {
				charList.add(board.getTile(row + j, column));
				j = j + 1;
			}
			
			charList.add(0, board.getTile(row, column + i));
			
			while(board.getTile(row - k, column) != ' ') {
				charList.add(0,board.getTile(row - j, column));
				k = k + 1;
			}
			
			for(int l = 0; l < charList.size(); l++) {
				adjacentWord = adjacentWord + charList.get(l);
			}
			
			if(adjacentWord.equals(firstLetter + board.getTile(row, column + i))) {
				flag = true;
			}
			
			if(flag == false) {
				if(wordChecker.isValidWord(adjacentWord) == null) {
					throw new WordDoesNotExist();
				}
			}
			
			i = i + 1;
		}
		
	}
	
	public static void main(String[] args) {
		
		Board board = new Board();
		AdjacentWordChecker checker = new AdjacentWordChecker(board);
		
		board.setTile('h', 8, 8); board.setTile('e', 8, 9); board.setTile('l', 8, 10); board.setTile('l', 8, 11); board.setTile('o', 8, 12);
		board.setTile('o', 7, 10); board.setTile('a', 6, 10); board.setTile('n', 5, 10); board.setTile('r', 9, 10); board.setTile('e', 10, 10);
		board.setTile('i', 9, 8);
		
		checker.checkHorizontalWordAdjacency("hello", 8, 8);
		
	}
	
	
}
