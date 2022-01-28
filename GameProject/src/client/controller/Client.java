package client.controller;

import client.view.ClientTUI;
import scrabble.view.utils.Protocol;
import server.controller.ServerHandler;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 2
 */
public class Client {
    private Socket socket;
    private String username;
    private BufferedWriter out;
    private BufferedReader in;
    private ServerHandler serverHandler;
    private ClientTUI clientTUI;
    private boolean isLoggedIn;
    private boolean isRunning;

    public Client(Socket socket) throws IOException{
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.serverHandler = new ServerHandler(socket);
    }

    public static void main(String[] args) {
        ClientTUI tui = new ClientTUI();
        InetAddress ip = tui.getHostIP();
        int port = tui.getPort();

        try (Socket socket = new Socket(ip, port)) {

            Client client = new Client(socket);

            //Connect ServerHandler, Client, TUI
            client.setClientTUI(tui);
            client.getClientTUI().setClient(client);
            client.setServerHandler(new ServerHandler(socket));
            tui.setServerHandler(client.getServerHandler());
            client.getServerHandler().setClientTUI(client.getClientTUI());

            new Thread(client.getServerHandler()).start();
            client.start();
        }

        catch (IOException e) {
            System.out.println("ERROR: Connection to socket failed (IP: "+ip+", Port: "+port);
            System.exit(0); //Necessary?
        }
    }

    public void start(){
        isRunning = true;
        username = clientTUI.getUsername();
        sendMessage("ANNOUNCE" + Protocol.UNIT_SEPARATOR + username + Protocol.MESSAGE_SEPARATOR);

        while (serverHandler.isLoggedIn()) {
            clientTUI.printMessage("This user name is already in use");
            username = clientTUI.getUsername();
        }
        setUsername(username);
        clientTUI.handleInput();
    }

    public void sendMessage(String msg){
        try {
            out.write(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientTUI.printMessage("To Server: "+msg);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public ClientTUI getClientTUI() {
        return clientTUI;
    }

    public void setClientTUI(ClientTUI clientTUI) {
        this.clientTUI = clientTUI;
    }
}
