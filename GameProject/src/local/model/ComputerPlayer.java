package local.model;

import scrabble.model.NaiveStrategy;
import scrabble.model.Strategy;
import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;
import scrabble.model.Player;

public class ComputerPlayer implements Player {
	
	private String name;
	private int score;
	private LetterDeck letterdeck;
	private Strategy strategy;
	
	public ComputerPlayer(Bag bag, Strategy strategy) {
		//this.name = name;
		this.letterdeck = new LetterDeck(bag);
		this.score = 0;
		this.strategy = strategy;
	}
	public ComputerPlayer(Bag bag) {
		//this.name = name;
		this.letterdeck = new LetterDeck(bag);
		this.score = 0;
		this.strategy = new NaiveStrategy();
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
