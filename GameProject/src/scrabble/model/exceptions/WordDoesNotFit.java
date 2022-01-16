package scrabble.model.exceptions;

public class WordDoesNotFit extends IllegalArgumentException{
	
	private String error = "Word does not fit on the board";
	
	public String getMessage() {
		return error;
	}
}
