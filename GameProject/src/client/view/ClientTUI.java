package client.view;

import client.controller.Client;
import local.view.InputHandler;
import scrabble.view.utils.Protocol;
import server.controller.ServerHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * 1
 */

public class ClientTUI {
    private BufferedReader bufferedReader;
    private ServerHandler serverHandler;
    private Client client;

    public ClientTUI(){
        this.bufferedReader  = new BufferedReader(new InputStreamReader(System.in));
    }

    public void printMessage(String msg){
        System.out.println(msg);
    }

    /**
     * Asks Client for a hostname to get ip address of server
     * @return ip address of server
     */
    public InetAddress getHostIP(){
        InetAddress ip = null;
        String input = "";

        while (ip == null){
            System.out.println("Please enter a host address");
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                System.out.println("ERROR: Input could not be read");
            }
            try {
                ip = InetAddress.getByName(input);
            } catch (UnknownHostException e) {
                System.out.println("ERROR: The given hostname '"+input+"' is unknown");
            }
        }
        return ip;
    }

    /**
     * Asks Client for port number of the server
     * @return port number
     */
    public int getPort(){
        int port = -1;
        String input = "";

        while (port == -1){
            System.out.println("Please enter a port number");
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                System.out.println("ERROR: Input could not be read");
            }
            try {
                port = Integer.parseInt(input);
            }
            catch (NumberFormatException e){
                System.out.println("ERROR: The given port '"+input+"' is not an Integer");
            }
            if(port<0 || port>65535){
                System.out.println("Port number must be a positive Integer and be smaller than 65535");
                port = -1;
            }
        }
        return port;
    }

    /**
     * Asks user for name
     * @return username
     */
    public String getUsername(){
        String username = null;

        while (username == null || username.trim().isEmpty()){
            System.out.println("Please enter a username");
            try {
                username = bufferedReader.readLine();
            } catch (IOException e) {
                System.out.println("ERROR: Input could not be read");
            }
        }
        return username;
    }

    /**
     * Handles input from Client
     */
    public void handleInput(){
        printCommands();
        String move = null;
        System.out.println("Enter your next move");
        try {
            move = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("ERROR: Input could not be read");
        }

        String[] parts = move.split(" ");

        String word = parts[0];
        switch (word.toUpperCase()){
            case "ANNOUNCE":
                client.sendMessage(word+Protocol.UNIT_SEPARATOR+parts[1]+Protocol.MESSAGE_SEPARATOR);
                break;
            case "REQUESTGAME":
                client.sendMessage(word+Protocol.MESSAGE_SEPARATOR);
                break;
            case "MAKEMOVE":
                if(serverHandler.isAllowedToMove()) {
                    if (parts[1].equals("WORD")) {
                        client.sendMessage(word + Protocol.UNIT_SEPARATOR + parts[1] + Protocol.UNIT_SEPARATOR + parts[2] + Protocol.UNIT_SEPARATOR + parts[3] + Protocol.UNIT_SEPARATOR + parts[4] + Protocol.MESSAGE_SEPARATOR);
                    } else if (parts[1].equals("SWAP")) {
                        if (parts.length == 3) {
                            client.sendMessage(word + Protocol.UNIT_SEPARATOR + parts[1] + Protocol.UNIT_SEPARATOR + parts[2] + Protocol.MESSAGE_SEPARATOR);
                        } else if (parts.length == 2) {
                            client.sendMessage(word + Protocol.UNIT_SEPARATOR + parts[1] + Protocol.MESSAGE_SEPARATOR);
                        } else {
                            printMessage("Error: Unknown Swap command");
                        }
                    } else {
                        printMessage("Error: Unknown move");
                    }
                }
                break;
            case "COMMANDS":
                printCommands();
                break;
            default:
                printMessage("Error invalid command");
                printCommands();
                break;
        }
    }

    public void printCommands(){
        System.out.println("You can do the following moves:\n1... 2... 3...");
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }
}

