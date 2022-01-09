package model.player;

import model.LetterDeck;

public interface Player {
	
	public String getName();
	
	public void setName(String name);
	
	public LetterDeck getLetterDeck();
	
	public int getScore();
	
	public void setScore(int score);

}