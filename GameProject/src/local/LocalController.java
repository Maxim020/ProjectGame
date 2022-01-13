package local;

import scrabble.model.Board;
import local.view.LocalTUI;

public class LocalController {
    public static void main(String[] args) {
        Board board = new Board();
        for (int i=0; i<15; i++){
            for (int j=0; j<15;j++){
                board.setTile('X',j,i);
            }

        }
        System.out.println(new LocalTUI(board));

    }
}
