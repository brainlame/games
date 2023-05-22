package Games.Connect_4;

import javax.swing.JFrame;

public class Runner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Connect 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        PieceGrid grid = new PieceGrid();
        grid.setFocusable(true);
        frame.add(grid);
        frame.setVisible(true);
    }
}