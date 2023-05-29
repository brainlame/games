package Games.Mastermind;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gameboard extends JPanel implements KeyListener,Runnable {
    private int[][] board = new int[10][4];
    private int currentRow = 0;
    private int currentCol = 0;
    private int[] code = {(int)(Math.random()*6+1),(int)(Math.random()*6+1),(int)(Math.random()*6+1),(int)(Math.random()*6+1)};
    private String[] feedbacks = new String[10];
    public Gameboard() {
        addKeyListener(this);
        new Thread(this).start();
    }
    public Color decideColor(int num) {
        switch(num) {
            case 1: return Color.RED;
            case 2: return Color.ORANGE;
            case 3: return Color.YELLOW;
            case 4: return Color.GREEN;
            case 5: return Color.CYAN;
            case 6: return Color.PINK;
        }
        return Color.WHITE;
    }
    public void update(Graphics window) {
        paint(window);
    }
    public void paint(Graphics window) {
        paintGrid(window);
        paintGridAccessories(window);
        paintFeedback(window);
    }
    public void paintGrid(Graphics window) {
        // print the grid
        int x = 200, y = 100;
        window.setColor(Color.DARK_GRAY);
        window.fillRect(x-20,y-20,240,540);
        for (int i=0;i<10;i++) {
            x = 200;
            for (int j=0;j<4;j++) {
                window.setColor(decideColor(board[i][j]));
                window.fillOval(x,y,50,50);
                window.setColor(Color.BLACK);
                window.setFont(new Font("Sans Serif",Font.PLAIN,30));
                window.drawString(board[i][j]!=0 ? ""+board[i][j] : "",x+15,y+40);
                x += 50;
            }
            y += 50;
        }

        // check loss
        if (currentRow >= 10) {
            window.setColor(Color.BLACK);
            window.drawString("YOU LOSE",250,700);
            removeKeyListener(this);
        }
    }
    public void paintGridAccessories(Graphics window) {
        // print the secret code
        int x = 200, y = 100;
        for (int i=0;i<4;i++) {
            window.setColor(decideColor(code[i]));
            window.fillOval(x+i*50,20,50,50);
        }

        // print the number-color pairs
        window.setFont(new Font("Comic Sans MS",Font.BOLD,70));
        x = 30; y = 100;
        for (int i=1;i<=6;i++) {
            window.setColor(decideColor(i));
            window.fillOval(x,y,80,80);
            window.setColor(Color.BLACK);
            window.drawString(""+i,x+20,y+65);
            y += 90;
        }
    }
    public void paintFeedback(Graphics window) {
        window.setFont(new Font("Serif",Font.BOLD,40));
        int xCount, oCount;
        for (int i=0;i<feedbacks.length;i++) {
            if (feedbacks[i] == null) break;

            // get count of X's and O's
            int index = feedbacks[i].indexOf("O");
            if (index == -1) {
                xCount = feedbacks[i].length();
                oCount = 0;
            } else {
                xCount = index;
                oCount = feedbacks[i].length() - index;
            }

            // check win
            if (xCount == 4) {
                window.setColor(Color.BLACK);
                window.drawString("YOU WIN!",250,700);
                removeKeyListener(this);
            }

            // display the feedback
            int y = 100 + 50 * i;
            window.setColor(Color.RED);
            window.fillRoundRect(140,y+5,40,40,10,10);
            window.setColor(Color.WHITE);
            window.fillRoundRect(420,y+5,40,40,10,10);
            window.setColor(Color.BLACK);
            window.drawString(""+xCount,150,y+40);
            window.drawString(""+oCount,430,y+40);
        }
    }
    public void place(int type) {
        if (currentCol >= 4 || currentRow >= 10) {
            return;
        }
        board[currentRow][currentCol] = type;
        currentCol++;
    }
    public String checkGuess() {
        String ret = "";
        int[] codeCopy = new int[4];
        for (int i=0;i<4;i++) codeCopy[i] = code[i];

        for (int i=0;i<4;i++) {
            if (board[currentRow][i] == codeCopy[i]) {
                ret += "X";
                codeCopy[i] = 0;
            }
        }
        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                if (board[currentRow][i] == codeCopy[j]) {
                    ret += "O";
                    codeCopy[j] = 0;
                }
            }
        }
        currentRow++;
        currentCol = 0;
        return ret;
    }
    public void remove() {
        if (currentCol <= 0) {
            return;
        }
        currentCol--;
        board[currentRow][currentCol] = 0;
    }
    @Override
    public void run() {
        try {
            while(true) {
                Thread.sleep(15);
                repaint();
            }
        } catch (Exception e) {
            System.out.println("error! error! "+e.getMessage());
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_1: place(1); break;
            case KeyEvent.VK_2: place(2); break;
            case KeyEvent.VK_3: place(3); break;
            case KeyEvent.VK_4: place(4); break;
            case KeyEvent.VK_5: place(5); break;
            case KeyEvent.VK_6: place(6); break;
            case KeyEvent.VK_BACK_SPACE: remove(); break;
            case KeyEvent.VK_ENTER: 
                if (currentCol == 4 && currentRow < 10) {
                    feedbacks[currentRow] = checkGuess(); 
                }
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
