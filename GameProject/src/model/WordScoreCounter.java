package model;

import java.util.ArrayList;
import java.util.Collections;

public class WordScoreCounter {
	
	//Attributes
	private LetterScoreChecker letterChecker;
	private TileMultiplierChecker multiplierChecker;
	private Board board;
	
	//Constructor
	/**
	 * WordScoreCounter is constructed using the board as its argument
	 * @param Board board
	 * @author Maxim
	 */
	public WordScoreCounter(Board board) {
		this.letterChecker = new LetterScoreChecker();
		this.multiplierChecker = new TileMultiplierChecker();
		this.board = board;
	}
	
	//Methods
	
	/**
	 * !!CHECKS SCORE FOR HORIZONTAL WORDS!!
	 * Takes the inserted letters as a character list. Every letter from this list has it's score checked and multiplied either by 1, 2 or 3.
	 * The score total is calculated from the first for cycle. Also in this cycle, all multiplier tiles that have been used are removed. In the next for cycle, every tile is checked again but with a word multiplier checker.
	 * A list is created from all the word multipliers, this is necessary if there are two word multipliers present, and the list elements from the list are multiplied with each other to form the final word multiplier.
	 * Again in this cycle, all word multiplier tiles are removed and switched to normal tiles.
	 * @param ArrayList<Character> letterList
	 * @param int row
	 * @param int columnOfFirstLetter
	 * @return int score for entire word
	 * @requires both checkers and board to be initialized
	 * @ensures return of proper score and removal of used multiplier tiles
	 * @author Maxim
	 */
	public int getTotalWordScoreHorizontal(ArrayList<Character> letterList, int row, int columnOfFirstLetter){
		int score = 0;
		int finalWordMultiplier = 1;
		ArrayList<Integer> listOfWordMultipliers = new ArrayList<>();
		
		for(int i = 0; i < letterList.size(); i++) {
			
			score = score + (letterChecker.scoreChecker(letterList.get(i)) * multiplierChecker.letterMultiplierChecker(board.checkField(row, columnOfFirstLetter + i)));
			
			if(board.doubleLetterScore.contains(board.convert(row, columnOfFirstLetter + i))) {
				board.doubleLetterScore.remove(board.convert(row, columnOfFirstLetter + i));
			}
			if(board.tripleLetterScore.contains(board.convert(row, columnOfFirstLetter + i))) {
				board.tripleLetterScore.remove(board.convert(row, columnOfFirstLetter + i));
			}
		}
		
		for(int i = 0; i < letterList.size(); i++) {
			
			if(multiplierChecker.wordMultiplierChecker(board.checkField(row, columnOfFirstLetter + i)) > 1) {
			listOfWordMultipliers.add(multiplierChecker.wordMultiplierChecker(board.checkField(row, columnOfFirstLetter + i)));
			
			if(board.doubleWordScore.contains(board.convert(row, columnOfFirstLetter + i))) {
				board.doubleWordScore.remove(board.convert(row, columnOfFirstLetter + i));
			}
			if(board.tripleWordScore.contains(board.convert(row, columnOfFirstLetter + i))) {
				board.tripleWordScore.remove(board.convert(row, columnOfFirstLetter + i));
			}
			
			if(board.center.contains(board.convert(row, columnOfFirstLetter + i))) {
				board.center.remove(board.convert(row, columnOfFirstLetter + i));
			}
			
			}
		}
		
		for(int i = 0; i < listOfWordMultipliers.size(); i++) {
			
			finalWordMultiplier = finalWordMultiplier * listOfWordMultipliers.get(i);
		}
		
		return score * finalWordMultiplier;
		
	}
	
	/**
	 * !!CHECKS SCORE FOR VERTICAL WORDS!!
	 * Takes the inserted letters as a character list. Every letter from this list has it's score checked and multiplied either by 1, 2 or 3.
	 * The score total is calculated from the first for cycle. Also in this cycle, all multiplier tiles that have been used are removed. In the next for cycle, every tile is checked again but with a word multiplier checker.
	 * A list is created from all the word multipliers, this is necessary if there are two word multipliers present, and the list elements from the list are multiplied with each other to form the final word multiplier.
	 * Again in this cycle, all word multiplier tiles are removed and switched to normal tiles.
	 * @param ArrayList<Character> letterList
	 * @param int rowOfFirstLetter
	 * @param int column
	 * @return int score for entire word
	 * @requires both checkers and board to be initialized
	 * @ensures return of proper score and removal of used multiplier tiles
	 * @author Maxim
	 */
	public int getTotalWordScoreVertical(ArrayList<Character> letterList, int rowOfFirstLetter, int column){
		int score = 0;
		int finalWordMultiplier = 1;
		ArrayList<Integer> listOfWordMultipliers = new ArrayList<>();
		
		for(int i = 0; i < letterList.size(); i++) {
			
			score = score + (letterChecker.scoreChecker(letterList.get(i)) * multiplierChecker.letterMultiplierChecker(board.checkField(rowOfFirstLetter + i, column)));
			
			if(board.doubleLetterScore.contains(board.convert(rowOfFirstLetter + i, column))) {
				board.doubleLetterScore.remove(board.convert(rowOfFirstLetter + i, column));
			}
			
			if(board.tripleLetterScore.contains(board.convert(rowOfFirstLetter + i, column))) {
				board.tripleLetterScore.remove(board.convert(rowOfFirstLetter + i, column));
			}
		}
		
		for(int i = 0; i < letterList.size(); i++) {
			
			if(multiplierChecker.wordMultiplierChecker(board.checkField(rowOfFirstLetter + i, column)) > 1) {
			listOfWordMultipliers.add(multiplierChecker.wordMultiplierChecker(board.checkField(rowOfFirstLetter + i, column)));
			}
			
			if(board.doubleWordScore.contains(board.convert(rowOfFirstLetter + i, column))) {
				board.doubleWordScore.remove(board.convert(rowOfFirstLetter + i, column));
			}
			if(board.tripleWordScore.contains(board.convert(rowOfFirstLetter + i, column))) {
				board.tripleWordScore.remove(board.convert(rowOfFirstLetter + i, column));
			}
			
			if(board.center.contains(board.convert(rowOfFirstLetter + i, column))) {
				board.center.remove(board.convert(rowOfFirstLetter + i, column));
			}
		}
		
		for(int i = 0; i < listOfWordMultipliers.size(); i++) {
			
			finalWordMultiplier = finalWordMultiplier * listOfWordMultipliers.get(i);
		}
		
		return score * finalWordMultiplier;
		
	}

}
