package Games.Minesweeper;

import javax.swing.JFrame;

public class Runner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Minesweep");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,625);
        frame.add(new MinePanel("medium"));
        frame.setVisible(true);
    }
}
