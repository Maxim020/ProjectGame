package model;

import model.player.Player;

public class ComputerPlayer implements Player {
	
	private String name;
	private int score;
	private LetterDeck letterdeck;
	
	public ComputerPlayer(String name, Bag bag) {
		this.name = name;
		this.letterdeck = new LetterDeck(bag);
		this.score = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LetterDeck getLetterDeck() {
		return letterdeck;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
