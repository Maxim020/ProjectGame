package client.controller;

import client.view.ClientTUI;
import server.controller.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Provides a Class that represents the Client
 * @author Yasin Fahmy
 */

public class Client {
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private final String username;
    private final ClientTUI clientTUI;


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


    public synchronized void listenForMessages(){
        new Thread(() -> {
            String messageFromServer;

            while (socket.isConnected()){
                try {
                    messageFromServer = in.readLine();
                    handleInput(messageFromServer);
                    //System.out.println(messageFromGroupChat);
                }catch (IOException e){
                    closeEverything();
                }
            }
        }).start();
    }


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
                //Used for WELCOME, NEWTILES, and group chat
                ClientTUI.showMessage(input);
            }
        }
    }


    public void closeEverything(){
        ClientHandler.closeConnection(in, out, socket);
    }
}
