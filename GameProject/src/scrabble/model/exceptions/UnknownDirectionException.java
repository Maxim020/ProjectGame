package scrabble.model.exceptions;

public class UnknownDirectionException extends IllegalArgumentException{
	
	private String error = "Unknown direction";
	
	public String getMessage() {
		return error;
	}
}
