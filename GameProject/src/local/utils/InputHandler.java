package local.utils;

import scrabble.model.player.ComputerPlayer;
import scrabble.model.player.HumanPlayer;
import scrabble.model.player.PlayerList;
import scrabble.model.*;
import scrabble.model.exceptions.*;
import scrabble.model.Bag;
import scrabble.model.checker.CrossChecker;
import scrabble.model.player.Player;
import scrabble.model.checker.AdjacentWordChecker;
import scrabble.model.checker.InMemoryScrabbleWordChecker;
import scrabble.model.checker.IsAdjacentChecker;
import scrabble.model.checker.ScrabbleWordChecker;
import scrabble.model.strategy.SmartStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to receive, process and validate input from a user, when playing a local game
 */

public class InputHandler {
    private Board board;
    private Player currentPlayer;
    private Bag bag;
    private final ScrabbleWordChecker wordChecker = new InMemoryScrabbleWordChecker();
    private AdjacentWordChecker adjacentWordChecker;
    private IsAdjacentChecker isAdjacentChecker;
    private String lastInput;

    public InputHandler(Board board, Player currentPlayer){
        this.board = board;
        this.adjacentWordChecker = new AdjacentWordChecker(board);
        this.isAdjacentChecker = new IsAdjacentChecker(board);
        this.currentPlayer = currentPlayer;
        this.bag = Bag.getInstance();
    }

    /**
     * @return an ArrayList of Players
     * Scans user input for the name of all players and if wanted any computer players
     */
    public static List<Player> getPlayers(){
        String[] players;
        int amountOfPlayers;

        while (true) {
            System.out.println("> Please type in the name of the players with a space in between (2-4)");
            System.out.println("> If you want to play against the computer, type '-N' for a naive and '-S' for a smart computer");
            String playersNames = TextIO.getlnString();
            players = playersNames.split(" ");
            amountOfPlayers = players.length;

            if (amountOfPlayers < 2 || amountOfPlayers > 4){
                System.out.println("\nScrabble needs at least 2 and maximum 4 Players");
            } else {
                break;
            }
        }

        List<Player> playerArrayList = new ArrayList<>();

        for (int i=0; i<players.length; i++) {
            if (players[i].equals("-N")) {
                playerArrayList.add(new ComputerPlayer(Bag.getInstance(), "NaiveComputerPlayer"+i));
            } else if (players[i].equals("-S")) {
                playerArrayList.add(new ComputerPlayer(Bag.getInstance(), new SmartStrategy(), "SmartComputerPlayer"+i));
            } else {
                playerArrayList.add(new HumanPlayer(players[i], Bag.getInstance()));
            }
        }

        return playerArrayList;
    }

    /**
     * Input validation also happens in this method
     * @return true if input is valid
     */
    public boolean askForMove(){
        boolean isWordValid;

        String move = TextIO.getlnString();

        while (true) {
            try {
                validateInput(move);
                setLastInput(move);
                isWordValid = true;
                break;

            } catch (CenterIsNotCoveredException | FieldDoesNotExistException | IllegalSwapException | NotEnougTilesException | NotEnoughBlankTilesException |
                    UnknownCommandException | UnknownDirectionException | WordDoesNotFitException | WordIsNotAdjacentException | InvalidCrossException e) {

                e.printStackTrace();
                System.out.println("Please type in a valid move");
                move = TextIO.getlnString();

            } catch (InvalidWordException e) {
                e.printStackTrace();
                System.out.println(PlayerList.getInstance().getCurrentPlayer().getName() + " lost his turn");
                setLastInput(move);
                isWordValid = false;
                break;
            }
        }

        return isWordValid;
    }

    /**
     * Throws IllegalArgumentException provided the input is not valid
     * The order of these checker is important. Do not change, unless it would solve a new bug
     * @param input from the user
     * @author Yasin Fahmy
     */
    public void validateInput(String input) throws IllegalArgumentException{
        String[] parts = input.split(" ");

        if(!(parts.length == 4 | parts.length == 2 | parts.length == 1)){
            throw new UnknownCommandException();
        }

        //Set word
        if(parts.length == 4){
            String command = parts[0]; String startCoordinate = parts[1]; String direction = parts[2]; String word = parts[3];

            if(wordChecker.isValidWord(word) == null) {
                throw new InvalidWordException();
            } else {
                //If one of the new composed words is invalid: throw new InvalidWordException - ADDED
                if(!adjacentWordChecker.areAdjacentWordsValid(startCoordinate, direction, word)) {
                    throw new InvalidWordException();
                }
            }

            int lowercase = Game.tilesNotOwned(parts,currentPlayer,board,true);
            if(lowercase != 0){
                int blankTiles =currentPlayer.getLetterDeck().numberOfBlankTiles();

                if(blankTiles != lowercase){
                    throw new NotEnoughBlankTilesException();
                }
            }

            if(board.isCenterCovered()) {
                if(!isAdjacentChecker.isAdjacent(startCoordinate, direction, word)) {
                    throw new WordIsNotAdjacentException();
                }
            }

            if(board.isBoardEmpty() && board.fieldsCovered(startCoordinate, direction, word).contains("H8")) {
                board.setCenterCovered(true);
            }

            if(!CrossChecker.isMoveValid(board,startCoordinate,direction,word)){
                throw new scrabble.model.exceptions.InvalidCrossException();
            }

            if(!command.equalsIgnoreCase("word") || startCoordinate.length() < 2 || startCoordinate.length() > 3 || direction.length() != 1){
                throw new UnknownCommandException();
            }

            if (!board.isFieldValid(startCoordinate)){
                throw new FieldDoesNotExistException();
            }

            if(!(direction.equalsIgnoreCase("H") || direction.equalsIgnoreCase("V"))){
                throw new UnknownDirectionException();
            }

            if(!board.isCenterCovered()){
                throw new CenterIsNotCoveredException();
            }

            if(!board.doesWordFit(startCoordinate, direction, word)){
                throw new WordDoesNotFitException();
            }

        }

        //Swap tiles
        else if(parts.length == 2){
            String command = parts[0]; String tiles = parts[1];

            if(!command.equalsIgnoreCase("swap")){
                throw new UnknownCommandException();
            }
            if(Game.tilesNotOwned(parts,currentPlayer,board,false) != 0){
                throw new IllegalSwapException();
            }
            if(tiles.length() > 7){
                throw new IllegalSwapException();
            }
            if(tiles.length() > bag.getLetterList().size()){
                throw new NotEnougTilesException();
            }
        }

        //Skip turn
        else {
            String command = parts[0];

            if(!command.equalsIgnoreCase("swap")){
                throw new UnknownCommandException();
            }
        }
    }

    /**
     * @return true if user wants to play another game
     * @author Yasin
     */
    public static boolean askForNextGame(){
        System.out.println("Do you want to play another Game? (y/n)");
        String decision = TextIO.getlnString();
        return decision.equals("y");
    }

    public String getLastInput() {
        return lastInput;
    }

    public void setLastInput(String lastInput) {
        this.lastInput = lastInput;
    }
}
