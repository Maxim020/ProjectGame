package scrabble.model;

import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterDeck;

public class Player {
    private String name;
    private int score;
    private LetterDeck letterdeck;

    public Player(String name, Bag bag){
        this.name = name;
        this.letterdeck = new LetterDeck(bag);
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LetterDeck getLetterDeck() {
        return letterdeck;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString(){
        return getName()+" (Score: "+getScore()+")";
    }
}
