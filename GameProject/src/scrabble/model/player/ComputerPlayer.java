package scrabble.model.player;

import scrabble.model.Board;
import scrabble.model.Bag;
import scrabble.model.strategy.NaiveStrategy;
import scrabble.model.strategy.Strategy;

public class ComputerPlayer extends Player {
	
	private Strategy strategy;
	
	public ComputerPlayer(Bag bag, Strategy strategy, String name) {
		super(name, bag);
		this.strategy = strategy;
	}

	public ComputerPlayer(Bag bag, String name) {
		super(name, bag);
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
