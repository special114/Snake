/* Basic game with the gameplay of snake
   author: Rafał Surdej
   03.2020
 */

package com.games.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;


public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private final int HEAD_IDX = 0;
    private final int FIELD_SIZE = 25;
    private final int X_OFFSET = 25;
    private final int Y_OFFSET = 75;
    private final int MAX_SNAKE_LENGTH = 30 * 19;

    private Image title_image;

    private int[][] board = new int [30][19]; // x and y of playable board

    private final short EMPTY = 0;
    private final short SNAKE = 1;
    private final short APPLE = 2;

    private ArrayList<Point> snake_points = new ArrayList();

    private int snake_length = 3;

    private boolean start_game = false;
    private boolean end_game = false;

    private boolean right = true;
    private boolean left = false;
    private boolean up = false;
    private boolean down = false;

    private Random random = new Random();

    private int apple_x_pos;
    private int apple_y_pos;

    private Image snake_head;
    private Image snake_head_r;
    private Image snake_head_l;
    private Image snake_head_u;
    private Image snake_head_d;
    private Image snake_body;
    private Image apple;

    private Timer timer;
    private final int DELAY =  140;

    private long currentTime;
    private long lastKeyProcessed = 0;

    public Gameplay() {
        loadImages();
        initValues();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(DELAY, this);
        timer.start();

    }

    private void initValues() {

        for (int i = 3; i >= 1; --i) {
            Point beginning_snake = new Point(i, 1);
            snake_points.add(beginning_snake);
            board[beginning_snake.x][beginning_snake.y] = SNAKE;
        }
        locateApple();
    }
    private void loadImages () {
        title_image = new ImageIcon("Graphics/snaketitle.jpg").getImage();
        apple = new ImageIcon("Graphics/snakeimage1.png").getImage();
        snake_body = new ImageIcon("Graphics/snakeimage2.png").getImage();
        snake_head_r = new ImageIcon("Graphics/snakeimageR.png").getImage();
        snake_head_l = new ImageIcon("Graphics/snakeimageL.png").getImage();
        snake_head_u = new ImageIcon("Graphics/snakeimageU.png").getImage();
        snake_head_d = new ImageIcon("Graphics/snakeimageD.png").getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!end_game) {

            // drawing title image
            g.drawImage(title_image, 350, 0,this);


            // drawing playable space background
            g.setColor(Color.WHITE);
            g.fillRect(25, 75, 750, 475);

            g.setColor(Color.BLACK);
            String score = "Score: ";
            g.drawString(score + (snake_length -3), 30, 50);


            if (right) {
                snake_head = snake_head_r;
            } else if (left) {
                snake_head = snake_head_l;
            } else if (up) {
                snake_head = snake_head_u;
            } else if (down) {
                snake_head = snake_head_d;
            }

            // drawing the snake - head
            Point head = snake_points.get(HEAD_IDX);
            g.drawImage(snake_head, head.x * FIELD_SIZE + X_OFFSET, head.y * FIELD_SIZE + Y_OFFSET, this);

            // rest of body
            for (int i = 1; i < snake_length; ++i) {
                Point body = snake_points.get(i);
                g.drawImage(snake_body, body.x * FIELD_SIZE + X_OFFSET, body.y * FIELD_SIZE + Y_OFFSET, this);
            }

            // drawing apple
            g.drawImage(apple, apple_x_pos * FIELD_SIZE + X_OFFSET, apple_y_pos * FIELD_SIZE + Y_OFFSET, this);

            Toolkit.getDefaultToolkit().sync();
        }
        else {
            if (snake_length == MAX_SNAKE_LENGTH)
                g.drawString("Congratulations, You have won! Your score was: " + (snake_length - 3), 30, 200);
            else
                g.drawString("Game over. Your score was: " + (snake_length - 3), 30, 200);
        }

        g.dispose();

    }





    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (start_game) {

            // moving snake's body forward
            Point tail = snake_points.get(snake_length - 1);
            board[tail.x][tail.y] = EMPTY;

            for (int i = snake_length - 1; i > 0; --i) {
                Point p1 = snake_points.get(i);
                Point p2 = snake_points.get(i - 1);

                p1.x = p2.x;
                p1.y = p2.y;
            }

            Point head = snake_points.get(HEAD_IDX);

            if (left) {
                head.x = head.x - 1;
            } else if (right) {
                head.x = head.x + 1;
            } else if (up) {
                head.y = head.y - 1;
            } else if (down) {
                head.y = head.y + 1;
            }

            // checking possible colissions with board borders and snake's body
            if (head.x < 0 || head.x >= 30 || head.y < 0 || head.y >= 19 || board[head.x][head.y] == SNAKE)
                end_game = true;

            else {
                if (board[head.x][head.y] == APPLE) {
                    board[head.x][head.y] = SNAKE;
                    eatApple();
                }

                board[head.x][head.y] = SNAKE;
            }

            if (end_game) {
                timer.stop();
            }
        }
        repaint();
    }


    private void eatApple() {                // increasing sneake's length and replacing the apple
        Point snake_tail = snake_points.get(snake_length - 1);
        snake_points.add(new Point(snake_tail));
        ++snake_length;
        locateApple();
    }

    private void locateApple() {
        /* replacing the apple
           25 - 750 avaliable width coordinates, (1 - 30) * 25
           75 - 525 avaliable height coordinates, (3 - 21) * 25
         */
        if (snake_length == 30 * 19)
            end_game = true;
        else {
            apple_x_pos = random.nextInt(29);
            apple_y_pos = random.nextInt(18);
            if (board[apple_x_pos][apple_y_pos] == SNAKE)
                locateApple(); // locating it once more if the snake is on randomly chosen fiels
            else
                board[apple_x_pos][apple_y_pos] = APPLE;
        }

    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        currentTime = System.currentTimeMillis();

        /* Below we are checking if the keys are not pressd too fast which would lead
           to incorrect actions
         */
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT && currentTime - lastKeyProcessed >= DELAY) {
            if (!right) {           // if it wasn't previously moving to the right
                start_game = true;
                left = true;
                up = false;
                down = false;
            }
        }
        else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT && currentTime - lastKeyProcessed >= DELAY) {
            if (!left) {
                start_game = true;
                right = true;
                up = false;
                down = false;
            }
        }
        else if (keyEvent.getKeyCode() == KeyEvent.VK_UP && currentTime - lastKeyProcessed >= DELAY) {
            if (!down) {
                start_game = true;
                up = true;
                right = false;
                left = false;
            }
        }
        else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN && currentTime - lastKeyProcessed >= DELAY) {
            if (!up) {
                start_game = true;
                down = true;
                right = false;
                left = false;
            }
        }
        lastKeyProcessed = System.currentTimeMillis();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
