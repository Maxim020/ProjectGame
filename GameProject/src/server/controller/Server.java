package server.controller;
import client.controller.ClientHandler;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private BufferedReader in;
    private ArrayList<ClientHandler> listOfClients;
    private ArrayList<String> log;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.listOfClients = new ArrayList<>();
        this.log = new ArrayList<>();
    }

    /**
     * Repeatedly asks the user for a valid port number
     * @author Yasin
     */
    public static void main(String[] args) throws IOException { //throws or try catch?
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //not socket.getInputStream())?
        ServerSocket serverSocket = new ServerSocket(0);

        System.out.println("Please enter a port number or only press enter " +
                "when the port number is wished to be " + serverSocket.getLocalPort());

        //Ask as long as a given port number is invalid
        while (true){
            String input = br.readLine();

            //User wants to create a server on the preset port number
            if (input.trim().isEmpty()) {
                Server server = new Server(serverSocket);
                System.out.println("Server successfully established and listens on port " + serverSocket.getLocalPort());
                server.start();
                break;
            }
            //User wants to create a server on a custom port number
            else {
                serverSocket.close(); //close because a new serverSocket with a new port number will be created

                int port = Integer.parseInt(input);

                try {
                    port = Integer.parseInt(input);///
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: The given port '" + input + "' is not an Integer");
                }
                if (port < 0 || port > 65535) {
                    System.out.println("Port number must be a positive Integer and be smaller than 65535");
                }
                else {
                    //Throws IOException if port number is already used
                    try (ServerSocket serverSocket1 = new ServerSocket(port)) {
                        Server server = new Server(serverSocket1);
                        System.out.println("Server successfully established and listens on port " + serverSocket1.getLocalPort());
                        server.start();
                        break;
                    } catch (IOException e) {
                        System.out.println("ERROR: Port number " + port + " is already in use, " +
                                "please enter another port number");
                    }
                }
            }
        }
    }

    /**
     * Initializes WaitingList, add it to a thread and start it.
     * Everytime a new Client connects to server, a new concurrent ClientHandler will be initialized
     * @author Yasin
     */
    public void start(){
        WaitingList waitingList = new WaitingList(listOfClients);
        new Thread(waitingList).start();

        while (true){
            Socket socket;
            try {
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, log, listOfClients);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}




