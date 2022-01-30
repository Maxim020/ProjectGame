package local.view;

import local.model.PlayerList;
import scrabble.model.*;
import scrabble.model.letters.Bag;
import scrabble.view.TextBoardRepresentation;

import java.util.List;

public class LocalTUI {
    private TextBoardRepresentation representation;
    private Player currentPlayer;
    private Bag bag;

    public LocalTUI(Board board, Player currentPlayer){
        this.representation = new TextBoardRepresentation(board);
        this.currentPlayer = currentPlayer;
        this.bag = Bag.getInstance();
    }

    /**
     * prints Scoreboard after match ends
     * @author Yasin
     */
    public String printFinalScoreBoard(Player winner){
        PlayerList playerList = PlayerList.getInstance();
        List<Player> players = playerList.getPlayers();
        String scoreboard = "";

        for (int i=0; i<players.size(); i++){
            if(!players.get(i).equals(winner)) {
                scoreboard += "\n" + (i + 1) + ") " + players.get(i);
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
        String tiles = "\n"+currentPlayer.getName()+" has the tiles:";
        for(int i=0; i<currentPlayer.getLetterDeck().getLettersInDeck().size(); i++){
            tiles += " "+currentPlayer.getLetterDeck().getLettersInDeck().get(i);
        }
        System.out.println(tiles);
    }
}
