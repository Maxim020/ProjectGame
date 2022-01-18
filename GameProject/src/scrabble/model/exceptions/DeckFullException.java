package scrabble.model.exceptions;

public class DeckFullException extends IllegalArgumentException{
	
	private String error = "Deck is already full";
	
	public String getMessage() {
		return error;
	}

}
