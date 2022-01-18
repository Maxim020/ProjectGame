package scrabble.model.exceptions;

public class UnknownCommandException extends IllegalArgumentException{
	
	private String error = "Unknown command";
	
	public String getMessage() {
		return error;
	}
}
