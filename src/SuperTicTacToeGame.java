import java.awt.*;
import java.util.ArrayList;

public class SuperTicTacToeGame {

    private Player player;
    private Cell[][] board;
    private GameStatus status;

    private ArrayList<Point> moves = new ArrayList<Point>();
    private int currentMove = 0;

    //TODO For AI Select Later
    private int AIxSpot = 0;
    private int AIySpot = 0;

    //Default Number of Rows, Columns, and Connection Lengths
    private int numRows = 3;
    private int numCols = 3;
    private int numConnections = 3;


    //Sets size of board, initial player, and connection length needed to win
    public void setSize(int size){
        setRows(size);
        setCols(size);
    }

    public void setRows(int rows){
        numRows = rows;
    }

    public void setCols(int col){
        numCols = col;
    }

    public void setConnections(int connections){
        numConnections = connections;
    }

    public void setPlayer(Player playerIn){
        if (playerIn == Player.OPlayer)
            setNextPlayer();
    }

    //Switches X/O's turn
    public void setNextPlayer(){
        player = player.next();
    }

    //Gets size of board, current player, and connection length needed to win
    public int getRows() {
        return numRows;
    }

    public int getCols() {
        return numCols;
    }

    public int getNumConnections(){
        return numConnections;
    }

    public GameStatus getStatus(){
        return status;
    }

    public Cell[][] getBoard() {
        return board;
    }

    //Returns Current Player X's or O's
    public Player getPlayer(){
        return player;
    }



    //Creates board of row and col size
    public SuperTicTacToeGame(){
        status = GameStatus.IN_PROGRESS;
        player = Player.XPlayer;
        int x = getRows();
        int y = getCols();
        board = new Cell[x][y];

        for (int row = 0; row < x; row++)
            for (int col = 0; col < y; col++)
                board[row][col] = Cell.EMPTY;
    }

    //Resets the game
    public void reset(){
        status = GameStatus.IN_PROGRESS;
        int x = getRows();
        int y = getCols();
        board = new Cell[x][y];

        for (int row = 0; row < x; row++)
            for (int col = 0; col < y; col++)
                board[row][col] = Cell.EMPTY;
    }

    //Method to select cell for X/O placement
    public void select (int row, int col){
        if (player == Player.XPlayer && isValidMove(row, col)) {
            board[row][col] = Cell.X;
            saveMove(row, col);
            setNextPlayer();
        }
        else if (player == Player.OPlayer && isValidMove(row, col)) {
            board[row][col] = Cell.O;
            saveMove(row, col);
            setNextPlayer();
        }
    }

    //Verifies if space is empty
    public boolean isValidMove(int row, int col){
        return (board[row][col] == Cell.EMPTY);
    }

    //Saves last action
    private void saveMove(int row, int col){
        moves.add(new Point(row, col));
        currentMove += 1;
    }

    //Undo last action
    void undoAction(){
        //creates temp point and extracts x-y pos for clearing on the board
        Point temp = new Point (0,0);
        temp = moves.get(currentMove - 1);
        int x = (int) temp.getX();
        int y = (int) temp.getY();
        board[x][y] = Cell.EMPTY;
        //switches player and removes last index in moves
        setNextPlayer();
        moves.remove(currentMove -1);
        currentMove -= 1;
    }


