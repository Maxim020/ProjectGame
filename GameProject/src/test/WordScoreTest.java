package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;
import scrabble.model.Board.FieldType;
import scrabble.model.PlayerList;
import scrabble.model.letters.Bag;
import scrabble.model.words.WordScoreCounter;
import scrabble.model.Player;

class WordScoreTest {
	
	WordScoreCounter scoreCounter;
	Board board;
	PlayerList playerlist;
	List<Player> players;
	Bag bag;
	

	@BeforeEach
	void setUp() throws Exception {
		
		board = new Board();
		bag = Bag.getInstance();
		scoreCounter = new WordScoreCounter(board);
		playerlist = PlayerList.getInstance();
		players = new ArrayList<>();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
	}

	@Test
	void testMultiplierRemoval() {
		
		board.setWord("H8", "h", "hello");
		scoreCounter.getTotalWordScoreHorizontal("hello", 8, 8);
		assertEquals(FieldType.NORMAL,board.checkFieldType(8, 13));
		
	}

	@Test
	void testScoreResult() {
		
		board.setWord("H8", "h", "hello");
		assertEquals(17, scoreCounter.getTotalWordScoreHorizontal("hello", 8, 8));
		
		board.setWord("H13", "v", "olleh");
		assertEquals(16, scoreCounter.getTotalWordScoreVertical("olleh", 13, 8));
	}
	
	

}
