package scrabble.model;

import client.controller.Client;
import client.controller.ClientHandler;
import local.model.PlayerList;
import scrabble.model.letters.Bag;
import scrabble.view.utils.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Game logic
 */
public class Game {
	//Soll ich auch player mit ClientHandler connecten?
	private List<ClientHandler> clients;
	private PlayerList playerList;
	private int amountOfPlayers;
	int numberOfTurn = 0;
	int playersTurn;
	private Board board;
	private Bag bag;

	public void start(){
		while (true){
			playersTurn = numberOfTurn % amountOfPlayers;

			//Update Board
			notifyAll(getBoard());

			//Notify Turn
			notify(playersTurn, "NOTIFYTURN");


		}
	}

	public String getBoard(){
		return "BOARD"+ Protocol.UNIT_SEPARATOR + board+Protocol.MESSAGE_SEPARATOR;
	}

	public void notifyAll(String msg){
		for (int i=0; i<clients.size(); i++){
			clients.get(i).sendMessage(msg);
		}
	}

	public void notify(int playersTurn, String msg){
		clients.get(playersTurn).sendMessage(msg);
	}


	public Game(List<ClientHandler> clients) {
		this.board = new Board();
		this.bag = Bag.getInstance();
		this.clients = clients;

		//Connect Clients with a player
		for (int i=0; i<this.clients.size(); i++){
			this.clients.get(i).setPlayer(new Player(this.clients.get(i).getName(), bag));
		}

		//Create PlayerList
		List<Player> players = new ArrayList<>();
		for(int i=0; i<this.clients.size(); i++){
			players.add(clients.get(i).getPlayer());
		}
		playerList = PlayerList.getInstance();
		playerList.setPlayers(players);

		amountOfPlayers = playerList.getPlayers().size();
	}



	/**
	 * Processes move by client on board
	 *
	 * @param client
	 * @param move
	 */
	public synchronized void makeMove(ClientHandler client, String move) {
		//To be implemented
	}

	public int scoreMove() {
		//To be implemented
		return -1;
	}

	public void clientDisconnected(ClientHandler disconnectedClient) {
//		if(disconnectedClient.equals(ch1)){
//			ch2.sendMessage("GAME OVER; OTHER CLIENT DISCONNECTED");
//			ch2.setGame(null);
//		}
//		else if (disconnectedClient.equals(ch2)){
//			ch1.sendMessage("GAME OVER; OTHER CLIENT DISCONNECTED");
//			ch1.setGame(null);
//		}
//	}
	}
}
