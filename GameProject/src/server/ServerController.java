package server;
import local.LocalController;
import scrabble.model.Board;
import scrabble.model.PlayerList;
import local.view.InputHandler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerController implements Runnable{

    public static void main(String[] args) {
        // In main: Three lines:
        // ServerController instantiation
        ServerController serverController = new ServerController();
        // Print statement that the server started
        System.out.println("Server started");
        // New Thread(serverController).start() --> main is already a thread?!
        new Thread(serverController).start();

        ServerSocket server = null;

        List<ClientHandler> clientHandlerList = new ArrayList<>();

        try {
            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            // running infinite loop for getting client request
            while (true) {

                // socket object to receive incoming client requests
                Socket client = server.accept();

                // Displaying that new client is connected to server
                System.out.println("Client " + client.getInetAddress().getHostAddress() +" has successfully connected to this server");

                // create a new thread object
                ClientHandler clientHandler = new ClientHandler(client);

                //Add newly joined Client to list of clients
                clientHandlerList.add(clientHandler);

                // This thread will handle the client separately
                new Thread(clientHandler).start();


            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //run method:
    @Override
    public void run() {
        String[] arguments = new String[]{};
        LocalController.main(arguments);
    }

    //setUpGame method: Initialize a Game

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket){
            this.clientSocket = socket;
        }

        //All methods to client (Game logic)

        public void run() {

            BufferedWriter out = null;
            BufferedReader in = null;

            try {
                // get the outputstream and inputstream of client (Clients perspective)
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



                //Reads message from Client
                String line;
                while ((line = in.readLine()) != null) {
                    //Process Input with control flow

                    //Send Input Player to Server Controller

                    //WELCOME CLIENT
                    System.out.printf("> Message from Client: %s\n", line);
                    out.write(line);
                    //flush
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
