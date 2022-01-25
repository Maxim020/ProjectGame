package scrabble.model.exceptions;

public class InvalidCrossException extends IllegalArgumentException{
    private String error = "Crossing in this way is invalid";

    public String getMessage() {
        return error;
    }
}
