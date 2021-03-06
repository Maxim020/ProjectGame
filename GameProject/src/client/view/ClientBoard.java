package client.view;

import scrabble.model.Board;

public class ClientBoard {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private final Board board;

    public ClientBoard(Board board){
        this.board = board;
    }

    /**
     * @requires valid field
     * @param row - valid int to indicate row
     * @param column - valid int to indicate column
     * @return - The squares of the board that are the fields to place tiles on
     * @author Yasin Fahmy
     */
    public static String printSquare(int row, int column, Board board) throws IllegalArgumentException{
        if(!board.isFieldValid(row, column)){throw new IllegalArgumentException();}

        if (board.checkFieldType(row, column).equals(Board.FieldType.TRIPLE_WORD_SCORE)){
            return "|"+ANSI_RED_BACKGROUND+" "+board.getTile(row,column)+" "+ANSI_RESET;
        }
        else if(board.checkFieldType(row, column).equals(Board.FieldType.DOUBLE_WORD_SCORE)){
            return "|"+ANSI_PURPLE_BACKGROUND+" "+board.getTile(row,column)+" "+ANSI_RESET;
        }
        else if(board.checkFieldType(row, column).equals(Board.FieldType.TRIPLE_LETTER_SCORE)){
            return "|"+ANSI_BLUE_BACKGROUND+" "+board.getTile(row,column)+" "+ANSI_RESET;
        }
        else if(board.checkFieldType(row, column).equals(Board.FieldType.DOUBLE_LETTER_SCORE)){
            return "|"+ANSI_CYAN_BACKGROUND+" "+board.getTile(row,column)+" "+ANSI_RESET;
        }
        else {
            return "| "+board.getTile(row,column)+" ";
        }
    }

    /**
     * @param line - The number of the line, starting from 1
     * @return - A line of the board (Upper bound + Fields)
     * @author Yasin Fahmy
     */
    public String printLine(int line){
        int row = line-1;
        StringBuilder result = new StringBuilder();
        result.append(" ").append(String.format("%2d", line)).append(" ");
        for (int i=0; i<15; i++){
            result.append(printSquare(row, i, board));
        }
        result.append("| ").append(line);
        return result.toString();
    }

    /**
     * @param i - index for the rows
     * @return Strings that will display a visual representation of all special fields
     * @author Yasin Fahmy
     */
    public static String addFieldTypes(int i){
        if(i == 0) {
            return "        "+ANSI_RED_BACKGROUND+"Triple Word Score"+ANSI_RESET;
        }
        else if(i == 1) {
            return "        "+ANSI_PURPLE_BACKGROUND+"Double Word Score"+ANSI_RESET;
        }
        else if(i == 2) {
            return "        "+ANSI_BLUE_BACKGROUND+"Triple Letter Score"+ANSI_RESET;
        }
        else if(i == 3) {
            return "        "+ANSI_CYAN_BACKGROUND+"Double Letter Score"+ANSI_RESET;
        }
        else {
            return "";
        }
    }

    /**
     * @param i - index for the rows
     * @return instructions
     * @author Yasin Fahmy
     */
    public String addInstructions(int i){
        if(i == 10){
            return ANSI_GREEN+"       Place a word:      'MAKEMOVE' 'WORD' 'Start coordinate' 'Direction (H/V)' 'Word (lowercase = blank tile)' [i.e.: WORD B3 H SCRaBBLE]"+ANSI_RESET;
        }
        if(i == 11){
            return ANSI_GREEN+"       Swap tiles:        'MAKEMOVE' 'SWAP' 'Tiles you want to swap' [i.e.: SWAP ABC]"+ANSI_RESET;
        }
        if(i == 12){
            return ANSI_GREEN+"       Skip turn:         'MAKEMOVE' 'SWAP'"+ANSI_RESET;
        }
        if(i == 13){
            return "       If a character is white in the terminal input, pls type in the character again directly after it";
        }
        return "";
    }

    /**
     * @return - displays the whole with letters and numbers on the sides board
     * @author Yasin Fahmy
     */
    public String toString(){
        StringBuilder result = new StringBuilder("      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O\n");
        for (int i=0; i<15; i++){
            result.append("    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n");
            result.append(printLine(i + 1)).append(addFieldTypes(i)).append(addInstructions(i)).append("\n");
        }
        result.append("    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n      " +
                "A   B   C   D   E   F   G   H   I   J   K   L   M   N   O\n");
        return result.toString();
    }
}
