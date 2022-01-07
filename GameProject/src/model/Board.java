package model;

import java.util.ArrayList;

public class Board {

    public static final int SIZE = 15;
    private Square[][] squares;
    private ArrayList<Character> tiles;

    
    public Board() {
    	this.squares = new Square[15][15];
    }
    
    
}
