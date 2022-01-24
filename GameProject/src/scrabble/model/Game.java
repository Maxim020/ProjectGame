package scrabble.model;

import scrabble.model.letters.Bag;

import java.util.ArrayList;

public class Game {
	private PlayerList playerList;
	private Board board;
	private Bag bag;
	boolean continueGame;
	boolean isGameRunning;

	public Game(Board board, Bag bag) {
		this.playerList = PlayerList.getInstance();
		this.board = board;
		this.bag = bag;
		continueGame = false;
		isGameRunning = true;
	}


	/**
	 * This method is executed when a game is set up and is ready to start. This is done manually by the user
	 * WIP
	 */
	public void start() {
		while (isGameRunning){
			board.reset();
			processTurns();
			/**
			 * Scanner does not work
			 */

//			System.out.println("\n> Play another time? (y/n)");
//			Scanner scanner = new Scanner(System.in);
//			String input = scanner.nextLine();
//			scanner.close();
//			continueGame = input.equals("y");
		}


	}

	public void processTurns(){

	}

	public PlayerList getPlayerList() {
		return playerList;
	}

	public void setPlayerList(PlayerList playerList) {
		this.playerList = playerList;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Bag getBag() {
		return bag;
	}

	public void setBag(Bag bag) {
		this.bag = bag;
	}

	public boolean isContinueGame() {
		return continueGame;
	}

	public void setContinueGame(boolean continueGame) {
		this.continueGame = continueGame;
	}

	public boolean isGameRunning() {
		return isGameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
		isGameRunning = gameRunning;
	}
}
