package scrabble.model;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;
import scrabble.model.words.InMemoryScrabbleWordChecker;
import scrabble.model.words.ScrabbleWordChecker;

public class ComputerPlayer extends Player {
	
	private String name;
	private int score;
	private LetterDeck letterdeck;
	private Strategy strategy;
	private ScrabbleWordChecker checker;
	
	public ComputerPlayer(Bag bag, Strategy strategy) {
		super("Smart Computer", bag);
		this.strategy = strategy;
		this.checker = new InMemoryScrabbleWordChecker();
	}

	public ComputerPlayer(Bag bag) {
		super("Naive Computer", bag);
		this.strategy = new NaiveStrategy();
	}

	public String determineMove(Board board) {
		return strategy.determineMove(board, letterdeck, checker);
	}
}
