package local.model;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;
import scrabble.model.Player;

public class ComputerPlayer implements Player {
	
	private String name;
	private int score;
	private LetterDeck letterdeck;
	
	public ComputerPlayer(String name, Bag bag) {
		this.name = name;
		this.letterdeck = new LetterDeck(bag);
		this.score = 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public LetterDeck getLetterDeck() {
		return letterdeck;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String makeMove() {
		return null;
	}

}
