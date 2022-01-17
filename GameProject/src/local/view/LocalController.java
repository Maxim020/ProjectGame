package local.view;

import scrabble.model.ComputerPlayer;
import scrabble.model.HumanPlayer;
import scrabble.model.*;
import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterScoreChecker;

import java.util.ArrayList;
import java.util.Collections;
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
        boolean continueGame = true;
        Scanner scanner = new Scanner(System.in);

        while(continueGame) {
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

            while (game.isGameRunning()) {

                /** Allows for rotation of players turn */
                playersTurn = numberOfTurn % amountOfPlayers;
                playerList.setCurrentPlayer(playersTurn);

                /** Creates localTUI and connects it with board and currentPlayer */
                LocalTUI localTUI = new LocalTUI(board, playerList.getCurrentPlayer());
                localTUI.updateBoard();

                //Timer of Opponent starts --> revert move within timeframe?

                /** Validate input */
                String move = scanner.nextLine();
                localTUI.validateInput(move);

                // Player makes move
                processMove(move, board, playerList.getCurrentPlayer(), bag); //Controlflow for computerplayers
                //If a user plays an invalid word the player gets back his tiles and loses his turn

                /** Announces new Score only if a new word is placed*/
                if (move.charAt(0) == 'W' || move.charAt(0) == 'w') {
                    localTUI.updateBoard();
                }

                /** breakes out of second while loop proivded the game ended */
                if(checkEndOfGame(bag, playerList.getCurrentPlayer())){
                    break;
                }

                numberOfTurn++;
            }

            /** Adjusts scores and prints out final scoreboard*/
            new LocalTUI(board, PlayerList.getInstance().getCurrentPlayer())
                    .printFinalScoreBoard(
                            announceWinner(bag, PlayerList.getInstance().getCurrentPlayer()));

            /** Ask for another game */
            System.out.println("Do you want to play another Game? (y/n)");
            String decision = scanner.nextLine();
            continueGame = decision.equals("y");

        }

        scanner.close();
    }

    /**
     * @param bag - a universal bag of letters
     * @param currentPlayer - The player who has just made a move
     * @return - winner of the game and adjusts Scores
     * @author Yasin
     */
    public static Player announceWinner(Bag bag, Player currentPlayer){
        LetterScoreChecker letterScoreChecker = new LetterScoreChecker();
        boolean goingOut = bag.getLetterList().isEmpty() && currentPlayer.getLetterDeck().getLettersInDeck().isEmpty();
        int sumOfUnplacedTiles = 0;
        Player winnerBeforeAdjustment = Collections.max(PlayerList.getInstance().getPlayers());

        //Each player's score is reduced by the sum of his or her unplaced letters.
        for (int i=0; i<PlayerList.getInstance().getPlayers().size(); i++){
            ArrayList<Character> letterDeck = PlayerList.getInstance().getPlayers().get(i).getLetterDeck().getLettersInDeck();
            for (int j=0; j<letterDeck.size(); i++){
                sumOfUnplacedTiles += letterScoreChecker.scoreChecker(letterDeck.get(j));
                PlayerList.getInstance().getPlayers().get(i).subtractScore(letterScoreChecker.scoreChecker(letterDeck.get(j)));
            }
        }

        //In addition, if a player has used all of his or her letters, the sum of the other players' unplaced letters is added to that player's score.
        if(goingOut){
            for (int i=0; i<PlayerList.getInstance().getPlayers().size(); i++){
                ArrayList<Character> letterDeck = PlayerList.getInstance().getPlayers().get(i).getLetterDeck().getLettersInDeck();
                if(letterDeck.isEmpty()){
                    PlayerList.getInstance().getPlayers().get(i).addToScore(sumOfUnplacedTiles);
                }
            }
        }

        //Sort PlayerList in descending order
        Collections.sort(PlayerList.getInstance().getPlayers());
        Collections.reverse(PlayerList.getInstance().getPlayers());
        Player winnerAfterAdjustment = Collections.max(PlayerList.getInstance().getPlayers());

        //The player with the highest final score wins the game. In case of a tie, the player with the highest score before adding or deducting unplaced letters wins
        if (PlayerList.getInstance().getPlayers().get(0).getScore() == PlayerList.getInstance().getPlayers().get(1).getScore()){
            return winnerBeforeAdjustment;
        } else {
            return winnerAfterAdjustment;
        }
    }

    /**
     * Game ends if:
     * 1) Either player uses more than 10 minutes of overtime.
     * 2) The game ends when all letters have been drawn and one player uses his or her last letter (known as "going out")
     * 3) When all possible plays have been made
     * @param bag - a universal bag of letters
     * @param currentPlayer - The player who has just made a move
     * @return - return true if game ended
     * @author Yasin
     */
    public static boolean checkEndOfGame(Bag bag, Player currentPlayer){
        //Either player uses more than 10 minutes of overtime.

        boolean goingOut = bag.getLetterList().isEmpty() && currentPlayer.getLetterDeck().getLettersInDeck().isEmpty();

        //boolean deadEnd = ???

        return goingOut; //|| deadEnd;
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
     * @author Yasin
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
