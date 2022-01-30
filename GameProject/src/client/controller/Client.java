package client.controller;

import client.view.ClientTUI;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * IDEAS:
 * 1) CLientTUI implementation
 */
public class Client {
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private String username;
    private ClientTUI clientTUI;

    public Client(Socket socket, String username){
        try {
            this.socket = socket;
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            clientTUI = new ClientTUI();
        } catch (IOException e){
            closeEverything();
        }

        //clientTUI = new ClientTUI(this);
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please announce yourself by typing: ANNOUNCE [NAME]");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket, username);
        client.clientTUI.printCommands();
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

    //Listen for messages, need thread because it is a blocking operation
    public void listenForMessages(){
        new Thread(() -> {
            String messageFromGroupChat;

            while (socket.isConnected()){
                try {
                    messageFromGroupChat = in.readLine();
                    handleInput(messageFromGroupChat);
                    //System.out.println(messageFromGroupChat);
                }catch (IOException e){
                    closeEverything();
                }
            }
        }).start();
    }

    public void handleInput(String input){
        String[] parts = input.split(" ");
        String command = parts[0];

        switch (command){
            case "WELCOME":
                System.out.println(input+"\n");
                break;
            case "BOARD":
                if(parts.length == 2) {
                    clientTUI.updateBoard(parts[1]);
                }
                else {
                    clientTUI.updateBoard("");
                }
                break;
            case "TILES":
                System.out.println(input);
                break;
            case "NOTIFYTURN":
                System.out.println(input);
                break;
        }
    }

    public void closeEverything(){
        try{
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
