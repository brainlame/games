package Games.Connect_4;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

public class PieceGrid extends JPanel implements KeyListener, Runnable {
    private char[][] grid;
    private char currentPlayer;
    private boolean won;
    // * r is red, y is yellow
    public PieceGrid() {
        grid = new char[6][7];
        currentPlayer = 'r';
        this.addKeyListener(this);
        new Thread(this).start();
        won = false;
    }
    public boolean dropPiece(int col) {
        for (int row=grid.length-1;row>=0;row--) {
            if (grid[row][col] == 0) {
                grid[row][col] = currentPlayer;
                return true;
            }
        }
        return false;
    }
    public void update(Graphics window) {
        paint(window);
    } 
    public void paint(Graphics window) {
        super.paintComponent(window);

        // * board decorations
        window.setColor(Color.GRAY);
        window.fillRect(50,150,700,600);
        window.setColor(currentPlayer == 'r' ? Color.RED : Color.YELLOW);
        window.fillOval(350,50,100,100);

        window.setColor(Color.BLACK);
        window.setFont(new Font("Monospace",Font.BOLD,60));
        int pos = 80;
        for (int i=1;i<=7;i++) {
            window.drawString(""+i,pos,120);
            pos+=100;
        }

        // * grid
        int x = 55, y = 155;
        for (int i=0;i<6;i++) {
            x = 55;
            for (int j=0;j<7;j++) {
                if (grid[i][j] == 'r') {
                    window.setColor(Color.RED);
                }
                else if (grid[i][j] == 'y') {
                    window.setColor(Color.YELLOW);
                }
                else {
                    window.setColor(Color.WHITE);
                }
                window.fillOval(x,y,90, 90);
                x += 100;
            }
            y += 100;
        }

        // * check win
        if (won) {
            window.setColor(Color.BLACK);
            window.setFont(new Font("Comic Sans MS",Font.BOLD,50));
            window.drawString(currentPlayer == 'r' ? "RED WINS!" : "YELLOW WINS!",200,350);
            super.removeKeyListener(this);
        } 
    }
    public void changePlayer() {
        if (currentPlayer == 'r') currentPlayer = 'y';
        else currentPlayer = 'r';
    }
    public boolean hasWon() {
        // * horizontal
        for (int i=0;i<6;i++) {
            for (int j=0;j<=3;j++) {
                if (grid[i][j] == currentPlayer && grid[i][j+1] == currentPlayer && grid[i][j+2] == currentPlayer && grid[i][j+3] == currentPlayer) {
                    return true;
                }
            }
        }

        // * vertical
        for (int i=0;i<7;i++) {
            for (int j=0;j<=2;j++) {
                if (grid[j][i] == currentPlayer && grid[j+1][i] == currentPlayer && grid[j+2][i] == currentPlayer && grid[j+3][i] == currentPlayer) {
                    return true;
                }
            }
        }

        // * diagonal descending
        for (int i=0;i<=2;i++) {
            for (int j=0;j<=3;j++) {
                if (grid[i][j] == currentPlayer && grid[i+1][j+1] == currentPlayer && grid[i+2][j+2] == currentPlayer && grid[i+3][j+3] == currentPlayer) {
                    return true;
                }
            }
        }
        
        // * diagonal ascending
        for (int i=0;i<=2;i++) {
            for (int j=6;j>=3;j--) {
                if (grid[i][j] == currentPlayer && grid[i+1][j-1] == currentPlayer && grid[i+2][j-2] == currentPlayer && grid[i+3][j-3] == currentPlayer) {
                    return true;
                }
            }
        }

        return false;
    }
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(20);
                repaint();
            }
        } 
        catch (Exception e) {}
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        boolean dropped = false;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_1: dropped = dropPiece(0); break;
            case KeyEvent.VK_2: dropped = dropPiece(1); break;
            case KeyEvent.VK_3: dropped = dropPiece(2); break;
            case KeyEvent.VK_4: dropped = dropPiece(3); break;
            case KeyEvent.VK_5: dropped = dropPiece(4); break;
            case KeyEvent.VK_6: dropped = dropPiece(5); break;
            case KeyEvent.VK_7: dropped = dropPiece(6); break;
        }
        if (dropped) {
            if (hasWon()) {
                System.out.println(currentPlayer+" WINS");
                won = true;
            } else 
                changePlayer();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
