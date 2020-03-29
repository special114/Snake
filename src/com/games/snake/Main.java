/* Basic game with the gameplay of snake
   author: Rafał Surdej
   03.2020
 */


package com.games.snake;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setBounds(300, 100, 800, 600);
        frame.setBackground(Color.DARK_GRAY);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Gameplay gameplay = new Gameplay();
        frame.add(gameplay);
        frame.setVisible(true);
    }
}
