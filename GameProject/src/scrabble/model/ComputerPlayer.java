package scrabble.model;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;
import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.InMemoryScrabbleWordChecker;
import scrabble.model.words.ScrabbleWordChecker;
import scrabble.model.words.WordScoreCounter;

public class ComputerPlayer extends Player {
	
	private String name;
	private int score;
	private LetterDeck letterdeck;
	private Strategy strategy;
	private ScrabbleWordChecker checker;
	private AdjacentWordChecker checkerAdjacency;
	private WordScoreCounter scoreCounter;
	private Board board;
	
	public ComputerPlayer(Bag bag, Strategy strategy, Board board) {
		super("Smart Computer", bag);
		this.strategy = strategy;
		this.checker = new InMemoryScrabbleWordChecker();
		this.checkerAdjacency = new AdjacentWordChecker(board);
	}

	public ComputerPlayer(Bag bag, Board board) {
		super("Naive Computer", bag);
		this.strategy = new NaiveStrategy();
		this.checker = new InMemoryScrabbleWordChecker();
		this.checkerAdjacency = new AdjacentWordChecker(board);
	}

	public String determineMove(Board board) {
		if(strategy.determineMove(board, letterdeck, checker, checkerAdjacency, scoreCounter) != "") {
			return strategy.determineMove(board, letterdeck, checker, checkerAdjacency, scoreCounter);
		}
		else {
			return "SWAP " + strategy.swapHand(letterdeck);
		}
	}
}
