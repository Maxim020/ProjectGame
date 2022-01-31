package client.view;

import scrabble.model.Board;

/**
 * provides a ClientTUI
 * @author Yasin Fahmy
 */

public class ClientTUI {
    private ClientBoard representation;
    private final Board board;


    public ClientTUI(){
        board = new Board();
        representation = new ClientBoard(board);
    }

    public static void promptToAnnounce(){
        System.out.println("Please announce yourself by typing: ANNOUNCE [NAME] (Type ANNOUNCE -C for computer player)");
    }

    public static void promptToMakeMove(String p0, String p1, String p2){
        if (p1.equalsIgnoreCase("TRUE")) {
            System.out.println(p0 + " - Make a move!");
        } else {
            System.out.println("It's " + p2 + "'s turn");
        }
    }

    public static void printCommands(){
        System.out.println("\nUse Chat:  CHAT 'Your Message'\nRequest Game: REQUESTGAME 'amount of players (2-4)'\n\n");
    }


    public static void showMessage(String msg){
        System.out.println(msg);
    }


    public void updateBoard(String tiles){
        board.updateBoard(tiles);
        setRepresentation(new ClientBoard(board));
        System.out.println(representation);
    }


    public void setRepresentation(ClientBoard representation) {
        this.representation = representation;
    }
}

