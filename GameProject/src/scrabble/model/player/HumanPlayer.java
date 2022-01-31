package scrabble.model.player;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;
import scrabble.model.player.Player;

public class HumanPlayer extends Player {

	private String name;
	private int score;
	private LetterDeck letterdeck;
	
	public HumanPlayer(String name, Bag bag) {
		super(name, bag);
	}
}
