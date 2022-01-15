package scrabble.model;

import scrabble.model.letters.Bag;

import java.util.ArrayList;

public class Game {
	
	//Attributes
	private ArrayList<Player> listOfPlayers; //Use PlayerList Class
	private Board board;
	private Bag bag;
	
	//Constructor
	public Game(Player p1, Player p2) {
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

	/**
	 * There is a DesignPattern called Singleton (PlayerList Class) that provides a universal and static list of players. I had to make these changes, because otherwise it wouldn't compile
	 * I didnt yet adapt the java doc, because I'm not 100% sure whats the best way to do it, Im just testing things out. I will look into the Design Pattern tmrw probably
	 */
	public void addComputerPlayer(Strategy strategy) {
		//if (name == null) {System.out.println("Player must have a name");}
		//else
			if (listOfPlayers.size() == 4) {System.out.println("There cannot be more than 4 players");}
		else {
			Player player = new ComputerPlayer(bag, strategy); //The arguments had to be changed. From name, bag to bag, Strategy
			listOfPlayers.add(player);
		}
	}
	
	/**
	 * This method is executed when a game is set up and is ready to start. This is done manually by the user
	 * WIP
	 */
	public void start() {
		boolean continueGame = true;
		while (continueGame){
			board.reset();
			play();
			/**
			 * Scanner does not work
			 */
//			System.out.println("\n> Play another time? (y/n)");
//			Scanner scanner = new Scanner(System.in);
//			String input = scanner.nextLine();
//			scanner.close();
//			continueGame = input.equals("y");
		}

//		if(listOfPlayers.size() <= 2) {System.out.println("A game must have at least two players");}
//		else {
//
//		}
	}

	public void play(){

	}
}
