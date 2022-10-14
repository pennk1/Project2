package project2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**********************************************************************
 * 
 * @author Kody Penn, Kegan Socher, Adam Berendt
 * 
 * @version Fall 2022
 *
 *********************************************************************/
public class SuperTicTacToeGame {

	/** Who is playing the game */
	private Player player;

	/** Which player has the first turn */
	private Player initialPlayer;

	/** The game board */
	private Cell[][] board;

	/** The status of the game */
	private GameStatus status;

	/** List of moves played */
	private final ArrayList<Point> moves = new ArrayList<>();

	/** The number of move that the current turn is */
	private int currentMove = 0;

	/** Row for the AI */
	private int AIxSpot = 0;

	/** Column for the AI */
	private int AIySpot = 0;

	/** Whether there is an AI playing */
	private boolean AIPlayer = false;

	/** Number of rows on the board */
	private int numRows = 3;

	/** Number of columns on the board */
	private int numCols = 3;

	/** Number of connections to win */
	private int numConnections = 3;

	/******************************************************************
	 * 
	 * Sets the number of rows and columns to create the board size
	 * 
	 * @param size - Number of rows and columns for the board
	 * 
	 *****************************************************************/
	public void setSize(int size) {
		setRows(size);
		setCols(size);
	}

	/******************************************************************
	 * 
	 * Sets the number of rows for the game board
	 * 
	 * @param rows - Number of rows
	 * 
	 *****************************************************************/
	public void setRows(int rows) {
		numRows = rows;
	}

	/******************************************************************
	 * 
	 * Sets the number of columns for the game board
	 * 
	 * @param col - Number of columns
	 * 
	 *****************************************************************/
	public void setCols(int col) {
		numCols = col;
	}

	/******************************************************************
	 * Sets the number of connections needed to win the game
	 * 
	 * 
	 * @param connections - Number of connections to win
	 * 
	 *****************************************************************/
	public void setConnections(int connections) {
		numConnections = connections;
	}

	/******************************************************************
	 * 
	 * Sets the current player
	 * 
	 * @param playerIn - Who is playing currently
	 * 
	 *****************************************************************/
	public void setPlayer(Player playerIn) {
		player = playerIn;
	}

	/******************************************************************
	 * 
	 * Sets the player who has the first turn
	 * 
	 * @param playerIn - The player who will play first
	 * 
	 *****************************************************************/
	public void setInitialPlayer(Player playerIn) {
		initialPlayer = playerIn;
	}

	/******************************************************************
	 * 
	 * Sets whether there is an AI opponent
	 * 
	 * @param s - 0 if there is an AI opponent, otherwise no opponent
	 * 
	 *****************************************************************/
	public void setAIPlayer(int s) {
		if (s == 0)
			AIPlayer = true;
	}

	/******************************************************************
	 * 
	 * Sets which player's turn it is between Player X and Player O
	 * 
	 *****************************************************************/
	public void setNextPlayer() {
		player = player.next();
	}

	/******************************************************************
	 * 
	 * Gets the number of rows the game board has
	 * 
	 * @return numRows - Number of rows on the board
	 * 
	 *****************************************************************/
	public int getRows() {
		return numRows;
	}

	/******************************************************************
	 * 
	 * Gets the number of columns the game board has
	 * 
	 * @return numCols - Number of columns on the board
	 * 
	 *****************************************************************/
	public int getCols() {
		return numCols;
	}

	/******************************************************************
	 * 
	 * Gets the player who is able to make a move currently
	 * 
	 * @return player - The current player
	 * 
	 *****************************************************************/
	public Player getPlayer() {
		return player;
	}

	/******************************************************************
	 * 
	 * Gets the status of the game
	 * 
	 * @return status - What state the game is in
	 * 
	 *****************************************************************/
	public GameStatus getStatus() {
		return status;
	}

