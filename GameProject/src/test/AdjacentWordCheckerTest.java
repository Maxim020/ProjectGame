package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;
import scrabble.model.Player;
import scrabble.model.PlayerList;
import scrabble.model.letters.Bag;
import scrabble.model.words.AdjacentWordChecker;

class AdjacentWordCheckerTest {
	
	AdjacentWordChecker checker;
	Board board;
	PlayerList playerlist;
	List<Player> players;
	Bag bag;

	@BeforeEach
	void setUp() throws Exception {
		board = new Board();
		checker = new AdjacentWordChecker(board);
		playerlist = PlayerList.getInstance();
		players = new ArrayList<>();
	}

	@Test
	void testAreAdjacentWordsValidHorizontal() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "H", "HELL");
		board.setWord("H9", "H", "ILO");
		board.setWord("I10", "H", "KW");
		board.setWord("J7", "H", "SL");
		board.setWord("K6", "H", "A");
		
		assertTrue(checker.areAdjacentWordsValid("H8", "H", "HELL"));
		
	}
	
	@Test
	void testAreAdjacentWordsValidVertical() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "v", "hello");
		board.setWord("H10", "h", "loan");
		
		assertTrue(checker.areAdjacentWordsValid("H8", "V", "HELLO"));
		
	}
	
	@Test
	void testInvalidWordHorizontal() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "h", "hello");
		board.setWord("J8", "v", "lkju");
		
		assertFalse(checker.areAdjacentWordsValid("H8", "H", "HELLO"));
		
	}
	
	@Test
	void testInvalidWordVertical() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "v", "hello");
		board.setWord("H10", "h", "lujada");
		
		assertFalse(checker.areAdjacentWordsValid("H8", "V", "HELLO"));
		
	}

}
