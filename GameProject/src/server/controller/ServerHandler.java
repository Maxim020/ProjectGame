package server.controller;

import client.view.ClientTUI;

import java.io.IOException;
import java.net.Socket;

public class ServerHandler implements Runnable{

    private ClientTUI clientTUI;

    public ServerHandler(Socket socket) throws IOException {

    }

    public boolean validateInput(String move){
        return true;
    }

    public void setClientTUI(ClientTUI clientTUI) {
        this.clientTUI = clientTUI;
    }

    @Override
    public void run() {

    }
}
