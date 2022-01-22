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

	@BeforeEach
	void setUp() throws Exception {
		
		board = new Board();
		scoreCounter = new WordScoreCounter(board);
		
	}
	
	@Test
	void testMultiplierRemoval() {
		
		scoreCounter.getTotalWordScoreHorizontal("satyr", 8, 8);
		assertEquals(FieldType.NORMAL,board.checkFieldType(8, 12));
		assertEquals(FieldType.NORMAL,board.checkFieldType(8, 8));
		
	}
	
	@Test
	void testScoreResult() {
		
		assertEquals(18, scoreCounter.getTotalWordScoreHorizontal("satyr", 8, 8));
		//Here the center DOUBLE_WORD_TILE is removed by the previous call hence it is only 9 not 18
		assertEquals(9, scoreCounter.getTotalWordScoreVertical("hello", 8, 8));
		
	}
	
	

}