package scrabble.model;

import scrabble.model.letters.LetterDeck;
import scrabble.model.words.ScrabbleWordChecker;

public class SmartStrategy implements Strategy{

    @Override
    public String determineMove(Board board, LetterDeck letterDeck, ScrabbleWordChecker checker) {
        return null;
    }
}
