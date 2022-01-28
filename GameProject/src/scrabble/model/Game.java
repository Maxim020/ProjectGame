package scrabble.model;

import client.controller.Client;
import client.controller.ClientHandler;
import local.model.PlayerList;
import scrabble.model.letters.Bag;

import java.util.ArrayList;

/**
 * Game logic
 */
public class Game {

	private ClientHandler ch1;
	private ClientHandler ch2;
	private Board board;
	private Bag bag;

	private PlayerList playerList;
	boolean continueGame = false;
	boolean isGameRunning = true;

	//Each game needs a list of
	public Game(ClientHandler ch1, ClientHandler ch2) {
		this.ch1 = ch1;
		this.ch2 = ch2;
	}

	public void start() {
		this.board = new Board();
		this.bag = Bag.getInstance();
		this.playerList = PlayerList.getInstance();
	}

	/**
	 * Processes move by client on board
	 * @param client
	 * @param move
	 */
	public synchronized void makeMove(ClientHandler client, String move){
		//To be implemented
	}

	public int scoreMove(){
		//To be implemented
		return -1;
	}

	public void clientDisconnected(ClientHandler disconnectedClient){
		if(disconnectedClient.equals(ch1)){
			ch2.sendMessage("GAME OVER; OTHER CLIENT DISCONNECTED");
			ch2.setGame(null);
		}
		else if (disconnectedClient.equals(ch2)){
			ch1.sendMessage("GAME OVER; OTHER CLIENT DISCONNECTED");
			ch1.setGame(null);
		}
	}
}
