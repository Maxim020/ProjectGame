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

        Board board = new Board();
        Bag bag = Bag.getInstance();
        Game game = new Game(board, bag);

        int numberOfTurn = 0;
        int playersTurn;

        while (game.isGameRunning()){

            /** Allows for rotation of players turn */
            playersTurn = numberOfTurn%amountOfPlayers;
            playerList.setCurrentPlayer(playersTurn);

            /** Creates localTUI and connects it with board and currentPlayer */
            LocalTUI localTUI = new LocalTUI(board, playerList.getCurrentPlayer());
            localTUI.updateBoard();

            //Timer of Opponent starts --> revert move within timeframe?

            /** Validate input */
            String move = scanner.nextLine();
            localTUI.validateInput(move);


            // Player makes move
            processMove(move, board, playerList.getCurrentPlayer(),bag); //Challenge word?

            /***Announces new Score only if a new word is placed*/
            if (move.charAt(0) == 'W' || move.charAt(0) == 'w'){localTUI.updateBoard();}

            //CheckWinner()
                //No more tiles in bag and in one players rack
                //At least six successive scoreless turns have occurred and either player decides to end the game
                //Either player uses more than 10 minutes of overtime.

            numberOfTurn++;
        }

        //When the game ends, each player's score is reduced by the sum of their unused letters;
        // in addition, if a player has used all of their letters (known as "going out" or "playing out"),
        // the sum of all other players' unused letters is added to that player's score. In tournament play,
        // a player who goes out adds twice that sum, and their opponent is not penalized.

        //Print Scoreboard

        //Ask for another game

        scanner.close();
    }

    /**
     * processes input to make a move
     * @requires the input to be valid
     * @param input the input that the user wrote
     * @author Yasin
     */
    public static void processMove(String input, Board board, Player currentPlayer, Bag bag){
        String[] parts = input.split(" ");

        if(parts[0].equalsIgnoreCase("word")){
            //WORD A1 H TEST
            board.setWord(parts[1], parts[2], parts[3]);
            exchangeTiles(parts[3], currentPlayer, bag); //HOW DOES THE PROGRAM HANDLE <7 TILES IN THE BAG
        }
        else {
            if(parts.length == 2){
                //SWAP TEST
                exchangeTiles(parts[1], currentPlayer, bag);
            }
            //SWAP
        }
        board.setBoardEmpty(false);
    }

    /**
     * @param tiles that need to be exchanged
     * @param currentPlayer - The player who is currently making the move
     * @param bag - a universal bag of letters
     */
    public static void exchangeTiles(String tiles, Player currentPlayer, Bag bag){

        for (int i=0; i<tiles.length(); i++) {
            if(Character.isLowerCase(tiles.charAt(i))){
                currentPlayer.getLetterDeck().removeFromDeck('*'); //removes blank tile if a letter is lowercase
            }
            else {
                currentPlayer.getLetterDeck().removeFromDeck(tiles.charAt(i)); //removes old tiles from deck
            }
        }

        bag.shuffleBag();
        currentPlayer.getLetterDeck().addToDeck(tiles.length()); //Add new Tiles to deck
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
