package local.view;

import local.model.ComputerPlayer;
import local.model.HumanPlayer;
import scrabble.model.Board;
import scrabble.model.Game;
import scrabble.model.Player;
import scrabble.model.SmartStrategy;
import scrabble.model.letters.Bag;

import java.util.Scanner;

public class LocalController {
    public static void main(String[] args) {
        Bag bag1 = new Bag();
        Bag bag2 = new Bag();
        HumanPlayer humanPlayer = new HumanPlayer("Joe",bag1);
        Board board = new Board();
        LocalTUI localTUI = new LocalTUI(board, humanPlayer);

        Scanner scanner = new Scanner(System.in);
        System.out.println("If you want to play against the computer, type '-N' for a naive and '-S' for a smart computer\n");
        System.out.println("Please input the name of player 1");
        String player1 = scanner.nextLine();

        System.out.println("Please input the name of player 2");
        String player2 = scanner.nextLine();

        scanner.close();
        System.out.println(player1);System.out.println(player2);

        localTUI.updateBoard();

        setUpGame(player1, player2, bag1, bag2);



    }

    public static void setUpGame(String player1, String player2, Bag bag1, Bag bag2){
        if(player1.equals("-N")) {
            Player p1 = new ComputerPlayer(bag1);
            Player p2 = new HumanPlayer(player2,bag2);
            Game game = new Game(p1,p2);
            game.start();

        }

        else if(player2.equals("-N")) {
            Player p1 = new HumanPlayer(player1,bag1);
            Player p2 = new ComputerPlayer(bag2);
            Game game = new Game(p1,p2);
            game.start();
        }

        else if(player1.equals("-S")) {
            Player p1 = new ComputerPlayer(bag1, new SmartStrategy());
            Player p2 = new HumanPlayer(player2, bag2);
            Game game = new Game(p1,p2);
            game.start();

        }

        else if(player2.equals("-S")) {
            Player p1 = new HumanPlayer(player1, bag1);
            Player p2 = new ComputerPlayer(bag2, new SmartStrategy());
            Game game = new Game(p1,p2);
            game.start();
        }

        else if(player1.equals("-N") && player2.equals("-S")) {
            Player p1 = new ComputerPlayer(bag1);
            Player p2 = new ComputerPlayer(bag2, new SmartStrategy());
            Game game = new Game(p1,p2);
            game.start();
        }

        else if(player2.equals("-N") && player1.equals("-S")) {
            Player p1 = new ComputerPlayer(bag1, new SmartStrategy());
            Player p2 = new ComputerPlayer(bag2);
            Game game = new Game(p1,p2);
            game.start();
        }

        else {
            Player p1 = new HumanPlayer(player1, bag1);
            Player p2 = new HumanPlayer(player2, bag2);
            Game game = new Game(p1,p2);
            game.start();
        }
    }
}
