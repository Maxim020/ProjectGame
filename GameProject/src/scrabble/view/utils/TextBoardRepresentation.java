package scrabble.view.utils;
import scrabble.model.Board;
import scrabble.model.PlayerList;

/**
 * Class for TUI. It enables the display of the game.
 */

public class TextBoardRepresentation {
    // Declaring ANSI_RESET so that we can reset the color
    public static final String ANSI_RESET = "\u001B[0m";
    // Declaring the background color red, blue, purple, cyan
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private Board board;
    private PlayerList playerList;

    public TextBoardRepresentation(Board board){
        this.board = board;
        playerList = PlayerList.getInstance();
    }

    /**
     * @requires valid field
     * @param row - valid int to indicate row
     * @param column - valid int to indicate column
     * @return - The squares of the board that are the fields to place tiles on
     * @author Yasin Fahmy
     */
    public String printSquare(int row, int column) throws IllegalArgumentException{
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
    public String printLine(int line){//For Loop?
        int row = line-1;
        String result = "";
        result += " "+String.format("%2d",line)+" ";
        for (int i=0; i<15; i++){
            result += printSquare(row,i);
        }
        result += "| "+line;
        return  result;
    }

    /**
     * @param i - index for the rows
     * @return Strings that will display a visual representation of all special fields
     * @author Yasin Fahmy
     */
    public String addFieldTypes(int i){
        if(i == 2) {
            return "        "+ANSI_RED_BACKGROUND+"Triple Word Score"+ANSI_RESET;
        }
        else if(i == 3) {
            return "        "+ANSI_PURPLE_BACKGROUND+"Double Word Score"+ANSI_RESET;
        }
        else if(i == 4) {
            return "        "+ANSI_BLUE_BACKGROUND+"Triple Letter Score"+ANSI_RESET;
        }
        else if(i == 5) {
            return "        "+ANSI_CYAN_BACKGROUND+"Double Letter Score"+ANSI_RESET;
        }
        else {
            return "";
        }
    }

    /**
     * @param i - index for the rows
     * @return Strings that will display the score of the players
     * @author Yasin Fahmy
     */
    public String addScores(int i){
        if(i == 8) {
            return "        "+playerList.getPlayers().get(0).getName()+": "+playerList.getPlayers().get(0).getScore();
        }
        else if(i == 9) {
            return "       "+playerList.getPlayers().get(1).getName()+": "+playerList.getPlayers().get(1).getScore();
        }
        else if(i == 10) {
            if(playerList.getPlayers().size() > 2) {
                return "       "+playerList.getPlayers().get(2).getName()+": "+playerList.getPlayers().get(2).getScore();
            }
        }
        else if(i == 11) {
            if(playerList.getPlayers().size() > 3){
                return "       "+playerList.getPlayers().get(3).getName()+": "+playerList.getPlayers().get(3).getScore();
            }
        }
        return "";
    }

    /**
     * @return - displays the whole with letters and numbers on the sides board
     * @author Yasin Fahmy
     */
    public String toString(){
        String result = "      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O\n";
        for (int i=0; i<15; i++){
            result +="    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n";
            result +=printLine(i+1)+addFieldTypes(i)+addScores(i)+"\n";
        }
        result +="    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                 "      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O";
        return result;
    }
}
