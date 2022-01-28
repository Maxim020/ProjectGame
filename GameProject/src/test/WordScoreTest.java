package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;
import scrabble.model.Board.FieldType;
import scrabble.model.words.WordScoreCounter;

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
		
		scoreCounter.getTotalWordScoreHorizontal("SATYR", 7, 7);
		assertEquals(FieldType.NORMAL,board.checkFieldType(7, 11));
		assertEquals(FieldType.NORMAL,board.checkFieldType(7, 7));
		
	}
	
	@Test
	void testScoreResult() {
		
		assertEquals(18, scoreCounter.getTotalWordScoreHorizontal("SATYR", 7, 7));
		//Here the center DOUBLE_WORD_TILE is removed by the previous call hence it is only 9 not 18
		assertEquals(9, scoreCounter.getTotalWordScoreVertical("HELLO", 7, 7));
		
	}
	
	@Test
	void testBlankLetter1() {
		
		assertEquals(10, scoreCounter.getTotalWordScoreHorizontal("hELLO", 7, 7));
		
	}
	
	@Test
	void testBlankLetter2() {
		
		assertEquals(16, scoreCounter.getTotalWordScoreHorizontal("HeLLO", 7, 7));
		
	}
	
	@Test
	void testBlankLetter3() {
		
		assertEquals(8, scoreCounter.getTotalWordScoreHorizontal("hElLO", 7, 7));
		
	}
	
	@Test
	void testBlankLetter4() {
		
		assertEquals(14, scoreCounter.getTotalWordScoreHorizontal("HELLo", 7, 7));
		
	}
	
	
	

}