package view.gui;

import javax.swing.*;

public class Frame extends JFrame {
    public Frame(){
        this.add(new Panel());
        this.setTitle("Scrabble");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); //The pack() method is defined in Window class in Java, and it sizes the frame so that all its contents are at or above preferred sizes
        this.setVisible(true);
        this.setLocationRelativeTo(null); //Window appears in the middle og the screen
    }

}
