package scrabble.model;

import scrabble.model.player.ComputerPlayer;
import scrabble.model.player.HumanPlayer;
import server.utils.Countdown;
import server.controller.ClientHandler;
import scrabble.model.player.Player;
import scrabble.model.player.PlayerList;
import server.view.ServerTUI;
import scrabble.model.exceptions.*;
import scrabble.model.checker.CrossChecker;
import scrabble.model.checker.DeadEndChecker;
import scrabble.model.checker.LetterScoreChecker;
import scrabble.model.checker.AdjacentWordChecker;
import scrabble.model.checker.InMemoryScrabbleWordChecker;
import scrabble.model.checker.IsAdjacentChecker;
import scrabble.model.checker.ScrabbleWordChecker;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private final List<ClientHandler> clients;
	private PlayerList playerList;
	private final int amountOfPlayers;
	private int playersTurn;

	private final Board board;
	private final Bag bag;

	private final ScrabbleWordChecker wordChecker = new InMemoryScrabbleWordChecker();
	private final AdjacentWordChecker adjacentWordChecker;
	private final IsAdjacentChecker isAdjacentChecker;
	private String move;
	private String oldMove;

	private boolean isMoveValid;
	private boolean timeLeft;
	private boolean oneVsTwo = false;
	private int numberOfTurn = 0;
	private int indexOfSingle;

	public Game(List<ClientHandler> clients) {
		/** Instantiates a Board, a Game, a universal PlayerList and Bag*/
		this.board = new Board();
		this.bag = Bag.getInstance();
		this.clients = clients;
		this.adjacentWordChecker = new AdjacentWordChecker(board);
		this.isAdjacentChecker = new IsAdjacentChecker(board);

		connectClientsWithPlayers();
		createPlayerlist();
		connectPlayerWithTeamMates();

		if(playerList.getPlayers().size() == 3){checkOneVsTwo();}

		amountOfPlayers = playerList.getPlayers().size();
	}

	public void start(){
		while (true){
			/** Allows for rotation of players turn */
			playersTurn = numberOfTurn % amountOfPlayers;
			playerList.setCurrentPlayer(playersTurn);

			/** Updates ServerTUI and broadcasts board to clients */
			ServerTUI serverTUI = new ServerTUI(board, playerList.getCurrentPlayer());
			serverTUI.updateBoard();
			broadcastMessage(stringBoard());
			sendTiles();

			/** Notify Turn */
			notifyTurn(clients.get(playersTurn));

			if(playerList.getCurrentPlayer() instanceof HumanPlayer) {
				/**Timer of Opponent starts */
				Countdown countdown = new Countdown();
				getCurrentClient().sendMessage("You have one minute to decide!");
				countdown.counter(60, this);
				setTimeLeft(true);

				/** Wait for move from client */
				outer:
				while (timeLeft) {
					while (move == null) {
						sleep(500);
						if (!timeLeft) {
							break outer;
						}
					}

					/** Wait until Client gives valid input */
					while (true) {
						while (move.equals(oldMove)) {
							sleep(500);
							if (!timeLeft) {
								break outer;
							}
						}
						try {
							validateInput(move);
							setOldMove(move);
							isMoveValid = true;
							break outer;
						} catch (CenterIsNotCoveredException | FieldDoesNotExistException | IllegalSwapException | NotEnougTilesException | NotEnoughBlankTilesException |
								UnknownCommandException | UnknownDirectionException | WordDoesNotFitException | WordIsNotAdjacentException | InvalidCrossException e) {

							e.printStackTrace();
							getCurrentClient().sendMessage("Please type in a valid move");
							setOldMove(move);
							isMoveValid = false;

						} catch (InvalidWordException e) {
							e.printStackTrace();
							getCurrentClient().sendMessage("Either one or more words were invalid, you lost your turn");
							setOldMove(move); //maybe not necessary
							isMoveValid = false;
							break outer;
						}
					}
				}
				if (timeLeft) {
					countdown.getTimerA().cancel();
				}

			}
			else {
				ComputerPlayer computerPlayer = (ComputerPlayer) PlayerList.getInstance().getCurrentPlayer();
				setMove(computerPlayer.determineMove(board));
				isMoveValid = true;
			}

			if(move != null && isMoveValid){
				processMove(move, board, playerList.getCurrentPlayer(), bag);
			}
			setMove(null);
			setOldMove(null);

			if(checkEndOfGame(bag, playerList.getCurrentPlayer(), board)){
				broadcastMessage("\nGame ends because either a player played all tiles left or there are no more possibilities\n");
				break;
			}

			if(oneVsTwo){skipTurn();}

			numberOfTurn++;
		}

		adjustScores(bag, playerList.getCurrentPlayer());
		broadcastMessage(ServerTUI.printFinalScoreBoard());
		Bag.reset();
	}

	/**
	 * Connects a client with a player to make it possible to play the game
	 */
	public void connectClientsWithPlayers(){
		for (int i=0; i<this.clients.size(); i++){
			if(clients.get(i).getName().equalsIgnoreCase("ComputerPlayer")){
				this.clients.get(i).setPlayer(new ComputerPlayer(bag,"ComputerPlayer"+(i+1)));
			}
			else {
				this.clients.get(i).setPlayer(new HumanPlayer(this.clients.get(i).getName(), bag));
			}
		}
	}

	/**
	 * creates a universal Playerlist
	 */
	public void createPlayerlist(){
		List<Player> players = new ArrayList<>();
		for (ClientHandler client : this.clients) {
			players.add(client.getPlayer());
		}
		playerList = PlayerList.getInstance();
		playerList.setPlayers(players);
	}

	/**
	 * Connects player with teammates in order to have a correct score baord in the end
	 */
	public void connectPlayerWithTeamMates(){
		for (int i=0; i < playerList.getPlayers().size(); i++){
			playerList.getPlayers().get(i).setTeammate(clients.get(i).getTeammate());
		}
	}

	/**
	 * check if there is a 2 vs 1 Situation, updated boolean and determines the index of the solo player
	 */
	public void checkOneVsTwo(){
		int count = 0;
		for (Player player : playerList.getPlayers()){
			if(player.getTeammate() == null){
				count++;
			}
			if(count == 1){
				oneVsTwo = true;
			}
		}
		if(oneVsTwo) {
			for (int i=0; i<playerList.getPlayers().size(); i++) {
				if (playerList.getPlayers().get(i).getTeammate() == null) {
					indexOfSingle = i;
				}
			}
		}
	}

	/**
	 * Broadcasts a message to all clients who are playing this game
	 * @param msg - Message
	 */
	public void broadcastMessage(String msg){
		for(ClientHandler clientHandler : clients){
			clientHandler.sendMessage(msg);
		}
	}

	/**
	 * @return a string representation of board and keyword BOARD in front of it
	 */
	public String stringBoard(){
		return "BOARD "+board;
	}

	/**
	 * informs each client about their tiles
	 */
	public void sendTiles(){
		for(ClientHandler clientHandler : clients){
			clientHandler.sendMessage("TILES "+clientHandler.getPlayer().getLetterDeck().getLettersInDeck()+"\n");
		}
	}

	/**
	 * Notifies each client about their turn
	 * @param ch - ClientHandler that has the turn
	 */
	public void notifyTurn(ClientHandler ch){
		for(ClientHandler clientHandler : clients){
			clientHandler.sendMessage("NOTIFYTURN "+ch.equals(clientHandler)+" "+ch.getName());
		}
	}

	/**
	 * Let thread sleep
	 * @param ms - duration in ms
	 */
	public void sleep(int ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Throws IllegalArgumentException provided the input is not valid
	 * @param move from the user
	 * @author Yasin Fahmy
	 */
	public void validateInput(String move) throws IllegalArgumentException{
		String[] parts = move.split(" ");

		if(!(parts.length == 4 | parts.length == 2 | parts.length == 1)){
			throw new UnknownCommandException();
		}

		//Set word
		if(parts.length == 4){
			String command = parts[0]; String startCoordinate = parts[1]; String direction = parts[2]; String word = parts[3];
			/**
			 * The order of these checker is important. Do not change, unless it would solve a new bug
			 */

			if(wordChecker.isValidWord(word) == null) {
				throw new InvalidWordException();
			} else {
				//If one of the new composed words is invalid: throw new InvalidWordException - ADDED
				if(!adjacentWordChecker.areAdjacentWordsValid(startCoordinate, direction, word)) {
					throw new InvalidWordException();
				}
			}

			int lowercase = tilesNotOwned(parts, playerList.getCurrentPlayer(), board,true);
			if(lowercase != 0){
				int blankTiles =playerList.getCurrentPlayer().getLetterDeck().numberOfBlankTiles();

				if(blankTiles != lowercase){
					throw new NotEnoughBlankTilesException();
				}
			}

			if(board.isCenterCovered()) {
				if(!isAdjacentChecker.isAdjacent(startCoordinate, direction, word)) {
					throw new WordIsNotAdjacentException();
				}
			}

			if(board.isBoardEmpty() && board.fieldsCovered(startCoordinate, direction, word).contains("H8")) {
				board.setCenterCovered(true);
			}

			if(!CrossChecker.isMoveValid(board,startCoordinate,direction,word)){
				throw new scrabble.model.exceptions.InvalidCrossException();
			}

			if(!command.equalsIgnoreCase("word") || startCoordinate.length() < 2 || startCoordinate.length() > 3 || direction.length() != 1){
				throw new UnknownCommandException();
			}

			if (!board.isFieldValid(startCoordinate)){
				throw new FieldDoesNotExistException();
			}

			if(!(direction.equalsIgnoreCase("H") || direction.equalsIgnoreCase("V"))){
				throw new UnknownDirectionException();
			}

			if(!board.isCenterCovered()){
				throw new CenterIsNotCoveredException();
			}

			if(!board.doesWordFit(startCoordinate, direction, word)){
				throw new WordDoesNotFitException();
			}

		}

		//Swap tiles
		else if(parts.length == 2){
			String command = parts[0]; String tiles = parts[1];

			if(!command.equalsIgnoreCase("swap")){
				throw new UnknownCommandException();
			}
			if(tilesNotOwned(parts,playerList.getCurrentPlayer(),board,false) != 0){
				throw new IllegalSwapException();
			}
			if(tiles.length() > 7){
				throw new IllegalSwapException();
			}
			if(tiles.length() > bag.getLetterList().size()){
				throw new NotEnougTilesException();
			}
		}
		//Skip turn
		else {
			String command = parts[0];

			if(!command.equalsIgnoreCase("swap")){
				throw new UnknownCommandException();
			}
		}
	}

	/**
	 * The process of checking not owned tiles is different when wanting to place a word and swapping, because
	 * the algorithm needs to check the already placed tiles on the board, so that it does not assume they are
	 * not owned by the player
	 * @param parts - String array with the parts of the command/input
	 * @param currentPlayer- currentPlayer that is checked for now owned tiles
	 * @param board - the board that the game is played on
	 * @param word - true if tiles not owned refer to placing a word on the board and false if it refers to swapping
	 * @return the number of tiles not owned by the player
	 * @author Yasin
	 */
	public static int tilesNotOwned(String[] parts, Player currentPlayer, Board board, boolean word){
		ArrayList<Character> letterDeck = currentPlayer.getLetterDeck().getLettersInDeck();

		int count = 0;

		if(word){
			String coordinate = parts[1];
			String direction = parts[2];
			String tiles = parts[3];
			int[] rowcol = board.convert(coordinate);
			int length = tiles.length();

			for (int i = 0; i < length; i++) {
				if (direction.equalsIgnoreCase("h")) {
					if (!letterDeck.contains(tiles.charAt(i)) && board.isFieldEmpty(rowcol[0], (rowcol[1] + i))) {
						count++;
					}
				}
				else{
					if (!letterDeck.contains(tiles.charAt(i)) && board.isFieldEmpty((rowcol[0]+i),rowcol[1])){
						count++;
					}
				}
			}
		}
		else {
			String tiles = parts[1];
			int length = tiles.length();

			for (int i = 0; i < length; i++) {
				if (!letterDeck.contains(tiles.charAt(i))) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * processes input to make a move and return
	 * @requires the input to be valid
	 * @param input the input that the user wrote
	 * @author Yasin
	 */
	public static void processMove(String input, Board board, Player currentPlayer, Bag bag) {
		String[] parts = input.split(" ");

		if (parts[0].equalsIgnoreCase("word")) {
			//WORD A1 H TEST
			exchangeTiles(parts, currentPlayer, bag, board, true);
			board.setWord(parts[1], parts[2], parts[3]);
			board.setBoardEmpty(false);
			board.addPlayedWords(parts[1], parts[2], parts[3]);
		}
		else {
			if (parts.length == 2) {
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
		boolean goingOut = bag.getLetterList().isEmpty() && currentPlayer.getLetterDeck().getLettersInDeck().isEmpty();

		boolean deadEnd = bag.getLetterList().isEmpty() && new DeadEndChecker().isDeadEnd(board);

		return goingOut || deadEnd;
	}

	/**
	 * Skips turn to make a 2 vs 1 more fair
	 */
	public void skipTurn(){
		switch (indexOfSingle){
			case 2:
				if(playersTurn == 0){
					numberOfTurn++;
				}
				if(numberOfTurn%12 == 2 || numberOfTurn%12 == 8){
					numberOfTurn++;
				}
				break;
			case 1:
				if(playersTurn == 2){
					numberOfTurn++;
				}
				if(numberOfTurn%12 == 4 || numberOfTurn%12 == 10){
					numberOfTurn++;
				}
				break;
			case 0:
				if(playersTurn == 1){
					numberOfTurn++;
				}
				if(numberOfTurn%12 == 3 || numberOfTurn%12 == 9){
					numberOfTurn++;
				}
				break;
		}
	}

	/**
	 * @param bag - a universal bag of letters
	 * @param currentPlayer - The player who has just made a move
	 * @author Yasin
	 */
	public static void adjustScores(Bag bag, Player currentPlayer){ //Adjust scores
		LetterScoreChecker letterScoreChecker = new LetterScoreChecker();
		boolean goingOut = bag.getLetterList().isEmpty() && currentPlayer.getLetterDeck().getLettersInDeck().isEmpty();
		int sumOfUnplacedTiles = 0;
		//Player winnerBeforeAdjustment = Collections.max(PlayerList.getInstance().getPlayers());

		//Each player's score is reduced by the sum of his or her unplaced letters.
		for (int i=0; i<PlayerList.getInstance().getPlayers().size(); i++){
			ArrayList<Character> letterDeck = PlayerList.getInstance().getPlayers().get(i).getLetterDeck().getLettersInDeck();
			for (Character character : letterDeck) {
				sumOfUnplacedTiles += letterScoreChecker.scoreChecker(character);
				PlayerList.getInstance().getPlayers().get(i).subtractScore(letterScoreChecker.scoreChecker(character));
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
	}

	public void setOldMove(String oldMove) {
		this.oldMove = oldMove;
	}

	public void setTimeLeft(boolean timeLeft) {
		this.timeLeft = timeLeft;
	}

	public ClientHandler getCurrentClient() {
		return clients.get(playersTurn);
	}

	public Board getBoard() {
		return board;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public PlayerList getPlayerList() {
		return playerList;
	}

	public Bag getBag() {
		return bag;
	}
}
