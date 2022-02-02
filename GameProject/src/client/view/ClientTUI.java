package client.view;

import scrabble.model.Board;

public class ClientTUI {
    private ClientBoard representation;
    private final Board board;

    public ClientTUI(){
        board = new Board();
        representation = new ClientBoard(board);
    }

    /**
     * prompts the client to announce him/herself
     * @author Yasin Fahmy
     */
    public static void promptToAnnounce(){
        System.out.println("Please announce yourself by typing: ANNOUNCE NAME (Type ANNOUNCE -C for computer player)");
    }

    /**
     * Prompts the user to make a move
     * @param p0 - parts of input, received from server
     * @param p1 - parts of input, received from server
     * @param p2 - parts of input, received from server
     * @author Yasin Fahmy
     */
    public static void promptToMakeMove(String p0, String p1, String p2){
        if (p1.equalsIgnoreCase("TRUE")) {
            System.out.println(p0 + " - Make a move!");
        } else {
            System.out.println("It's " + p2 + "'s turn");
        }
    }

    /**
     * Prints all important commands
     * @author Yasin Fahmy
     */
    public static void printCommands(){
        System.out.println("\nUse Chat:  CHAT 'Your Message'\nRequest Game: REQUESTGAME 'amount of players (2-4)'\nRequest team: REQUESTTEAM 'Name of Teammate'\n");
    }

    /**
     * Shows message from Server
     * @param msg - Message
     * @author Yasin Fahmy
     */
    public static void showMessage(String msg){
        System.out.println(msg);
    }

    /**
     * Updates current Board
     * @param tiles - String representation of current Board
     * @author Yasin Fahmy
     */
    public void updateBoard(String tiles){
        board.updateBoard(tiles);
        setRepresentation(new ClientBoard(board));
        System.out.println(representation);
    }

    public void setRepresentation(ClientBoard representation) {
        this.representation = representation;
    }
}

