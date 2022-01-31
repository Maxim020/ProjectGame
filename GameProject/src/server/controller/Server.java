package server.controller;
import client.controller.ClientHandler;
import scrabble.model.Game;
import java.io.*;
import java.net.*;

/**
 * IDEAS:
 * 1) ServerTUI?
 */

public class Server{
    private ServerSocket serverSocket;
    private Game game;
    private boolean requestGame = false;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startGame();
        server.startServer();
    }


    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                if(requestGame){
                    break;
                }
                Socket socket = serverSocket.accept();
                System.out.println("New Client has connected");
                ClientHandler clientHandler = new ClientHandler(socket, this);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        //System.out.println("setting up game ...");
        //setUpGame();
    }

    public void startGame(){
        new Thread(() -> {
            while (true){
                
                //Another (dirty) solution: Make Thread sleep 100 ms
                synchronized (this) {
                    if (requestGame) {
                        setUpGame();
                    }
                }
            }
        }).start();
    }


    public void setUpGame(){
        game = new Game(ClientHandler.clientHandlers);
        game.start(); //main method in local controller
    }

    public Game getGame() {
        return game;
    }

    public void setRequestGame(boolean requestGame) {
        this.requestGame = requestGame;
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




