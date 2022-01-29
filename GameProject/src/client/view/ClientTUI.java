package client.view;

import client.controller.Client;
import scrabble.model.Board;
import scrabble.view.TextBoardRepresentation;
import scrabble.view.utils.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * IDEAS:
 * 1)
 */

public class ClientTUI {
    private Scanner scanner;
    private Client client;
    private TextBoardRepresentation representation;


    public ClientTUI(Client client){
        this.client = client;
        this.scanner = new Scanner(System.in);
    }


    public void start(){
        printCommands();
        while (true){
            showMessage("Input: ");
            String s = scanner.nextLine();
            client.sendMessage();//Send input to client
        }
    }


    public void showMessage(String msg){
        System.out.println(msg);
    }


    public void updateBoard(String tiles){
        Board board = new Board();
        board.updateBoard(tiles);
        setRepresentation(new TextBoardRepresentation(board));
        System.out.println(representation);
    }


    public void setRepresentation(TextBoardRepresentation representation) {
        this.representation = representation;
    }


    public boolean getBoolean(String question){
        showMessage(question);
        showMessage("yes or no?");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }


    public int getInt(String question){
        showMessage(question);
        String s = scanner.nextLine();
        if(isNumeric(s)){
            return Integer.parseInt(s);
        }
        else {
            return 0;
        }
    }


    public boolean isNumeric(String string){
        try{
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }


//    /**
//     * Handles input from Client
//     */
//    public void handleInput(){
//        printCommands();
//        String move = null;
//        System.out.println("Enter your next move");
//        try {
//            move = bufferedReader.readLine();
//        } catch (IOException e) {
//            System.out.println("ERROR: Input could not be read");
//        }
//
//        String[] parts = move.split(" ");
//
//        String word = parts[0];
//        switch (word.toUpperCase()){
//            case "ANNOUNCE":
//                client.sendMessage(word+Protocol.UNIT_SEPARATOR+parts[1]+Protocol.MESSAGE_SEPARATOR);
//                break;
//            case "REQUESTGAME":
//                client.sendMessage(word+Protocol.MESSAGE_SEPARATOR);
//                break;
//            case "MAKEMOVE":
//                if(client.isAllowedToMove()) {
//                    if (parts[1].equals("WORD")) {
//                        client.sendMessage(word + Protocol.UNIT_SEPARATOR + parts[1] + Protocol.UNIT_SEPARATOR + parts[2] + Protocol.UNIT_SEPARATOR + parts[3] + Protocol.UNIT_SEPARATOR + parts[4] + Protocol.MESSAGE_SEPARATOR);
//                    } else if (parts[1].equals("SWAP")) {
//                        if (parts.length == 3) {
//                            client.sendMessage(word + Protocol.UNIT_SEPARATOR + parts[1] + Protocol.UNIT_SEPARATOR + parts[2] + Protocol.MESSAGE_SEPARATOR);
//                        } else if (parts.length == 2) {
//                            client.sendMessage(word + Protocol.UNIT_SEPARATOR + parts[1] + Protocol.MESSAGE_SEPARATOR);
//                        } else {
//                            showMessage("Error: Unknown Swap command");
//                        }
//                    } else {
//                        showMessage("Error: Unknown move");
//                    }
//                }
//                break;
//            case "COMMANDS":
//                printCommands();
//                break;
//            default:
//                showMessage("Error invalid command");
//                printCommands();
//                break;
//        }
//    }

    public void printCommands(){
        System.out.println("");
    }

    public void setClient(Client client) {
        this.client = client;
    }


}

