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
	void testHorizontalWordAdjacency() throws Exception {
		bag = Bag.getInstance();
		//players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "h", "hello");
		board.setWord("J8", "v", "loan");
		
		System.out.println(board.getTile(7, 9));
		assertTrue(board.isFieldEmpty(9, 9));
		
		checker.checkHorizontalWordAdjacency("hello", 8, 8);
		
	}

}
