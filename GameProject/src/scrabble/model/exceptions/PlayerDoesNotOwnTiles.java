package scrabble.model.exceptions;

public class PlayerDoesNotOwnTiles extends IllegalArgumentException{
	
	private String error = "Player does not own all tiles or does not have enough blank tiles";
	
	public String getMessage() {
		return error;
	}
}
