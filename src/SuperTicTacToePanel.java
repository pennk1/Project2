import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

public class SuperTicTacToePanel extends JPanel {

    //TODO ADD User friendly message for invalid inputs or excessive undo presses

    private JButton[][] jButtonsBoard;
    private Cell[][] iBoard = new Cell[100][100];
    private JButton resetButton = new JButton("RESET");
    private JButton quitButton = new JButton("QUIT");
    private JButton undoButton = new JButton("UNDO");
    private ImageIcon xIcon;
    private ImageIcon oIcon;
    private ImageIcon emptyIcon;

    private SuperTicTacToeGame game;

    private listener listener;

    //TODO add icons
    //Assigns JPG Images to X/O Icons
    private void IconAssignment() {
        xIcon = new ImageIcon(".src/x.jpg");
        oIcon = new ImageIcon(".src/o.jpg");
        emptyIcon = new ImageIcon(".src/empty.jpg");
    }


    //Preps GUI Board
    public SuperTicTacToePanel() {
        //Creates new TicTacToeGame
        game = new SuperTicTacToeGame();

        //Asks the player for TicTacToe Parameters

        //OLD CODE THAT SETS SEPARATE SIZES
//        String rows = JOptionPane.showInputDialog(null, "Number of Rows?");
//        int temp = returnVal(rows);
//        game.setRows(temp);
//        String cols = JOptionPane.showInputDialog(null, "Number of Cols?");
//        temp = returnVal(cols);
//        game.setCols(temp);

        String size = JOptionPane.showInputDialog(null, "Board Size?");
        int temp = returnVal(size);
        game.setSize(temp);
        String length = JOptionPane.showInputDialog(null, "Winning Connection Length?");
        temp = returnVal(length);
        game.setConnections(temp);

        Object[] possibleAnswers = {"X", "O"};
        Object answer = JOptionPane.showInputDialog(null,
                            "Who Goes First",
                               "Who Goes First",
                                    JOptionPane.INFORMATION_MESSAGE,
                               null, possibleAnswers, possibleAnswers[0]);
        Player tempPlayer = returnPlayer(answer);
        game.setPlayer(tempPlayer);
//        JOptionPane.showConfirmDialog(null,
//                              "AI Opponent?",
//                                  "The AI would be O's",
//                                      JOptionPane.YES_NO_OPTION);


        //Sets new board up with given parameters
        game.reset();
        jButtonsBoard = new JButton[game.getRows()][game.getCols()];
        listener = new listener();
        IconAssignment();

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

    //ActionListener for TicTacToeBoard
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            for (int row = 0; row < game.getRows(); row++)
                for (int col = 0; col < game.getCols(); col++)
                    if (jButtonsBoard[row][col] == event.getSource()) {
                        game.select(row, col);
                        iBoard = game.getBoard();
                        displayBoard();
                        if (game.isComplete())
                            if (game.getStatus() == GameStatus.X_WON)
                                JOptionPane.showMessageDialog(null, "X WINS!");
                            else if (game.getStatus() == GameStatus.O_WON)
                                JOptionPane.showMessageDialog(null, "O WINS!");
                            else
                                JOptionPane.showMessageDialog(null, "CATS! NO ONE WINS!");
                    }
            //game.AIChoice();
        }
    }

    //Action Listener for Side Panel
    public void ActionListeners(){
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.reset();
                iBoard = game.getBoard();
                displayBoard();
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.undoAction();
                displayBoard();
            }
        });
    }

    //Conversion of Strings to appropriate type
    private int returnVal (String s){
        int val = parseInt(s);

        return val;
    }

    private Player returnPlayer (Object s){
        String x = "" + s;
        if (x.equals("O")) {
            return Player.OPlayer;
        }
        else
            return Player.XPlayer;

    }

    //Assigns Icon Given Cell Data
    private void displayBoard(){     //Checks for X Cell

        for (int row = 0; row < game.getRows(); row++)
            for (int col = 0; col < game.getCols(); col++)
                if (iBoard[row][col] == Cell.X)
                    jButtonsBoard[row][col].setBackground(Color.RED);
//                    jButtonsBoard[row][col].setIcon(xIcon);

        //Checks for O Cell
        for (int row = 0; row < game.getRows(); row++)
            for (int col = 0; col < game.getCols(); col++)
                if (iBoard[row][col] == Cell.O)
                    jButtonsBoard[row][col].setBackground(Color.BLUE);
//                    jButtonsBoard[row][col].setIcon(oIcon);

        //Checks for Empty Cell
        for (int row = 0; row < game.getRows(); row++)
            for (int col = 0; col < game.getCols(); col++)
                if (iBoard[row][col] == Cell.EMPTY || iBoard[row][col] == null)
                    jButtonsBoard[row][col].setBackground(Color.WHITE);
                    //jButtonsBoard[row][col].setIcon(emptyIcon);
    }

}
