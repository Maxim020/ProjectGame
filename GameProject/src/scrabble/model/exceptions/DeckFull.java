package scrabble.model.exceptions;

public class DeckFull extends IllegalArgumentException{
	
	private String error = "Deck is already full";
	
	public String getMessage() {
		return error;
	}

}
