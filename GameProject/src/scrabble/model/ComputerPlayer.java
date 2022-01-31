package scrabble.model;

import scrabble.model.letters.Bag;

public class ComputerPlayer extends Player {
	
	private Strategy strategy;
	
	public ComputerPlayer(Bag bag, Strategy strategy) {
		super("Smart Computer", bag);
		this.strategy = strategy;
	}

	public ComputerPlayer(Bag bag) {
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

	@Override
	public String toString(){
		return "Im a computerplayer";
	}
}
