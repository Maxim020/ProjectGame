package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;

class BoardTest {
	
	Board board;

	@BeforeEach
	void setUp() throws Exception {
		board = new Board();
	}

	@Test
	void testMoveHorizontal() {
		ArrayList<String> listOfCoveredTiles = new ArrayList<>(); listOfCoveredTiles.add("H8"); listOfCoveredTiles.add("I8"); listOfCoveredTiles.add("J8"); listOfCoveredTiles.add("K8"); listOfCoveredTiles.add("L8");
		//board.processMove("word H8 h hello");
		assertEquals(listOfCoveredTiles ,board.fieldsCovered("H8", "h", "hello"));
	}
	
	@Test
	void testMoveVertical() {
		ArrayList<String> listOfCoveredTiles = new ArrayList<>(); listOfCoveredTiles.add("H8"); listOfCoveredTiles.add("H9"); listOfCoveredTiles.add("H10"); listOfCoveredTiles.add("H11"); listOfCoveredTiles.add("H12");
		//board.processMove("word H8 v hello");
		assertEquals(listOfCoveredTiles ,board.fieldsCovered("H8", "v", "hello"));
	}
	
	@Test
	void testMultipleMoves() {
		ArrayList<String> listOfCoveredTilesH = new ArrayList<>(); listOfCoveredTilesH.add("H8"); listOfCoveredTilesH.add("I8"); listOfCoveredTilesH.add("J8"); listOfCoveredTilesH.add("K8"); listOfCoveredTilesH.add("L8");
		ArrayList<String> listOfCoveredTilesV = new ArrayList<>(); listOfCoveredTilesV.add("L8"); listOfCoveredTilesV.add("L9"); listOfCoveredTilesV.add("L10"); listOfCoveredTilesV.add("L11"); listOfCoveredTilesV.add("L12");
		//board.processMove("word H8 h hello");
		//board.processMove("word L8 v olleh");
		assertEquals(listOfCoveredTilesH ,board.fieldsCovered("H8", "h", "hello"));
		assertEquals(listOfCoveredTilesV ,board.fieldsCovered("L8", "v", "olleh"));
	}
	
	@Test
	void testWordFit() {
		assertFalse(board.doesWordFit("H8", "h", "WordMostLikelyTooBigToFit"));
		assertFalse(board.doesWordFit("H8", "v", "WordMostLikelyTooBigToFit"));
		assertTrue(board.doesWordFit("H8", "h", "Small"));
		assertTrue(board.doesWordFit("H8", "v", "Small"));
		assertFalse(board.doesWordFit("H13", "h", "Word"));
		assertTrue(board.doesWordFit("H13", "h", "Bee"));
		assertFalse(board.doesWordFit("C8", "v", "Word"));
		assertTrue(board.doesWordFit("C8", "v", "Bee"));
	}
	
	@Test
	void testExpectedException() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			//board.processMove("word H16 h hello");
		}, "IllegalArgumentException was expected");
		
		Assertions.assertEquals("For input string: \"word H10 h hello\"", thrown.getMessage());
	}

}
