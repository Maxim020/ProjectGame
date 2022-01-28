package client.controller;

import client.view.ClientTUI;
import scrabble.view.utils.Protocol;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 2
 */
public class Client {
    private Socket socket;
    private String username;
    private BufferedWriter out;
    private BufferedReader in;
    private ClientTUI clientTUI;
    private boolean isLoggedIn;
    private boolean isRunning;
    private boolean isAllowedToMove;

    public Client(Socket socket) throws IOException{
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String[] args) {
        ClientTUI tui = new ClientTUI();
        InetAddress ip = tui.getHostIP();
        int port = tui.getPort();

        try (Socket socket = new Socket(ip, port)) {

            Client client = new Client(socket);

            //Connect ServerHandler, Client, TUI
            client.setClientTUI(tui);
            client.getClientTUI().setClient(client);

            client.start();
        }

        catch (IOException e) {
            System.out.println("ERROR: Connection to socket failed (IP: "+ip+", Port: "+port);
            System.exit(0); //Necessary?
        }
    }

    /**
     * handles input form server
     */
    public void handleInput(){
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
                        System.out.println("Test");
                        clientTUI.printMessage("WELCOME "+username);
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

    public void start(){
        isRunning = true;
        username = clientTUI.getUsername();
        sendMessage("ANNOUNCE" + Protocol.UNIT_SEPARATOR + username + Protocol.MESSAGE_SEPARATOR);

        while (isLoggedIn) {
            clientTUI.printMessage("This user name is already in use");
            username = clientTUI.getUsername();
        }
        setUsername(username);

        clientTUI.handleInput();
        handleInput();
    }

    public void sendMessage(String msg){
        try {
            out.write(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientTUI.printMessage("To Server: "+msg);
    }

    public boolean isAllowedToMove() {
        return isAllowedToMove;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ClientTUI getClientTUI() {
        return clientTUI;
    }

    public void setClientTUI(ClientTUI clientTUI) {
        this.clientTUI = clientTUI;
    }
}
