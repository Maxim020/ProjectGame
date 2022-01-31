package server.view;
import client.view.ClientBoard;
import scrabble.model.Board;
import local.model.PlayerList;
import scrabble.model.letters.Bag;

/**
 * Class for TUI. It enables the display of the game.
 */

public class ServerBoard {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private final Board board;
    private final PlayerList playerList;
    private final Bag bag;

    public ServerBoard(Board board){
        this.board = board;
        playerList = PlayerList.getInstance();
        bag = Bag.getInstance();
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
            result.append(ClientBoard.printSquare(row, i, board));
        }
        result.append("| ").append(line);
        return result.toString();
    }

    /**
     * @param i - index for the rows
     * @return Strings that will display the score of the players
     * @author Yasin Fahmy
     */
    public String addScores(int i){
        if(i == 5) {
            return "        "+playerList.getPlayers().get(0).getName()+": "+playerList.getPlayers().get(0).getScore();
        }
        else if(i == 6) {
            return "        "+playerList.getPlayers().get(1).getName()+": "+playerList.getPlayers().get(1).getScore();
        }
        else if(i == 7) {
            if(playerList.getPlayers().size() > 2) {
                return "        "+playerList.getPlayers().get(2).getName()+": "+playerList.getPlayers().get(2).getScore();
            }
        }
        else if(i == 8) {
            if(playerList.getPlayers().size() > 3){
                return "        "+playerList.getPlayers().get(3).getName()+": "+playerList.getPlayers().get(3).getScore();
            }
        }
        return "";
    }

    /**
     * TESTING
     */
    public String addBag(){
        return "Size of Bag: "+bag.getLetterList().size()+"\n"+bag.getLetterList().toString();
    }

    /**
     * @param i - index for the rows
     * @return instructions
     * @author Yasin
     */
    public String addInstructions(int i){
        if(i == 10){
            return ANSI_GREEN+"       Place a word:      'WORD' 'Start coordinate' 'Direction (H/V)' 'Word (lowercase = blank tile)' [i.e.: WORD B3 H SCRaBBLE]"+ANSI_RESET;
        }
        if(i == 11){
            return ANSI_GREEN+"       Swap tiles:        'SWAP' 'Tiles you want to swap' [i.e.: SWAP ABC]"+ANSI_RESET;
        }
        if(i == 12){
            return ANSI_GREEN+"       Skip turn:         'SWAP'"+ANSI_RESET;
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
            result.append(printLine(i + 1)).append(ClientBoard.addFieldTypes(i)).append(addScores(i)).append(addInstructions(i)).append("\n");
        }
        result.append("    +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O\n").append(addBag());
        return result.toString();
    }
}
