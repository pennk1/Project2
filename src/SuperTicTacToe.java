import java.awt.Dimension;
import javax.swing.JFrame;

public class SuperTicTacToe {

    //private Object game = new SuperTicTacToeGame();

    public static void main(String[] args){
        JFrame frame = new JFrame("SuperTicTacToe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SuperTicTacToePanel panel = new SuperTicTacToePanel();

        frame.getContentPane().add(panel);
        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(900, 650));
        frame.pack();
        frame.setVisible(true);

    }

}
