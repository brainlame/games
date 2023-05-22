package Games.e2048;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BlockGrid extends JPanel implements KeyListener, Runnable {
    private Block[][] grid;
    private int score = 0;
    public BlockGrid() {
        grid = new Block[4][4];
        spawnRandom();
        spawnRandom();
        this.addKeyListener(this);
        new Thread(this).start();
    }
    /**
     * Shift a specific block as far as possible toward a specified direction. 
     * The block will travel the maximum distance towards the specified direction before it either
     * MERGES with a block of the same value, or STOPS before a block of differing value or a wall.
     * @param row of block
     * @param col of block
     * @param direction left, right, up, down
     */
    public void shiftBlock(int row, int col, String direction) {
        if (grid[row][col] == null) return;
        if (direction.equals("left")) {
            for (int i=col-1;i>=0;i--) {
                if (grid[row][i] == null) {
                    grid[row][i] = grid[row][col];
                    grid[row][col] = null;
                    col--;
                } else {
                    if (grid[row][i].getValue() == grid[row][col].getValue()) {
                        grid[row][i].doubleValue();
                        grid[row][col] = null;
                        score += grid[row][i].getValue();
                    }
                    return;
                }
            }
        }
        else if (direction.equals("right")) {
            for (int i=col+1;i<4;i++) {
                if (grid[row][i] == null) {
                    grid[row][i] = grid[row][col];
                    grid[row][col] = null;
                    col++;
                } else {
                    if (grid[row][i].getValue() == grid[row][col].getValue()) {
                        grid[row][i].doubleValue();
                        grid[row][col] = null;
                        score += grid[row][i].getValue();
                    }
                    return;
                }
            }
        }
        else if (direction.equals("up")) {
            for (int i=row-1;i>=0;i--) {
                if (grid[i][col] == null) {
                    grid[i][col] = grid[row][col];
                    grid[row][col] = null;
                    row--;
                } else {
                    if (grid[i][col].getValue() == grid[row][col].getValue()) {
                        grid[i][col].doubleValue();
                        grid[row][col] = null;
                        score += grid[i][col].getValue();
                    }
                    return;
                }
            }
        }
        else if (direction.equals("down")) {
            for (int i=row+1;i<4;i++) {
                if (grid[i][col] == null) {
                    grid[i][col] = grid[row][col];
                    grid[row][col] = null;
                    row++;
                } else {
                    if (grid[i][col].getValue() == grid[row][col].getValue()) {
                        grid[i][col].doubleValue();
                        grid[row][col] = null;
                        score += grid[i][col].getValue();
                    }
                    return;
                }
            }
        }
    }
    public void shiftLeft() {
        for (int i=0;i<4;i++) {
            for (int j=1;j<4;j++) {
                shiftBlock(i,j,"left");
            }
        }
    }
    public void shiftRight() {
        for (int i=0;i<4;i++) {
            for (int j=2;j>=0;j--) {
                shiftBlock(i,j,"right");
            }
        }
    }
    public void shiftUp() {
        for (int i=1;i<4;i++) {
            for (int j=0;j<4;j++) {
                shiftBlock(i,j,"up");
            }
        }
    }
    public void shiftDown() {
        for (int i=2;i>=0;i--) {
            for (int j=0;j<4;j++) {
                shiftBlock(i,j,"down");
            }
        }
    }
    public void spawnRandom() {
        int row,col;
        do {
            row = (int)(Math.random() * 4); 
            col = (int)(Math.random() * 4);
        } while (grid[row][col] != null);
        grid[row][col] = new Block((int) (Math.random() * 2) * 2 + 2);
    }
    public void paintGrid(Graphics window) {
        window.setColor(Color.GRAY);
        window.fillRoundRect(50,150,400,400,20,20);
        window.setColor(Color.BLACK);
        window.setFont(new Font("Roboto",Font.BOLD,30));
        window.drawString("Score: "+score,180,75);
        int x = 55, y = 155;
        for (int i=0;i<4;i++) {
            x = 55;
            for (int j=0;j<4;j++) {
                Block square = grid[i][j];
                if (square != null) {
                    window.setColor(square.getColor());
                    window.fillRoundRect(x,y,92,92,15,15);
                    window.setColor(Color.BLACK);
                    window.setFont(new Font("Roboto",Font.BOLD,30));
                    window.drawString(""+square.getValue(), x+30, y+60);
                } else {
                    window.setColor(Color.LIGHT_GRAY);
                    window.fillRoundRect(x,y,92,92,15,15);
                }
                x += 100;
            }
            y += 100;
        }
    }
    public boolean isGameLost() {
        // Check for empty cells
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == null) {
                    return false; // Game is not lost
                }
            }
        }
        
        // Check for adjacent cells with the same value
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int current = grid[i][j].getValue();
                
                // Check horizontally adjacent cells
                if (j < grid[i].length - 1 && current == grid[i][j + 1].getValue()) {
                    return false;
                }
                
                // Check vertically adjacent cells
                if (i < grid.length - 1 && current == grid[i + 1][j].getValue()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public void update(Graphics window) {
        paint(window);
    }
    public void paint(Graphics window) {
        super.paintComponent(window);
        paintGrid(window);
        if (isGameLost()) {
            window.setColor(Color.RED);
            window.fillOval(50, 150, 400, 400);
            window.setColor(Color.BLACK);
            window.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
            window.drawString("YOU SUCK YOU DED",0,400);
        }
    }
    public void run() {
        try {
            while (true) {
                Thread.sleep(10);
                repaint();
            }
        } 
        catch (Exception e) {}
    }
    public Block[][] copyGrid() {
        Block[][] copy = new Block[4][4];
        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                copy[i][j] = grid[i][j];
            }
        }
        return copy;
    }
    public boolean isEqualArray(Block[][] copy) {
        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                if (grid[i][j] != copy[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        Block[][] copy = copyGrid();
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT: case KeyEvent.VK_A: case KeyEvent.VK_J: shiftLeft(); break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: case KeyEvent.VK_L: shiftRight(); break;
            case KeyEvent.VK_UP: case KeyEvent.VK_W: case KeyEvent.VK_I: shiftUp(); break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_S: case KeyEvent.VK_K: shiftDown(); break;
        }
        if (!isEqualArray(copy)) {
            spawnRandom();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}

