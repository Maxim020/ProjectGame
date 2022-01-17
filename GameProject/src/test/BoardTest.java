package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;
import scrabble.model.Player;
import scrabble.model.PlayerList;
import scrabble.model.letters.Bag;
import scrabble.model.words.WordScoreCounter;

class BoardTest {
	
	WordScoreCounter scoreCounter;
	Board board;
	PlayerList playerlist;
	List<Player> players;
	Bag bag;

	@BeforeEach
	void setUp() throws Exception {
		board = new Board();
		scoreCounter = new WordScoreCounter(board);
		playerlist = PlayerList.getInstance();
		players = new ArrayList<>();
		
	}

	@Test
	void testMoveHorizontal() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		ArrayList<String> listOfCoveredTiles = new ArrayList<>(); listOfCoveredTiles.add("H8"); listOfCoveredTiles.add("I8"); listOfCoveredTiles.add("J8"); listOfCoveredTiles.add("K8"); listOfCoveredTiles.add("L8");
		board.setWord("H8", "h", "hello");
		assertEquals(listOfCoveredTiles ,board.fieldsCovered("H8", "h", "hello"));
	}
	
	@Test
	void testMoveVertical() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		ArrayList<String> listOfCoveredTiles = new ArrayList<>(); listOfCoveredTiles.add("H8"); listOfCoveredTiles.add("H9"); listOfCoveredTiles.add("H10"); listOfCoveredTiles.add("H11"); listOfCoveredTiles.add("H12");
		board.setWord("H8", "h", "hello");
		assertEquals(listOfCoveredTiles ,board.fieldsCovered("H8", "v", "hello"));
	}
	
	@Test
	void testMultipleMoves() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		ArrayList<String> listOfCoveredTilesH = new ArrayList<>(); listOfCoveredTilesH.add("H8"); listOfCoveredTilesH.add("I8"); listOfCoveredTilesH.add("J8"); listOfCoveredTilesH.add("K8"); listOfCoveredTilesH.add("L8");
		ArrayList<String> listOfCoveredTilesV = new ArrayList<>(); listOfCoveredTilesV.add("L8"); listOfCoveredTilesV.add("L9"); listOfCoveredTilesV.add("L10"); listOfCoveredTilesV.add("L11"); listOfCoveredTilesV.add("L12");
		board.setWord("H8", "h", "hello");
		board.setWord("L8", "v", "olleh");
		assertEquals(listOfCoveredTilesH ,board.fieldsCovered("H8", "h", "hello"));
		assertEquals(listOfCoveredTilesV ,board.fieldsCovered("L8", "v", "olleh"));
	}
	
	@Test
	void testWordFit() {
		assertFalse(board.doesWordFit("H8", "h", "WordMostLikelyTooBigToFit"));
		assertFalse(board.doesWordFit("H8", "v", "WordMostLikelyTooBigToFit"));
		assertTrue(board.doesWordFit("H8", "h", "Small"));
		assertTrue(board.doesWordFit("H8", "v", "Small"));
		assertFalse(board.doesWordFit("M13", "h", "Word"));
		assertTrue(board.doesWordFit("M13", "h", "Bee"));
		assertFalse(board.doesWordFit("C13", "v", "Word"));
		assertTrue(board.doesWordFit("C13", "v", "Bee"));
	}
	
	@Test
	void testExpectedException() throws Exception {
		bag = Bag.getInstance();
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			board.setWord("H16", "h", "hello");
		}, "IllegalArgumentException was expected");
	}

}
