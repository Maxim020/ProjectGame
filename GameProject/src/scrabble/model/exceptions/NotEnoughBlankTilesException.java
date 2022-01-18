package scrabble.model.exceptions;

import scrabble.model.letters.Bag;

public class NotEnoughBlankTilesException extends IllegalArgumentException {

    private Bag bag = Bag.getInstance();
    private String error = "Player does not have enough blank tiles";


    public String getMessage() {
        return error;
    }
}
