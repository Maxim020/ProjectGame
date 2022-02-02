package scrabble.model.checker;

import scrabble.model.Board;

public class CrossChecker {
    public static boolean isMoveValid(Board board, String coordinate, String direction, String word){

        int[] rowcol = board.convert(coordinate);

        for (int i = 0; i < word.length(); i++) {
            if(direction.equalsIgnoreCase("h")) {
                if(!board.isFieldEmpty(rowcol[0],(rowcol[1]+i))){
                    if(board.getTile(rowcol[0],(rowcol[1]+i)) != word.charAt(i)){
                        return false;
                    }
                }
            }
            else {
                if(!board.isFieldEmpty((rowcol[0]+i),rowcol[1])){
                    if(board.getTile((rowcol[0]+i),rowcol[1]) != word.charAt(i)){
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
