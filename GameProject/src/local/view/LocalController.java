package local.view;

import scrabble.model.ComputerPlayer;
import scrabble.model.HumanPlayer;
import scrabble.model.*;
import scrabble.model.letters.Bag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LocalController {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("> Please type in the name of the players with a space in between (2-4)");
        System.out.println("> If you want to play against the computer, type '-N' for a naive and '-S' for a smart computer");
        String playersNames = scanner.nextLine();
        String[] players = playersNames.split(" ");

        PlayerList playerList = PlayerList.getInstance();
        playerList.setPlayers(createPlayerArrayList(players));

        System.out.println("\n"+PlayerList.getInstance().getPlayers());



        Bag bag1 = new Bag();
        Bag bag2 = new Bag();
        HumanPlayer humanPlayer = new HumanPlayer("Joe",bag1);
        Board board = new Board();
        LocalTUI localTUI = new LocalTUI(board, humanPlayer);

        localTUI.updateBoard();

        //setUpGame(player1, player2, bag1, bag2);

        scanner.close();


    }
    public static List<Player> createPlayerArrayList(String[] players) throws IllegalArgumentException{
        if (players.length < 2 || players.length > 4){
            throw new IllegalArgumentException();
        }

        List<Player> playerArrayList = new ArrayList<>();

        for (int i=0; i<players.length; i++){
            Bag bag = new Bag();
            if (players[i].equals("-N")){
                playerArrayList.add(new ComputerPlayer(bag));
            } else if (players[i].equals("-S")){
                playerArrayList.add(new ComputerPlayer(bag, new SmartStrategy()));
            } else {
                playerArrayList.add(new HumanPlayer(players[i], bag));
            }
        }

        return playerArrayList;
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
