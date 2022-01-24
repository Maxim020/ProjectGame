package local.view;

import scrabble.model.*;
import scrabble.model.exceptions.*;
import scrabble.model.letters.Bag;
import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.InMemoryScrabbleWordChecker;
import scrabble.model.words.IsAdjacentChecker;
import scrabble.model.words.ScrabbleWordChecker;
import scrabble.view.UserInterface;
import scrabble.view.utils.TextBoardRepresentation;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LocalTUI implements UserInterface {
    private Board board;
    private TextBoardRepresentation representation;
    private Player currentPlayer;
    private Bag bag;
    private ScrabbleWordChecker wordChecker = new InMemoryScrabbleWordChecker();
    private AdjacentWordChecker adjacentWordChecker;
    private IsAdjacentChecker isAdjacentChecker;

    public LocalTUI(Board board, Player currentPlayer){
        this.board = board;
        this.representation = new TextBoardRepresentation(board);
        this.adjacentWordChecker = new AdjacentWordChecker(board);
        this.isAdjacentChecker = new IsAdjacentChecker(board);
        this.currentPlayer = currentPlayer;
        this.bag = Bag.getInstance();
    }

    /**
     * prints Scoreboard after match ends
     * @author Yasin
     */
    public void printFinalScoreBoard(Player winner){
        PlayerList playerList = PlayerList.getInstance();
        List<Player> players = playerList.getPlayers();
        String scoreboard = "";

        for (int i=0; i<players.size(); i++){
            if(!players.get(i).equals(winner)) {
                scoreboard += "\n" + (i + 1) + ") " + players.get(i);
            }
        }

        System.out.println ("*******************************\n"+
                            "WINNER WINNER CHICKEN DINNER!\n\n"+
                            winner.getName()+" has won the game!"+
                            scoreboard+"\n\n"+
                            "*******************************"
                            );
    }

    /**
     * Updates Board with instructions, Board representation and Rack of current Player
     * @author Yasin
     */
    @Override
    public void updateBoard() {
        System.out.println("\n\n"+representation);
        showRack();
    }

    /**
     * prints out rack of current Player
     * @author Yasin
     */
    @Override
    public void showRack() {
        String tiles = "\n"+currentPlayer.getName()+" has the tiles:";
        for(int i=0; i<currentPlayer.getLetterDeck().getLettersInDeck().size(); i++){
            tiles += " "+currentPlayer.getLetterDeck().getLettersInDeck().get(i);
        }
        System.out.println(tiles);
    }
}
