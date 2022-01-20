package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scrabble.model.Board;
import scrabble.model.NaiveStrategy;
import scrabble.model.Player;
import scrabble.model.PlayerList;
import scrabble.model.SmartStrategy;
import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;
import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.InMemoryScrabbleWordChecker;
import scrabble.model.words.ScrabbleWordChecker;
import scrabble.model.words.WordScoreCounter;

class SmartStrategyTest {

	SmartStrategy smart;
	LetterDeck deck;
	ScrabbleWordChecker checker;
	Board board;
	Bag bag;
	PlayerList playerlist;
	List<Player> players;
	AdjacentWordChecker adjacentChecker;
	WordScoreCounter scoreCounter;

	@BeforeEach
	void setUp() throws Exception {
		smart = new SmartStrategy();
		checker = new InMemoryScrabbleWordChecker();
		board = new Board();
		adjacentChecker = new AdjacentWordChecker(board);
		scoreCounter = new WordScoreCounter(board);
		playerlist = PlayerList.getInstance();
		players = new ArrayList<>();
		
		
	}

	@Test
	void testNaiveStrategyFirstTurnOnBoard() {
		
		bag = Bag.getInstance();
		deck = new LetterDeck(bag);
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		System.out.println(deck.getLettersInDeck());
		String s = smart.determineMove(board, deck, checker, adjacentChecker, scoreCounter);
		System.out.print(s);
		if(!s.equals("")) {
		String[] splt = s.split(" ");
		System.out.println(splt[1]);
		System.out.println(splt[2]);
		System.out.println(splt[3]);
		board.setWord(splt[1], splt[2], splt[3]);
		assertFalse(board.isFieldEmpty(board.convert(splt[1])[0], board.convert(splt[1])[1]));
		ArrayList<String> coveredTiles = board.fieldsCovered(splt[1], splt[2], splt[3]);
		for(String str : coveredTiles) {
			System.out.print(" " + str);
			assertTrue(board.isFieldValid(str));
		}
		System.out.println("\n");
		}
	}
	
	@Test
	void testNaiveStrategySecondTurnOnBoard() {
		
		bag = Bag.getInstance();
		deck = new LetterDeck(bag);
		players.add(new Player("Richard", bag));
		playerlist.setPlayers(players);
		playerlist.setCurrentPlayer(0);
		
		board.setWord("H8", "H", "ADVANCE");
		board.addPlayedWords("H8", "H", "ADVANCE");
		
		System.out.println(deck.getLettersInDeck());
		String s = smart.determineMove(board, deck, checker, adjacentChecker, scoreCounter);
		String[] splt = s.split(" ");
		System.out.println(splt[1]);
		System.out.println(splt[2]);
		System.out.println(splt[3]);
		board.setWord(splt[1], splt[2], splt[3]);
		ArrayList<String> coveredTiles = board.fieldsCovered(splt[1], splt[2], splt[3]);
		System.out.print(">");
		for(String str : coveredTiles) {
			System.out.print(" " + str);
			assertTrue(board.isFieldValid(str));
		}
		
	}
}
