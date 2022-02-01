package scrabble.view.utils;

import scrabble.model.Game;

import java.util.Timer;
import java.util.TimerTask;

public class Countdown {
    int timer;
    Timer timerA = new Timer();

    public void counter(int counter, Game game){
        timer = counter;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (timer > 0){
                    timer--;
                } else {
                    timerA.cancel();
                    game.setTimeLeft(false);
                    game.getCurrentClient().sendMessage("Your time is over\n");
                }
            }
        };
        timerA.schedule(task,0,1000);
    }

    public Timer getTimerA() {
        return timerA;
    }
}
