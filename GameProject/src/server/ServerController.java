package server;
import java.io.*;
import java.net.*;



public class ServerController {
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
                System.out.println("WELCOME" + client.getInetAddress().getHostAddress());

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

                // This thread will handle the client separately
                new Thread(clientSock).start();
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

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket){
            this.clientSocket = socket;
        }

        public void run() {

            PrintWriter out = null;
            BufferedReader in = null;
            try {

                // get the outputstream of client (Clients perspective)
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // get the inputstream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String line;
                //Reads message from Client
                while ((line = in.readLine()) != null) {
                    //Process Input with control flow

                    //WELCOME CLIENT
                    System.out.printf("WELCOME: %s\n", line);
                    out.println(line);
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
