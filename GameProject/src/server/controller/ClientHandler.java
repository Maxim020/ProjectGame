package server.controller;

import scrabble.model.player.Player;
import scrabble.model.letters.Bag;
import server.view.ServerTUI;

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
    private List<String> teamRequests;
    private String teammate;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            closeEverything();
        }
        this.server = server;
        teamRequests = new ArrayList<>();
        teammate = null;
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
        String[] parts = input.split(" "); //Wie soll ich Unit und message seperator trennen?
        String command = parts[0];

        ServerTUI.showMessage(input,clientUsername);

        switch (command){
            case "ANNOUNCE":
                if(parts[1] == null) {
                    sendMessage("Please enter a name");
                }
                else if(parts[1].equalsIgnoreCase("-C")){
                    this.clientUsername = "ComputerPlayer";
                    clientHandlers.add(this);
                    broadcastMessage("SERVER: " + clientUsername + " has entered the chat!",false);
                    sendMessage("WELCOME " + clientUsername+"\n");
                }
                else {
                    this.clientUsername = parts[1];
                    clientHandlers.add(this);
                    broadcastMessage("SERVER: " + clientUsername + " has entered the chat!",false);
                    sendMessage("WELCOME " + clientUsername+"\n");
                }
                break;
            case "CHAT":
                StringBuilder message = new StringBuilder(clientUsername + ":");
                for (int i=1; i<parts.length; i++){
                    message.append(" ").append(parts[i]);
                }
                broadcastMessage(message.toString(),false);
                break;
            case "REQUESTGAME":
                if(parts.length == 2) {
                    int inQueue = ClientHandler.clientHandlers.size();
                    int playersNeeded = Integer.parseInt(parts[1]);
                    int playersStillNeeded = playersNeeded - inQueue;
                    broadcastMessage("INFORMQUEUE " + inQueue + " " + playersNeeded, true);

                    if (playersNeeded < 2 || playersNeeded > 4) {
                        sendMessage("Please type in a number between 2 and 4");
                    } else if (playersStillNeeded == 0) {
                        StringBuilder msg = new StringBuilder("STARTGAME");
                        for (int i = 0; i < playersNeeded; i++) {
                            msg.append(" ").append(ClientHandler.clientHandlers.get(i).getName());
                        }
                        broadcastMessage(msg.toString(), true);
                        server.setRequestGame(true);
                        //server.setUpGame(); //The game takes all connected clients as arguments in the constructor
                    } else {
                        sendMessage("The number of clients connected and the indicated amount of players to start a game must be equal");
                    }
                }
                else {
                    sendMessage("Please indicate number of players");
                }
                break;
            case "MAKEMOVE":
                if(clientUsername.equals(server.getGame().getCurrentClient().getName())) {

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
            case "REQUESTTEAM":
                if(clientHandlers.size() > 2) {
                    if (getClientHandlerWithName(parts[1]).getTeammate() != null) {
                        sendMessage(parts[1] + " is already in a team");
                    }
                    else {
                        getClientHandlerWithName(parts[1]).getTeamRequests().add(clientUsername);
                        sendPM("\n"+clientUsername + " wants to team up with you!\nAccept invitation with: ACCEPT "
                                + clientUsername + "\nOr deny with: DENY " + clientUsername + "\n", parts[1]);
                        sendMessage("Request has been send");
                    }
                }
                else {
                    sendMessage("Need at least 3 players to form a team of two");
                }
                break;
            case "ACCEPT":
                if(teamRequests.contains(parts[1])){
                    sendPM("Your request has been accepted",parts[1]);
                    teamRequests.remove(parts[1]);
                    setTeammate(parts[1]);
                    getClientHandlerWithName(parts[1]).setTeammate(clientUsername);
                }
                else {
                    sendMessage("There is no open team request from "+parts[1]);
                }
                break;
            case "DENY":
                if(teamRequests.contains(parts[1])){
                    sendPM("Your request has been denied", parts[1]);
                    teamRequests.remove(parts[1]);
                }
                else {
                    sendMessage("There is no open team request from "+parts[1]);
                }
                break;
        }
    }

    public ClientHandler getClientHandlerWithName(String name){
        for(ClientHandler clientHandler : clientHandlers){
            if(clientHandler.getName().equals(name)){
                return clientHandler;
            }
        }
        return null;
    }

    public void broadcastMessage(String msg, boolean includeSender){
        for(ClientHandler clientHandler : clientHandlers){
            if(includeSender || !clientHandler.clientUsername.equals(clientUsername)){
                clientHandler.sendMessage(msg);
            }
        }
    }

    public void sendPM(String msg, String clientUsername){
        int count = 0;
        for(ClientHandler clientHandler : clientHandlers){
            if(clientHandler.getName().equals(clientUsername)){
                clientHandler.sendMessage(msg);
                count++;
            }
        }
        if(count != 1){
            sendMessage("Client "+clientUsername+" does not exist!");
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

    public String getTeammate() {
        return teammate;
    }

    public void setTeammate(String teammate) {
        this.teammate = teammate;
    }

    public List<String> getTeamRequests() {
        return teamRequests;
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