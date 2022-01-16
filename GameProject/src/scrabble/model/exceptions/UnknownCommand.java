package scrabble.model.exceptions;

public class UnknownCommand extends IllegalArgumentException{
	
	private String error = "Unknown command";
	
	public String getMessage() {
		return error;
	}
}
