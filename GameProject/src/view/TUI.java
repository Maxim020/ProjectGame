package view;
import model.Board;

import java.io.*;

public class TUI {
    // Declaring ANSI_RESET so that we can reset the color
    public static final String ANSI_RESET = "\u001B[0m";
    // Declaring the background color red
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    // Declaring the background color blue
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    // Declaring the background color purple
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    // Declaring the background color cyan
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

    private Board board;

    public TUI(Board board){
        this.board = board;
    }

    public String printSquare(int row, int column){ //Jedes Square einzeln behandeln und wenns besonders ist background setzen
        //Center!!!!!
        if (board.checkField(row, column).equals(Board.FieldType.TRIPLE_WORD_SCORE)){
            return ANSI_RED_BACKGROUND+" "+board.getTile(row,column)+" "+ANSI_RESET;
        }
        else if(board.checkField(row, column).equals(Board.FieldType.DOUBLE_WORD_SCORE)){
            return ANSI_PURPLE_BACKGROUND+" "+board.getTile(row,column)+" "+ANSI_RESET;
        }
        else if(board.checkField(row, column).equals(Board.FieldType.TRIPLE_LETTER_SCORE)){
            return ANSI_BLUE_BACKGROUND+" "+board.getTile(row,column)+" "+ANSI_RESET;
        }
        else if(board.checkField(row, column).equals(Board.FieldType.DOUBLE_LETTER_SCORE)){
            return ANSI_CYAN_BACKGROUND+" "+board.getTile(row,column)+" "+ANSI_RESET;
        }
        else {
            return " "+board.getTile(row,column)+" ";
        }
    }

    public String printLine(int line){//For Loop?
        int row = line-1;
        return  " "+String.format("%2d",line)+" | "+board.getTile(row,0)+" | "+board.getTile(row,1)+" | "+board.getTile(row,2)+" | "+board.getTile(row,3)+" | "+board.getTile(row,4)+" | "+board.getTile(row,5)+" | "+board.getTile(row,6)+" | "+board.getTile(row,7)+" | "+board.getTile(row,8)+" | "+board.getTile(row,9)+" | "+board.getTile(row,10)+" | "+board.getTile(row,11)+" | "+board.getTile(row,12)+" | "+board.getTile(row,13)+" | "+board.getTile(row,14)+" | "+line+"\n";
    }

    public String toString(){
        return
                "      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O\n" +
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(1)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(2)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(3)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(4)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(5)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(6)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(7)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(8)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(9)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(10)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(11)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(12)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(13)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(14)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                        printLine(15)+
                "    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n" +
                "      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O";
    }
}