	/******************************************************************
	 * 
	 * Gets the current game board
	 * 
	 * @return board - The game board
	 * 
	 *****************************************************************/
	public Cell[][] getBoard() {
		return board;
	}

	/******************************************************************
	 * 
	 * Gets the player who played first
	 * 
	 * @return initialPlayer - The player who goes first
	 * 
	 *****************************************************************/
	public Player getInitialPlayer() {
		return initialPlayer;
	}

	/******************************************************************
	 * 
	 * Default constructor makes the game in progress, makes the
	 * current player Player X, and creates a 3 by 3 board
	 * 
	 *****************************************************************/
	public SuperTicTacToeGame() {
		status = GameStatus.IN_PROGRESS;
		player = Player.XPlayer;
		int x = numRows;
		int y = numCols;
		board = new Cell[x][y];

		for (int row = 0; row < x; row++)
			for (int col = 0; col < y; col++)
				board[row][col] = Cell.EMPTY;
	}

	/******************************************************************
	 * 
	 * Resets the game to being in progress and clearing all spaces on
	 * the board
	 * 
	 *****************************************************************/
	public void reset() {
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

	/******************************************************************
	 * 
	 * Checks if the space a player has chosen is valid and then
	 * changes the space, saves the move, and changes the turn
	 * 
	 *****************************************************************/
	public void select(int row, int col) {
		if (player == Player.XPlayer && isValidMove(row, col)) {
			board[row][col] = Cell.X;
			saveMove(row, col);
			setNextPlayer();
		} else if (player == Player.OPlayer && isValidMove(row, col)) {
			board[row][col] = Cell.O;
			saveMove(row, col);
			setNextPlayer();
		}
	}

	/******************************************************************
	 * 
	 * Checks if the inputted row and column are empty spaces that can
	 * be chosen by a player
	 * 
	 *****************************************************************/
	public boolean isValidMove(int row, int col) {
		return (board[row][col] == Cell.EMPTY
				&& status == GameStatus.IN_PROGRESS);
	}

	/******************************************************************
	 * 
	 * Saves the move on the inputted row and col into a list
	 * 
	 *****************************************************************/
	private void saveMove(int row, int col) {
		moves.add(new Point(row, col));
		currentMove += 1;
	}

	/******************************************************************
	 * 
	 * Undoes the last move by changing the last changed space to be
	 * empty and then changing which player's turn it is
	 * 
	 *****************************************************************/
	protected void undoAction() throws IndexOutOfBoundsException {

		// creates temp point to get last x-y pos
		Point temp;
		temp = moves.get(currentMove - 1);
		int x = (int) temp.getX();
		int y = (int) temp.getY();
		board[x][y] = Cell.EMPTY;

		// switches player and removes last index in moves
		setNextPlayer();
		moves.remove(currentMove - 1);
		currentMove -= 1;

		if (AIPlayer) {

			// Repeats undo if AI is playing
			temp = moves.get(currentMove - 1);
			x = (int) temp.getX();
			y = (int) temp.getY();
			board[x][y] = Cell.EMPTY;
			setNextPlayer();
			moves.remove(currentMove - 1);
			currentMove -= 1;
		}

	}

	/******************************************************************
	 * 
	 * Checks the board for any horizontal wins that will complete the
	 * game
	 * 
	 * @return true - If the game is complete from a horizontal win.
	 *         Otherwise, returns false
	 * 
	 *****************************************************************/
	private boolean horizCompleteCheck() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// Checks for a win for Player X
		for (int r = 0; r < cols; r++) {
			count = 0;
			for (int c = 0; c < rows; c++) {
				if (board[r][c] == Cell.X)
					count += 1;
				else
					count = 0;

				if (count == cons) {
					status = GameStatus.X_WON;
					return true;
				}
			}
		}

		// Checks for a win for Player O
		for (int r = 0; r < cols; r++) {
			count = 0;
			for (int c = 0; c < rows; c++) {
				if (board[r][c] == Cell.O)
					count += 1;
				else
					count = 0;

				if (count == cons) {
					status = GameStatus.O_WON;
					return true;
				}
			}
		}
		return false;
	}

