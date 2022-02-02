package client.controller;

import client.view.ClientTUI;
import server.controller.ClientHandler;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private final String username;
    private final ClientTUI clientTUI;

    /**
     * Constructor of client
     * @param socket - socket to connect to server
     * @param username - A client needs a username to be instantiated
     * @author Yasin Fahmy
     */
    public Client(Socket socket, String username){
        try {
            this.socket = socket;
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            closeEverything();
        }
        this.username = username;
        clientTUI = new ClientTUI();
    }

    /**
     * Scans for username
     * Creates new socket on port 1234, localhost
     * TUI prints commands
     * Listens for and is ready to send messages at the same time
     * @author Yasin Fahmy
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ClientTUI.promptToAnnounce();
        String username = scanner.nextLine();

        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket, username);
        ClientTUI.printCommands();
        client.listenForMessages();
        client.sendMessage();
    }

    /**
     * Sends messages to clienthandler
     * @author Yasin Fahmy
     */
    public synchronized void sendMessage(){
        try {
            out.write(username);
            out.newLine();
            out.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                out.write(messageToSend);
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    /**
     * listens for messages from clienthandler
     * @author Yasin Fahmy
     */
    public synchronized void listenForMessages(){
        new Thread(() -> {
            String messageFromServer;

            while (socket.isConnected()){
                try {
                    messageFromServer = in.readLine();
                    handleInput(messageFromServer);

                }catch (IOException e){
                    closeEverything();
                }
            }
        }).start();
    }

    /**
     * handles input from client handler, such as a string representation of the current board
     * @param input - input that is received by the listenForMessagesThread
     * @author Yasin Fahmy
     */
    public void handleInput(String input){
        if(input.startsWith("BOARD")){
            StringBuilder stringBuilder = new StringBuilder(input);
            stringBuilder.delete(0,6);
            input = String.valueOf(stringBuilder);
            clientTUI.updateBoard(input);
        }
        else {
            String[] parts = input.split(" ");
            String command = parts[0];

            if ("NOTIFYTURN".equals(command)) {
                ClientTUI.promptToMakeMove(parts[0], parts[1], parts[2]);
            } else {
                ClientTUI.showMessage(input);
            }
        }
    }

    /**
     * Closes bufferedReader, -writer and socket
     * @author Yasin Fahmy
     */
    public void closeEverything(){
        ClientHandler.closeConnection(in, out, socket);
    }
}
