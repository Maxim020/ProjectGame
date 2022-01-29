package client.controller;

import client.view.ClientTUI;
import scrabble.view.utils.Protocol;
import server.model.ExitProgram;
import server.model.ProtocolException;
import server.model.ServerUnavailableException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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


    public Client(Socket socket, String username){
        try {
            this.socket = socket;
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e){
            closeEverything();
        }

        //clientTUI = new ClientTUI(this);
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket, username);
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
                out.write(username+": "+messageToSend);
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    //Listen for messages, need thread because it is a blocking operation
    public void listenForMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;

                while (socket.isConnected()){
                    try {
                        messageFromGroupChat = in.readLine();
                        System.out.println(messageFromGroupChat);
                    }catch (IOException e){
                        closeEverything();
                    }
                }
            }
        }).start();
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
