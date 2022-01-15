package local.view;

import scrabble.model.Board;
import scrabble.model.Player;
import scrabble.view.UserInterface;
import scrabble.view.utils.TextBoardRepresentation;

import java.util.Scanner;

public class LocalTUI implements UserInterface {
    private Board board;
    private TextBoardRepresentation representation;
    private Player currentPlayer;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

    public LocalTUI(Board board, Player currentPlayer){
        this.board = board;
        this.representation = new TextBoardRepresentation(board);
        this.currentPlayer = currentPlayer;
    }

    @Override
    public boolean isInputValid(String input) {
        String[] words = input.split(" ");

        if(words.length == 4){
            return words[0].equalsIgnoreCase("word") &&
                    board.isFieldValid(words[1]) &&
                    words[2].equalsIgnoreCase("H") || words[1].equalsIgnoreCase("V");
                    //&& does the word fit?

        } else if(words.length == 2){
            int count = 0;

            for(int i=0; i< words[1].length(); i++){
                if(currentPlayer.getLetterDeck().getLettersInDeck().contains(words[1].charAt(i))){
                    count++;
                }
            }

            return words[0].equalsIgnoreCase("swap")
                    && count == words[1].length();

        } else if(words.length == 1){
            return words[0].equalsIgnoreCase("swap");

        } else {
            return false;
        }
    }

    @Override
    public String getInput() throws IllegalArgumentException{ //no usage found
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        if (!isInputValid(input)){
            throw new IllegalArgumentException();
        }

        scanner.close();

        return input;
    }

    @Override
    public void updateBoard() {
        printInstructions();
        System.out.println(representation);
        showRack();
    }

    @Override
    public void showRack() {
        String tiles = "\n"+currentPlayer.getName()+" has the tiles:";
        for(int i=0; i<currentPlayer.getLetterDeck().getLettersInDeck().size(); i++){
            tiles += " "+currentPlayer.getLetterDeck().getLettersInDeck().get(i);
        }
        System.out.println(tiles);
    }

    @Override
    public void printInstructions() {
        System.out.println(ANSI_GREEN+
                "1) Place a word:  'WORD' 'Start coordinate' 'Direction (H/V)' 'Word (lowercase = blank tile)' [i.e.: WORD B3 H SCRaBBLE]\n"
                +"2) Swap tiles:    'SWAP' 'Tiles you want to swap' [i.e.: SWAP ABC]\n"
                +"3) Skip turn:     'SWAP'\n\n"+ANSI_RESET

                +ANSI_RED_BACKGROUND+"Triple Word Score"+ANSI_RESET+" "
                +ANSI_PURPLE_BACKGROUND+"Double Word Score"+ANSI_RESET+" "
                +ANSI_BLUE_BACKGROUND+"Triple Letter Score"+ANSI_RESET+" "
                +ANSI_CYAN_BACKGROUND+"Double Letter Score"+ANSI_RESET+"\n");
    }
}
