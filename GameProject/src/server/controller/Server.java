package server.controller;
import scrabble.model.Game;
import java.io.*;
import java.net.*;

/**
 * Provides a Class that represents the Server
 * @author Yasin Fahmy & Maxim Frano
 */

public class Server{
    private final ServerSocket serverSocket;
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
    }


    public void startGame(){
        new Thread(() -> {
            while (true){
                synchronized (this) {
                    if (requestGame) {
                        setUpGame();
                        requestGame = false;
                    }
                }
            }
        }).start();
    }

    public void setUpGame(){
        game = new Game(ClientHandler.clientHandlers);
        game.start();
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




