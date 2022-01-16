package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;

class LetterDeckTest {
	
	private LetterDeck letterdeck;
	private Bag bag;

	@BeforeEach
	void setUp() throws Exception {
		bag = Bag.getInstance();
		letterdeck = new LetterDeck(bag);
	}

	@Test
	void ConstructorTest() {
		assertFalse(letterdeck.getLettersInDeck().isEmpty());
		assertTrue(letterdeck.getLettersInDeck().size() == 7);
		System.out.print(letterdeck.getLettersInDeck());
	}
	
	@Test
	void removeTest() {
		System.out.print(letterdeck.getLettersInDeck());
		letterdeck.removeFromDeck(letterdeck.getLettersInDeck().get(0));
		assertTrue(letterdeck.getLettersInDeck().size() == 6);
		System.out.print(letterdeck.getLettersInDeck());
	}

}
