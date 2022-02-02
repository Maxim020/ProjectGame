package local;

import scrabble.model.player.PlayerList;
import local.utils.InputHandler;
import server.view.ServerTUI;
import scrabble.model.player.ComputerPlayer;
import scrabble.model.player.HumanPlayer;
import scrabble.model.*;
import scrabble.model.Bag;

public class LocalController {
    public static void main(String[] args) {

        boolean continueGame = true;

        while(continueGame) {
            /** Instantiates a Board, a Game, a universal PlayerList and Bag*/
            Board board = new Board();
            PlayerList playerList = PlayerList.getInstance();
            playerList.setPlayers(InputHandler.getPlayers(board));

            Bag bag = Bag.getInstance();

            int amountOfPlayers = playerList.getPlayers().size();
            int numberOfTurn = 0;
            int playersTurn;

            while (true) { //handle turn
                /** Allows for rotation of players turn */
                playersTurn = numberOfTurn % amountOfPlayers;
                playerList.setCurrentPlayer(playersTurn);

                /** Creates localTUI and connects it with board and currentPlayer */
                ServerTUI serverTUI = new ServerTUI(board, playerList.getCurrentPlayer());
                serverTUI.updateBoard();

                //Make move
                InputHandler inputHandlerMove = new InputHandler(board, playerList.getCurrentPlayer());

                /** DetermineMove() and Validates input */
                if(PlayerList.getInstance().getCurrentPlayer() instanceof HumanPlayer) {

                    //validates and asks for input processes move if word is valid
                    if(inputHandlerMove.askForMove()){
                        String move = inputHandlerMove.getLastInput();
                        Game.processMove(move, board, playerList.getCurrentPlayer(), bag);
                    }
                }
                else {
                    ComputerPlayer computerPlayer = (ComputerPlayer) PlayerList.getInstance().getCurrentPlayer();
                    String move = computerPlayer.determineMove(board);


                    Game.processMove(move, board, playerList.getCurrentPlayer(), bag);
                }

                /** breaks out of second while loop provided the game ended */
                if(Game.checkEndOfGame(bag, playerList.getCurrentPlayer(), board) && bag.getLetterList().size() <= 7){
                    System.out.println("Either a player played all tiles left or there are no more possibilities"); //InputHandler or local TUI
                    break;
                }

                numberOfTurn++;
            }

            /** Adjusts scores and prints out final scoreboard*/

            Game.adjustScores(bag, playerList.getCurrentPlayer());
            ServerTUI.printFinalScoreBoard();

            /** Ask for another game */
            continueGame = InputHandler.askForNextGame();
        }
    }
}
