package scrabble.model;

public interface Strategy {
    public String getName();

    public String determineMove(Board board);
}
