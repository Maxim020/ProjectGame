package server.controller;
import client.controller.ClientHandler;
import scrabble.model.Game;
import server.model.ExitProgram;
import server.view.ServerTUI;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private ServerSocket serverSocket;
    private ServerTUI serverTUI;
    private Game game;
    private List<ClientHandler> clients;

    public Server() {
        clients = new ArrayList<>();
        serverTUI = new ServerTUI();
    }

    /**
     * Repeatedly asks the user for a valid port number
     * @author Yasin
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        System.out.println("Server is starting ...");
        new Thread(server).start();
    }

    public void setUpGame(){
        game = new Game(clients);
        game.start(); //main method in local controller
    }

    //Done
    public void setUp() throws ExitProgram{
        serverSocket = null;

        while (serverSocket == null){
            int port = serverTUI.getInt("Please enter a server port");

            try{
                serverTUI.showMessage("Attempting to create socket at 127.0.0.1 on port "+port+" ...");
                serverSocket = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
                serverTUI.showMessage("Server started on port "+port);
            } catch (IOException e) {
                serverTUI.showMessage("ERROR: Could not create socket 127.0.0.1 and port "+port);
                e.printStackTrace();

                if(!serverTUI.getBoolean("Do you want to try again?")){
                    throw new ExitProgram("User indicated to exit program");
                }
            }
        }
    }

    public void removeClient(ClientHandler clientHandler){
        this.clients.remove(clientHandler);
    }

    @Override
    public void run() {
        boolean openNewSocket = true;

        while (openNewSocket){
            try {
                setUp();

                while (true) {
                    Socket socket = serverSocket.accept();
                    serverTUI.showMessage("New Client connected");
                    ClientHandler clientHandler = new ClientHandler(socket, this);
                    new Thread(clientHandler).start();
                    clients.add(clientHandler);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("A server IO error occured: "+e.getMessage());
                if(!serverTUI.getBoolean("Do you want to open a new Socket for clients to connect to")){
                    openNewSocket = false;
                }
            } catch (ExitProgram e){
                openNewSocket = false;
            }
        }
        serverTUI.showMessage("No more clients will be accepted");
    }

    public List<ClientHandler> getClients() {
        return clients;
    }
}




