package scrabble.model;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;

public class ComputerPlayer extends Player {
	
	private String name;
	private int score;
	private LetterDeck letterdeck;
	private Strategy strategy;
	
	public ComputerPlayer(Bag bag, Strategy strategy) throws Exception {
		super("Smart Computer", bag);
		this.strategy = strategy;
	}
	public ComputerPlayer(Bag bag) throws Exception {
		super("Naive Computer", bag);
		this.strategy = new NaiveStrategy();
	}

	public String determineMove(Board board) {
		return strategy.determineMove(board);
	}
}
