package scrabble.model.player;

import scrabble.model.Bag;
import scrabble.model.LetterDeck;

public class HumanPlayer extends Player {

	private String name;
	private int score;
	private LetterDeck letterdeck;
	
	public HumanPlayer(String name, Bag bag) {
		super(name, bag);
	}
}
