package scrabble.model.exceptions;

public class WordIsNotAdjacentException extends IllegalArgumentException{

    private String error = "Word does not connect with other words";

    public String getMessage() {
            return error;
        }

}
