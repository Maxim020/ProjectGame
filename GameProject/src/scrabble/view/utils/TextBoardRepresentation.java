package scrabble.view.utils;
import scrabble.model.Board;
/**
 * Class for TUI. It enables the display of the game.
 * @author Yasin Fahmy
 * @version 09.01.2022
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

    /**
     * @param board - Constructor needs an instance of the Class Board, in order to display the game
     */
    public TextBoardRepresentation(Board board){
        this.board = board;
    }

    /**
     * @param row - valid int to indicate row
     * @param column - valid int to indicate column
     * @return - The squares of the board that are the fields to place tiles on
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
     */
    public String printLine(int line){//For Loop?
        int row = line-1;
        String result = "";
        result += " "+String.format("%2d",line)+" ";
        for (int i=0; i<15; i++){
            result += printSquare(row,i);
        }
        result += "| "+line+"\n";
        return  result;
    }

    /**
     * @return - displays the whole with letters and numbers on the sides board
     */
    public String toString(){
        String result = "      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O\n";
        for (int i=0; i<15; i++){
            result +="    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n";
            result +=printLine(i+1);
        }
        result +="    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                 "      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O";
        return result;
    }
}
