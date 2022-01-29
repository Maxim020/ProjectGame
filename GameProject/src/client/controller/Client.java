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
 * 2
 */
public class Client {
    private Socket socket;
    private String username;
    private Scanner scanner;
    private BufferedWriter out;
    private BufferedReader in;
    private ClientTUI clientTUI;
    private boolean isTurn;

    public Client(){
        clientTUI = new ClientTUI(this);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        (new Client()).start();

    }



    public void start(){
        //Wie kann ich gleichzeitig input vom client scannen und input vom server verarbeiten?
        //ClientTUI Runnable?
        try{
            createConnection();
        } catch (ExitProgram e){
            e.printStackTrace();
        }

        username = clientTUI.getUsername(); //ändern

        sendMessage("ANNOUNCE"+Protocol.UNIT_SEPARATOR+username+Protocol.MESSAGE_SEPARATOR);

        clientTUI.printCommands();
        while (true){
            clientTUI.showMessage("Input: ");
            String s = scanner.nextLine();
            sendMessage(s);//Send input to client
        }



        clientTUI.start(); //Start to welcome input from client

    }



    //done
    public void createConnection() throws ExitProgram {

        clearConnection();

        while (socket == null) {
            String host = "127.0.0.1";
            int port = clientTUI.getInt("Please type in a port number");
                try {
                    InetAddress address = InetAddress.getByName(host);
                    clientTUI.showMessage("Attempting to connect to " + address + " on port " + port);
                    socket = new Socket(address, port);
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println("Successfully connected");
                } catch (IOException e) {
                    clientTUI.showMessage("Error: Could not create socket on " + host + " and port " + port);
                    if (!clientTUI.getBoolean("Do you want to try again?")) {
                        throw new ExitProgram("Exit ...");
                    }
                }
        }
    }

    public String readLineFromServer() throws  ServerUnavailableException{
        if(in != null){
            try{
                String answer = in.readLine();
                if(answer == null){
                    throw new ServerUnavailableException("Could not read from server");
                }
                return answer;
            } catch (IOException e){
                throw new ServerUnavailableException("Could not read from server");
            }
        }
        else {
            throw new ServerUnavailableException("Could not read from server");
        }
    }


    public void clearConnection(){
        socket = null;
        in = null;
        out = null;
    }

    public void sendMessage(String msg){
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientTUI.showMessage("To Server: "+msg);
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public ClientTUI getClientTUI() {
        return clientTUI;
    }

    public void setClientTUI(ClientTUI clientTUI) {
        this.clientTUI = clientTUI;
    }
}
