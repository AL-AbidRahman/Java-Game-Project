import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class DinoRun extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 750;
    int boardHeight = 250;

    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //dinosaur position relative to the frame
    int dinosaurWidth = 80;
    int dinosaurHeight = 85;
    int dinosaurX = 35;
    int dinosaurY = boardHeight - dinosaurHeight; // ground position

    Block dinosaur;

    
    int cactus1Width = 30;
    int cactus2Width = 75;
    int cactus3Width = 100;

    int cactusHeight = 70;
    int cactusX = 650;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray;

    
    int velocityX = -15; //cactus moving left speed
    int velocityY = 0; 
    int gravity = 2;

    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer CactusTimer;

    public DinoRun() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(getClass().getResource("./dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./cactus3.png")).getImage();

        //dinosaur
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);
        //cactus
        cactusArray = new ArrayList<Block>();

        //game timer
        gameLoop = new Timer(1000/60, this); //1000/60 = 60 frames per 1000ms (1s), update
        gameLoop.start();

        //place cactus timer
        CactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        CactusTimer.start();
    }

    void placeCactus() {
        if (gameOver) {
            return;
        }

        double renderCactus = Math.random(); 
        if (renderCactus > .90) { //10% chance to get cactus3 //
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
            cactusArray.add(cactus);
        }
        else if (renderCactus > .70) { //20% chance to  get cactus2 //
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
            cactusArray.add(cactus);
        }
        else if (renderCactus > .50) { //20% chance to  get cactus1 //
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
            cactusArray.add(cactus);
        }

        if (cactusArray.size() > 10) {
            cactusArray.remove(0); //remove the first cactus from ArrayList
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //dinosaur
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        //cactus
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 30));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else {
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    public void movement() {
        //here velocityY will keep on updating dinosaur vertical position, gravity will ensure dinosaur will remain in frame//
        velocityY += gravity;
        dinosaur.y += velocityY;

        if (dinosaur.y > dinosaurY) { 
            //stop the dinosaur from falling past the ground//
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        //cactus
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            cactus.x += velocityX;

            if (collision(dinosaur, cactus)) {
                gameOver = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }

        score++;
    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&   
               a.x + a.width > b.x &&   
               a.y < b.y + b.height &&  
               a.y + a.height > b.y;   
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        movement();
        repaint();
        if (gameOver) {
            CactusTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            
            if (dinosaur.y == dinosaurY) {
                velocityY = -25;
                dinosaur.img = dinosaurJumpImg;
            }
            
            if (gameOver) {
                
                dinosaur.y = dinosaurY;
                dinosaur.img = dinosaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                CactusTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
