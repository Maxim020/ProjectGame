package client;

import scrabble.view.utils.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {
    public static void main(String[] args) {
        // establish a connection by providing host and port number
        try (Socket socket = new Socket("localhost", 1234)) {

            System.out.println("Client has successfully connected to localhost on port 1234\n");

            // writing to server
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = null;

            System.out.println("Please announce yourself by typing: ANNOUNCE 'Your Name'");

            while (!"exit".equalsIgnoreCase(line)) {

                // reading from user
                line = sc.nextLine();

                //Validate Input

                // sending the user input to server
                //ANNOUNCE+Protocol.UNIT_SEPARATOR+NAME+Protocol.UNIT_SEPARATOR+FLAG+Protocol.MESSAGE_SEPARATOR
                //ANNOUNCE+Protocol.UNIT_SEPARATOR+NAME+Protocol.MESSAGE_SEPARATOR
                out.write(line);

                out.flush();

                // displaying server reply
                System.out.println("> Server has received this message: " + in.readLine());
            }

            // closing the scanner object
            sc.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class ServerController implements Runnable {
        private final Socket clientSocket;

        public ServerController(Socket socket) {
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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
