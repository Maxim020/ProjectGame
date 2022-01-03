package model;

public class Player {
	
	//Attributes
    private String name;
    private int score;
    private char[] rack;
    
    //Constructor
    public Player(String name) {
    	this.name = name;
    }
    
    //Methods
	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public char[] getRack() {
		return rack;
	}

	public void setRack(char[] rack) {
		this.rack = rack;
	}
    

}
