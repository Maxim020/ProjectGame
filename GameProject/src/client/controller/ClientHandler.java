package client.controller;

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
    private ArrayList<String> log;
    private Player player;
    private String name;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.log = new ArrayList<>();
        this.server = server;
    }

    @Override
    public void run() {
        String input;

        try{
            input = in.readLine();

            System.out.println();
            while (input != null) {
                if(name != null){
                    System.out.println("> ["+name+"] Incomming: "+input);
                }
                else {
                    System.out.println("Incomming: "+input);
                }
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
                setName(parts[1]);
                setPlayer(new Player(getName(), Bag.getInstance()));
                sendMessage("WELCOME" + Protocol.UNIT_SEPARATOR + getName() + Protocol.MESSAGE_SEPARATOR);
                break;
            case "REQUESTGAME": //Argument: Numbers of players
                sendMessage("INFORMQUEUE"+Protocol.UNIT_SEPARATOR+server.getClients().size()+Protocol.UNIT_SEPARATOR+(2-server.getClients().size())+Protocol.MESSAGE_SEPARATOR);
                if(server.getClients().size() >= 2){
                    sendMessage("Not enough clients connected");
                }
                else {
                    String s = "STARTGAME";
                    for(int i=0; i<server.getClients().size(); i++){
                        s += +Protocol.UNIT_SEPARATOR+server.getClients().get(i).getName();
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