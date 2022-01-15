package scrabble.model;

import java.util.ArrayList;
import java.util.List;

/**
 * SINGLETON DESIGN PATTERN
 * Makes sure that there is only one list of player throughout the program
 *
 * PlayerList list = PlayerList.getInstance();
 * list.getPlayers()
 *
 * NOT THREAD SAFE
 */

public class PlayerList {
    private static PlayerList instance = null;
    private List<Player> players = new ArrayList<>(); //Currently, not thread-safe!

    private PlayerList(){}//Private to prevent anyone from instantiating

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
}
