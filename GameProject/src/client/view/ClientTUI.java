package client.view;

import scrabble.model.Board;

/**
 * IDEAS:
 * 1)
 */

public class ClientTUI {
    private PlainBoardRepresentation representation;
    private Board board;

    public ClientTUI(){
        board = new Board();
        representation = new PlainBoardRepresentation(board);
    }

    public void showMessage(String msg){
        System.out.println(msg);
    }


    public void updateBoard(String tiles){
        board.updateBoard(tiles);
        setRepresentation(new PlainBoardRepresentation(board));
        System.out.println(representation);
    }


    public void setRepresentation(PlainBoardRepresentation representation) {
        this.representation = representation;
    }

    public void printCommands(){
        System.out.println( "\nUse Chat:  CHAT 'Your Message'\n"+
                            "Request Game: REQUESTGAME 'amount of players (2-4)'\n");
    }

}

