package scrabble.model.exceptions;

public class CenterIsNotCovered extends IllegalArgumentException{
	
	private String error = "The first word needs to cover the center square (H8)";
	
	public String getMessage() {
		return error;
	}
}
