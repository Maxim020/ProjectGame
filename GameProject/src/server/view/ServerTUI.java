package server.view;

import scrabble.model.player.PlayerList;
import scrabble.model.*;
import scrabble.model.player.Player;

import java.util.*;
import java.util.stream.Stream;

public class ServerTUI {
    private final ServerBoard representation;
    private final Player currentPlayer;

    public ServerTUI(Board board, Player currentPlayer){
        this.representation = new ServerBoard(board);
        this.currentPlayer = currentPlayer;
    }

    /**
     * prints Scoreboard after match ends
     * @author Yasin
     */
    public static String printFinalScoreBoard(){
        PlayerList playerList = PlayerList.getInstance();
        List<Player> players = playerList.getPlayers();
        String scoreboard = "";

        HashSet<String> checked = new HashSet<>();

        for (Player player : players) {
            checked.add(player.getName());
        }

        HashMap<String, Integer> unsorted = new HashMap<>();

        for(int i=0; i<players.size(); i++){
            if(players.get(i).getTeammate() == null){
                unsorted.put(players.get(i).getName(), players.get(i).getScore());
                checked.remove(players.get(i).getName());
            }
            else {
                if(checked.contains(players.get(i).getName())){
                    unsorted.put("Team-"+players.get(i).getName()+"-"+players.get(i).getTeammate(),
                            (players.get(i).getScore()
                                    +getPlayerWithName(players.get(i).getTeammate(), players).getScore()));
                    checked.remove(players.get(i).getName());
                    checked.remove(players.get(i).getTeammate());
                }
            }
        }

        String winner = Collections.max(unsorted.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        LinkedHashMap<String, Integer> sorted = new LinkedHashMap<>();
        unsorted.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

        for (HashMap.Entry<String, Integer> entry : sorted.entrySet()) {
            scoreboard += "\n"+entry.getKey()+": "+entry.getValue();
        }

        return  "*******************************\n"+
                "WINNER WINNER CHICKEN DINNER!\n\n"+
                winner+" has won the game!"+
                scoreboard+"\n\n"+
                "*******************************";
    }

    public static Player getPlayerWithName(String name, List<Player> players ){
        for (Player player : players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }


    /**
     * Updates Board with instructions, Board representation and Rack of current Player
     * @author Yasin
     */
    public void updateBoard() {
        System.out.println("\n\n"+representation);
        showRack();
    }

    /**
     * prints out rack of current Player
     * @author Yasin
     */
    public void showRack() {
        StringBuilder tiles = new StringBuilder("\n" + currentPlayer.getName() + " has the tiles:");
        for(int i=0; i<currentPlayer.getLetterDeck().getLettersInDeck().size(); i++){
            tiles.append(" ").append(currentPlayer.getLetterDeck().getLettersInDeck().get(i));
        }
        System.out.println(tiles);
    }

    public static void showMessage(String msg, String clientName){
        System.out.println("[INCOMING FROM "+clientName+"] "+msg+"\n");
    }
}
