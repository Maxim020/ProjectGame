package local.view;

import scrabble.model.*;
import scrabble.model.exceptions.*;
import scrabble.model.letters.Bag;
import scrabble.model.letters.CrossChecker;
import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.InMemoryScrabbleWordChecker;
import scrabble.model.words.IsAdjacentChecker;
import scrabble.model.words.ScrabbleWordChecker;
import scrabble.view.utils.TextIO;
import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    private Board board;
    private Player currentPlayer;
    private Bag bag;
    private ScrabbleWordChecker wordChecker = new InMemoryScrabbleWordChecker();
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

    public InputHandler(){
        //this needs to be here smh for the program to work
    }

    /**
     * @param board
     * @return an ArrayList of Players
     */
    public List<Player> getPlayers(Board board){
        /** Scans user input for the name of all players and if wanted any computer players */
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
                    UnknownCommandException | UnknownDirectionException | WordDoesNotFitException | WordIsNotAdjacentException e) {

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
            /**
             * The order of these checker is important. Do not change, unless it would solve a new bug
             */

            if(wordChecker.isValidWord(word) == null) {
                throw new InvalidWordException();
            } else {
                //If one of the new composed words is invalid: throw new InvalidWordException - ADDED
                if(!adjacentWordChecker.areAdjacentWordsValid(startCoordinate, direction, word)) {
                    throw new InvalidWordException();
                }
            }

            int lowercase = tilesNotOwned(parts,currentPlayer,board,true);
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

            if(CrossChecker.isMoveValid(board,startCoordinate,direction,word)){
                throw new scrabble.model.exceptions.InvalidCrossException();
            }
        }

        //Swap tiles
        else if(parts.length == 2){
            String command = parts[0]; String tiles = parts[1];

            if(!command.equalsIgnoreCase("swap")){
                throw new UnknownCommandException();
            }
            if(tilesNotOwned(parts,currentPlayer,board,false) != 0){
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
        else if(parts.length == 1) {
            String command = parts[0];

            if(!command.equalsIgnoreCase("swap")){
                throw new UnknownCommandException();
            }
        }
    }

    /**
     * The process of checking not owned tiles is different when wanting to place a word and swapping, because
     * the algorithm needs to check the already placed tiles on the board, so that it does not assume they are
     * not owned by the player
     * @param parts - String array with the parts of the command/input
     * @param currentPlayer- currentPlayer that is checked for now owned tiles
     * @param board - the board that the game is played on
     * @param word - true if tiles not owned refer to placing a word on the board and false if it refers to swapping
     * @return the number of tiles not owned by the player
     * @author Yasin
     */
    public int tilesNotOwned(String[] parts, Player currentPlayer, Board board, boolean word){
        ArrayList<Character> letterDeck = currentPlayer.getLetterDeck().getLettersInDeck();

        int count = 0;

        if(word){
            String coordinate = parts[1];
            String direction = parts[2];
            String tiles = parts[3];
            int[] rowcol = board.convert(coordinate);
            int length = tiles.length();

            for (int i = 0; i < length; i++) {
                if (direction.equalsIgnoreCase("h")) {
                    if (!letterDeck.contains(tiles.charAt(i)) && board.isFieldEmpty(rowcol[0], (rowcol[1] + i))) {
                        count++;
                    }
                }
                else{
                    if (!letterDeck.contains(tiles.charAt(i)) && board.isFieldEmpty((rowcol[0]+i),rowcol[1])){
                        count++;
                    }
                }
            }
        }
        else {
            String tiles = parts[1];
            int length = tiles.length();

            for (int i = 0; i < length; i++) {
                if (!letterDeck.contains(tiles.charAt(i))) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @return true if user wants to play another game
     * @author Yasin
     */
    public boolean askForNextGame(){
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
