package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.letters.Bag;

class BagTest {
	
	private Bag bag;
	private final String LETTERS = "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGHHIIIIIIIIJJKKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ**";

	@BeforeEach
	void setUp() throws Exception {
		bag = Bag.getInstance();
	}

	@Test
	void constructorTest() {
		assertEquals(100,bag.getLetterList().size());
		String a = "";
		for(int i = 0; i < bag.getLetterList().size(); i++) {a = a + bag.getLetterList().get(i).toString();}
		assertEquals(LETTERS, a);
	}
	
	@Test
	void shuffleTest() {
		ArrayList<Character> a = bag.getLetterList();
		bag.shuffleBag();
		assertNotEquals(a.toArray(), bag.getLetterList().toArray());
	}
	
	@Test
	void removeTest() {
		bag.removeFromBag('A');
		assertTrue(bag.getLetterList().size() == 99);
		assertNotEquals(bag.getLetterList(), bag.getLetterList().add('A'));
	}
	
	@Test void pullTest() throws Exception {
		bag.getLetterList().clear();
		bag.getLetterList().add('X');
		assertEquals(bag.pull(), 'X');
		assertTrue(bag.getLetterList().isEmpty());
		
	}

}
