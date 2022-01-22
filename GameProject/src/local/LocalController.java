package local;

import local.view.LocalTUI;
import scrabble.model.ComputerPlayer;
import scrabble.model.HumanPlayer;
import scrabble.model.*;
import scrabble.model.exceptions.*;
import scrabble.model.letters.Bag;
import scrabble.model.letters.LetterScoreChecker;
import scrabble.view.utils.Countdown;

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

            /** Scans user input for the name of all players and if wanted any computer players */
            String[] players;
            int amountOfPlayers;

            while (true) {
                System.out.println("> Please type in the name of the players with a space in between (2-4)");
                System.out.println("> If you want to play against the computer, type '-N' for a naive and '-S' for a smart computer");
                String playersNames = scanner.nextLine();
                players = playersNames.split(" ");
                amountOfPlayers = players.length;

                if (amountOfPlayers < 2 || amountOfPlayers > 4){
                    System.out.println("\nScrabble needs at least 2 and maximum 4 Players");
                } else {
                    break;
                }
            }

            /** Instantiates universal PlayerList, Bag and other important objects */
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

                /** DetermineMove() and Validates input */
                boolean updateTUI = false;

                if(PlayerList.getInstance().getCurrentPlayer() instanceof HumanPlayer) {
                    /** Timer starts
                    //Timer of Opponent starts --> revert move within timeframe?
                    int counter = 60;

                    Countdown countdown = new Countdown();
                    System.out.println("\nYou have one minute to decide!");
                    countdown.counter(counter);
                    */


                    String move = scanner.nextLine();




                    //Program keeps asking user for input except if it's an InvalidWordException. Then the user loses his/her turn
                    boolean isWordValid; //Zusammenspiel von updateTUI updateTUI

                    while (true) {
                        try {
                            localTUI.validateInput(move);
                            isWordValid = true;
                            break;

                        } catch (CenterIsNotCoveredException | FieldDoesNotExistException | IllegalSwapException | NotEnougTilesException | NotEnoughBlankTilesException |
                                UnknownCommandException | UnknownDirectionException | WordDoesNotFitException | WordIsNotAdjacentException e) {

                            e.printStackTrace();
                            System.out.println("Please type in a valid move");
                            move = scanner.nextLine();

                        } catch (InvalidWordException e) {
                            e.printStackTrace();
                            System.out.println(PlayerList.getInstance().getCurrentPlayer().getName() + " lost his turn");
                            isWordValid = false;
                            break;
                        }
                    }
                    if(isWordValid) {
                        updateTUI = processMove(move, board, playerList.getCurrentPlayer(), bag);
                    }

                } else {
                    ComputerPlayer computerPlayer = (ComputerPlayer) PlayerList.getInstance().getCurrentPlayer();
                    String move = computerPlayer.determineMove(board);
                    localTUI.validateInput(move);//Actually not necessary because the Computer should only make valid moves. But useful for testing purposes

                    updateTUI = processMove(move, board, playerList.getCurrentPlayer(), bag);
                }


                /** Announces new Score only if a new word is placed*/
                if (updateTUI) {
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
                            announceWinner(bag, PlayerList.getInstance().getCurrentPlayer())); //Umstellen!

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
            for (int j=0; j<letterDeck.size(); j++){
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
     * processes input to make a move and return
     * @requires the input to be valid
     * @param input the input that the user wrote
     * @author Yasin
     * @return true if the move was placing a word on the board
     */
    public static boolean processMove(String input, Board board, Player currentPlayer, Bag bag) {
        String[] parts = input.split(" ");

        if(parts[0].equalsIgnoreCase("word")){
            //WORD A1 H TEST
            board.setWord(parts[1], parts[2], parts[3]);
            exchangeTiles(parts[3], currentPlayer, bag); //HOW DOES THE PROGRAM HANDLE <7 TILES IN THE BAG
            board.setBoardEmpty(false);
            return true;
        }
        else {
            if(parts.length == 2){
                //SWAP TEST
                exchangeTiles(parts[1], currentPlayer, bag);
            }
            //SWAP
            return false;
        }
    }

    /**
     * @param tiles that need to be exchanged
     * @param currentPlayer - The player who is currently making the move
     * @param bag - a universal bag of letters
     * @author Yasin
     */
    public static void exchangeTiles(String tiles, Player currentPlayer, Bag bag) {

        for (int i=0; i<tiles.length(); i++) {
            if(Character.isLowerCase(tiles.charAt(i))){
                currentPlayer.getLetterDeck().removeFromDeck('*'); //removes blank tile if a letter is lowercase
                bag.removeFromBag('*');
            }
            else {
                currentPlayer.getLetterDeck().removeFromDeck(tiles.charAt(i)); //removes old tiles from deck
                bag.removeFromBag(tiles.charAt(i));
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
    public static List<Player> createPlayerArrayList(String[] players){
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
