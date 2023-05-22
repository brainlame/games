package Games.e2048;

import javax.swing.*;
import java.awt.*;

public class Runner extends Canvas{
    public static void main(String[] args) {
        JFrame frame = new JFrame("2048");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,600);
        BlockGrid grid = new BlockGrid();
        grid.setFocusable(true);
        frame.add(grid);
        frame.setVisible(true);
    }
}
