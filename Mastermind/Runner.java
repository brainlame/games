package Games.Mastermind;

import javax.swing.JFrame;

public class Runner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mastermind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,800);
        Gameboard board = new Gameboard();
        board.setFocusable(true);
        frame.add(board);
        frame.setVisible(true);
    }
}
