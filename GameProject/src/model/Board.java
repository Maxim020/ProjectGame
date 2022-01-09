package model;
import java.util.HashSet;

public class Board {
    /**
     * @provides A Class that represents the board of the game scrabble
     * @author Yasin Fahmy
     */
    public static final int SIZE =15;
    private char[][] squares;
    public enum FieldType{NORMAL,TRIPLE_WORD_SCORE, DOUBLE_WORD_SCORE, TRIPLE_LETTER_SCORE, DOUBLE_LETTER_SCORE, CENTER}
    //A set holds all squares that belong to a special type of field
    protected HashSet<String> tripleWordScore; protected HashSet<String> doubleWordScore;
    protected HashSet<String> tripleLetterScore; protected HashSet<String> doubleLetterScore;
    protected HashSet<String> center;

    /**
     * Constructor of the Board Class
     */
    public Board() {
    	this.squares = new char[SIZE][SIZE];
        //Setting a space as the default value
        for (int i=0; i<SIZE;i++){
            for(int j=0; j<SIZE;j++){
                squares[i][j] = ' ';
            }
        }
        //Fills all sets with squares
        filSets();
    }

    /**
     * @param tile - valid char to indicate the tile that shall be placed
     * @param row - valid int to indicate row
     * @param column - valid int to indicate column
     * @throws IllegalArgumentException
     */
    public void setTile(char tile, int row, int column) throws IllegalArgumentException {
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        squares[row][column] = tile;
    }

    /**
     * @param row -  valid int to indicate row
     * @param column -  valid int to indicate column
     * @return - the tile that is placed on the square with row/columns as coordinates
     * @throws IllegalArgumentException
     */
    public char getTile(int row, int column) throws IllegalArgumentException{
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        //if(squares[row][column] == ' '){return null;}
        return squares[row][column];
    }

    /**
     * @param row - valid int to indicate row
     * @param column - valid int to indicate column
     * @return - converted coordinates in the form [Letter|Number]
     * @throws IllegalArgumentException
     */
    public String convert(int row, int column) throws IllegalArgumentException{
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        char[] letters = "ABCDEFGHIJKLMNO".toCharArray();
        return Character.toString(letters[column])+(row+1);
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

    /**
     * @param row - valid int to indicate row
     * @param column - valid int to indicate column
     * @return - the field type of a square
     * @throws IllegalArgumentException
     */
    public FieldType checkField(int row, int column) throws IllegalArgumentException{
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        if (tripleWordScore.contains(convert(row,column))){return FieldType.TRIPLE_WORD_SCORE;}
        else if(doubleWordScore.contains(convert(row,column))){return FieldType.DOUBLE_WORD_SCORE;}
        else if(tripleLetterScore.contains(convert(row,column))){return FieldType.TRIPLE_LETTER_SCORE;}
        else if(doubleLetterScore.contains(convert(row,column))){return FieldType.DOUBLE_LETTER_SCORE;}
        else if(center.contains(convert(row,column))){return FieldType.CENTER;}
        else {return FieldType.NORMAL;}
    }

    /**
     * @param row - valid int to indicate row
     * @param column - valid int to indicate column
     * @return - boolean value depending on if the indicated field actually exists
     */
    public boolean isFieldValid(int row, int column){
        return row >= 0 && row < SIZE && column >= 0 && column < SIZE;
    }


}
