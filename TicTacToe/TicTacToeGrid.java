package Games.TicTacToe;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.*;
import java.awt.*;

public class TicTacToeGrid extends JPanel implements MouseListener,Runnable {
    private char[] board = {' ',' ',' ',' ',' ',' ',' ',' ',' '};
    private char currPlayer;
    private char winner;
    private String scene;
    private String mode;
    private int xWins, oWins;

    public TicTacToeGrid() {
        currPlayer = 'X';
        winner = ' ';
        scene = "choose mode";
        this.addMouseListener(this);
        new Thread(this).start();
    }
    public boolean checkWin(int move) {
        int rowStart = move / 3 * 3;
        int colStart = move % 3;
        if (board[rowStart] == 'X' && board[rowStart+1] == 'X' && board[rowStart+2] == 'X'
            || board[colStart] == 'X' && board[colStart+3] == 'X' && board[colStart+6] == 'X'
            || board[0] == 'X' && board[4] == 'X' && board[8] == 'X'
            || board[2] == 'X' && board[4] == 'X' && board[6] == 'X') {
            return true;
        } 
        else if (board[rowStart] == 'O' && board[rowStart+1] == 'O' && board[rowStart+2] == 'O'
            || board[colStart] == 'O' && board[colStart+3] == 'O' && board[colStart+6] == 'O'
            || board[0] == 'O' && board[4] == 'O' && board[8] == 'O'
            || board[2] == 'O' && board[4] == 'O' && board[6] == 'O') {
            return true;
        }
        return false;
    }
    public boolean emptySpaces() {
        for (char i : board) {
            if (i == ' ')
                return true;
        }
        return false;
    }
    public boolean moveAvailable(int move) {
        if ((move >= 0 && move <= 8) && board[move] == ' ')
            return true;
        return false;
    }
    public void changePlayer() {
        if (currPlayer == 'X') currPlayer = 'O';
        else if (currPlayer == 'O') currPlayer = 'X';
    }
    public void update(Graphics window) {
        paint(window);
    }
    public void paint(Graphics window) {
        
        // add the 2 player or vs computer button
        if (scene.equals("choose mode")) {
            paintStartScreen(window);
        } 
        
        else if (scene.equals("play")){
            paintGrid(window);
        } 
        
        else if (scene.equals("end")) {
            paintGrid(window);
            paintEndScreen(window);
        }
    }
    public void paintStartScreen(Graphics window) {
        window.setFont(new Font("Monospace",Font.CENTER_BASELINE,15));
        window.setColor(Color.GRAY);
        window.fillRect(100,200,100,100);
        window.fillRect(300,200,100,100);
        window.setColor(Color.WHITE);
        window.drawString("2 Player",100,260);
        window.drawString("vs Computer",280,260);
    }
    public void paintGrid(Graphics window) {
        window.setColor(Color.BLACK);
        window.fillRect(0,0,500,500);
        // paint the grid
        window.setFont(new Font("Comic Sans MS",Font.CENTER_BASELINE,40));

        window.setColor(Color.LIGHT_GRAY);
        window.fillRect(200,100,5,300);
        window.fillRect(300,100,5,300);
        window.fillRect(100,200,300,5);
        window.fillRect(100,300,300,5);

        int x = 100, y = 0;
        for (int i=0;i<9;i++) {
            if (i%3 == 0) {
                x = 100;
                y += 100;
            }
            if (board[i] == 'X') {
                window.setColor(Color.ORANGE);
                window.drawString("X",x+35,y+65);
            } else if (board[i] == 'O') {
                window.setColor(Color.MAGENTA);
                window.drawString("O",x+35,y+65);
            }
            x += 100;
        }

        // highlight the player whose turn it is
        if (currPlayer == 'X') {
            window.setColor(Color.RED);
            window.fillOval(40,180,20,20);
            window.setColor(Color.LIGHT_GRAY);
            window.fillRoundRect(10,210,80,80,10,10);
            window.setColor(Color.DARK_GRAY);
            window.fillRoundRect(410,210,80,80,10,10);
        } else {
            window.setColor(Color.RED);
            window.fillOval(440,180,20,20);
            window.setColor(Color.DARK_GRAY);
            window.fillRoundRect(10,210,80,80,10,10);
            window.setColor(Color.LIGHT_GRAY);
            window.fillRoundRect(410,210,80,80,10,10);
        }

        // display xWins and yWins
        window.setColor(Color.ORANGE);
        window.drawString("X:",30,250);
        window.setColor(Color.MAGENTA);
        window.drawString("O:",430,250);
        
        window.setColor(Color.WHITE);
        window.setFont(new Font("Monospace",Font.PLAIN,20));
        window.drawString(""+xWins,40,280);
        window.drawString(""+oWins,440,280);
    }
    public void paintEndScreen(Graphics window) {
        window.setColor(Color.BLACK);

        // print winner
        window.setFont(new Font("Comic Sans MS",Font.CENTER_BASELINE,20));

        window.setColor(Color.GRAY);
        window.fillRect(130,30,240,60);
        window.setColor(Color.BLACK);
        if (winner == 'X' || winner == 'O') {
            window.drawString(winner+" WINS!!!",200,70);
        } else if (winner == 'T') {
            window.drawString("TIE!!!",220,70);
        }

        // play again button
        window.setColor(Color.LIGHT_GRAY);
        window.fillRect(150,420,200,60);
        window.setColor(Color.WHITE);
        window.drawString("PLAY AGAIN",185,460);
    }
    @Override
    public void run() {
        try{
            while (true) {
                Thread.sleep(15);
                repaint();
            }
        } catch (Exception e) {}
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        int row = e.getY() / 100 - 1;
        int col = e.getX() / 100 - 1;
        int index = row * 3 + col;

        if (scene.equals("choose mode")) {
            // set the game mode
            if (row == 1 && col == 0) {
                mode = "player";
                scene = "play";
            } else if (row == 1 && col == 2) {
                mode = "computer";
                scene = "play";
            }
        } 
        else if (scene.equals("play")){
            if (moveAvailable(index)) {
                board[index] = currPlayer;

                // change winner
                if (checkWin(index)) {
                    winner = currPlayer;
                    repaint();
                    if (winner == 'X') xWins++;
                    else oWins++;
                    scene = "end";
                } 

                // tie
                else if (!emptySpaces()) {
                    winner = 'T';
                    scene = "end";
                } 

                changePlayer();
            }
        } 
        else if (scene.equals("end")) {
            if (row == 3 && col >= 0 && col <= 2) {
                for (int i=0;i<9;i++) board[i] = ' ';
                scene = "play";
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
