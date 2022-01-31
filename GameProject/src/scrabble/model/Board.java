package scrabble.model;
import scrabble.model.player.PlayerList;
import scrabble.model.words.WordScoreCounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class that represents the board of the game scrabble.
 *
 * The fields are shown to the user in the format Letter|Number and the user uses this format for his/her input
 * The program internally uses the format Number|Number
 */

public class Board {
    public static final int SIZE = 15;
    public static final String LETTERS = "ABCDEFGHIJKLMNO";
    private char[][] fields;
    public enum FieldType{NORMAL,TRIPLE_WORD_SCORE, DOUBLE_WORD_SCORE, TRIPLE_LETTER_SCORE, DOUBLE_LETTER_SCORE}
    protected HashSet<String> tripleWordScore;
    protected HashSet<String> doubleWordScore;
    protected HashSet<String> tripleLetterScore;
    protected HashSet<String> doubleLetterScore;
    boolean isBoardEmpty;
    boolean isCenterCovered;
    WordScoreCounter wordScoreCounter;
    PlayerList playerList;
    ArrayList<String> playedWords = new ArrayList<>();
    HashMap<String, String> wordCoordinateMap = new HashMap<>();
    HashMap<String, String> wordDirectionMap = new HashMap<>();

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
        isBoardEmpty = true;
        isCenterCovered = false;
        wordScoreCounter = new WordScoreCounter(this);
        playerList = PlayerList.getInstance();
    }

    public void updateBoard(String tiles){
        for(int i=0; i<tiles.length(); i++){
            int row = i/15;
            int col = i%15;
            if(tiles.charAt(i) != ' '){
                FieldType fieldType = checkFieldType(row,col);

                switch (fieldType){
                    case DOUBLE_LETTER_SCORE:
                        doubleLetterScore.remove(convert(row, col));
                        break;
                    case TRIPLE_LETTER_SCORE:
                        tripleLetterScore.remove(convert(row,col));
                        break;
                    case DOUBLE_WORD_SCORE:
                        doubleWordScore.remove(convert(row, col));
                        break;
                    case TRIPLE_WORD_SCORE:
                        tripleWordScore.remove(convert(row, col));
                        break;
                }
            }
            setTile(tiles.charAt(i),row, col);
        }
    }

    /**
     * Empties all fields of this board
     * @ensures all fields are empty
     * @author Yasin Fahmy
     */
    public void reset(){
        for (int i=0; i<SIZE;i++){
            for(int j=0; j<SIZE;j++){
                fields[i][j] = ' ';
            }
        }
    }

    public Board deepCopy(){
        return null;
    }

    /**
     * @requires (isFieldValid(row, column)) && isTileValid()
     * @param tile - char to indicate the tile that shall be placed
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @author Yasin Fahmy
     */
    public void setTile(char tile, int row, int column) throws IllegalArgumentException {
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        fields[row][column] = tile;
    }

    /**
     * @param coordinate coordinate in String format
     * @param direction direction (h: horizontal and v: vertical)
     * @param word the word that is wished to be placed on the board
     * @author Yasin Fahmy
     */
    public void setWord(String coordinate, String direction, String word){
        int[] convertedCoordinate = convert(coordinate);
        int row = convertedCoordinate[0];
        int column = convertedCoordinate[1];

        if(direction.equalsIgnoreCase("H")){
            for (int i=0; i<word.length(); i++){
                setTile(word.charAt(i),row,(column+i));
            }
            playerList.getCurrentPlayer().addToScore(wordScoreCounter.getTotalWordScoreHorizontal(word,row,column));
        }
        else {
            for (int i=0; i<word.length(); i++){
                setTile(word.charAt(i),(row+i),column);
            }
            playerList.getCurrentPlayer().addToScore(wordScoreCounter.getTotalWordScoreVertical(word,row,column));
        }
    }



    /**
     * @param startCoordinate - the field of the first letter of a word
     * @param direction - the direction of the word (h/v)
     * @param word - word that is or will be on the board
     * @return an ArrayList of Strings that represent each field that is covered by a word
     * @author Yasin Fahmy
     */
    public ArrayList<String> fieldsCovered (String startCoordinate, String direction, String word){
        ArrayList<String> result = new ArrayList<>();
        int[] coordinates = convert(startCoordinate);

        if(direction.equalsIgnoreCase("H")){
            for (int i=0; i<word.length(); i++){
                result.add(convert(coordinates[0],coordinates[1]+i));
            }
        } else {
            for (int i=0; i<word.length(); i++){
                result.add(convert(coordinates[0]+i,coordinates[1]));
            }
        }
        return result;
    }

    public boolean doesWordFit(String field, String direction, String word){
        int[] coordinates = convert(field);

        if(direction.equalsIgnoreCase("H")){
            return isFieldValid(coordinates[0],coordinates[1]+word.length()-1);
        }else {
            return isFieldValid(coordinates[0]+word.length()-1,coordinates[1]);
        }
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return - the tile that is placed on the square
     * @author Yasin Fahmy
     */
    public char getTile(int row, int column) throws IllegalArgumentException{
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        return fields[row][column];
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return - converted coordinates in the form [Letter|Number]
     * @author Yasin Fahmy
     */
    public String convert(int row, int column) throws IllegalArgumentException{
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}
        char[] letters = "ABCDEFGHIJKLMNO".toCharArray();
        return Character.toString(letters[column])+(row + 1);
    }

    /**
     * @requires (isFieldValid(field))
     * @param field - textual representation of the coordinates of a field
     * @return - int array that holds [row,column]
     * @author Yasin Fahmy
     */
    public int[] convert(String field) throws IllegalArgumentException{
        if(!isFieldValid(field)){throw new IllegalArgumentException();}
        field = field.toUpperCase();
        int number;
        if (field.length() == 2){
            number = Character.getNumericValue(field.charAt(1));
        } else{
            number = Character.getNumericValue(field.charAt(1)+field.charAt(2));
        }

        int[] rowcol = new int[2];
        rowcol[0] = number-1;
        rowcol[1] = LETTERS.indexOf(field.charAt(0));

        return rowcol;
    }

    @Override
    public String toString(){
        String result = "";

        for(int i=0; i<15; i++){
            for(int j=0; j<15; j++){
                result += fields[i][j];
            }
        }
        return result;
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return - the field type
     * @author Yasin Fahmy
     */
    public FieldType checkFieldType(int row, int column) throws IllegalArgumentException{
        String field = convert(row,column);
        if(!isFieldValid(row, column)){throw new IllegalArgumentException();}

        if (tripleWordScore.contains(field)){return FieldType.TRIPLE_WORD_SCORE;}
        else if(doubleWordScore.contains(field)){return FieldType.DOUBLE_WORD_SCORE;}
        else if(tripleLetterScore.contains(field)){return FieldType.TRIPLE_LETTER_SCORE;}
        else if(doubleLetterScore.contains(field)){return FieldType.DOUBLE_LETTER_SCORE;}
        else {return FieldType.NORMAL;}
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return - boolean value depending on if the indicated field actually exists
     * @author Yasin Fahmy
     */
    public boolean isFieldValid(int row, int column){
        return row >= 0 &&
                row < SIZE &&
                column >= 0 &&
                column < SIZE;
    }

    /**
     * @requires (isFieldValid(row, column))
     * @param field - textual representation of the coordinates of a field
     * @return - boolean value depending on if the indicated field actually exists
     * @author Yasin Fahmy
     */
    public boolean isFieldValid(String field){
        String letter = String.valueOf(field.charAt(0)).toUpperCase();
        int number;
        //System.out.println("field.length(): "+field.length());
        if (field.length() == 2){
            number = Character.getNumericValue(field.charAt(1));
        } else{
            number = Character.getNumericValue(field.charAt(1)+field.charAt(2));
        }
        return LETTERS.contains(letter) && number >= 1 && number <= 15;
    }

    /**
     * Returns true if the field reffered to by the (row,column) pair is empty
     * @requires (isFieldValid(row, column))
     * @param row - int to indicate row
     * @param column - int to indicate column
     * @return true if field is empty
     * @author Yasin Fahmy
     */
    public boolean isFieldEmpty(int row, int column){
        return fields[row][column] == ' ';
    }

    /**
     * Fills all sets with the squares that have a special field type
     * @author Yasin Fahmy
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
        doubleWordScore.add("H8");
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
        doubleLetterScore.add("D8");doubleLetterScore.add("L8");
        doubleLetterScore.add("C9");doubleLetterScore.add("G9");doubleLetterScore.add("I9");doubleLetterScore.add("M9");
        doubleLetterScore.add("A12");doubleLetterScore.add("H12");doubleLetterScore.add("O12");
        doubleLetterScore.add("G13");doubleLetterScore.add("I13");
        doubleLetterScore.add("D15");doubleLetterScore.add("L15");
    }

    public boolean isBoardEmpty() {
        return isBoardEmpty;
    }

    public void setBoardEmpty(boolean boardEmpty) {
        isBoardEmpty = boardEmpty;
    }

    public void setCenterCovered(boolean centerCovered) {
        isCenterCovered = centerCovered;
    }

    public boolean isCenterCovered() {
        return isCenterCovered;
    }

    public HashSet<String> getTripleWordScore() {
        return tripleWordScore;
    }

    public HashSet<String> getDoubleWordScore() {
        return doubleWordScore;
    }

    public char[][] getFields() {
        return fields;
    }

    public HashSet<String> getTripleLetterScore() {
        return tripleLetterScore;
    }

    public HashSet<String> getDoubleLetterScore() {
        return doubleLetterScore;
    }
    
    /** Added by Maxim, might be shit
     * Still in use? - Yasin*/
    public void addPlayedWords(String coordinate, String direction, String word) {
    	playedWords.add(word);
    	wordCoordinateMap.put(word, coordinate);
    	wordDirectionMap.put(word, direction);
    }
    
    public ArrayList<String> getPlayedWords(){
    	return playedWords;
    }
    
    public HashMap<String, String> getWordCoordinateMap(){
    	return wordCoordinateMap;
    }
    
    public HashMap<String, String> getWordDirectionMap(){
    	return wordDirectionMap;
    }
    
}
