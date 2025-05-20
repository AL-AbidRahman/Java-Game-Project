import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 750;
        int boardHeight = 250;

        JFrame frame = new JFrame("Dino Run");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DinoRun dinoRun = new DinoRun();
        frame.add(dinoRun);
        frame.pack();
        dinoRun.requestFocus();
        frame.setVisible(true);
    }
}


