package Games.Mastermind;

public class Gameboard {
    private int[][] board = new int[10][4];
    public Gameboard() {
        new Thread(this).start();
    }
}
