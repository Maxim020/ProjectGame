package client.controller;

import scrabble.model.Player;
import scrabble.model.letters.Bag;
import scrabble.view.utils.Protocol;
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
    public static List<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientUsername;

    private Server server;
    private Player player;


    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = in.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: "+clientUsername+" has entered the chat!");
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
                broadcastMessage(messageFromClient);

                //handleInput(messageFromClient);
                }
            catch (IOException e){
                closeEverything();
                break;
            }
        }
    }


    public void broadcastMessage(String msg){
        for(ClientHandler clientHandler : clientHandlers){
            if(!clientHandler.clientUsername.equals(clientUsername)){
                clientHandler.sendMessage(msg);
            }
        }
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


    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: "+clientUsername+" has left the chat");
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
                setName(parts[1]);
                setPlayer(new Player(getName(), Bag.getInstance()));
                sendMessage("WELCOME" + Protocol.UNIT_SEPARATOR + getName() + Protocol.MESSAGE_SEPARATOR);
                break;
            case "REQUESTGAME": //Argument: Numbers of players
                sendMessage("INFORMQUEUE"+Protocol.UNIT_SEPARATOR+ClientHandler.clientHandlers.size()+Protocol.UNIT_SEPARATOR+(2-ClientHandler.clientHandlers.size())+Protocol.MESSAGE_SEPARATOR);
                if(ClientHandler.clientHandlers.size() >= 2){
                    sendMessage("Not enough clients connected");
                }
                else {
                    String s = "STARTGAME";
                    for(int i=0; i<ClientHandler.clientHandlers.size(); i++){
                        s += +Protocol.UNIT_SEPARATOR+ClientHandler.clientHandlers.get(i).getName();
                    }
                    sendMessage(s+Protocol.MESSAGE_SEPARATOR);
                    server.setUpGame();
                }

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
            out.newLine();
            out.flush();
        } catch (IOException e) {
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