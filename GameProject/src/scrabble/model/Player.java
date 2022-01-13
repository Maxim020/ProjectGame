package scrabble.model;

import scrabble.model.letters.LetterDeck;

public interface Player {
	
	public String getName();
	
	public void setName(String name);
	
	public LetterDeck getLetterDeck();
	
	public int getScore();
	
	public void setScore(int score);

	public String makeMove();

}
