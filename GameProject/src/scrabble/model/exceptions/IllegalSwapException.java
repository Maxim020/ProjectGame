package scrabble.model.exceptions;

public class IllegalSwapException extends IllegalArgumentException{
    private String error = "Player cannot swap more than 7 tiles and all tiles must be owned by the Player";

    public String getMessage() {
        return error;
    }
}
