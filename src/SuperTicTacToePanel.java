import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

public class SuperTicTacToePanel extends JPanel {

    private final JButton[][] jButtonsBoard;
    private Cell[][] iBoard = new Cell[16][16];
    private final JButton resetButton = new JButton("RESET");
    private final JButton quitButton = new JButton("QUIT");
    private final JButton undoButton = new JButton("UNDO");
    private ImageIcon xIcon;
    private ImageIcon oIcon;
    private ImageIcon emptyIcon;

    private final SuperTicTacToeGame game;

    private listener listener;

    //Assigns JPG Images to X/O Icons
    private void IconAssignment(int size) {
        int x = 100 / size * 3;
        int y = 100 / size * 3;
        String projectDir = "src/";
        xIcon = new ImageIcon(new ImageIcon (projectDir + "xIcon.png").
                getImage().getScaledInstance(x, y,
                        Image.SCALE_SMOOTH));
        oIcon = new ImageIcon(new ImageIcon (projectDir + "oIcon.png").
                getImage().getScaledInstance(x, y,
                        Image.SCALE_SMOOTH));
        emptyIcon = new ImageIcon(new ImageIcon (projectDir +
                "emptyIcon.png").getImage().getScaledInstance(
                x, y, Image.SCALE_SMOOTH));
    }


    //Preps GUI Board
    public SuperTicTacToePanel() {
        //Creates new TicTacToeGame
        game = new SuperTicTacToeGame();

        //Asks the player for TicTacToe Parameters
        int boardSize = getBoardSize();
        game.setSize(boardSize);
        IconAssignment(boardSize);

        int connectionSize = getConnectionSize(boardSize);
        game.setConnections(connectionSize);

        Player initialPlayer = getInitialPlayer();
        game.setPlayer(initialPlayer);

        int AIAnswer = checkAIPlayer();
        game.setAIPlayer(AIAnswer);



        //Sets new board up with given parameters
        game.reset();
        jButtonsBoard = new JButton[game.getRows()][game.getCols()];
        listener = new listener();

        JPanel boardPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(game.getRows(), game.getCols(), 1, 1));
        buttonPanel.setLayout(new GridLayout(5, 1));

        //Creates a 3 by 3 grid
        for (int row = 0; row < game.getRows(); row++)
            for (int col = 0; col < game.getCols(); col++) {
                jButtonsBoard[row][col] = new JButton("", emptyIcon);
                jButtonsBoard[row][col].addActionListener(listener);

                jButtonsBoard[row][col].setBackground(Color.WHITE);
                boardPanel.add(jButtonsBoard[row][col]);
                iBoard[row][col] = Cell.EMPTY;
            }

        //Sets up side panel for TicTacToeBoard
        buttonPanel.add(undoButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(quitButton);

        //Sets Panels Side by Side
        boardPanel.setBackground(Color.BLACK);
        buttonPanel.setPreferredSize(new Dimension(250, 600));
        boardPanel.setPreferredSize(new Dimension(600, 600));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel, boardPanel);

        add(splitPane);
        ActionListeners();


    }

    private int getBoardSize(){
        int temp = 0;
        String size;
        String s = "Board Size? (3-15)";
        String m = "Invalid / Null Input";
        while (temp < 2 || temp > 15) {
            size = JOptionPane.showInputDialog(null, s);
            try{
                temp = returnVal(size);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, m);
                System.exit(0);
            }
        }
        return temp;
    }

    private int getConnectionSize(int boardSize){
        int temp = 0;
        String length;
        String s = "Connection Length? (Larger than 3x3 must have 4+ length)";
        String m = "Invalid / Null Input";
        while (temp > boardSize ||
                (boardSize == 3 && temp != 3) ||
                (boardSize > 3 && temp < 4)){
            length = JOptionPane.showInputDialog(null, s);

            try {
                temp = returnVal(length);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, m);
                System.exit(0);
            }
        }

        return temp;
    }

    private Player getInitialPlayer(){
        Object[] possibleAnswers = {"X", "O"};
        Object answer = JOptionPane.showInputDialog(
                null,
                "Who Goes First",
                "Who Goes First",
                JOptionPane.INFORMATION_MESSAGE,
                null, possibleAnswers, possibleAnswers[0]);

        return returnPlayer(answer);
    }

    private int checkAIPlayer(){
        int AIAnswer = JOptionPane.showConfirmDialog(
                null,
                "AI Opponent?",
                "The AI would be O's",
                JOptionPane.YES_NO_OPTION);
        return AIAnswer;
    }


    //ActionListener for TicTacToeBoard
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            boolean Message = true;
            for (int row = 0; row < game.getRows(); row++)
                for (int col = 0; col < game.getCols(); col++)
                    if (jButtonsBoard[row][col] == event.getSource()){
                        game.select(row, col);
                        iBoard = game.getBoard();
                        displayBoard();
                        if (game.isComplete()) {
                            gameOver();
                            Message = false;
                        }
                    }
            game.AIChoice();
            displayBoard();
            if (game.isComplete() && Message)
                gameOver();
        }
    }

    //GameOver Message
    private void gameOver(){
            GameStatus status = game.getStatus();
            if (status == GameStatus.X_WON)
                JOptionPane.showMessageDialog(
                        null,
                        "X WINS!");
            else if (status == GameStatus.O_WON)
                JOptionPane.showMessageDialog(
                        null,
                        "O WINS!");
            else
                JOptionPane.showMessageDialog(
                        null,
                        "CATS! NO ONE WINS!");
    }

    //Action Listener for Side Panel
    public void ActionListeners(){
        quitButton.addActionListener(e -> System.exit(0));

        resetButton.addActionListener(e -> {
            game.reset();
            iBoard = game.getBoard();
            displayBoard();
        });

        undoButton.addActionListener(e -> {
            game.undoAction();
            displayBoard();
        });
    }

    //Conversion of Strings to appropriate type
    private int returnVal (String s){
        return parseInt(s);
    }

    private Player returnPlayer (Object s){
        String m = "" + s;
        if (m.equals("O"))
            return Player.OPlayer;
        else
            return Player.XPlayer;
    }

    //Assigns Icon Given Cell Data
    private void displayBoard(){
        //Checks for X Cell
        for (int row = 0; row < game.getRows(); row++)
            for (int col = 0; col < game.getCols(); col++) {
                //Sets X Cell
                if (iBoard[row][col] == Cell.X)
                    jButtonsBoard[row][col].setIcon(xIcon);
                //Sets O Cell
                else if (iBoard[row][col] == Cell.O)
                    jButtonsBoard[row][col].setIcon(oIcon);
                //Leaves Empty Cell Empty
                else
                    jButtonsBoard[row][col].setIcon(emptyIcon);

            }
    }

}
