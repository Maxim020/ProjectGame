package scrabble.model.exceptions;

import scrabble.model.Bag;

public class NotEnougTilesException extends IllegalArgumentException{

    private Bag bag = Bag.getInstance();
    private String error = "There are only "+bag.getLetterList().size()+" tiles in the bag";



    public String getMessage() {
        return error;
    }
}

