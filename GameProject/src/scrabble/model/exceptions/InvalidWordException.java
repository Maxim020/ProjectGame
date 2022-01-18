package scrabble.model.exceptions;

public class InvalidWordException extends IllegalArgumentException{
    private String error = "Either the word itself or any adjacent words do not exist";

    public String getMessage() {
        return error;
    }
}
