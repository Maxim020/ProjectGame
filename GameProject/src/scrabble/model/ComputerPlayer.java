package scrabble.model;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;

public class ComputerPlayer extends Player {
	
	private String name;
	private int score;
	private LetterDeck letterdeck;
	private Strategy strategy;
	
	public ComputerPlayer(Bag bag, Strategy strategy) {
		super("Smart Computer", bag);
		this.strategy = strategy;
	}
	public ComputerPlayer(Bag bag) {
		super("Naive Computer", bag);
		this.strategy = new NaiveStrategy();
	}
}
