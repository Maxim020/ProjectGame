package scrabble.model.exceptions;

public class FieldDoesNotExist extends IllegalArgumentException{
	
	private String error = "Selected field or field that would be taken by a desired word does not exist";
	
	public String getMessage() {
		return error;
	}
}
