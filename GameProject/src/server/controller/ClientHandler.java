package server.controller;

import scrabble.model.player.Player;
import scrabble.model.letters.Bag;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * IDEAS:
 * 1) Muss ein ClientHandler wirklich mit Server connected sein?
 */

public class ClientHandler implements Runnable {
    private final Server server;
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

        System.out.println("[INCOMING FROM "+clientUsername+"] "+input+"\n");

        switch (command){
            case "ANNOUNCE":
                if(parts[1] == null) {
                    sendMessage("Please enter a name");
                }
                else if(parts[1].equalsIgnoreCase("-C")){
                    this.clientUsername = "ComputerPlayer";
                    clientHandlers.add(this);
                    broadcastMessage("SERVER: " + clientUsername + " has entered the chat!",false);
                    sendMessage("WELCOME " + clientUsername);
                }
                else {
                    this.clientUsername = parts[1];
                    clientHandlers.add(this);
                    broadcastMessage("SERVER: " + clientUsername + " has entered the chat!",false);
                    sendMessage("WELCOME " + clientUsername);
                }
                break;
            case "CHAT":
                StringBuilder message = new StringBuilder(clientUsername + ":");
                for (int i=1; i<parts.length; i++){
                    message.append(" ").append(parts[i]);
                }
                broadcastMessage(message.toString(),false);
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
                    StringBuilder msg = new StringBuilder("STARTGAME");
                    for(int i=0; i<playersNeeded; i++){
                        msg.append(" ").append(ClientHandler.clientHandlers.get(i).getName());
                    }
                    broadcastMessage(msg.toString(),true);
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
                    StringBuilder move = new StringBuilder();
                    for (int i = 1; i < parts.length; i++) {
                        move.append(parts[i]).append(" ");
                    }
                    this.server.getGame().setMove(move.toString());

                }
                else {
                    sendMessage("ERROR: It is not your turn");
                }
                break;
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
        closeConnection(in,out,socket);
    }

    public static void closeConnection(BufferedReader in, BufferedWriter out,Socket socket){
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