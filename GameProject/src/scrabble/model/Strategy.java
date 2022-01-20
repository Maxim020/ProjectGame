package scrabble.model;

import scrabble.model.letters.LetterDeck;
import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.ScrabbleWordChecker;
import scrabble.model.words.WordScoreCounter;

public interface Strategy {

    public String determineMove(Board board, LetterDeck letterDeck, ScrabbleWordChecker checker, AdjacentWordChecker adjacentChecker, WordScoreCounter scoreChecker);
    
    public String swapHand(LetterDeck letterDeck);
}
