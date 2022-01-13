package local.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Panel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 900;
    static final int SCREEN_HEIGHT = 900;
    static final int UNIT_SIZE = 60;
    static final int GAME_UNITS = 15*15;


    int[] x = new int[GAME_UNITS];
    int[] y = new int[GAME_UNITS];

    boolean running = false;



    public Panel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }


    public void startGame(){
        running = true;
    }


    public void draw(Graphics graphics){
        if (running){
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++){
                graphics.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
        }
    }


    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }


    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){

        }
    }
}
