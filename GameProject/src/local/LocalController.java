package local;

import local.model.PlayerList;
import local.view.InputHandler;
import local.view.LocalTUI;
import scrabble.model.ComputerPlayer;
import scrabble.model.HumanPlayer;
import scrabble.model.*;
import scrabble.model.letters.Bag;
import scrabble.model.letters.DeadEndChecker;
import scrabble.model.letters.LetterScoreChecker;
import java.util.ArrayList;
import java.util.Collections;

public class LocalController {
    public static void main(String[] args) {

        boolean continueGame = true;

        while(continueGame) {
            /** Instantiates a Board, a Game, a universal PlayerList and Bag*/
            Board board = new Board();
            PlayerList playerList = PlayerList.getInstance();
            playerList.setPlayers(new InputHandler().getPlayers(board));

            Bag bag = Bag.getInstance();

            int amountOfPlayers = playerList.getPlayers().size();
            int numberOfTurn = 0;
            int playersTurn;

            while (true) { //handle turn
                /** Allows for rotation of players turn */
                playersTurn = numberOfTurn % amountOfPlayers;
                playerList.setCurrentPlayer(playersTurn);

                /** Creates localTUI and connects it with board and currentPlayer */
                LocalTUI localTUI = new LocalTUI(board, playerList.getCurrentPlayer());
                localTUI.updateBoard();

                //Make move
                InputHandler inputHandlerMove = new InputHandler(board, playerList.getCurrentPlayer());

                /** DetermineMove() and Validates input */
                if(PlayerList.getInstance().getCurrentPlayer() instanceof HumanPlayer) {
                    //Timer

                    //validates and asks for input processes move if word is valid
                    if(inputHandlerMove.askForMove()){
                        String move = inputHandlerMove.getLastInput();
                        processMove(move, board, playerList.getCurrentPlayer(), bag);
                    }
                }
                else {
                    ComputerPlayer computerPlayer = (ComputerPlayer) PlayerList.getInstance().getCurrentPlayer();
                    String move = computerPlayer.determineMove(board);


                    processMove(move, board, playerList.getCurrentPlayer(), bag);
                }

                /** breaks out of second while loop provided the game ended */
                if(checkEndOfGame(bag, playerList.getCurrentPlayer(), board) && bag.getLetterList().size() <= 7){
                    System.out.println("Either a player played all tiles left or there are no more possibilities"); //InputHandler or local TUI
                    break;
                }

                numberOfTurn++;
            }

            /** Adjusts scores and prints out final scoreboard*/
            new LocalTUI(board, PlayerList.getInstance().getCurrentPlayer())
                    .printFinalScoreBoard(
                            announceWinner(bag, PlayerList.getInstance().getCurrentPlayer())); //Umstellen!

            /** Ask for another game */
            continueGame = new InputHandler().askForNextGame();
        }
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
    public static boolean checkEndOfGame(Bag bag, Player currentPlayer, Board board){
        //Either player uses more than 10 minutes of overtime.

        boolean goingOut = bag.getLetterList().isEmpty() && currentPlayer.getLetterDeck().getLettersInDeck().isEmpty();

        boolean deadEnd = bag.getLetterList().isEmpty() && new DeadEndChecker().isDeadEnd(board);

        return goingOut || deadEnd;
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
                //SWAP ABC
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
            int tilesUsed = 0;

            //Loops over length of word
            for (int i = 0; i < tiles.length(); i++) {
                if(direction.equalsIgnoreCase("h")) {
                    //Only exchange tiles if a square is not already occupied by a tile
                    if(board.isFieldEmpty(rowcol[0],(rowcol[1]+i))){
                        processExchangeTile(tiles, i, currentPlayer, bag, word);
                        tilesUsed++;
                    }
                }
                else {
                    //Only exchange tiles if a square is not already occupied by a tile
                    if(board.isFieldEmpty((rowcol[0]+i),rowcol[1])){
                        processExchangeTile(tiles, i, currentPlayer, bag, word);
                        tilesUsed++;

                    }
                }
            }

            if(tilesUsed == 7){
                currentPlayer.addToScore(50); //Bingo!
            }
        }
        else {
            //Declaring String variables out of the parts of the input
            String tiles = parts[1];

            for (int i = 0; i < tiles.length(); i++) {
                processExchangeTile(tiles, i, currentPlayer, bag, word);

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
    public static void processExchangeTile(String tiles, int i, Player currentPlayer, Bag bag, boolean word){
        if (Character.isLowerCase(tiles.charAt(i))) {
            currentPlayer.getLetterDeck().removeFromDeck('*'); //removes old tiles from deck
            if(word){
                bag.removeFromBag('*'); //removes old tile from bag
            }
        }
        else {
            currentPlayer.getLetterDeck().removeFromDeck(tiles.charAt(i)); //shuffle bag
            if(word){
                bag.removeFromBag(tiles.charAt(i)); //Add new Tiles to deck
            }
        }
        bag.shuffleBag();
        currentPlayer.getLetterDeck().addToDeck(1); //Already removes from bag
    }
}
