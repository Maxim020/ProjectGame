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
            Board board = new Board();
            playerList.setPlayers(createPlayerArrayList(players, board));
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
                        processMove(move, board, playerList.getCurrentPlayer(), bag);
                    }

                } else {
                    ComputerPlayer computerPlayer = (ComputerPlayer) PlayerList.getInstance().getCurrentPlayer();
                    String move = computerPlayer.determineMove(board);
                    localTUI.validateInput(move);//Actually not necessary because the Computer should only make valid moves. But useful for testing purposes

                    processMove(move, board, playerList.getCurrentPlayer(), bag);
                }

                /** breakes out of second while loop proivded the game ended */
                if(checkEndOfGame(bag, playerList.getCurrentPlayer())){
                    System.out.println("Either a player played all tiles left or there are no more possibilities");
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
     */
    public static void processMove(String input, Board board, Player currentPlayer, Bag bag) {
        String[] parts = input.split(" ");

        if(parts[0].equalsIgnoreCase("word")){
            //WORD A1 H TEST
            exchangeTiles(parts, currentPlayer, bag, board, true); //HOW DOES THE PROGRAM HANDLE <7 TILES IN THE BAG
            board.setWord(parts[1], parts[2], parts[3]);
            board.setBoardEmpty(false);
            board.addPlayedWords(parts[1], parts[2], parts[3]);
        }
        else {
            if(parts.length == 2){
                //SWAP TEST
                exchangeTiles(parts, currentPlayer, bag, board, false);
            }
            //SWAP
            //Do nothing
        }
    }

    /**
     * The process of exchanging tiles after placing a word and swapping is different, because
     * for after placing a word you need to regard already placed tiles on a board and the correct exchange of blank tiles
     * @param parts - String array with the parts of the command/input
     * @param currentPlayer - currentPlayer that needs tiles to be exchanged
     * @param bag - a universal bag of letters
     * @param board - the board that the game is played on
     * @param word - true if tiles need to be exchanged after placing a word and false if the player just want to swap
     * @author Yasin
     */
    public static void exchangeTiles(String[] parts, Player currentPlayer, Bag bag, Board board, boolean word) {
        if(word){
            //Declaring String variables out of the parts of the input
            String coordinate = parts[1];
            String direction = parts[2];
            String tiles = parts[3];
            int[] rowcol = board.convert(coordinate);

            //Loops over length of word
            for (int i = 0; i < tiles.length(); i++) {
                if(direction.equalsIgnoreCase("h")) {
                    //Only exchange tiles if a square is not already occupied by a tile
                    if(board.isFieldEmpty(rowcol[0],(rowcol[1]+i))){
                        processExchangeTile(tiles, i, currentPlayer, bag);
                    }
                }
                else {
                    //Only exchange tiles if a square is not already occupied by a tile
                    System.out.println("board.isFieldEmpty((rowcol[0]+i),rowcol[1]): "+board.isFieldEmpty((rowcol[0]+i),rowcol[1]));
                    if(board.isFieldEmpty((rowcol[0]+i),rowcol[1])){
                        processExchangeTile(tiles, i, currentPlayer, bag);
                    }
                }
            }
        }
        else {
            //Declaring String variables out of the parts of the input
            String tiles = parts[1];

            for (int i = 0; i < tiles.length(); i++) {
                processExchangeTile(tiles, i, currentPlayer, bag); //Was passiert, wenn ich lower case swappen will?
//                currentPlayer.getLetterDeck().removeFromDeck(tiles.charAt(i));
//                bag.removeFromBag(tiles.charAt(i));
//                bag.shuffleBag();
//                currentPlayer.getLetterDeck().addToDeck(1);
            }
        }
    }

    /**
     * Process for exchanging tiles specific for after placing a word
     * @param tiles - that need to be exchanged
     * @param i - for Iteration of for loop
     * @param currentPlayer - currentPlayer that needs tiles to be exchanged
     * @param bag - a universal bag of letters
     * @author Yasin
     */
    public static void processExchangeTile(String tiles, int i, Player currentPlayer, Bag bag){
        if (Character.isLowerCase(tiles.charAt(i))) {
            currentPlayer.getLetterDeck().removeFromDeck('*'); //removes old tiles from deck
            bag.removeFromBag('*'); //removes old tile from bag --> ist das hier richtig von der logik?
        }
        else {
            System.out.println("currentPlayer.getLetterDeck().removeFromDeck: "+tiles.charAt(i));
            currentPlayer.getLetterDeck().removeFromDeck(tiles.charAt(i)); //shuffle bag
            bag.removeFromBag(tiles.charAt(i)); //Add new Tiles to deck
        }
        bag.shuffleBag();
        currentPlayer.getLetterDeck().addToDeck(1);
    }


    /**
     * @requires the nr. of total players to be at least 2 and max. 4
     * @param players a String array that holds the name (and type) of all Players
     * @return a list of all Players
     * @author Yasin
     */
    public static List<Player> createPlayerArrayList(String[] players, Board board){
        if (players.length < 2 || players.length > 4){
            throw new IllegalArgumentException();
        }

        List<Player> playerArrayList = new ArrayList<>();

        for (int i=0; i<players.length; i++){
            if (players[i].equals("-N")){
                playerArrayList.add(new ComputerPlayer(Bag.getInstance(), board));
            } else if (players[i].equals("-S")){
                playerArrayList.add(new ComputerPlayer(Bag.getInstance(), new SmartStrategy(), board));
            } else {
                playerArrayList.add(new HumanPlayer(players[i], Bag.getInstance()));
            }
        }

        return playerArrayList;
    }
}
