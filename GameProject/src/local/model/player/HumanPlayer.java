package local.model.player;

import scrabble.model.Bag;
import scrabble.model.LetterDeck;
import scrabble.model.Player;

public class HumanPlayer implements Player {
	
	private String name;
	private int score;
	private LetterDeck letterdeck;
	
	public HumanPlayer(String name, Bag bag) {
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
