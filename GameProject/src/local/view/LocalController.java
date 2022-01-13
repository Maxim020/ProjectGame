package local.view;

import local.model.HumanPlayer;
import scrabble.model.Board;
import scrabble.model.letters.Bag;
import scrabble.view.utils.TextBoardRepresentation;

public class LocalController {
    public static void main(String[] args) {
        Bag bag = new Bag();
        HumanPlayer humanPlayer = new HumanPlayer("Joe",bag);
        Board board = new Board();
        LocalTUI localTUI = new LocalTUI(board, humanPlayer);

        for (int i=0; i<15; i++){
            for (int j=0; j<15;j++){
                board.setTile('$',j,i);
            }

        }


        localTUI.updateBoard();

    }
}
