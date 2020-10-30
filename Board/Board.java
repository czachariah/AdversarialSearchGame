package Board;

import java.util.Random;

/**
 * This is the Board class. This will be the interface used in order to make
 * moves and play the game.
 */
public class Board {

    // Global variables
    public int size;                       // this is the dimension of the board (size x size) ; will be dimension of 3
    public Piece[][] board;                // this is the main board that the game will take place on
    public Random rand = new Random();     // randomizer

    /**
     * This is the constructor of the Board class.
     * @param size is the dimension size of the board (must be a multiple of 3)
     */
    public Board(int size) {
        this.size = size;
        this.board = new Piece[size][size];
        for (int i = 0 ; i < size ; ++i) {
            for (int j = 0; j < size ; ++j) {
                this.board[i][j] = new Piece(i, j, 4, 0); // initialize everything as an empty space and as color red
            }
        }
        initBoard();
    } // ends the Board() constructor
    
    /**
     * This method will be used in order to initilize the Board with all the pieces and elements for the game.
     */
    public void initBoard() {
        int curType = 1;                    // use this to determine which piece is to be placed on the board
        for(int i = 0 ; i < size ; ++i) {   // initialize the top row (for the AI)
            board[0][i].type = curType;
            board[0][i].color = 0;          // red for AI
            ++curType;
            if (curType >= 4) {
                curType = 1;
            }
        }

        curType = 1;
        for(int i = 0 ; i < size ; ++i) {   // initialize the bottom row (for the Human-Player)
            board[size-1][i].type = curType;
            board[size-1][i].color = 1;     // white for Human
            ++curType;
            if (curType >= 4) {
                curType = 1;
            }
        }

        int numPitsPerRow = (size/3) -1;
        int numPlaced = 0;
        if (numPitsPerRow > 0) {
            for(int i = 1 ; i < size-1 ; ++i) { // set the pits in each row except the top and bottom
                // place the pit(s) randomly in the row
                while(numPlaced < numPitsPerRow) {
                    int col = rand.nextInt(size);       // random place between [0,size-1]
                    if (board[i][col].type != 0) {
                        board[i][col].type = 0;
                        ++numPlaced;
                    }
                }
                numPlaced = 0;
            } 
        }
        
    } // ends the initBoard() method

    /**
     * This method will be used in order to print the current state of the board.
     */
    public void printBoard() {
        for(int i = 0 ; i < size ; ++i) {
            for (int j = 0 ; j< size ; ++j) {
                if (board[i][j].type == 0) {        // pit
                    System.out.print("  PT  ");
                } else if (board[i][j].type == 1) { // wumpus
                    if (board[i][j].color == 0) {   // red
                        System.out.print("  rW  ");
                    } else {
                        System.out.print("  wW  ");
                    }
                } else if (board[i][j].type == 2) { // hero
                    if (board[i][j].color == 0) {   // red
                        System.out.print("  rH  ");
                    } else {
                        System.out.print("  wH  ");
                    }
                } else if (board[i][j].type == 3) { // mage
                    if (board[i][j].color == 0) {   // red
                        System.out.print("  rM  ");
                    } else {
                        System.out.print("  wM  ");
                    }
                } else {                            // empty
                    System.out.print("  XX  ");
                }
            }
            System.out.println();
        }
    } // ends the printBoard() method

} // this ends the Board class
