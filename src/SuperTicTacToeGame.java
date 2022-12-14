import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SuperTicTacToeGame {

    private Player player;
    private Player initialPlayer;
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


    //Setters
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

    public void setInitialPlayer(Player playerIn){
        initialPlayer = playerIn;
    }

    public void setAIPlayer(int s){
        if (s == 0)
            AIPlayer = true;
    }

    //Switches X/O's turn
    public void setNextPlayer(){
        player = player.next();
    }

    //Getters
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

    public Player getInitialPlayer(){
        return initialPlayer;
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
        return (board[row][col] == Cell.EMPTY &&
                status == GameStatus.IN_PROGRESS);
    }

    //Saves last action
    private void saveMove(int row, int col){
        moves.add(new Point(row, col));
        currentMove += 1;
    }

    //Undo last action
    void undoAction(){
        try {
            //creates temp point to get last x-y pos
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
                //Repeats undo if AI is playing
                temp = moves.get(currentMove - 1);
                x = (int) temp.getX();
                y = (int) temp.getY();
                board[x][y] = Cell.EMPTY;
                setNextPlayer();
                moves.remove(currentMove - 1);
                currentMove -= 1;
            }
        }
        catch(Exception e){
            String m = "No more Undo's Available";
            JOptionPane.showMessageDialog(null, m);
        }
    }

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
                    count = 0;

                if (count == n) {
                    status = GameStatus.X_WON;
                    System.out.println("H");
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
                    count = 0;

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
                    count = 0;
                }
                if (count == n) {
                    status = GameStatus.X_WON;
                    System.out.println("V");
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
                    count = 0;

                if (count == n) {
                    status = GameStatus.O_WON;
                    return true;
                }
            }
        }

        //Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            for (int c = 0; c < (y - n + 1); c++){
                count = 0;
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c + i] == Cell.X)
                        count += 1;
                    else
                        count = 0;

                    if (count == n) {
                        status = GameStatus.X_WON;
                        System.out.println("D");
                        return true;
                    }
                }
            }
        }

        for (int r = 0; r < (x - n + 1); r++) {
            for (int c = 0; c < (y - n + 1); c++){
                count = 0;
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
        }

        //Reverse Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            for (int c = n - 1; c < y; c++){
                count = 0;
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c - i] == Cell.X)
                        count += 1;
                    else
                        count = 0;

                    if (count == n) {
                        status = GameStatus.X_WON;
                        System.out.println("RD");
                        return true;
                    }
                }
            }
        }

        for (int r = 0; r < (x - n + 1); r++) {
            for (int c = n - 1; c < y; c++) {
                count = 0;
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
                System.out.println("Can Win");
            }
            else if (canLose()) {
                select(AIxSpot, AIySpot);
                System.out.println("Can Lose");
            }
            else if (canPlaceNearSelf()) {
                System.out.println("Can Place Near Self");
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
        int b;  //buffer space
        int count;

        //Horizontal Check
        for (int r = 0; r < x - n - 1; r++) {
            for (int c = 0; c < y - n - 1; c++) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i ++) {
                    if (board[r + i][c] == Cell.O)
                        count += 1;
                    else if (board[r + i][c] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Vertical Check
        for (int c = 0; c < y - n - 1; c++) {
            for (int r = 0; r < x; r++) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i ++) {
                    if (board[r][c + i] == Cell.O)
                        count += 1;
                    else if (board[r][c + i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r;
                        AIySpot = c + i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Horizontal Check
        for (int r = 0; r < x; r++) {
            count = 0;
            b = 1;
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
                    count = 0;
                    b = 1;
                }
                if (count == n)
                    return true;
            }
        }

        //Backward Horizontal Check
        for (int r = x - 1; r >= 0; r--) {
            count = 0;
            b = 1;
            for (int c = y - 1; c >= 0; c--) {
                if (board[r][c] == Cell.O)
                    count += 1;
                else if (board[r][c] == Cell.EMPTY && b == 1) {
                    count += 1;
                    AIxSpot = r;
                    AIySpot = c;
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

        //Vertical Check
        for (int c = 0; c < y; c++) {
            count = 0;
            b = 1;
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
                    count = 0;
                    b = 1;
                }
                if (count == n)
                    return true;
            }
        }

        //Backward Vertical Check
        for (int c = y - 1; c >= 0; c--) {
            count = 0;
            b = 1;
            for (int r = x - 1; r >= 0; r--) {
                if (board[r][c] == Cell.O)
                    count += 1;
                else if (board[r][c] == Cell.EMPTY && b == 1) {
                    count += 1;
                    AIxSpot = r;
                    AIySpot = c;
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

        //Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            for (int c = 0; c < (y - n + 1); c++) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c + i] == Cell.O)
                        count += 1;
                    else if (board[r + i][c + i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c + i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Backward Diagonal Check
        for (int r = x - 1; r >= n - 1; r--) {
            for (int c = y - 1; c >= n - 1; c--) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i++) {
                    if (board[r - i][c - i] == Cell.O)
                        count += 1;
                    else if (board[r - i][c - i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r - i;
                        AIySpot = c - i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Reverse Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            for (int c = n - 1; c < y; c++) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c - i] == Cell.O)
                        count += 1;
                    else if (board[r + i][c - i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c - i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Backward Reverse Diagonal Check
        for (int r = x - 1; r > n - 2; r--) {
            for (int c = y - n - 1; c >= 0; c--) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i++) {
                    if (board[r - i][c + i] == Cell.O)
                        count += 1;
                    else if (board[r - i][c + i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r - i;
                        AIySpot = c + i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        return false;
    }

    private boolean canLose(){
        int x = numCols;
        int y = numRows;
        int n = numConnections;
        int b;  //buffer space
        int count;

        //Horizontal Check
        for (int r = 0; r < x - n - 1; r++) {
            for (int c = 0; c < y - n - 1; c++) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i ++) {
                    if (board[r + i][c] == Cell.X)
                        count += 1;
                    else if (board[r + i][c] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Vertical Check
        for (int c = 0; c < y - n - 1; c++) {
            for (int r = 0; r < x; r++) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i ++) {
                    if (board[r][c + i] == Cell.X)
                        count += 1;
                    else if (board[r][c + i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r;
                        AIySpot = c + i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }


        //Horizontal Check
        for (int r = 0; r < x; r++) {
            count = 0;
            b = 1;
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
                    count = 0;
                    b = 1;
                }
                if (count == n)
                    return true;
            }
        }

        //Backwards Horizontal Check
        for (int r = x - 1; r >= 0; r--) {
            count = 0;
            b = 1;
            for (int c = y - 1; c >= 0; c--) {
                if (board[r][c] == Cell.X)
                    count += 1;
                else if (board[r][c] == Cell.EMPTY && b == 1) {
                    count += 1;
                    AIxSpot = r;
                    AIySpot = c;
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

        //Vertical Check
        for (int c = 0; c < y; c++) {
            count = 0;
            b = 1;
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
                    count = 0;
                    b = 1;
                }
                if (count == n)
                    return true;
            }
        }

        //Backward Vertical Check
        for (int c = y - 1; c >= 0; c--) {
            count = 0;
            b = 1;
            for (int r = x - 1; r >= 0; r--) {
                if (board[r][c] == Cell.X)
                    count += 1;
                else if (board[r][c] == Cell.EMPTY && b == 1) {
                    count += 1;
                    AIxSpot = r;
                    AIySpot = c;
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

        //Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            for (int c = 0; c < (y - n + 1); c++) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c + i] == Cell.X)
                        count += 1;
                    else if (board[r + i][c + i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c + i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Backward Diagonal Check
        for (int r = x - 1; r >= n - 1; r--) {
            for (int c = y - 1; c >= n - 1; c--) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i++) {
                    if (board[r - i][c - i] == Cell.X)
                        count += 1;
                    else if (board[r - i][c - i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r - i;
                        AIySpot = c - i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Reverse Diagonal Check
        for (int r = 0; r < (x - n + 1); r++) {
            for (int c = n - 1; c < y; c++) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i++) {
                    if (board[r + i][c - i] == Cell.X)
                        count += 1;
                    else if (board[r + i][c - i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r + i;
                        AIySpot = c - i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
            }
        }

        //Backward Reverse Diagonal Check
        for (int r = x - 1; r > n - 2; r--) {
            for (int c = y - n - 1; c >= 0; c--) {
                count = 0;
                b = 1;
                for (int i = 0; i < n; i++) {
                    if (board[r - i][c + i] == Cell.X)
                        count += 1;
                    else if (board[r - i][c + i] == Cell.EMPTY && b == 1) {
                        count += 1;
                        AIxSpot = r - i;
                        AIySpot = c + i;
                        b = 0;
                    } else {
                        count = 0;
                        b = 1;
                    }
                    if (count == n)
                        return true;
                }
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
