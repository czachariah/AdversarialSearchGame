package Board;

import java.util.Random;

/**
 * This is the Board class. This will be the interface used in order to make
 * moves and play the game.
 */
public class Board {

    // Global variables
    public int size;                        // this is the dimension of the board (size x size) ; will be dimension of 3
    public Piece[][] board;                 // this is the main board that the game will take place on
    public Random rand = new Random();      // randomizer

    /**
     * This is the constructor of the Board class.
     * @param size is the dimension size of the board (must be a multiple of 3)
     */
    public Board(int size) {
        this.size = size;
        this.board = new Piece[size][size];
        for (int i = 0 ; i < size ; ++i) {
            for (int j = 0; j < size ; ++j) {
                this.board[i][j] = new Piece(i, j, 4, 2); // initialize everything as an empty space and as color green
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
            this.board[0][i].type = curType;
            this.board[0][i].color = 0;          // red for AI
            ++curType;
            if (curType >= 4) {
                curType = 1;
            }
        }

        curType = 1;
        for(int i = 0 ; i < size ; ++i) {   // initialize the bottom row (for the Human-Player)
            this.board[size-1][i].type = curType;
            this.board[size-1][i].color = 1;     // white for Human
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
                    if (this.board[i][col].type != 0) {
                        this.board[i][col].type = 0;
                        this.board[i][col].color = 3;
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
            for (int j = 0 ; j < size ; ++j) {
                if (this.board[i][j].type == 0) {        // pit
                    System.out.print("PT\t");
                } else if (this.board[i][j].type == 1) { // wumpus
                    if (this.board[i][j].color == 0) {   // red
                        System.out.print("rW\t");
                    } else {
                        System.out.print("wW\t");
                    }
                } else if (this.board[i][j].type == 2) { // hero
                    if (this.board[i][j].color == 0) {   // red
                        System.out.print("rH\t");
                    } else {
                        System.out.print("wH\t");
                    }
                } else if (this.board[i][j].type == 3) { // mage
                    if (this.board[i][j].color == 0) {   // red
                        System.out.print("rM\t");
                    } else {
                        System.out.print("wM\t");
                    }
                } else {                            // empty
                    System.out.print("XX\t");
                }
                // print row num at the end of the columns
                if (j == size-1) {
                    System.out.print(i);
                }
            }
            // print columns at the last row
            System.out.println();
            if (i == size-1) {
                for (int k = 0 ; k < size ; ++k) {
                    System.out.print(k + "\t");
                }
                System.out.println();
            }
        }
    } // ends the printBoard() method

    /**
     * This method will check if the game is done.
     * @return true if the game is conculded, false otherwise 
     */
    public boolean isDone() {
        if (aiHasWon() || manHasWon() || draw()) { return true; }
        return false;
    } // ends the isDone() method

    /**
     * This method will check if the AI has won.
     * @return true if the AI has won the game, false otherwise
     */
    public boolean aiHasWon() {
        return ((getNumAIPieces() > 0) && (getNumManPieces() <= 0)) ? true : false;
    } // ends the aiHasWon() method

    /**
     * This method will check if the human has won.
     * @return true if the human has won the game, false otherwise
     */
    public boolean manHasWon() {
        return ((getNumManPieces() > 0) && (getNumAIPieces() <= 0)) ? true : false;
    } // ends the manHasWon() method

    /**
     * This method will check if the game is a draw.
     * @return true if the game is a draw, false otherwise
     */
    public boolean draw() {
        return ((getNumManPieces() == 0) && (getNumAIPieces() == 0)) ? true : false;
    } // ends the draw() method

    
    public boolean nextMove(int[] move , int color) {
        if ((isSquareOnBoard(move[0], move[1]) == false) 
        || (isSquareOnBoard(move[2], move[3]) == false) 
        || (isNeighbor(move[0], move[1], move[2], move[3]) == false)) {
            return false;
        }
        if ((board[move[0]][move[1]].type == 0) || (board[move[0]][move[1]].type == 4)) { // cannot be a pit or empty square
            return false;
        }
        if (board[move[0]][move[1]].color != color) { // cannot move opponent's piece
            return false;
        }
        if (board[move[2]][move[3]].color == color) { // cannot move into square with one's own piece in it
            return false;
        }

        // find the outcome of the move and update the board
        if (moveResult(move) == -1) {           // old piece gets destroyed, new piece stays the same
            
            board[move[0]][move[1]].type = 4;   // empty square   
            board[move[0]][move[1]].color = 2;  // green

        } else if (moveResult(move) == 0) {     // old and new piece are the same type, both get destoryed
            
            board[move[0]][move[1]].type = 4;   // empty square   
            board[move[0]][move[1]].color = 2;  // green
            board[move[2]][move[3]].type = 4;   // empty square   
            board[move[2]][move[3]].color = 2;  // green

        } else {                                // old piece wins battle, new piece gets destoryed

            board[move[2]][move[3]].type = board[move[0]][move[1]].type; 
            board[move[2]][move[3]].color = board[move[0]][move[1]].color;
            board[move[0]][move[1]].type = 4;   
            board[move[0]][move[1]].color = 2;

        }

        return true;

    } //  ends the nextMove() method


    /**
     * This method will figure out the outcome of the move to make
     * @param move the old and new placement of the piece
     * @return -1 if old piece loses , 1 if old piece wins , 0 if draw and both lose
     */
    public int moveResult(int[] move) {
        if (board[move[0]][move[1]].type == 1) {                // Wumpus
            /*
            Wumpus vs Wumpus : Draw   : 0
            Wumpus vs Hero   : Hero   : -1
            Wumpus vs Mage   : Wumpus : 1
            Wumpus vs Pit    : Pit    : -1
            Wumpus vs Empty  : Wumpus : 1
            */
            if (board[move[2]][move[3]].type == 1) {
                return 0;
            } else if (board[move[2]][move[3]].type == 2) {
                return -1;
            } else if (board[move[2]][move[3]].type == 3) {
                return 1;
            } else if (board[move[2]][move[3]].type == 0) {
                return -1;
            } else {
                return 1;
            }
        } else if (board[move[0]][move[1]].type == 2) {         // Hero
            /*
            Hero vs Hero   : Draw   : 0
            Hero vs Mage   : Mage   : -1
            Hero vs Wumpus : Hero   : 1
            Hero vs Pit    : Pit    : -1
            Hero vs Empty  : Hero   : 1
            */
            if (board[move[2]][move[3]].type == 2) {
                return 0;
            } else if (board[move[2]][move[3]].type == 3) {
                return -1;
            } else if (board[move[2]][move[3]].type == 1) {
                return 1;
            } else if (board[move[2]][move[3]].type == 0) {
                return -1;
            } else {
                return 1;
            }
        } else {                                                // Mage
            /*
            Mage vs Mage   : Draw   : 0
            Mage vs Wumpus : Wumpus : -1
            Mage vs Hero   : Mage   : 1
            Mage vs Pit    : Pit    : -1
            Mage vs Empty  : Mage   : 1
            */
            if (board[move[2]][move[3]].type == 3) {
                return 0;
            } else if (board[move[2]][move[3]].type == 1) {
                return -1;
            } else if (board[move[2]][move[3]].type == 2) {
                return 1;
            } else if (board[move[2]][move[3]].type == 0) {
                return -1;
            } else {
                return 1;
            }
        }
    } // ends the moveResult() method

    /**
     * This method will check if a given coordinate pair is on the board or not.
     * @param x the row
     * @param y the column
     * @return true if the square is valid and false otherwise
     */
    public boolean isSquareOnBoard(int x , int y) {
        if ((x >= 0 && x < size) && (y >= 0 && y < size)) {
            return true;
        } else {
            return false;
        }
    } // ends the isSquareOnBoard() method


    /**
     * This method will check is the new (x,y) coordinate is a neighbor (one move away) of the old (x,y) coordinate
     * @param oldX old x
     * @param oldY old y 
     * @param newX new x
     * @param newY new y
     * @return true if the new (x,y) is a neighbor of old (x,y)
     */
    public boolean isNeighbor(int oldX, int oldY , int newX , int newY) {
        for (int i = -1 ; i <= 1 ; ++i) {
            for (int j = -1 ; j <= 1 ; ++j) {
                int curX = oldX + i;
                int curY = oldY + j;
                if (curX == newX && curY == newY) {
                    return true;
                }
            }
        }
        return false;
    } // ends the isNeighbor() method

    /**
     * This method is used in order to get the total number of AI's pieces on the board currently.
     * @return number of AI's pieces on the board
     */
    public int getNumAIPieces() {
        int num = 0;
        for (int i = 0 ; i < size ; ++i) {
            for (int j = 0 ; j < size ; ++j) {
                if (board[i][j].color == 0) {
                    ++num;
                }
            }
        }
        return num;
    } // ends the getNumAIPieces() method

    /**
     * This method is used in order to get thr total number of Human's pieces on the board currently.
     * @return number of Human's pieces on the board
     */
    public int getNumManPieces() {
        int num = 0;
        for (int i = 0 ; i < size ; ++i) {
            for (int j = 0 ; j < size ; ++j) {
                if (board[i][j].color == 1) {
                    ++num;
                }
            }
        }
        return num;
    } // ends the getNumManPieces() method

} // this ends the Board class
