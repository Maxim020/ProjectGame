package scrabble.model;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;

public class HumanPlayer extends Player {

	private String name;
	private int score;
	private LetterDeck letterdeck;
	
	public HumanPlayer(String name, Bag bag) throws Exception {
		super(name, bag);
	}
}