	/******************************************************************
	 * 
	 * Checks the board for any vertical wins that will complete the
	 * game
	 * 
	 * @return true - If the game is complete from a vertical win.
	 *         Otherwise, returns false
	 * 
	 *****************************************************************/
	private boolean vertCompleteCheck() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// Checks if Player X can win vertically
		for (int c = 0; c < rows; c++) {
			count = 0;
			for (int r = 0; r < cols; r++) {
				if (board[r][c] == Cell.X)
					count += 1;
				else {
					count = 0;
				}
				if (count == cons) {
					status = GameStatus.X_WON;
					return true;
				}
			}
		}

		// Checks if Player O can win vertically
		for (int c = 0; c < rows; c++) {
			count = 0;
			for (int r = 0; r < cols; r++) {
				if (board[r][c] == Cell.O)
					count += 1;
				else
					count = 0;

				if (count == cons) {
					status = GameStatus.O_WON;
					return true;
				}
			}
		}

		return false;
	}

	/******************************************************************
	 * 
	 * Checks the board for upper left to lower right diagonal wins
	 * that will complete the game
	 * 
	 * @return true - If the game is complete from a diagonal win.
	 *         Otherwise, return false
	 * 
	 *****************************************************************/
	private boolean diagCompleteCheck() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// Diagonal check for Player X win
		for (int r = 0; r < (cols - cons + 1); r++) {
			for (int c = 0; c < (rows - cons + 1); c++) {
				count = 0;
				for (int i = 0; i < cons; i++) {
					if (board[r + i][c + i] == Cell.X)
						count += 1;
					else
						count = 0;

					if (count == cons) {
						status = GameStatus.X_WON;
						return true;
					}
				}
			}
		}

		// Diagonal check for Player O win
		for (int r = 0; r < (cols - cons + 1); r++) {
			for (int c = 0; c < (rows - cons + 1); c++) {
				count = 0;
				for (int i = 0; i < cons; i++) {
					if (board[r + i][c + i] == Cell.O)
						count += 1;
					else
						count = 0;

					if (count == cons) {
						status = GameStatus.O_WON;
						return true;
					}
				}
			}
		}

		return false;
	}

	/******************************************************************
	 * 
	 * Checks the board for lower left to upper right diagonal wins
	 * that will complete the game
	 * 
	 * @return true - If the game is complete from a reverse diagonal
	 *         win. Otherwise, return false
	 * 
	 *****************************************************************/
	private boolean revDiagCompleteCheck() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// Reverse diagonal check for Player X win
		for (int r = 0; r < (cols - cons + 1); r++) {
			for (int c = cons - 1; c < rows; c++) {
				count = 0;
				for (int i = 0; i < cons; i++) {
					if (board[r + i][c - i] == Cell.X)
						count += 1;
					else
						count = 0;

					if (count == cons) {
						status = GameStatus.X_WON;
						return true;
					}
				}
			}
		}

		// Reverse diagonal check for Player O win
		for (int r = 0; r < (cols - cons + 1); r++) {
			for (int c = cons - 1; c < rows; c++) {
				count = 0;
				for (int i = 0; i < cons; i++) {
					if (board[r + i][c - i] == Cell.O)
						count += 1;
					else
						count = 0;

					if (count == cons) {
						status = GameStatus.O_WON;
						return true;
					}
				}
			}
		}

		return false;
	}

	/******************************************************************
	 * 
	 * Checks the board for cats that will complete the game
	 * 
	 * @return true - If the game is complete from a cats. Otherwise,
	 *         return false
	 * 
	 *****************************************************************/
	private boolean catsCompleteCheck() {
		int cols = numCols;
		int rows = numRows;
		int count;

		count = 0;
		for (int r = 0; r < cols; r++)
			for (int c = 0; c < rows; c++) {
				if (board[r][c] != Cell.EMPTY)
					count += 1;
				if (count == (cols * rows)) {
					status = GameStatus.CATS;
					return true;
				}
			}

		return false;
	}

	/******************************************************************
	 * 
	 * Checks if the game is complete by seeing if either player has
	 * won or if there is a cats. If so, changes the status of the game
	 * 
	 * @return true - If game is complete. Otherwise, return false
	 * 
	 *****************************************************************/
	public boolean isComplete() {

		if (horizCompleteCheck()) {
			return true;
		} else if (vertCompleteCheck()) {
			return true;
		} else if (diagCompleteCheck()) {
			return true;
		} else if (catsCompleteCheck()) {
			return true;
		} else if (revDiagCompleteCheck()) {
			return true;
		} else {
			return false;
		}

	}

	/******************************************************************
	 * 
	 * Makes the AI select a position on the board to play by first
	 * checking if it can win and doing so then checking if it can lose
	 * to then block it. If neither, it will choose a space around
	 * itself in a pattern
	 * 
	 *****************************************************************/
	protected void AIChoice() {
		if (AIPlayer && player == Player.OPlayer
				&& status == GameStatus.IN_PROGRESS) {
			if (canWin()) {
				select(AIxSpot, AIySpot);
			} else if (canLose()) {
				select(AIxSpot, AIySpot);
			} else if (canPlaceNearSelf()) {
				select(AIxSpot, AIySpot);
			} else {
				int randomX = ThreadLocalRandom.current().nextInt(0,
						numRows);
				int randomY = ThreadLocalRandom.current().nextInt(0,
						numCols);
				if (isValidMove(randomX, randomY)) {
					select(randomX, randomY);
				} else
					AIChoice();
			}
		}
	}

	/******************************************************************
	 * 
	 * Checks if there is a win for the AI horizontally
	 * 
	 * @return true - If AI can win horizontally. Otherwise, false if
	 *         it can't win
	 * 
	 *****************************************************************/
	private boolean horizAIWin() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// buffer space for checking between connections
		int buffer;

		// Horizontal Check
		for (int r = 0; r < cols; r++) {
			count = 0;
			buffer = 1;
			for (int c = 0; c < rows; c++) {
				if (board[r][c] == Cell.O)
					count += 1;
				else if (board[r][c] == Cell.EMPTY && buffer == 1) {
					count += 1;
					AIxSpot = r;
					AIySpot = c;
					buffer = 0;
				} else {
					count = 0;
					buffer = 1;
				}
				if (count == cons)
					return true;
			}
		}

		// Backward Horizontal Check
		for (int r = cols - 1; r >= 0; r--) {
			count = 0;
			buffer = 1;
			for (int c = rows - 1; c >= 0; c--) {
				if (board[r][c] == Cell.O)
					count += 1;
				else if (board[r][c] == Cell.EMPTY && buffer == 1) {
					count += 1;
					AIxSpot = r;
					AIySpot = c;
					buffer = 0;
				} else {
					count = 0;
					buffer = 1;
				}
				if (count == cons)
					return true;
			}
		}

		return false;
	}

	/******************************************************************
	 * 
	 * Checks if there is a win for the AI vertically
	 * 
	 * @return true - If AI can win vertically. Otherwise, false if it
	 *         can't win
	 * 
	 *****************************************************************/
	private boolean vertAIWin() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// buffer space for checking between connections
		int buffer;

		// Vertical Check
		for (int c = 0; c < rows; c++) {
			count = 0;
			buffer = 1;
			for (int r = 0; r < cols; r++) {
				if (board[r][c] == Cell.O)
					count += 1;
				else if (board[r][c] == Cell.EMPTY && buffer == 1) {
					count += 1;
					AIxSpot = r;
					AIySpot = c;
					buffer = 0;
				} else {
					count = 0;
					buffer = 1;
				}
				if (count == cons)
					return true;
			}
		}

		// Backward Vertical Check
		for (int c = rows - 1; c >= 0; c--) {
			count = 0;
			buffer = 1;
			for (int r = cols - 1; r >= 0; r--) {
				if (board[r][c] == Cell.O)
					count += 1;
				else if (board[r][c] == Cell.EMPTY && buffer == 1) {
					count += 1;
					AIxSpot = r;
					AIySpot = c;
					buffer = 0;
				} else {
					count = 0;
					buffer = 1;
				}
				if (count == cons)
					return true;
			}
		}

		return false;
	}

	/******************************************************************
	 * 
	 * Checks if there is a win for the AI from an upper left to lower
	 * right or lower right to upper left diagonal
	 * 
	 * @return true - If AI can win diagonally. Otherwise, false if it
	 *         can't win
	 * 
	 *****************************************************************/
	private boolean diagAIWin() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// buffer space for checking between connections
		int buffer;

		// Diagonal check from top left to bottom right
		for (int r = 0; r < (cols - cons + 1); r++) {
			for (int c = 0; c < (rows - cons + 1); c++) {
				count = 0;
				buffer = 1;
				for (int i = 0; i < cons; i++) {
					if (board[r + i][c + i] == Cell.O)
						count += 1;
					else if (board[r + i][c + i] == Cell.EMPTY
							&& buffer == 1) {
						count += 1;
						AIxSpot = r + i;
						AIySpot = c + i;
						buffer = 0;
					} else {
						count = 0;
						buffer = 1;
					}
					if (count == cons)
						return true;
				}
			}
		}

		// Diagonal check from bottom right to top left
		for (int r = cols - 1; r >= cons - 1; r--) {
			for (int c = rows - 1; c >= cons - 1; c--) {
				count = 0;
				buffer = 1;
				for (int i = 0; i < cons; i++) {
					if (board[r - i][c - i] == Cell.O)
						count += 1;
					else if (board[r - i][c - i] == Cell.EMPTY
							&& buffer == 1) {
						count += 1;
						AIxSpot = r - i;
						AIySpot = c - i;
						buffer = 0;
					} else {
						count = 0;
						buffer = 1;
					}
					if (count == cons)
						return true;
				}
			}
		}

		return false;

	}

	/******************************************************************
	 * 
	 * Checks if there is a win for the AI from a lower left to upper
	 * right or upper right to lower left diagonal
	 * 
	 * @return true - If AI can win from a reverse diagonal. Otherwise,
	 *         false if it can't win
	 * 
	 *****************************************************************/
	private boolean revDiagAIWin() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// buffer space for checking between connections
		int buffer;

		// Diagonal check from bottom left to top right
		for (int r = 0; r < (cols - cons + 1); r++) {
			for (int c = cons - 1; c < rows; c++) {
				count = 0;
				buffer = 1;
				for (int i = 0; i < cons; i++) {
					if (board[r + i][c - i] == Cell.O)
						count += 1;
					else if (board[r + i][c - i] == Cell.EMPTY
							&& buffer == 1) {
						count += 1;
						AIxSpot = r + i;
						AIySpot = c - i;
						buffer = 0;
					} else {
						count = 0;
						buffer = 1;
					}
					if (count == cons)
						return true;
				}
			}
		}

		// Diagonal check from top right to bottom left
		for (int r = cols - 1; r > cons - 2; r--) {
			for (int c = rows - cons - 1; c >= 0; c--) {
				count = 0;
				buffer = 1;
				for (int i = 0; i < cons; i++) {
					if (board[r - i][c + i] == Cell.O)
						count += 1;
					else if (board[r - i][c + i] == Cell.EMPTY
							&& buffer == 1) {
						count += 1;
						AIxSpot = r - i;
						AIySpot = c + i;
						buffer = 0;
					} else {
						count = 0;
						buffer = 1;
					}
					if (count == cons)
						return true;
				}
			}
		}

		return false;

	}

	/******************************************************************
	 * 
	 * Checks if the AI can win with the current spots it has and
	 * records the row and column of where it can win
	 * 
	 * @return true - If the AI can win. Otherwise, false
	 * 
	 *****************************************************************/
	private boolean canWin() {

		if (horizAIWin()) {
			return true;
		} else if (vertAIWin()) {
			return true;
		} else if (diagAIWin()) {
			return true;
		} else if (revDiagAIWin()) {
			return true;
		} else {
			return false;
		}

	}

	/******************************************************************
	 * 
	 * Checks whether or not the AI can lose from the other player
	 * creating a horizontal connection
	 * 
	 * @return true - If AI can lose from a horizontal connection.
	 *         Otherwise, returns false
	 * 
	 *****************************************************************/
	private boolean horizAILose() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// buffer space for checking between connections
		int buffer;

		// Horizontal Check
		for (int r = 0; r < cols; r++) {
			count = 0;
			buffer = 1;
			for (int c = 0; c < rows; c++) {
				if (board[r][c] == Cell.X)
					count += 1;
				else if (board[r][c] == Cell.EMPTY && buffer == 1) {
					count += 1;
					AIxSpot = r;
					AIySpot = c;
					buffer = 0;
				} else {
					count = 0;
					buffer = 1;
				}
				if (count == cons)
					return true;
			}
		}

		// Backwards Horizontal Check
		for (int r = cols - 1; r >= 0; r--) {
			count = 0;
			buffer = 1;
			for (int c = rows - 1; c >= 0; c--) {
				if (board[r][c] == Cell.X)
					count += 1;
				else if (board[r][c] == Cell.EMPTY && buffer == 1) {
					count += 1;
					AIxSpot = r;
					AIySpot = c;
					buffer = 0;
				} else {
					count = 0;
					buffer = 1;
				}
				if (count == cons)
					return true;
			}
		}
		return false;
	}

	/******************************************************************
	 * 
	 * Checks whether or not the AI can lose from the other player
	 * creating a vertical connection
	 * 
	 * @return true - If AI can lose from a vertical connection.
	 *         Otherwise, returns false
	 * 
	 *****************************************************************/
	private boolean vertAILose() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// buffer space for checking between connections
		int buffer;

		// Vertical Check
		for (int c = 0; c < rows; c++) {
			count = 0;
			buffer = 1;
			for (int r = 0; r < cols; r++) {
				if (board[r][c] == Cell.X)
					count += 1;
				else if (board[r][c] == Cell.EMPTY && buffer == 1) {
					count += 1;
					AIxSpot = r;
					AIySpot = c;
					buffer = 0;
				} else {
					count = 0;
					buffer = 1;
				}
				if (count == cons)
					return true;
			}
		}

		// Backward Vertical Check
		for (int c = rows - 1; c >= 0; c--) {
			count = 0;
			buffer = 1;
			for (int r = cols - 1; r >= 0; r--) {
				if (board[r][c] == Cell.X)
					count += 1;
				else if (board[r][c] == Cell.EMPTY && buffer == 1) {
					count += 1;
					AIxSpot = r;
					AIySpot = c;
					buffer = 0;
				} else {
					count = 0;
					buffer = 1;
				}
				if (count == cons)
					return true;
			}
		}
		return false;

	}

	/******************************************************************
	 * 
	 * Checks whether or not the AI can lose from the other player
	 * creating a diagonal connection from upper left to lower right or
	 * from lower right to upper left
	 * 
	 * @return true - If AI can lose from a diagonal connection.
	 *         Otherwise, returns false
	 * 
	 *****************************************************************/
	private boolean diagAILose() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// buffer space for checking between connections
		int buffer;

		// Diagonal check from top left to bottom right
		for (int r = 0; r < (cols - cons + 1); r++) {
			for (int c = 0; c < (rows - cons + 1); c++) {
				count = 0;
				buffer = 1;
				for (int i = 0; i < cons; i++) {
					if (board[r + i][c + i] == Cell.X)
						count += 1;
					else if (board[r + i][c + i] == Cell.EMPTY
							&& buffer == 1) {
						count += 1;
						AIxSpot = r + i;
						AIySpot = c + i;
						buffer = 0;
					} else {
						count = 0;
						buffer = 1;
					}
					if (count == cons)
						return true;
				}
			}
		}

		// Diagonal check from bottom right to top left
		for (int r = cols - 1; r >= cons - 1; r--) {
			for (int c = rows - 1; c >= cons - 1; c--) {
				count = 0;
				buffer = 1;
				for (int i = 0; i < cons; i++) {
					if (board[r - i][c - i] == Cell.X)
						count += 1;
					else if (board[r - i][c - i] == Cell.EMPTY
							&& buffer == 1) {
						count += 1;
						AIxSpot = r - i;
						AIySpot = c - i;
						buffer = 0;
					} else {
						count = 0;
						buffer = 1;
					}
					if (count == cons)
						return true;
				}
			}
		}
		return false;

	}

	/******************************************************************
	 * 
	 * Checks whether or not the AI can lose from the other player
	 * creating a reverse diagonal connection from lower left to upper
	 * right or from upper right to lower left
	 * 
	 * @return true - If AI can lose from a reverse diagonal
	 *         connection. Otherwise, returns false
	 * 
	 *****************************************************************/
	private boolean revDiagAILose() {
		int cols = numCols;
		int rows = numRows;
		int cons = numConnections;
		int count;

		// buffer space for checking between connections
		int buffer;

		// Diagonal check from bottom left to top right
		for (int r = 0; r < (cols - cons + 1); r++) {
			for (int c = cons - 1; c < rows; c++) {
				count = 0;
				buffer = 1;
				for (int i = 0; i < cons; i++) {
					if (board[r + i][c - i] == Cell.X)
						count += 1;
					else if (board[r + i][c - i] == Cell.EMPTY
							&& buffer == 1) {
						count += 1;
						AIxSpot = r + i;
						AIySpot = c - i;
						buffer = 0;
					} else {
						count = 0;
						buffer = 1;
					}
					if (count == cons)
						return true;
				}
			}
		}

		// Diagonal check from top right to bottom left
		for (int r = cols - 1; r > cons - 2; r--) {
			for (int c = rows - cons - 1; c >= 0; c--) {
				count = 0;
				buffer = 1;
				for (int i = 0; i < cons; i++) {
					if (board[r - i][c + i] == Cell.X)
						count += 1;
					else if (board[r - i][c + i] == Cell.EMPTY
							&& buffer == 1) {
						count += 1;
						AIxSpot = r - i;
						AIySpot = c + i;
						buffer = 0;
					} else {
						count = 0;
						buffer = 1;
					}
					if (count == cons)
						return true;
				}
			}
		}
		return false;

	}

	/******************************************************************
	 * 
	 * Checks if the AI can lose with the current board and records the
	 * row and column of where it can block a loss
	 * 
	 * @return true - If the AI can lose. Otherwise, false
	 * 
	 *****************************************************************/
	private boolean canLose() {

		if (horizAILose()) {
			return true;
		} else if (vertAILose()) {
			return true;
		} else if (diagAILose()) {
			return true;
		} else if (revDiagAILose()) {
			return true;
		} else {
			return false;
		}

	}

	/******************************************************************
	 * 
	 * Checks the board to see if the AI can select a spot near one it
	 * has already
	 * 
	 * @return true - If the AI can place around itself. Otherwise,
	 *         false
	 * 
	 *****************************************************************/
	private boolean canPlaceNearSelf() {
		int cols = numCols;
		int rows = numRows;

		for (int r = 0; r < rows - 1; r++)
			for (int c = 0; c < cols - 1; c++)
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
		for (int r = 1; r < rows; r++)
			for (int c = 1; c < cols; c++)
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