package scrabble.model;

import local.model.ComputerPlayer;
import local.model.HumanPlayer;
import scrabble.model.letters.Bag;

import java.util.ArrayList;

public class Game {
	
	//Attributes
	private ArrayList<Player> listOfPlayers; //Use PlayerList Class
	private Board board;
	private Bag bag;
	
	//Constructor
	public Game() {
		listOfPlayers = new ArrayList<>();
		board = new Board();
		bag = new Bag();
	}
	
	//Methods
	
	/**
	 * This method creates and adds a human player to the player list
	 * @param String name
	 * @ensures listOfPlayers.contains(player)
	 * @requires listOfPlayers != null && name != null
	 * @author Maxim 
	 */
	public void addHumanPlayer(String name) {
		if (name == null) {System.out.println("Player must have a name");}
		else if (listOfPlayers.size() == 4) {System.out.println("There cannot be more than 4 players");}
		else {
			Player player = new HumanPlayer(name, bag);
			listOfPlayers.add(player);
		}
	}
	
	/**
	 * This method creates and adds a computer player to the player list
	 * @param String name
	 * @ensures listOfPlayers.contains(player)
	 * @requires listOfPlayers != null && name != null
	 * @author Maxim 
	 */
	public void addComputerPlayer(String name) {
		if (name == null) {System.out.println("Player must have a name");}
		else if (listOfPlayers.size() == 4) {System.out.println("There cannot be more than 4 players");}
		else {
			Player player = new ComputerPlayer(name, bag);
			listOfPlayers.add(player);
		}
	}
	
	/**
	 * This method is executed when a game is set up and is ready to start. This is done manually by the user
	 * WIP
	 */
	public void start() {
		if(listOfPlayers.size() < 2) {System.out.println("A game must have at least two players");}
		else {
			
		}
	}
}
