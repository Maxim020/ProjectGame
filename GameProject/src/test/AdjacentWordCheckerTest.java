package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;
import scrabble.model.player.Player;
import scrabble.model.player.PlayerList;
import scrabble.model.Bag;
import scrabble.model.checker.AdjacentWordChecker;

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
		
		board.setWord("H8", "V", "HELLO");
		board.setWord("H10", "H", "LOAN");
		board.setWord("F12", "H", "ABOVE");
		
		assertTrue(checker.areAdjacentWordsValid("H8", "V", "HELLO"));
		
	}
	
	@Test
	void testInvalidWordHorizontal() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "H", "HELLO");
		board.setWord("J8", "V", "LKLKLK");
		
		assertFalse(checker.areAdjacentWordsValid("H8", "H", "HELLO"));
		
	}
	
	@Test
	void testInvalidWordVertical() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "V", "HELLO");
		board.setWord("H10", "H", "LKLKLKL");
		
		assertFalse(checker.areAdjacentWordsValid("H8", "V", "HELLO"));
		
	}
	
	@Test
	void testAreAdjacentWordsValidHorizontalExtended() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "H", "HELLO");
		board.setWord("F8", "H", "HE");
		board.setWord("M8", "H", "DO");
		
		assertFalse(checker.areAdjacentWordsValid("H8", "H", "HELLO"));
		assertFalse(checker.areAdjacentWordsValid("F8", "H", "HE"));
		assertFalse(checker.areAdjacentWordsValid("M8", "H", "DO"));
		
		board.setWord("A3", "V", "RAIL");
		board.setWord("A1", "V", "DE");
		board.setWord("A7", "V", "ED");
		
	}
	
	@Test
	void testInvalidGroupingOfWords() {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		
		board.setWord("H8", "H", "HELLO");
		board.setWord("I6", "V", "ELECTRIC");
		board.setWord("J6", "V", "MILITIA");
		
		assertTrue(checker.areAdjacentWordsValid("H8", "H", "HELLO"));
		assertFalse(checker.areAdjacentWordsValid("J6", "V", "MILITIA"));
		
	}
	
	@Test
	void testIBorderPlay() {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		assertTrue(checker.areAdjacentWordsValid("A1", "H", "HELLO"));
		assertTrue(checker.areAdjacentWordsValid("A1", "V", "HELLO"));
		assertTrue(checker.areAdjacentWordsValid("A15", "H", "HELLO"));
		assertTrue(checker.areAdjacentWordsValid("O10", "V", "HELLO"));
		
	}

}
