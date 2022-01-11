package model;
import java.util.HashSet;
/**
 * Class that represents the board of the game scrabble.
 *
 * The fields are shown to the user in the format Letter|Number and the user uses this format for his/her input
 * The program internally uses the format Number|Number
 *
 * @author Yasin Fahmy
 * @version 11.01.2022
 */

public class Board {
    public static final int SIZE = 15;
    public static final String LETTERS = "ABCDEFGHIJKLMNO";
    private char[][] fields;
    public enum FieldType{NORMAL,TRIPLE_WORD_SCORE, DOUBLE_WORD_SCORE, TRIPLE_LETTER_SCORE, DOUBLE_LETTER_SCORE, CENTER}
    protected HashSet<String> tripleWordScore;
    protected HashSet<String> doubleWordScore;
    protected HashSet<String> tripleLetterScore;
    protected HashSet<String> doubleLetterScore;
    protected HashSet<String> center;

    /**
     * Constructor of the Board Class.
     * Instantiating the matrix of the board.
     * Set all values of the matrix to ' ' --> necessary for the TUI
     * Fills sets with all fields that have a special FieldType
     */
    public Board() {
    	this.fields = new char[SIZE][SIZE];
        reset();
        filSets();
    }

    /**
     * Empties all fields of this board
     * @ensures all fields are empty
     */
    public void reset(){
        for (int i=0; i<SIZE;i++){
            for(int j=0; j<SIZE;j++){
                fields[i][j] = ' ';
            }
        }
    }

    /**
     * Maybe we need such function later
     */
    public Board deepCopy(){
        return null;
    }

    /**
     * @requires (isFieldValid(row, column)) && isTileValid()
     * @param tile - char to indicate the tile that shall be placed
     * @param row - int to indicate row
     * @param column - int to indicate column
     */
    public void setTile(char tile, int row, int column) throws IllegalArgumentException {
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        fields[row][column] = tile;
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return - the tile that is placed on the square
     */
    public char getTile(int row, int column) throws IllegalArgumentException{
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        //if(squares[row][column] == ' '){return null;}
        return fields[row][column];
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return - converted coordinates in the form [Letter|Number]
     */
    public String convert(int row, int column) throws IllegalArgumentException{
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        char[] letters = "ABCDEFGHIJKLMNO".toCharArray();
        return Character.toString(letters[column])+(row+1);
    }

    /**
     * @requires (isFieldValid(field))
     * @param field - textual representation of the coordinates of a field
     * @return - int array that holds [row,column]
     */
    public int[] convert(String field) throws IllegalArgumentException{
        if(!isFieldValid(field)){throw new IllegalArgumentException();}
        int number = Character.getNumericValue(field.charAt(1));

        int[] rowcol = new int[2];
        rowcol[0] = LETTERS.indexOf(field.charAt(0));
        rowcol[1] = number-1;

        return rowcol;
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return - the field type
     */
    public FieldType checkFieldType(int row, int column) throws IllegalArgumentException{
        String field = convert(row,column);

        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}

        if (tripleWordScore.contains(field)){return FieldType.TRIPLE_WORD_SCORE;}
        else if(doubleWordScore.contains(field)){return FieldType.DOUBLE_WORD_SCORE;}
        else if(tripleLetterScore.contains(field)){return FieldType.TRIPLE_LETTER_SCORE;}
        else if(doubleLetterScore.contains(field)){return FieldType.DOUBLE_LETTER_SCORE;}
        else if(center.contains(field)){return FieldType.CENTER;}
        else {return FieldType.NORMAL;}
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return - boolean value depending on if the indicated field actually exists
     */
    public boolean isFieldValid(int row, int column){
        return row >= 0 && row < SIZE && column >= 0 && column < SIZE;
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param field - textual representation of the coordinates of a field
     * @return - boolean value depending on if the indicated field actually exists
     */
    public boolean isFieldValid(String field){
        String letter = String.valueOf(field.charAt(0));
        int number = Character.getNumericValue(field.charAt(1));
        return LETTERS.contains(letter) && number >= 1 && number <= 15;
    }

    /**
     * Returns true if the field reffered to by the (row,column) pair is empty
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return true if field is empty
     */
    public boolean isEmptyField(int row, int column){
        return fields[row][column] == ' ';
    }

    /**
     * Fills all sets with the squares that have a special field type
     */
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
        tripleLetterScore.add("F6");tripleLetterScore.add("B6");tripleLetterScore.add("J6");tripleLetterScore.add("N6");
        tripleLetterScore.add("F10");tripleLetterScore.add("B10");tripleLetterScore.add("J10");tripleLetterScore.add("N10");
        tripleLetterScore.add("F14");tripleLetterScore.add("J14");
        doubleLetterScore = new HashSet<>();
        doubleLetterScore.add("D1");doubleLetterScore.add("L1");
        doubleLetterScore.add("G3");doubleLetterScore.add("I3");
        doubleLetterScore.add("A4");doubleLetterScore.add("H4");doubleLetterScore.add("O4");
        doubleLetterScore.add("C7");doubleLetterScore.add("G7");doubleLetterScore.add("I7");doubleLetterScore.add("M7");
        doubleLetterScore.add("C9");doubleLetterScore.add("G9");doubleLetterScore.add("I9");doubleLetterScore.add("M9");
        doubleLetterScore.add("A12");doubleLetterScore.add("H12");doubleLetterScore.add("O12");
        doubleLetterScore.add("G13");doubleLetterScore.add("I13");
        doubleLetterScore.add("D15");doubleLetterScore.add("L15");
        center = new HashSet<>();
        center.add("H8");
    }

}
