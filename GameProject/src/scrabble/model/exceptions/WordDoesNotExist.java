package scrabble.model.exceptions;

public class WordDoesNotExist extends IllegalArgumentException{
	
	private String error = "Word is not present in the dictionary";
	
	public String getMessage() {
		return error;
	}
}
