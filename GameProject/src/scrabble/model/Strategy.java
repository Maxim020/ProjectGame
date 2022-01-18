package scrabble.model;

import scrabble.model.letters.LetterDeck;
import scrabble.model.words.ScrabbleWordChecker;

public interface Strategy {

    public String determineMove(Board board, LetterDeck letterDeck, ScrabbleWordChecker checker);
}
