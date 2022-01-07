package model;

import java.util.ArrayList;

public class GameBoard {
	
    private char[][] grid;
    private ArrayList<Character> tiles;
    public enum PremiumSquare {DOUBLE_LETTER_SCORE,TRIPLE_LETTER_SCORE, DOUBLE_WORD_SCORE, TRIPLE_WORD_SCORE}
    
    public GameBoard() {
    	this.grid = new char[15][15];
    }
    
    
}
