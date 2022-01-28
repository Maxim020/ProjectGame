package server.controller;

import client.view.ClientTUI;
import local.view.LocalTUI;

import java.io.*;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean isLoggedIn;
    private boolean isTurn;
    private boolean allowedToMove;
    public String player1;
    public String player2;
    //String players?
    private ClientTUI clientTUI;
    private LocalTUI localTUI;


    public ServerHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * handles input form server
     */
    @Override
    public void run() {
        isLoggedIn = false;

        while (true){
            try {
                String input = in.readLine();

                String[] parts = input.split(" ");
                String command = parts[0];

                if(input == null){
                    clientTUI.printMessage("Server stopped working");
                    break;
                }

                switch (command){
                    case "WELCOME":
                        isLoggedIn = true;
                    case "INFORMQUEUE":
                        //‘INFORMQUEUE’: server informs the client about the queue for a new game
                        //Arguments:
                        //1. Number of players currently in queue (mandatory)
                        //2. Number of required players for game (mandatory)
                        break;
                    case "STARTGAME":
                        //‘STARTGAME’: server informs clients that the game is starting
                        //Arguments:
                        //1. Name of player 1
                        //2. Name of player 2
                        break;
                    case "NOTIFYTURN":
                        //‘INFORMQUEUE’: server informs the client about the queue for a new game
                        //Arguments:
                        //1. Number of players currently in queue (mandatory)
                        //2. Number of required players for game (mandatory)
                        break;
                    case "INFORMMOVE":
                        //‘INFORMMOVE’: server informs clients about the move that has just been made
                        //Arguments:
                        //1. Type of turn (WORD| SWAP)
                        //If WORD:
                        //2. Start coordinate (format: B3 (as in Project manual))
                        //3. Direction (horizontal/vertical)
                        //4. Word (e.g. DOg, where g is a blank tile))
                        //If SWAP:
                        //2. Number of tiles the player has swapped
                        break;
                    case "NEWTILES":
                        //‘NEWTILES’: server informs the client of their newly drawn tiles after their move/swap
                        //Arguments:
                        //1. String with newly drawn tiles (e.g. ABC)
                        break;
                    case "GAMEOVER":
                        //‘GAMEOVER’: server informs clients that the game is over
                        //Everyone needs to add to server implementation: if one client disconnects, their score is set to 0 and the game ends.
                        //Arguments:
                        //Type of win (WIN | DISCONNECT)
                        //Player 1 name
                        //Player 1 score
                        //Player 2 name
                        //Player 2 score
                        //Player 3 name (optional)
                        //Player 3 score (optional)
                        //Player 4 name (optional)
                        //Player 4 score (optional)
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public void handleInput(){

    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isAllowedToMove() {
        return allowedToMove;
    }

    public boolean validateInput(String move){
        return true;
    }

    public void setClientTUI(ClientTUI clientTUI) {
        this.clientTUI = clientTUI;
    }
}
