package scrabble.model.letters;

import java.util.ArrayList;

public class LetterDeck {
	
	//Attributes
	private ArrayList<Character> lettersInDeck;
	private Bag bag;
	
	//Constructor
	public LetterDeck(Bag bag) {
		this.lettersInDeck = new ArrayList<>();
		this.initializeDeck(bag);
		this.bag = Bag.getInstance();
	}
	
	//Methods
	public ArrayList<Character> getLettersInDeck() {
		return lettersInDeck;
	}
	
	/**
	 * Removes a letter from current deck and puts it back in the bag. Useful for when player plays a letter
	 * @requires lettersInDeck != null
	 * @ensures lettersInDeck != (old) lettersInDeck && lettersInDeck.contains(letter) = false
	 * @param letter
	 * @author Maxim & Yasin
	 */
	public void removeFromDeck(char letter) {
		lettersInDeck.remove(lettersInDeck.indexOf(letter));
		bag.add(letter);
	}

	/**
	 * @param amount of new Letters wished to be in the deck randomly pulled from the Bag
	 * @author Yasin
	 */
	public void addToDeck(int amount){
		for (int i=0; i<amount; i++) {
			lettersInDeck.add(bag.pull());
		}
	}
	
	/**
	 * This method is called when a LetterDeck object is constructed. Pulls 7 random letters from a bag object to fill initial deck
	 * @requires bag != null && lettersInDeck != null
	 * @ensures lettersInDeck.size() == 7
	 * @param bag
	 * @author Maxim
	 */
	public void initializeDeck(Bag bag) {
		int i = 0;
		while(i != 7) {
			lettersInDeck.add(bag.pull());
			i = i + 1;
		}
	}
}
