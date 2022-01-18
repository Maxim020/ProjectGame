package scrabble.model.words;

import java.util.ArrayList;
import java.util.Collections;

import scrabble.model.Board;
import scrabble.model.exceptions.InvalidWordException;
import scrabble.model.exceptions.WordIsNotAdjacentException;

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
	public boolean checkHorizontalWordAdjacency(String word, int row, int column) {
		
		int i = 0;
		
		boolean flag = true;
		
		while(i != word.toCharArray().length) {
			
			String adjacentWordReversed = "";
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
				
				System.out.println(adjacentWord + 1);
				System.out.println(firstLetter + board.getTile(row, column + i) + 1);
				
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
				
				System.out.println(adjacentWordReversed + 2);
				
				for(int f = 0; f < adjacentWordReversed.length(); f++) {
					char ch = adjacentWordReversed.charAt(f);
					adjacentWord = ch + adjacentWord;
				}
				
				System.out.println(adjacentWord + 2);
				System.out.println(firstLetter + board.getTile(row, column + i) + 2);
				
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
	
public void checkVerticalWordAdjacency(String word, int row, int column) {
		
		int i = 0;
		
		while(i != word.toCharArray().length) {
			
			String adjacentWordReversed = "";
			String adjacentWord = "";
			String firstLetter = "";
			ArrayList<Character> charList = new ArrayList<>();
			boolean flag = false;
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
				
				System.out.println(adjacentWord + 1);
				System.out.println(firstLetter + board.getTile(row + i, column) + 1);
				
				if(adjacentWord.equals(firstLetter + board.getTile(row + i, column))) {
					flag = true;
				}
				
				if(flag == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						throw new WordIsNotAdjacentException();
					}
				}
				
			}
			
			if(flag == false) {
				if(wordChecker.isValidWord(adjacentWord) == null) {
					throw new InvalidWordException();
				}
			}
			else {
				
				while(board.getTile(row + i, column + j) != ' ') {
					charList.add(board.getTile(row + i, column + j));
					j = j + 1;
				}
				
				Collections.reverse(charList);
				
				charList.add(0, board.getTile(row + i, column));
				
				while(board.getTile(row - i, column + k) != ' ') {
					charList.add(0,board.getTile(row - i, column + k));
					k = k + 1;
				}
				
				if(charList.get(1) == ' ') {
					charList.remove(1);
				}
				
				for(int l = 0; l < charList.size(); l++) {
					adjacentWordReversed = adjacentWordReversed + charList.get(l);
				}
				
				System.out.println(adjacentWordReversed + 2);
				
				for(int f = 0; f < adjacentWordReversed.length(); f++) {
					char ch = adjacentWordReversed.charAt(f);
					adjacentWord = ch + adjacentWord;
				}
				
				System.out.println(adjacentWord + 2);
				System.out.println(firstLetter + board.getTile(row + i, column) + 2);
				
				if(adjacentWord.equals(firstLetter + board.getTile(row + i, column))) {
					flag = true;
				}
				
				if(flag == false) {
					if(wordChecker.isValidWord(adjacentWord) == null) {
						throw new WordIsNotAdjacentException();
					}
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
		
		System.out.println(checker.checkHorizontalWordAdjacency("hello", 8, 8));
		
	}
	
	
}
