package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;
import scrabble.model.Player;
import local.model.PlayerList;
import scrabble.model.letters.Bag;
import scrabble.model.words.IsAdjacentChecker;

class IsAdjacentTest {
	
	IsAdjacentChecker adjacentChecker;
	Board board;
	PlayerList playerlist;
	List<Player> players;
	Bag bag;

	@BeforeEach
	void setUp() throws Exception {
		board = new Board();
		adjacentChecker = new IsAdjacentChecker(board);
		playerlist = PlayerList.getInstance();
		players = new ArrayList<>();
	}

	@Test
	void testIsAdjacent() {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "h", "hello");
		board.setWord("J8", "v", "loan");
		
		assertTrue(adjacentChecker.isAdjacent("H8", "h", "hello"));
	}
	
	@Test
	void testIsNotAdjacent() {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "h", "hello");
		board.setWord("I10", "v", "loan");
		
		assertFalse(adjacentChecker.isAdjacent("H8", "h", "hello"));
	}

}
