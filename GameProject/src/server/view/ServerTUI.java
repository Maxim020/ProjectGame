package server.view;

import scrabble.model.PlayerList;
import scrabble.model.*;

import java.util.List;

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
    public String printFinalScoreBoard(Player winner){
        PlayerList playerList = PlayerList.getInstance();
        List<Player> players = playerList.getPlayers();
        StringBuilder scoreboard = new StringBuilder();

        for (int i=0; i<players.size(); i++){
            if(!players.get(i).equals(winner)) {
                scoreboard.append("\n").append(i + 1).append(") ").append(players.get(i));
            }
        }

        return  "*******************************\n"+
                "WINNER WINNER CHICKEN DINNER!\n\n"+
                winner.getName()+" has won the game!"+
                scoreboard+"\n\n"+
                "*******************************";
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
}
