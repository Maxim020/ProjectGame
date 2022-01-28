package client.controller;

import scrabble.model.Game;
import scrabble.model.Player;
import scrabble.model.letters.Bag;
import scrabble.view.utils.Protocol;
import server.controller.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Server server;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private ArrayList<ClientHandler> clientHandlers;
    private ArrayList<String> log;
    private Player player;
    private String name;
    private Game game;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.log = new ArrayList<>();
    }

    @Override
    public void run() {
        String input = "";

        try{
            input = in.readLine();
            while (input != null) {
                System.out.println("> ["+name+"] Incomming: "+input);
                handleInput(input);
                out.newLine();
                out.flush();
                input = in.readLine();
            }
            shutdown();
        }catch (IOException e){
            shutdown();
        }

    }

    public void shutdown(){
        System.out.println("> ["+name+"] Shutting down");
        try{
            in.close();
            out.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        server.removeClient(this);
    }

    /**
     * handles input form client
     * @param input - input from client
     */
    public void handleInput(String input) {
        String[] parts = input.split(" "); //Wie soll ich Unit und message seperator trennen?
        String command = parts[0];

        switch (command){
            case "ANNOUNCE":
                if(!log.contains(parts[1])) {
                    log.add(parts[1]);
                    setName(parts[1]);
                    setPlayer(new Player(getName(), Bag.getInstance()));
                    sendMessage("WELCOME" + Protocol.UNIT_SEPARATOR + getName() + Protocol.MESSAGE_SEPARATOR);
                }
                else{
                    sendMessage("Client is already logged in");
                }
                break;
            case "REQUESTGAME": //Argument: Numbers of players
                int queuesize = clientHandlers.size();

                sendMessage("INFORMQUEUE"+Protocol.UNIT_SEPARATOR +
                        queuesize+Protocol.UNIT_SEPARATOR+
                        (Integer.parseInt(parts[1])-queuesize)+Protocol.MESSAGE_SEPARATOR);

                //If enough players
                sendMessage("STARTGAME");
                //‘STARTGAME’: server informs clients that the game is starting
                //Arguments:
                //1. Name of player 1
                //2. Name of player 2
                //3. Name of player 3 (optional)
                //4. Name of player 4 (optional)

                sendMessage("NOTIFYTURN");
                //‘NOTIFYTURN’: server informs clients whose turn it is
                //Arguments:
                //1. Whether it is the turn of the destination client (boolean)
                //2. Name of the player that can now make a turn
                break;
            case "MAKEMOVE":
                //‘MAKEMOVE’: client requesting to make a move to the server
                //Everyone needs to implement in their server: “MAKEMOVE-SWAP-” is swapping 0 tiles, not an error. “MAKEMOVE-SWAP” would be an error, since you are missing an argument (no delimiter).
                //Arguments:
                //1. Type of turn (WORD | SWAP)
                //If WORD:
                //2. Start coordinate (format: B3 (as in Project manual))
                //3. Direction (horizontal/vertical)
                //4. Word (e.g. DOg, where g is a blank tile)
                //If SWAP:
                //2. String with tiles you want to swap (e.g. PQG)
                //SWAP- (if you want to swap no tiles)
                break;
        }
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}