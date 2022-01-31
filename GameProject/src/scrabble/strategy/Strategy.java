package scrabble.strategy;

import scrabble.model.Board;
import scrabble.model.letters.LetterDeck;
import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.ScrabbleWordChecker;
import scrabble.model.words.WordScoreCounter;

public interface Strategy {

    public String determineMove(Board board, LetterDeck letterDeck);
    
    public String swapHand(LetterDeck letterDeck);
}
