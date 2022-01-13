package scrabble.model;

public class Square{
    public enum SquareType{NORMAL, DOUBLE_LETTER_SCORE,TRIPLE_LETTER_SCORE, DOUBLE_WORD_SCORE, TRIPLE_WORD_SCORE}
    private SquareType squareType;
    private char letter;

    public Square(char letter, SquareType squareType){
        this.letter=letter;
        this.squareType=squareType;
    }

    public String toString(){
        return
                " _____\n" +
                        "|     |\n" +
                        "|  x  |\n" +
                        "|_____|";

    }
}
