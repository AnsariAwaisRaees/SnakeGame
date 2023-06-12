import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;

public class Board extends JPanel implements ActionListener {
    int B_Height = 400;
    int B_Width = 400;
    int max_Dot = 1600;
    int dot_Size = 10;
    int dots;
    int x[] = new int[max_Dot];
    int y[] = new int[max_Dot];
    int apple_x, apple_y;
    Image body, head, apple;
    Timer timer;
    int DELAY = 150;
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean inGame = true;
    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);

        setPreferredSize(new Dimension(B_Width, B_Height));
        setBackground(Color.BLACK);
        initGame();
        loadingImage();
    }

    public void initGame(){
        dots = 3;

        x[0] = 50;
        y[0] = 50;
        for (int i=1; i<dots; i++){
            x[i] = x[0]+dot_Size*i;
            y[i] = y[0];
        }
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void loadingImage(){
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        doDrawing(graphics);
    }
    public void doDrawing(Graphics graphics){
        if(inGame){
            graphics.drawImage(apple, apple_x, apple_y, this);

            for(int i=0; i<dots; i++){
                if(i == 0){
                    graphics.drawImage(head, x[0], y[0], this);
                }
                else{
                    graphics.drawImage(body, x[i], y[i], this);
                }
            }
        }
        else{
            gameOver(graphics);
            timer.stop();
        }
    }
    public void locateApple(){
        apple_x = ((int) (Math.random()*39))*dot_Size;
        apple_y = ((int) (Math.random()*39))*dot_Size;
    }

    public void checkCollision(){
        for(int i=1; i<dots; i++){
            if(i > 4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        if(x[0] < 0){
            inGame = false;
        }
        if(x[0] >= B_Width){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
        if(y[0] >= B_Height){
            inGame = false;
        }
    }

    public void gameOver(Graphics graphics){
        String msg = "Game Over";
        int score = (dots-3)*100;
        String scoreMsg = "Score : " + Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(small);

        graphics.setColor(Color.WHITE);
        graphics.setFont(small);
        graphics.drawString(msg, (B_Width-fontMetrics.stringWidth(msg))/2, B_Height/4);
        graphics.drawString(scoreMsg, (B_Width-fontMetrics.stringWidth(scoreMsg))/2, 3*(B_Height/4));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public void move(){
        for(int i=dots-1; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0] -= dot_Size;
        }
        if(rightDirection){
            x[0] += dot_Size;
        }
        if(upDirection){
            y[0] -= dot_Size;
        }
        if(downDirection){
            y[0] += dot_Size;
        }
    }
    public void checkApple(){
        if(apple_x == x[0] && apple_y == y[0]){
            dots++;
            locateApple();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key =  keyEvent.getKeyCode();

            if(key == keyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == keyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == keyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key == keyEvent.VK_DOWN && !upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
