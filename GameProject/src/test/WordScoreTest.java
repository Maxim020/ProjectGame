package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;
import scrabble.model.WordScoreCounter;
import scrabble.model.Board.FieldType;

class WordScoreTest {
	
	WordScoreCounter scoreCounter;
	Board board;
	

	@BeforeEach
	void setUp() throws Exception {
		
		board = new Board();
		scoreCounter = new WordScoreCounter(board);
		
	}

	@Test
	void testMultiplierRemoval() {
		
		board.processMove("word H8 h hello");
		scoreCounter.getTotalWordScoreHorizontal("hello", 8, 8);
		assertEquals(FieldType.NORMAL,board.checkFieldType(8, 13));
		
	}
	
	void testScoreResult() {
		
		board.processMove("word H8 h hello");
		assertEquals(17, scoreCounter.getTotalWordScoreHorizontal("hello", 8, 8));
		
		board.processMove("word H13 v olleh");
		assertEquals(16, scoreCounter.getTotalWordScoreVerticalDown("olleh", 13, 8));
	}
	
	

}
