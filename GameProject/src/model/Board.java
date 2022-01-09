package model;

import java.util.ArrayList;
import java.util.HashSet;

public class Board { //Exceptions!!!
    public static final int SIZE =15;
    private char[][] squares;
    public enum FieldType{NORMAL,TRIPLE_WORD_SCORE, DOUBLE_WORD_SCORE, TRIPLE_LETTER_SCORE, DOUBLE_LETTER_SCORE, CENTER}
    //private ArrayList<Character> tiles;
    private HashSet<String> tripleWordScore;
    private HashSet<String> doubleWordScore;
    private HashSet<String> tripleLetterScore;
    private HashSet<String> doubleLetterScore;

    public Board() {
    	this.squares = new char[SIZE][SIZE];

        //Setting default value
        for (int i=0; i<SIZE;i++){
            for(int j=0; j<SIZE;j++){
                squares[i][j] = ' ';
            }
        }

        filSets();
    }

    public void setTile(char tile, int row, int column) throws IllegalArgumentException {
        if(row < 0 || row > SIZE-1){throw new IllegalArgumentException("Please set a row that is within the numbers 0 and 14");}
        if(column < 0 || column > SIZE-1){throw new IllegalArgumentException("Please set a column that is within the numbers 0 and 14");}
        //Check if Tile is valid
        squares[row][column] = tile;
    }

    public char getTile(int row, int column){
        return squares[row][column];
    }

    public String convert(int row, int column){ //Exception?
        char[] letters = "ABCDEFGHIJKLMNO".toCharArray();
        //if (column > 25) {return null;}
        return Character.toString(letters[column])+row;
    }

    public void filSets(){
        tripleWordScore = new HashSet<>();
        tripleWordScore.add("A1");tripleWordScore.add("H1");tripleWordScore.add("O1");
        tripleWordScore.add("A8");tripleWordScore.add("O8");
        tripleWordScore.add("A15");tripleWordScore.add("H15");tripleWordScore.add("O15");
        doubleWordScore = new HashSet<>();
        doubleWordScore.add("B2");doubleWordScore.add("N2");
        doubleWordScore.add("C3");doubleWordScore.add("M3");
        doubleWordScore.add("D4");doubleWordScore.add("L4");
        doubleWordScore.add("E5");doubleWordScore.add("K5");
        doubleWordScore.add("E11");doubleWordScore.add("K11");
        doubleWordScore.add("D12");doubleWordScore.add("L12");
        doubleWordScore.add("C13");doubleWordScore.add("M13");
        doubleWordScore.add("B14");doubleWordScore.add("N14");
        tripleLetterScore = new HashSet<>();
        tripleLetterScore.add("F2");tripleLetterScore.add("J2");
        tripleLetterScore.add("F2");tripleLetterScore.add("J2");tripleLetterScore.add("F2");tripleLetterScore.add("J2");
        tripleLetterScore.add("F2");tripleLetterScore.add("J2");tripleLetterScore.add("F2");tripleLetterScore.add("J2");
        tripleLetterScore.add("F2");tripleLetterScore.add("J2");

    }

    public FieldType checkField(int row, int column){
        if (tripleWordScore.contains(convert(row,column))){return FieldType.TRIPLE_WORD_SCORE;}
        return FieldType.NORMAL;
    }


}
