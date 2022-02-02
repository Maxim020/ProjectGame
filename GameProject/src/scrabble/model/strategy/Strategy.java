package scrabble.model.strategy;

import scrabble.model.Board;
import scrabble.model.LetterDeck;

public interface Strategy {

    public String determineMove(Board board, LetterDeck letterDeck);
    
    public String swapHand(LetterDeck letterDeck);
}
