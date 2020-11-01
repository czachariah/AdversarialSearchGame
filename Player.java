import Board.*;
import java.util.Scanner;

/**
 * This is the player class used in order to play the Wumpus Game.
 */
public class Player {
    // Global variables
    public static int boardSize = 0;                    // size of the board
    public static Board board;                          // main board used in order to play the game
    public static Scanner sc = new Scanner(System.in);  // scanner for input

    public static void main(String[] args) {
        System.out.print("Input Board size: ");
        boardSize = sc.nextInt();
        System.out.println();
        board = new Board(boardSize);
        board.printBoard();

        int[] move = new int[4];
        int color = 0;              // 0 = Player 1 (will be AI in future), 1 = Player 2
        while (!board.isDone()) {
            if (color == 0) {
                System.out.println("Player 1's move: Red(r)\nFormat:[oldRow] [oldCol] [newRow] [newCol])");
                for (int i = 0 ; i  < move.length ; ++i) {
                    move[i] = sc.nextInt();
                }
                System.out.println();
                if (board.nextMove(move, color)) {
                    board.printBoard();
                    color = 1;
                } else {
                    System.out.println("PLAYER 1 : BAD MOVE , TRY AGAIN.");
                    board.printBoard();
                    color = 0;
                }
            } else {
                System.out.println("Player 2's move: White(w)\nFormat:[oldRow] [oldCol] [newRow] [newCol])");
                for (int i = 0 ; i  < move.length ; ++i) {
                    move[i] = sc.nextInt();
                }
                System.out.println();
                if (board.nextMove(move, color)) {
                    board.printBoard();
                    color = 0;
                } else {
                    System.out.println("PLAYER 2 : BAD MOVE , TRY AGAIN.");
                    board.printBoard();
                    color = 1;
                }
            }    
        } // ends the while loop
        System.out.println();
        // find the winner
        if (board.aiHasWon()) {
            System.out.println("PLAYER 1 WINS!");
        } else if (board.manHasWon()) {
            System.out.println("PLAYER 2 WINS!");
        } else { // draw
            System.out.println("DRAW!");
        }
    } // ends main() method

} // ends the Player class
