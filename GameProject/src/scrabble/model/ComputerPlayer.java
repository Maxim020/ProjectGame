package scrabble.model;

import scrabble.model.letters.Bag;

public class ComputerPlayer extends Player {
	
	private Strategy strategy;
	
	public ComputerPlayer(Bag bag, Strategy strategy, Board board) {
		super("Smart Computer", bag);
		this.strategy = strategy;
	}

	public ComputerPlayer(Bag bag, Board board) {
		super("Naive Computer", bag);
		this.strategy = new NaiveStrategy();
	}

	public String determineMove(Board board) {
		if(strategy.determineMove(board, getLetterDeck()) != "") {
			return strategy.determineMove(board, getLetterDeck());
		}
		else {
			return "SWAP " + strategy.swapHand(getLetterDeck());
		}
	}
}
