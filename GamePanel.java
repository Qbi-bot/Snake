import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel  implements ActionListener {

    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int unitSize = 25;
    static final int gameUnits = (screenHeight * screenHeight) / unitSize;
    static final int delay = 50;
    final int[] x = new int[gameUnits];
    final int[] y = new int[gameUnits];
    int bodyparts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
        applesEaten = 0;
        bodyparts = 6;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if (running){
            for (int i = 0; i < screenHeight; i++) {
                g.drawLine(i*unitSize, 0,i*unitSize, screenHeight);
                g.drawLine(0,i*unitSize, screenWidth, i*unitSize);
            }
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,unitSize,unitSize);

            for (int i = 0; i < bodyparts; i++) {
                if (i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }else{
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free",Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (screenWidth - metrics.stringWidth("Score: " + applesEaten))/2,g.getFont().getSize());
        }else {
            gameOver(g);
        }

    }
    public void move(){
        for (int i = bodyparts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - unitSize;
            case 'D' -> y[0] = y[0] + unitSize;
            case 'L' -> x[0] = x[0] - unitSize;
            case 'R' -> x[0] = x[0] + unitSize;
        }
    }
    public void newApple(){
        appleX = random.nextInt(screenWidth/unitSize)*unitSize;
        appleY = random.nextInt(screenHeight/unitSize)*unitSize;
    }
    public void checkApple(){
        if (x[0] == appleX && y[0] == appleY){
            bodyparts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){

        // checks if head collides with body
        for (int i = bodyparts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        // check if head touches left border

        if(x[0]<0){
            x[0] = screenWidth;
        }
        // checks if head touches right border

        if(x[0]> screenWidth){
            x[0] = 0;
        }
        // checks if head touches floor

        if(y[0] < 0){
            y[0] = screenHeight;
        }

        //checks if head touched roof

        if (y[0] > screenHeight){
            y[0] = 0;
        }

        if (!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        // Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (screenWidth - metrics1.stringWidth("Score: " + applesEaten))/2,(screenHeight/4*3));

        g.setFont(new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screenWidth - metrics.stringWidth("Game Over"))/2,(screenHeight/2));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> {
                    if (direction != 'R' && y[0] >= 0 && y[0] < screenHeight) {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_D -> {
                    if (direction != 'L' && y[0] >= 0 && y[0] < screenHeight) {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_W -> {
                    if (direction != 'D' && x[0] >= 0 && x[0] < screenWidth) {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_S -> {
                    if (direction != 'U' && x[0] >= 0 && x[0] < screenWidth) {
                        direction = 'D';
                    }
                }
            }
        }
    }
}