package server.controller;

import client.controller.ClientHandler;
import scrabble.model.Game;

import java.util.ArrayList;

public class WaitingList implements Runnable {
    private ArrayList<ClientHandler> waitingList;
    private boolean requestGame;
    private Game game;

    public WaitingList(ArrayList<ClientHandler> waitingList) {
        this.waitingList = waitingList;
        this.requestGame = false;
    }

    /**
     * If there are at least two players in the queue and XYZ requests a game
     * All participants will be assigned a new game
     * The participants will be removed from the waiting list
     * The game is started
     */
    @Override
    public void run() {
        while (true) {
            while (waitingList.size() < 2){
                try{
                    Thread.sleep(250);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            game = new Game(waitingList.get(0), waitingList.get(1));
            waitingList.get(0).setGame(game);
            waitingList.get(1).setGame(game);
            waitingList.remove(waitingList.get(1));
            waitingList.remove(waitingList.get(0));
            game.start();
        }
    }

    public ArrayList<ClientHandler> getWaitingList() {
        return waitingList;
    }
}
