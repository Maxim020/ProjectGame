package scrabble.model;

import java.util.List;
import java.util.Vector;

/**
 * SINGLETON DESIGN PATTERN
 * Makes sure that there is only one list of player throughout the program
 * THREAD SAFE
 * @author Yasin Fahmy
 */

public class PlayerList {
    private static PlayerList instance = null;
    private List<Player> players = new Vector<>();
    private Player currentPlayer;

    private PlayerList(){}

    public static PlayerList getInstance() {
        if (instance == null) {
            instance = new PlayerList();
        }
        return instance;
    }

    public List<Player> getPlayers(){
        return this.players;
    }

    public void setPlayers(List<Player> players){
        this.players = players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int turn) {
        this.currentPlayer = players.get(turn);
    }
}
