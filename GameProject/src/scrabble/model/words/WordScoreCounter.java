package scrabble.model.words;

import scrabble.model.Board;
import scrabble.model.Board.FieldType;
import scrabble.model.letters.LetterScoreChecker;
import scrabble.model.letters.TileMultiplierChecker;

import java.util.ArrayList;

public class WordScoreCounter {
	
	//Attributes
	private LetterScoreChecker letterChecker;
	private TileMultiplierChecker multiplierChecker;
	private Board board;
	
	//Constructor
	/**
	 * WordScoreCounter is constructed using the board as its argument
	 * @param board
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
	 * @param word
	 * @param row
	 * @param columnOfFirstLetter
	 * @return int score for entire word
	 * @requires both checkers and board to be initialized
	 * @ensures return of proper score and removal of used multiplier tiles
	 * @author Maxim
	 */
	public int getTotalWordScoreHorizontal(String word, int row, int columnOfFirstLetter){
		int score = 0;
		int finalWordMultiplier = 1;
		ArrayList<Character> letterList = new ArrayList<>();
		ArrayList<Integer> listOfWordMultipliers = new ArrayList<>();
		
		for(int i = 0; i < word.toCharArray().length; i++) {
			letterList.add(word.toCharArray()[i]);
		}
		
		for(int i = 0; i < letterList.size(); i++) {
			
			if(board.checkFieldType(row, columnOfFirstLetter + i).equals(FieldType.DOUBLE_LETTER_SCORE) || board.checkFieldType(row, columnOfFirstLetter + i).equals(FieldType.TRIPLE_LETTER_SCORE)) {
				score = score + (letterChecker.scoreChecker(letterList.get(i)) * multiplierChecker.letterMultiplierChecker(board.checkFieldType(row, columnOfFirstLetter + i)));
			}
			
			else {
				score = score + letterChecker.scoreChecker(letterList.get(i));
			}
			
			if(board.getDoubleLetterScore().contains(board.convert(row, columnOfFirstLetter + i))) {
				board.getDoubleLetterScore().remove(board.convert(row, columnOfFirstLetter + i));
			}
			if(board.getTripleLetterScore().contains(board.convert(row, columnOfFirstLetter + i))) {
				board.getTripleLetterScore().remove(board.convert(row, columnOfFirstLetter + i));
			}
		}
		
		for(int i = 0; i < letterList.size(); i++) {
			
			if(board.checkFieldType(row, columnOfFirstLetter + i).equals(FieldType.DOUBLE_WORD_SCORE) || board.checkFieldType(row, columnOfFirstLetter + i).equals(FieldType.TRIPLE_WORD_SCORE)) {
				
			listOfWordMultipliers.add(multiplierChecker.wordMultiplierChecker(board.checkFieldType(row, columnOfFirstLetter + i)));
			
			if(board.getDoubleWordScore().contains(board.convert(row, columnOfFirstLetter + i))) {
				board.getDoubleWordScore().remove(board.convert(row, columnOfFirstLetter + i));
			}
			if(board.getTripleWordScore().contains(board.convert(row, columnOfFirstLetter + i))) {
				board.getTripleWordScore().remove(board.convert(row, columnOfFirstLetter + i));
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
	 * @param word
	 * @param rowOfFirstLetter
	 * @param column
	 * @return int score for entire word
	 * @requires both checkers and board to be initialized
	 * @ensures return of proper score and removal of used multiplier tiles
	 * @author Maxim
	 */
	public int getTotalWordScoreVertical(String word, int rowOfFirstLetter, int column){
		int score = 0;
		int finalWordMultiplier = 1;
		ArrayList<Character> letterList = new ArrayList<>();
		ArrayList<Integer> listOfWordMultipliers = new ArrayList<>();
		
		for(int i = 0; i < word.toCharArray().length; i++) {
			letterList.add(word.toCharArray()[i]);
		}
		
		for(int i = 0; i < letterList.size(); i++) {
			
			if(board.checkFieldType(rowOfFirstLetter + i, column).equals(FieldType.DOUBLE_LETTER_SCORE) || board.checkFieldType(rowOfFirstLetter + i, column).equals(FieldType.TRIPLE_LETTER_SCORE)) {
				score = score + (letterChecker.scoreChecker(letterList.get(i)) * multiplierChecker.letterMultiplierChecker(board.checkFieldType(rowOfFirstLetter + i, column)));
			}
			
			else {
				score = score + letterChecker.scoreChecker(letterList.get(i));
			}
			
			if(board.getDoubleLetterScore().contains(board.convert(rowOfFirstLetter + i, column))) {
				board.getDoubleLetterScore().remove(board.convert(rowOfFirstLetter + i, column));
			}
			
			if(board.getTripleLetterScore().contains(board.convert(rowOfFirstLetter + i, column))) {
				board.getTripleLetterScore().remove(board.convert(rowOfFirstLetter + i, column));
			}
		}
		
		for(int i = 0; i < letterList.size(); i++) {
			
			if(board.checkFieldType(rowOfFirstLetter + i, column).equals(FieldType.DOUBLE_WORD_SCORE) || board.checkFieldType(rowOfFirstLetter + i, column).equals(FieldType.TRIPLE_WORD_SCORE)) {
			listOfWordMultipliers.add(multiplierChecker.wordMultiplierChecker(board.checkFieldType(rowOfFirstLetter + i, column)));
			}
			
			if(board.getDoubleWordScore().contains(board.convert(rowOfFirstLetter + i, column))) {
				board.getDoubleWordScore().remove(board.convert(rowOfFirstLetter + i, column));
			}
			if(board.getTripleWordScore().contains(board.convert(rowOfFirstLetter + i, column))) {
				board.getTripleWordScore().remove(board.convert(rowOfFirstLetter + i, column));
			}
			
		}
		
		for(int i = 0; i < listOfWordMultipliers.size(); i++) {
			
			finalWordMultiplier = finalWordMultiplier * listOfWordMultipliers.get(i);
		}
		
		return score * finalWordMultiplier;
	 }
	}
	