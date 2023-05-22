package Games.TicTacToe;

import javax.swing.JFrame;

public class Runner {
    public static void main (String[] args) {
        JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        TicTacToeGrid grid = new TicTacToeGrid();
        grid.setFocusable(true);
        frame.add(grid);
        frame.setVisible(true);
    } 
}
