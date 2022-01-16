package local.view;

import scrabble.model.Board;
import scrabble.model.Player;
import scrabble.model.PlayerList;
import scrabble.model.words.FileStreamScrabbleWordChecker;
import scrabble.view.UserInterface;
import scrabble.view.utils.TextBoardRepresentation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalTUI implements UserInterface {
    private Board board;
    private TextBoardRepresentation representation;
    private Player currentPlayer;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private FileStreamScrabbleWordChecker wordChecker = new FileStreamScrabbleWordChecker();

    public LocalTUI(Board board, Player currentPlayer){
        this.board = board;
        this.representation = new TextBoardRepresentation(board);
        this.currentPlayer = currentPlayer;
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
     * @return true if input is valid
     * @author Yasin Fahmy
     */
    public void validateInput(String input) throws IllegalArgumentException{
        String[] parts = input.split(" ");

        //Set word
        if(parts.length == 4){
            String command = parts[0]; String startCoordinate = parts[1]; String direction = parts[2]; String word = parts[3];

            if(board.isBoardEmpty() && board.fieldsCovered(startCoordinate, direction, word).contains("H8")) {
                board.setCenterCovered(true);
            }
            if(!command.equalsIgnoreCase("word")){
                throw new IllegalArgumentException("Unknown command");
            }
            if (!board.isFieldValid(startCoordinate)){
                throw new IllegalArgumentException("Start-Coordinate is not a valid field");
            }
            if(!(direction.equalsIgnoreCase("H") || direction.equalsIgnoreCase("V"))){
                throw new IllegalArgumentException("Unknown direction");
            }
            if(!board.isCenterCovered()){
                throw new IllegalArgumentException("The first word placed on the board has to cover the center");
            }
            if(!board.doesWordFit(startCoordinate, direction, word)){
                throw new IllegalArgumentException("Word does not fit on the board");
            }
            if(tilesNotOwned(currentPlayer,word) != 0){
                int lowercase = tilesNotOwned(currentPlayer,word);
                int blankTiles =currentPlayer.getLetterDeck().numberOfBlankTiles();

                if(blankTiles != lowercase){
                    throw new IllegalArgumentException("Player does not own all tiles or does not have enough blank tiles");
                }
            }
            if(word.length() > 7){
                throw new IllegalArgumentException("A Player has up to 7 tiles");
            }
//            if(!wordChecker.isValidWord(word)){
//                throw new IllegalArgumentException("Word is not a valid word");
//            }
//            if(wordChecker.isValidWord(word)){
//                //Algorithm to check if all new composed words are valid
//            }
            //Does the new word connect on another word
        }

        //Swap tiles
        else if(parts.length == 2){
            String command = parts[0]; String tiles = parts[1];

            if(!command.equalsIgnoreCase("swap")){
                throw new IllegalArgumentException("Unknown command");
            }
            if(tilesNotOwned(currentPlayer,tiles) != 0){
                throw new IllegalArgumentException("Player does not own all tiles");
            }
            if(tiles.length() > 7){
                throw new IllegalArgumentException("A Player has up to 7 tiles");
            }
        }

        //Skip turn
        else if(parts.length == 1) {
            String command = parts[0];

            if(!command.equalsIgnoreCase("swap")){
                throw new IllegalArgumentException("Unknown command");
            }
        }
    }

    /**
     * @param currentPlayer - The Player whose turn it is
     * @param tiles - The tiles the Player wants to place on board or swap
     * @return amount of tiles that are not in a players letter deck
     * @author Yasin
     */
    public int tilesNotOwned(Player currentPlayer, String tiles){
        ArrayList<Character> letterDeck = currentPlayer.getLetterDeck().getLettersInDeck();
        int count = 0;

        for(int i=0; i<tiles.length(); i++){
            if(!letterDeck.contains(tiles.charAt(i))){
                count++;
            }
        }

        return count;
    }

    /**
     * prints Scoreboard after match ends
     */
    public void printFinalScoreBoard(){
        PlayerList playerList = PlayerList.getInstance();
        List<Player> players = playerList.getPlayers();
        String scoreboard = "";
        Collections.sort(players);
        Collections.reverse(players);

        for (int i=0; i<players.size(); i++){
            scoreboard += "\n"+i+1+") "+players.get(i);
        }

        System.out.println ("*******************************"+
                            "WINNER WINNER CHICKEN DINNER!\n"+
                            players.get(0)+"has won the game"+
                            scoreboard+"\n"+
                            "*******************************"
                            );
    }

    /**
     * Updates Board with instructions, Board representation and Rack of current Player
     * @author Yasin
     */
    @Override
    public void updateBoard() {
        printInstructions();
        System.out.println(representation);
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

    /**
     * prints out instructions
     * @author Yasin
     */
    @Override
    public void printInstructions() {
        System.out.println("\n"+ANSI_GREEN+
                 "1) Place a word:      'WORD' 'Start coordinate' 'Direction (H/V)' 'Word (lowercase = blank tile)' [i.e.: WORD B3 H SCRaBBLE]\n"
                +"2) Swap tiles:        'SWAP' 'Tiles you want to swap' [i.e.: SWAP ABC]\n"
                +"3) Skip turn:         'SWAP'\n"
                //+"4) Challenge word:    'Challenge' 'Start coordinate' 'Direction (H/V)' 'Word\n"
                +ANSI_RESET
        );
    }
}
