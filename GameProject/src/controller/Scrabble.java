package controller;

import model.Board;
import model.Square;
import view.TUI;

public class Scrabble {
    public static void main(String[] args) {
        Board board = new Board();
        for (int i=0; i<15; i++){
            for (int j=0; j<15;j++){
                board.setTile('X',j,i);
            }

        }
        System.out.println(new TUI(board));

        System.out.println(board.convert(5,10));
        
    }
}