package local.view;

import scrabble.model.Board;
import scrabble.model.Player;
import scrabble.model.PlayerList;
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

    @Override
    public String getInput() throws IllegalArgumentException{ //no usage found
//        Scanner scanner = new Scanner(System.in);
//        String input = scanner.nextLine();
//
//        if (!isInputValid(input)){
//            throw new IllegalArgumentException();
//        }
//
//        scanner.close();
//
        return "input";
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

            if(!command.equalsIgnoreCase("word")){
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
