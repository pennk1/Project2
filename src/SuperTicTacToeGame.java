import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SuperTicTacToeGame {

    private Player player;
    private Cell[][] board;
    private GameStatus status;

    private final ArrayList<Point> moves = new ArrayList<>();
    private int currentMove = 0;

    private int AIxSpot = 0;
    private int AIySpot = 0;
    private boolean AIPlayer = false;

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
        player = playerIn;
    }

    public void setAIPlayer(int s){
        if (s == 0)
            AIPlayer = true;
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

    public GameStatus getStatus(){
        return status;
    }

    public Cell[][] getBoard() {
        return board;
    }


    //Creates board of row and col size
    public SuperTicTacToeGame(){
        status = GameStatus.IN_PROGRESS;
        player = Player.XPlayer;
        int x = numRows;
        int y = numCols;
        board = new Cell[x][y];

        for (int row = 0; row < x; row++)
            for (int col = 0; col < y; col++)
                board[row][col] = Cell.EMPTY;
    }

    //Resets the game
    public void reset(){
        status = GameStatus.IN_PROGRESS;
        int x = numRows;
        int y = numCols;
        board = new Cell[x][y];

        for (int row = 0; row < x; row++)
            for (int col = 0; col < y; col++)
                board[row][col] = Cell.EMPTY;

        if (AIPlayer && player == Player.OPlayer)
            AIChoice();
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
        return (board[row][col] == Cell.EMPTY && status == GameStatus.IN_PROGRESS);
    }

    //Saves last action
    private void saveMove(int row, int col){
        moves.add(new Point(row, col));
        currentMove += 1;
    }

    //Undo last action
    void undoAction(){
        try {
            //creates temp point and extracts x-y pos for clearing on the board
            Point temp;
            temp = moves.get(currentMove - 1);
            int x = (int) temp.getX();
            int y = (int) temp.getY();
            board[x][y] = Cell.EMPTY;
            //switches player and removes last index in moves
            setNextPlayer();
            moves.remove(currentMove - 1);
            currentMove -= 1;

            if (AIPlayer) {
                temp = moves.get(currentMove - 1);
                x = (int) temp.getX();
                y = (int) temp.getY();
                board[x][y] = Cell.EMPTY;
                //switches player and removes last index in moves
                setNextPlayer();
                moves.remove(currentMove - 1);
                currentMove -= 1;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "No more Undo's Available");
        }
    }

    //TODO FIX 3+ x 3+
    //Checks if X or O has won
    public boolean isComplete(){
        int x = numCols;
        int y = numRows;
        int n = numConnections;
        int count;

        //Horizontal Check
        for (int r = 0; r < x; r++) {
            count = 0;
            for (int c = 0; c < y; c++) {
                if (board[r][c] == Cell.X)
                    count += 1;
                else
                    count += 0;

                if (count == n) {
                    status = GameStatus.X_WON;
                    System.out.println("horizontal");
                    return true;
                }
            }
        }

        for (int r = 0; r < x; r++) {
            count = 0;
            for (int c = 0; c < y; c++) {
                if (board[r][c] == Cell.O)
                    count += 1;
                else
                    count += 0;

                if (count == n) {
                    status = GameStatus.O_WON;
                    return true;
                }
            }
        }

        //Vertical Check
        for (int c = 0; c < y; c++) {
            count = 0;
            for (int r = 0; r < x; r++) {
                if (board[r][c] == Cell.X)
                    count += 1;
                else {
                    count += 0;
                }
                if (count == n) {
                    status = GameStatus.X_WON;
                    System.out.println("vertical");
                    return true;
                }
            }
        }

        for (int c = 0; c < y; c++) {
            count = 0;
            for (int r = 0; r < x; r++) {
                if (board[r][c] == Cell.O)
                    count += 1;
                else
                    count += 0;

                if (count == n) {
                    status = GameStatus.O_WON;
                    return true;
                }
            }
        }

        //Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            count = 0;
            for (int c = 0; c < (y - n + 1); c++)
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c + i] == Cell.X)
                        count += 1;
                    else
                        count = 0;

                    if (count == n) {
                        status = GameStatus.X_WON;
                        System.out.println("diagonal");
                        return true;
                    }
                }
        }

        for (int r = 0; r < (x - n + 1); r++) {
            count = 0;
            for (int c = 0; c < (y - n + 1); c++)
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c + i] == Cell.O)
                        count += 1;
                    else
                        count = 0;

                    if (count == n) {
                        status = GameStatus.O_WON;
                        return true;
                    }
                }
        }

        //Reverse Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            count = 0;
            for (int c = n - 1; c < y; c++)
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c - i] == Cell.X)
                        count += 1;
                    else
                        count = 0;

                    if (count == n) {
                        status = GameStatus.X_WON;
                        System.out.println("reverse diagonal");
                        return true;
                    }
                }
        }

        for (int r = 0; r < (x - n + 1); r++) {
            count = 0;
            for (int c = n - 1; c < y; c++)
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c - i] == Cell.O)
                        count += 1;
                    else
                        count = 0;

                    if (count == n) {
                        status = GameStatus.O_WON;
                        return true;
                    }
                }
        }

        //CATS Check
        count = 0;
        for (int r = 0; r < x; r++)
            for (int c = 0; c < y; c++) {
                if (board[r][c] != Cell.EMPTY)
                    count += 1;
                if (count == (x * y)) {
                    status = GameStatus.CATS;
                    return true;
                }
            }

        return false;
    }

    void AIChoice(){
        if (AIPlayer && player == Player.OPlayer && status == GameStatus.IN_PROGRESS) {
            if (canWin()) {
                select(AIxSpot, AIySpot);
                System.out.println("Not Random");
            }
            else if (canLose()) {
                select(AIxSpot, AIySpot);
                System.out.println("Not Random");
            }
            else if (canPlaceNearSelf()) {
                System.out.println("Not Random");
                select(AIxSpot, AIySpot);
            }
            else {
                int randomX = ThreadLocalRandom.current().nextInt(0, numRows);
                int randomY = ThreadLocalRandom.current().nextInt(0, numCols);
                if (isValidMove(randomX, randomY)) {
                    select(randomX, randomY);
                    System.out.println("Random");
                }
                else
                    AIChoice();
            }
        }
    }

    private boolean canWin(){
        int x = numCols;
        int y = numRows;
        int n = numConnections;
        int b = 1;  //buffer space
        int count;

        //Horizontal Check
        for (int r = 0; r < x; r++) {
            count = 0;
            for (int c = 0; c < y; c++) {
                if (board[r][c] == Cell.O)
                    count += 1;
                else if (board[r][c] == Cell.EMPTY && b == 1) {
                    count += 1;
                    AIxSpot = r;
                    AIySpot = c;
                    b = 0;
                }
                else {
                    count += 0;
                    b = 1;
                }
                if (count == n)
                    return true;
            }
        }

        //Vertical Check
        for (int c = 0; c < y; c++) {
            count = 0;
            for (int r = 0; r < x; r++) {
                if (board[r][c] == Cell.O)
                    count += 1;
                else if (board[r][c] == Cell.EMPTY && b == 1) {
                    count += 1;
                    AIxSpot = r;
                    AIySpot = c;
                    b = 0;
                }
                else {
                    count += 0;
                    b = 1;
                }
                if (count == n)
                    return true;
            }
        }

        //Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            count = 0;
            for (int c = 0; c < (y - n + 1); c++)
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c + i] == Cell.O)
                        count += 1;
                    else if (board[r + i][c + i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c + i;
                        b = 0;
                    }
                    else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
        }

        //Reverse Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            count = 0;
            for (int c = n - 1; c < y; c++)
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c - i] == Cell.O)
                        count += 1;
                    else if (board[r + i][c - i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c - i;
                        b = 0;
                    }
                    else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
        }

        return false;
    }

    private boolean canLose(){
        int x = numCols;
        int y = numRows;
        int n = numConnections;
        int b = 1;  //buffer space
        int count;

        //Horizontal Check
        for (int r = 0; r < x; r++) {
            count = 0;
            for (int c = 0; c < y; c++) {
                if (board[r][c] == Cell.X)
                    count += 1;
                else if (board[r][c] == Cell.EMPTY && b == 1) {
                    count += 1;
                    AIxSpot = r;
                    AIySpot = c;
                    b = 0;
                }
                else {
                    count += 0;
                    b = 1;
                }
                if (count == n)
                    return true;
            }
        }

        //Vertical Check
        for (int c = 0; c < y; c++) {
            count = 0;
            for (int r = 0; r < x; r++) {
                if (board[r][c] == Cell.X)
                    count += 1;
                else if (board[r][c] == Cell.EMPTY && b == 1) {
                    count += 1;
                    AIxSpot = r;
                    AIySpot = c;
                    b = 0;
                }
                else {
                    count += 0;
                    b = 1;
                }
                if (count == n)
                    return true;
            }
        }

        //Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            count = 0;
            for (int c = 0; c < (y - n + 1); c++)
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c + i] == Cell.X)
                        count += 1;
                    else if (board[r + i][c + i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c + i;
                        b = 0;
                    }
                    else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
        }

        //Reverse Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            count = 0;
            for (int c = n - 1; c < y; c++)
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c - i] == Cell.X)
                        count += 1;
                    else if (board[r + i][c - i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c - i;
                        b = 0;
                    }
                    else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
        }

        return false;
    }

    private boolean canPlaceNearSelf(){
        int x = numRows;
        int y = numCols;
        for (int r = 0; r < x - 1; r++)
            for (int c = 0; c < y - 1; c++)
                if (board[r][c] == Cell.O) {
                    if (board[r + 1][c] == Cell.EMPTY) {
                        AIxSpot = r + 1;
                        AIySpot = c;
                        return true;
                    }
                    if (board[r][c + 1] == Cell.EMPTY) {
                        AIxSpot = r;
                        AIySpot = c + 1;
                        return true;
                    }
                    if (board[r + 1][c + 1] == Cell.EMPTY) {
                        AIxSpot = r + 1;
                        AIySpot = c + 1;
                        return true;
                    }
                }
        for (int r = 1; r < x; r++)
            for (int c = 1; c < y; c++)
                if (board[r][c] == Cell.O) {
                    if (board[r - 1][c] == Cell.EMPTY) {
                        AIxSpot = r - 1;
                        AIySpot = c;
                        return true;
                    }
                    if (board[r][c - 1] == Cell.EMPTY) {
                        AIxSpot = r;
                        AIySpot = c - 1;
                        return true;
                    }
                    if (board[r - 1][c - 1] == Cell.EMPTY) {
                        AIxSpot = r - 1;
                        AIySpot = c - 1;
                        return true;
                    }
                }

        return false;
    }

}
