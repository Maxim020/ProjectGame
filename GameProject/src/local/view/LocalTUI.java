package local.view;

import scrabble.model.Board;
import scrabble.model.Player;
import scrabble.view.UserInterface;
import scrabble.view.utils.TextBoardRepresentation;
import java.util.ArrayList;

public class LocalTUI implements UserInterface {
    private Board board;
    private TextBoardRepresentation representation;
    private Player currentPlayer;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

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
     * @param input from the user
     * @return true if input is valid
     * @author Yasin Fahmy
     */
    public boolean isInputValid(String input) {
        String[] parts = input.split(" ");

        //Set word
        if(parts.length == 4){
            String command = parts[0]; String startCoordinate = parts[1];
            String direction = parts[2]; String word = parts[3];

            if(board.isBoardEmpty() && board.fieldsCovered(startCoordinate, direction, word).contains("H8")) {
                board.setCenterCovered(true);
            }

            return command.equalsIgnoreCase("word") &&
                    board.isFieldValid(startCoordinate) && //checks if start coordinate is valid
                    (direction.equalsIgnoreCase("H") || direction.equalsIgnoreCase("V")) && //checks if direction is valid
                    board.isCenterCovered() && //checks if center is covered in the beginning of the game
                    board.doesWordFit(startCoordinate, direction, word) && //does word fit on board
                    doesPlayerOwnTheseTiles(currentPlayer,word);
                    //Wort an wort !!
                    //Is word
        }

        //Swap tiles
        else if(parts.length == 2){
            String command = parts[0]; String tiles = parts[1];

            return command.equalsIgnoreCase("swap") &&
                    doesPlayerOwnTheseTiles(currentPlayer, tiles);
        }

        //Skip turn
        else if(parts.length == 1) {
            String command = parts[0];
            return command.equalsIgnoreCase("swap");

        }

        return false;
    }

    /**
     * @param currentPlayer - The Player whose turn it is
     * @param tiles - The tiles the Player wants to place on board or swap
     * @return true if Player owns all tiles
     * @author Yasin
     */
    public boolean doesPlayerOwnTheseTiles(Player currentPlayer, String tiles){
        ArrayList<Character> letterDeck = currentPlayer.getLetterDeck().getLettersInDeck();
        int count = 0;

        for(int i=0; i<tiles.length(); i++){
            if(letterDeck.contains(tiles.charAt(i))){
                count++;
            }
        }

        return count == tiles.length();
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
                "1) Place a word:  'WORD' 'Start coordinate' 'Direction (H/V)' 'Word (lowercase = blank tile)' [i.e.: WORD B3 H SCRaBBLE]\n"
                +"2) Swap tiles:    'SWAP' 'Tiles you want to swap' [i.e.: SWAP ABC]\n"
                +"3) Skip turn:     'SWAP'\n"+ANSI_RESET
        );
    }
}
