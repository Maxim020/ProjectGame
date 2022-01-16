package scrabble.model.exceptions;

public class UnknownDirection extends IllegalArgumentException{
	
	private String error = "Unknown direction";
	
	public String getMessage() {
		return error;
	}
}
