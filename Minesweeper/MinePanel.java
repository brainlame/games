package Games.Minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

// * CREDITS TO RICKY FOR GRAPHICS
public class MinePanel extends JPanel implements MouseListener, Runnable {
	private char[][] board; // actual board
	private char[][] mines; // used for checking mines
	private int status; //1=win, 2=loss
	
	public MinePanel(String diff) {
		this.setBackground(Color.LIGHT_GRAY);
		this.addMouseListener(this);
		this.initBoard(diff);
		this.setMines();
		status = -1; //first click, tells program to reroll if necessary
		new Thread(this).start();
		this.setVisible(true);
	}
    private enum Difficulties {
        EASY(5),
        MEDIUM(10),
        HARD(15);
        public int dim;
        private Difficulties(int dim) {
            this.dim = dim;
        }
    }
    private void initBoard(String difficulty) {
        Difficulties diff;
        switch (difficulty) {
            case "easy":
                diff = Difficulties.EASY;
                break;
            case "hard":
                diff = Difficulties.HARD;
                break;
            default:
                diff = Difficulties.MEDIUM;
        }
        board = new char[diff.dim][diff.dim];
        mines = new char[diff.dim][diff.dim];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = '/';
            }
        }
    }
    private void setMines() {
    	mines = new char[board.length][board.length];
        for (int i = 0; i < (board.length * board.length) / 6; i++) {
            int randrow = 0;
            int randcol = 0;
            do {
                // generate new random coordinate and try to find mineless square
                randrow = (int) (Math.random() * board.length);
                randcol = (int) (Math.random() * board[0].length);
            } while (mines[randrow][randcol] == 'X');
            mines[randrow][randcol] = 'X';
        }
    }
    /** rerolls the mines until the selected square is a 0 */
    private void initialReroll(int row, int col) {
        while (getMineCount(row, col) != 0 || mines[row][col] == 'X') {
            mines = new char[mines.length][mines[0].length];
            setMines();
        }
        revealSquare(row, col);
        status = 0;
    }
    /** check the # of mines surrounding a given square */
    private int getMineCount(int row, int col) {
        int mineCount = 0;
        // corners
        if (row == 0 && col == 0) {
            if (mines[row + 1][col] == 'X')
                mineCount++;
            if (mines[row][col + 1] == 'X')
                mineCount++;
            if (mines[row + 1][col + 1] == 'X')
                mineCount++;
        } else if (row == mines.length - 1 && col == 0) {
            if (mines[row - 1][col] == 'X')
                mineCount++;
            if (mines[row - 1][col + 1] == 'X')
                mineCount++;
            if (mines[row][col + 1] == 'X')
                mineCount++;
        } else if (row == 0 && col == mines[0].length - 1) {
            if (mines[row][col - 1] == 'X')
                mineCount++;
            if (mines[row + 1][col - 1] == 'X')
                mineCount++;
            if (mines[row + 1][col] == 'X')
                mineCount++;
        } else if (row == mines.length - 1 && col == mines[0].length - 1) {
            if (mines[row - 1][col - 1] == 'X')
                mineCount++;
            if (mines[row - 1][col] == 'X')
                mineCount++;
            if (mines[row][col - 1] == 'X')
                mineCount++;
        }
        // edges
        else if (row == 0 && !(col == 0 || col == mines[0].length - 1)) { // top edge
            if (mines[row][col - 1] == 'X')
                mineCount++;
            if (mines[row][col + 1] == 'X')
                mineCount++;
            if (mines[row + 1][col - 1] == 'X')
                mineCount++;
            if (mines[row + 1][col] == 'X')
                mineCount++;
            if (mines[row + 1][col + 1] == 'X')
                mineCount++;
        } else if (row == mines.length - 1 && !(col == 0 || col == mines[0].length - 1)) { // bottom edge
            if (mines[row - 1][col - 1] == 'X')
                mineCount++;
            if (mines[row - 1][col] == 'X')
                mineCount++;
            if (mines[row - 1][col + 1] == 'X')
                mineCount++;
            if (mines[row][col - 1] == 'X')
                mineCount++;
            if (mines[row][col + 1] == 'X')
                mineCount++;
        } else if (!(row == 0 || row == mines.length - 1) && col == 0) { // left edge
            if (mines[row - 1][col] == 'X')
                mineCount++;
            if (mines[row + 1][col] == 'X')
                mineCount++;
            if (mines[row - 1][col + 1] == 'X')
                mineCount++;
            if (mines[row][col + 1] == 'X')
                mineCount++;
            if (mines[row + 1][col + 1] == 'X')
                mineCount++;
        } else if (!(row == 0 || row == mines.length - 1) && col == mines[0].length - 1) { // right edge
            if (mines[row - 1][col - 1] == 'X')
                mineCount++;
            if (mines[row][col - 1] == 'X')
                mineCount++;
            if (mines[row + 1][col - 1] == 'X')
                mineCount++;
            if (mines[row - 1][col] == 'X')
                mineCount++;
            if (mines[row + 1][col] == 'X')
                mineCount++;
        }
        // middle
        else {
            if (mines[row - 1][col - 1] == 'X')
                mineCount++;
            if (mines[row - 1][col] == 'X')
                mineCount++;
            if (mines[row - 1][col + 1] == 'X')
                mineCount++;
            if (mines[row][col - 1] == 'X')
                mineCount++;
            if (mines[row][col + 1] == 'X')
                mineCount++;
            if (mines[row + 1][col - 1] == 'X')
                mineCount++;
            if (mines[row + 1][col] == 'X')
                mineCount++;
            if (mines[row + 1][col + 1] == 'X')
                mineCount++;
        }
        return mineCount;
    }
    /** display the number of mines on a given square */
    private void revealSquare(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board.length) {
            return;
        }
        if (board[row][col] != '/') {
            return;
        }
        // assign the number of adjacent mines to the square
        board[row][col] = Character.forDigit(getMineCount(row, col), 10);
        // if count is 0, clear an area of 0s
        if (board[row][col] == '0') {
            revealSquare(row + 1, col + 1);
            revealSquare(row + 1, col);
            revealSquare(row + 1, col - 1);
            revealSquare(row, col + 1);
            revealSquare(row, col - 1);
            revealSquare(row - 1, col + 1);
            revealSquare(row - 1, col - 1);
            revealSquare(row - 1, col);
        }
    }
    private void flagSquare(int row, int col) {
    	if (board[row][col] == '!') {
    		board[row][col] = '/';
    	} else {
    		board[row][col] = '!';
    	}
    }
    private void checkWin() {
        int emptySquareCount = 0;
        for (char[] a : board) {
            for (char b : a) {
                if (b == '/' || b == '!')
                    emptySquareCount++;
            }
        }
        if (emptySquareCount == (board.length * board.length) / 6) {
            status = 1;
        }
    }
    /** check if the selected square is mine, empty, flagged, or explored */
    private void evaluateSquare(int row, int col) {
        if (mines[row][col] == 'X') {
            board[row][col] = 'X';
            status = 2;
        } else if (board[row][col] == '/') {
            revealSquare(row, col);
        }
    }
    public void update(Graphics window) {
    	this.paint(window);
    }
	/** translate the text-based board to a visual board */
    public void paint(Graphics window) {
		window.setColor(Color.BLACK);
		window.setFont(new Font("Comic Sans", Font.PLAIN, 18));
		for (int row=0;row<board.length;row++) {
			for (int col=0;col<board[0].length;col++) {
                int xPos = 56 * col + 120;
                int yPos = 56 * row + 20;
				if (board[row][col] == '/') {
                    window.setColor(Color.BLACK);
					window.fillRect( xPos,yPos, 56, 56);
				} else if (board[row][col] == '!') {
					window.setColor(Color.RED);
                    BufferedImage flag = new BufferedImage(56, 35, BufferedImage.TYPE_INT_RGB);
                    try {flag = ImageIO.read(new File("Projects/Games/Minesweeper/flag.png"));}
                    catch (IOException e) {}
                    window.drawImage(flag, xPos, yPos,56,56, null);
				}  else if (board[row][col] == 'X') {
                    BufferedImage flag = new BufferedImage(56, 56, BufferedImage.TYPE_INT_RGB);
                    try {flag = ImageIO.read(new File("Projects/Games/Minesweeper/mine.jpeg"));}
                    catch (IOException e) {}
                    window.drawImage(flag, xPos, yPos, 56,56,null);
                    window.setColor(Color.YELLOW);
                    window.drawString("boom", xPos+5, yPos+33);
                } else {
                    window.setColor(Color.BLACK);
					window.drawString("" + board[row][col], xPos+21, yPos+33);
				}
			}
		}
        window.setColor(Color.DARK_GRAY);
        for (int i=1;i<board.length;i++) {
            window.drawLine(120, 20+i*56, 120+56*board.length, 20+i*56);
        }
        for (int i=1;i<board.length;i++) {
            window.drawLine(120+i*56, 20, 120+i*56, 20+56*board.length);
        }
		if (status == 1) {
			window.setColor(Color.GREEN);
			window.setFont(new Font("Arial", Font.BOLD,72));
			window.drawString("YOU WIN", 200, 300);
		} else if (status == 2) {
			window.setColor(Color.RED);
			window.setFont(new Font("Arial", Font.BOLD,72));
			window.drawString("YOU LOSE", 200, 300);
		}
		checkWin();
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(10);
				repaint();
			}
		} catch (Exception e) {
			System.out.println("OOPS SOMETHING BAD HAPPENED");
			System.out.println(e.getMessage());
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (status >= 1) return; // if already won or lost
		int row = (e.getY()-20)/56;
		int col = (e.getX()-120)/56;
		if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
			return;
		}
		if (e.getButton() == MouseEvent.BUTTON1) {
            if (status == -1) 
                initialReroll(row,col);
            else 
                evaluateSquare(row,col);
		} 
        else if (e.getButton() == MouseEvent.BUTTON3) {
			flagSquare(row,col);
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
