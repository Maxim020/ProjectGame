package scrabble.view.utils;

import java.util.Timer;
import java.util.TimerTask;

public class Countdown {
    int timer;

    public void counter(int counter){
        timer = counter;

        Timer timerA = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (timer >= 0){

//                    if(timer == 30) {
//                        System.out.println("You have "+timer+" seconds left");
//                    }
//                    else if(timer == 15 || (timer <= 5 && timer>0)){
//                        System.out.println(" ... "+timer);
//                    }

                    timer--;
                } else {
                    System.out.println("Your time is over");
                    timerA.cancel();
                }
            }
        };
        timerA.schedule(task,0,1000);
    }

    //Timer of Opponent starts --> revert move within timeframe?
//                    int counter = 60;
//
//                    Countdown countdown = new Countdown();
//                    System.out.println("\nYou have one minute to decide!");
//                    countdown.counter(counter);
}