    //TODO RECHECK "ACCEPTS NON ACCURATE WINS"
    //Checks if X or O has won
    public boolean isComplete(){
        int x = getCols();
        int y = getRows();
        int z = x * y;
        int spaceFilled = 0;

        int xSequence = 0;
        int oSequence = 0;


        //Checks for a winner
        for (int r = 0; r < x; r++)
            for (int c = 0; c < y; c++) {
                //Vertical / Horizontal Check
                if (r < (y - getNumConnections() + 1)) {
                    for (int count = 0; count < getNumConnections(); count++) {
                        if (board[r + xSequence][c] == Cell.X) {
                            xSequence += 1;
                            if (xSequence == getNumConnections()) {
                                status = GameStatus.X_WON;
                                System.out.println("Win Type c.1");
                                return true;
                            }
                        }
                        else
                            xSequence = 0;

                        if (board[r + oSequence][c] == Cell.O) {
                            oSequence += 1;
                            if (oSequence == getNumConnections()) {
                                status = GameStatus.O_WON;
                                System.out.println("Win Type c.2");
                                return true;
                            }
                        }
                        else
                            oSequence = 0;
                    }
                }
                if (c < (x - getNumConnections() + 1)) {
                    for (int count = 0; count < getNumConnections(); count++) {
                        if (board[r][c + xSequence] == Cell.X) {
                            xSequence += 1;
                            if (xSequence == getNumConnections()) {
                                status = GameStatus.X_WON;
                                System.out.println("Win Type r.1");
                                return true;
                            }
                        } else
                            xSequence = 0;

                        if (board[r][c + oSequence] == Cell.O) {
                            oSequence += 1;
                            if (oSequence == getNumConnections()) {
                                status = GameStatus.O_WON;
                                System.out.println("Win Type r.2");
                                return true;
                            }
                        } else
                            oSequence = 0;
                    }
                }

                //Diagonal Check
                if (r < (y - getNumConnections() + 1) && c < (x - getNumConnections() + 1)) {
                    for (int count = 0; count < getNumConnections(); count++) {
                        if (board[r + xSequence][c + xSequence] == Cell.X) {
                            xSequence += 1;
                            if (xSequence == getNumConnections()) {
                                status = GameStatus.X_WON;
                                System.out.println("Win Type d.1");
                                return true;
                            }
                        }
                        else
                            xSequence = 0;

                        }
                    for (int count = 0; count < getNumConnections(); count++) {
                        if (board[r + oSequence][c + oSequence] == Cell.O) {
                            oSequence += 1;
                            if (oSequence == getNumConnections()) {
                                status = GameStatus.O_WON;
                                System.out.println("Win Type d.1");
                                return true;
                            }
                        }
                        else
                            oSequence = 0;
                    }

                    //TODO DOUBLE CHECK "IT ACCEPTS WRONG WINS SOMETIMES"
                    //Example of bad win
                    //  o   o   x
                    //  -   o   x
                    //  -   x   -
                    for (int count = 0; count < getNumConnections(); count++) {
                        if (board[numRows - 1 - xSequence][numCols - 1 - xSequence] == Cell.X) {
                            xSequence += 1;
                            if (xSequence == getNumConnections()) {
                                status = GameStatus.X_WON;
                                System.out.println("Win Type d.2");
                                return true;
                            }
                        }
                        else
                            xSequence = 0;
                    }
                    for (int count = 0; count < getNumConnections(); count++) {
                        if (board[numRows - 1 - oSequence][numCols - 1 - oSequence] == Cell.O) {
                            oSequence += 1;
                            if (oSequence == getNumConnections()) {
                                status = GameStatus.O_WON;
                                System.out.println("Win Type d.2");
                                return true;
                            }
                        }
                        else
                            oSequence = 0;
                    }

                }

            }

        //CATS Check
        for (int r = 0; r < x; r++)
            for (int c = 0; c < y; c++)
            {
                if (board[r][c] == Cell.X || board[r][c] == Cell.O)
                    spaceFilled += 1;
                if (spaceFilled == z) {
                    status = GameStatus.CATS;
                    return true;
                }
            }

        return false;
    }







    //TODO Dont worry about for now
//    void AIChoice(){
//        if (canWin())
//            System.out.println("O can Win");
//        else if (canLose())
//            System.out.println("O can Lose");
//        else if (canPlaceNearSelf())
//            System.out.println("O can place near O");
//        else
//            System.out.println("O will place Random");
//
//    }
//
//    private boolean canWin(){
//
//
//
//
//        return false;
//    }
//
//    private boolean canLose(){
//
//
//        return false;
//    }
//
//    private boolean canPlaceNearSelf(){
//
//        return false;
//    }

}
