package local.view;

import scrabble.model.ComputerPlayer;
import scrabble.model.HumanPlayer;
import scrabble.model.*;
import scrabble.model.letters.Bag;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * I find it easier to program the game with everything in the main method
 * If something works for sure it can be moved and packed into a method
 */

public class LocalController {
    public static void main(String[] args) {
        /**
         * Scans user input for the name of all players and if wanted any computer players
         * Creates universal PlayerList (Singleton Design Pattern)
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("> Please type in the name of the players with a space in between (2-4)");
        System.out.println("> If you want to play against the computer, type '-N' for a naive and '-S' for a smart computer");
        String playersNames = scanner.nextLine();
        String[] players = playersNames.split(" ");
        int amountOfPlayers = players.length;
        PlayerList playerList = PlayerList.getInstance();
        playerList.setPlayers(createPlayerArrayList(players));


        //Instantiation
        Board board = new Board();
        Bag bag = Bag.getInstance();
        Game game = new Game(board, bag);

        int numberOfTurn = 0;
        int playersTurn;

        while (game.isGameRunning()){

            // Allows for rotation of players turn
            playersTurn = numberOfTurn%amountOfPlayers;
            playerList.setCurrentPlayer(playersTurn);

            //Creates localTUI and connects it with board and currentPlayer
            LocalTUI localTUI = new LocalTUI(board, playerList.getCurrentPlayer());
            localTUI.updateBoard();

            //Player makes move
            String move = scanner.nextLine();
            if(!localTUI.isInputValid(move)){
                throw new IllegalArgumentException();
            }
            processMove(move, board); //Next: Having a universal bag of letters and

            localTUI.updateBoard();

            numberOfTurn++;

        }
        scanner.close();

        //Ask for another game
    }

    /**
     * processes input to make a move
     * @requires the input to be valid
     * @param input the input that the user wrote
     * @author Yasin Fahmy
     */
    public static void processMove(String input, Board board){
        //Validation required (currently in LocalController)
        String[] parts = input.split(" ");

        if(parts[0].equalsIgnoreCase("word")){
            board.setWord(parts[1], parts[2], parts[3]);
        }
        else {
            System.out.println("Swaping not available yet");
        }
        board.setBoardEmpty(false);
    }


    /**
     * @requires the nr. of total players to be at least 2 and max. 4
     * @param players a String array that holds the name (and type) of all Players
     * @return a list of all Players
     * @author Yasin
     */
    public static List<Player> createPlayerArrayList(String[] players) throws IllegalArgumentException{
        if (players.length < 2 || players.length > 4){
            throw new IllegalArgumentException();
        }

        List<Player> playerArrayList = new ArrayList<>();

        for (int i=0; i<players.length; i++){
            if (players[i].equals("-N")){
                playerArrayList.add(new ComputerPlayer(Bag.getInstance()));
            } else if (players[i].equals("-S")){
                playerArrayList.add(new ComputerPlayer(Bag.getInstance(), new SmartStrategy()));
            } else {
                playerArrayList.add(new HumanPlayer(players[i], Bag.getInstance()));
            }
        }

        return playerArrayList;
    }
}
