package client.controller;

import local.model.PlayerList;
import local.view.InputHandler;
import scrabble.model.Board;
import scrabble.model.ComputerPlayer;
import scrabble.model.HumanPlayer;
import scrabble.model.Player;
import scrabble.model.exceptions.*;
import scrabble.model.letters.Bag;
import scrabble.model.letters.CrossChecker;
import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.InMemoryScrabbleWordChecker;
import scrabble.model.words.IsAdjacentChecker;
import scrabble.model.words.ScrabbleWordChecker;
import scrabble.view.utils.Protocol;
import scrabble.view.utils.TextIO;
import server.controller.Server;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * IDEAS:
 * 1) Muss ein ClientHandler wirklich mit Server connected sein?
 */

public class ClientHandler implements Runnable {
    private Server server;
    public static List<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientUsername;
    private Player player;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            closeEverything();
        }
        this.server = server;
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try{
                messageFromClient = in.readLine();
                handleInput(messageFromClient);
                }
            catch (IOException e){
                closeEverything();
                break;
            }
        }
    }

    /**
     * handles input form client
     * @param input - input from client
     */
    public void handleInput(String input) {
        //
        String[] parts = input.split(" "); //Wie soll ich Unit und message seperator trennen?
        String command = parts[0];

        switch (command){
            case "ANNOUNCE":
                if(parts[1] == null) {
                    sendMessage("Please enter your name");
                } else  if(parts[1].equalsIgnoreCase("C")){
                    this.clientUsername = "ComputerPlayer";
                    clientHandlers.add(this);
                    broadcastMessage("SERVER: " + clientUsername + " has entered the chat!",false);
                    sendMessage("WELCOME " + clientUsername);
                    setPlayer(new ComputerPlayer(Bag.getInstance()));
                } else {
                    this.clientUsername = parts[1];
                    clientHandlers.add(this);
                    broadcastMessage("SERVER: " + clientUsername + " has entered the chat!",false);
                    sendMessage("WELCOME " + clientUsername);
                    setPlayer(new HumanPlayer(getName(), Bag.getInstance()));
                }
                break;
            case "CHAT":
                String message = clientUsername+":";
                for (int i=1; i<parts.length; i++){
                    message += " "+parts[i];
                }
                broadcastMessage(message,false);
                break;
            case "REQUESTGAME": //Was passiert wenn mehr als players needed in der queue sind?
                int inQueue = ClientHandler.clientHandlers.size();
                int playersNeeded = Integer.parseInt(parts[1]);
                int playersStillNeeded = playersNeeded - inQueue;
                broadcastMessage("INFORMQUEUE "+inQueue+" "+playersNeeded, true);

                if (playersNeeded < 2 || playersNeeded > 4){
                    sendMessage("Please type in a number between 2 and 4");
                }
                else if(playersStillNeeded == 0){
                    String msg = "STARTGAME";
                    for(int i=0; i<playersNeeded; i++){
                        msg += " "+ClientHandler.clientHandlers.get(i).getName();
                    }
                    broadcastMessage(msg,true);
                    server.setRequestGame(true);
                    //server.setUpGame(); //The game takes all connected clients as arguments in the constructor
                }
                else {
                    sendMessage("The number of clients connected and the indicated amount of players to start a game must be equal");
                }

                break;
            case "MAKEMOVE":
                if(clientUsername.equals(server.getGame().getCurrentClient().getName())) {
                    System.out.println("TEST");
                    String move = "";
                    for (int i = 1; i < parts.length; i++) {
                        move += parts[i] + " ";
                    }
                    this.server.getGame().setMove(move);

                }
                else {
                    sendMessage("ERROR: It is not your turn");
                }
                break;
            default:
                System.out.println("[INCOMING FROM "+clientUsername+"] "+input);
        }
    }




    public void broadcastMessage(String msg, boolean includeSender){
        for(ClientHandler clientHandler : clientHandlers){
            if(includeSender || !clientHandler.clientUsername.equals(clientUsername)){
                clientHandler.sendMessage(msg);
            }
        }
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: "+clientUsername+" has left the chat",false);
    }


    public void closeEverything(){
        removeClientHandler();
        try{
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return clientUsername;
    }

    public void setName(String name) {
        this.clientUsername = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}