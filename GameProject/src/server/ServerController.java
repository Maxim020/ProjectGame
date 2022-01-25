package server;
import scrabble.model.Board;
import scrabble.model.PlayerList;
import local.view.InputHandler;

import java.io.*;
import java.net.*;



public class ServerController {
    private String communication;

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public static void main(String[] args) {
        ServerSocket server = null;

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

        PlayerList playerList = PlayerList.getInstance();
        Board board = new Board();
        //playerList.setPlayers();

    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket){
            this.clientSocket = socket;
        }

        public void run() {

            PrintWriter out = null;
            BufferedReader in = null;

            try {
                // get the outputstream and inputstream of client (Clients perspective)
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



                //Reads message from Client
                String line;
                while ((line = in.readLine()) != null) {
                    //Process Input with control flow

                    //Send Input Player to Server Controller

                    //WELCOME CLIENT
                    System.out.printf("> Message from Client: %s\n", line);
                    out.println(line); //?
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
