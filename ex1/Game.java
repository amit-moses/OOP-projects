/**
 * Represents a game of tic-tac-toe with customizable board size and win condition.
 * This class manages the game flow between two players and determines the winner
 * based on a configurable winning streak length.
 *
 * Features:
 * - Customizable board size
 * - Configurable winning streak length
 * - Supports different player implementations
 * - Flexible rendering system
 * - Win detection for rows, columns, and diagonals
 *
 * @author Amit Moses
 */
public class Game {
    /**
     * Default winning streak length.
     */
    private static final int DEFAULT_STREAK = 3;

    /**
     * Number of players in the game.
     */
    private static final int NUM_PLAYERS = 2;

    /**
     * jumps to next cell
     */
    private static final int POSITIVE_JUMP = 1;

    /**
     * jumps to previous cell
     */
    private static final int NEGATIVE_JUMP = -1;

    /**
     * Number of marks in a row needed to win.
     */
    private int streak;

    /**
     * Array of players participating in the game.
     */
    private Player[] players;

    /**
     * Renderer to display the game board.
     */
    private Renderer renderer;

    /**
     * The game board.
     */
    private Board board;

    /**
     * Initializes a new game with the specified players and renderer.
     * Uses the default board size and winning streak length.
     *
     * @param playerX The player using the X mark
     * @param playerO The player using the O mark
     * @param renderer The renderer to display the game board
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        board = new Board();
        this.streak = DEFAULT_STREAK;
        this.players = new Player[]{playerX, playerO};
        this.renderer = renderer;
    }


    /**
     * Initializes a new game with the specified players, board size, winning streak length, and renderer.
     *
     * @param playerX The player using the X mark
     * @param playerO The player using the O mark
     * @param size The size of the game board
     * @param winStreak The number of marks in a row needed to win
     * @param renderer The renderer to display the game board
     */
    public Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        board = new Board(size);
        this.streak = winStreak;
        this.players = new Player[]{playerX, playerO};
        this.renderer = renderer;
    }

    /**
     * Gets the required streak to win.
     *
     * @return Number of marks needed in a row to win
     */
    public int getWinStreak() {
        return this.streak;
    }

    /**
     * Gets the board size.
     *
     * @return Size of the game board
     */
    public int getBoardSize() {
        return this.board.getSize();
    }

    /**
     * Runs the game until completion.
     * Alternates between players until someone wins or the board is full.
     *
     * @return The winning mark (X or O) or BLANK for a tie
     */
    public Mark run() {
        for(int i = 0; i < board.getSize() * board.getSize() ; i++){
            Mark current = i % NUM_PLAYERS == 0 ? Mark.X : Mark.O;
            players[i % NUM_PLAYERS].playTurn(board, current);
            renderer.renderBoard(board);
            if(checkWin(current)){
                return current;
            }
        }
        return Mark.BLANK;
    }


    /**
     * Checks if the current mark has won the game.
     *
     * @param mark Mark to check for win
     * @return true if mark has won, false otherwise
     */
    private boolean checkWin(Mark mark) {
        for(int i = 0; i < board.getSize(); i++){
            if(checkLine(mark, i, true) ||
               checkLine(mark, i, false) ||

               checkDiagonal(mark, 0, i, POSITIVE_JUMP, POSITIVE_JUMP) ||
               checkDiagonal(mark, i, 0, NEGATIVE_JUMP, POSITIVE_JUMP) ||
               checkDiagonal(mark, i, getBoardSize() - 1, POSITIVE_JUMP, NEGATIVE_JUMP) ||
               checkDiagonal(mark, getBoardSize() - 1, i , NEGATIVE_JUMP, NEGATIVE_JUMP)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks a line (row or column) for a winning streak.
     *
     * @param mark Mark to check
     * @param index Row/column index
     * @param isRow true to check row, false to check column
     * @return true if winning streak found, false otherwise
     */
    private boolean checkLine(Mark mark, int index, boolean isRow) {
        int current = 0;
        for (int i = 0; i < board.getSize(); i++) {
            int row = isRow ? index : i;
            int col = isRow ? i : index;
            if (isValidCord(board, row, col) && board.getMark(row, col) == mark) {
                current++;
                if (current == streak) {
                    return true;
                }
            } else {
                current = 0;
            }
        }
        return false;
    }

    /**
     * Checks a diagonal for a winning streak.
     *
     * @param mark Mark to check
     * @param startX Starting position x
     * @param startY Starting position y
     * @param addToX Direction modifier for x
     * @param addToY Direction modifier for y
     * @return true if winning streak found, false otherwise
     */
    private boolean checkDiagonal(Mark mark, int startX, int startY, int addToX, int addToY) {
        int current = 0;
        while(isValidCord(board, startX, startY)){
            if(this.board.getMark(startX, startY) == mark){
                current++;
                if (current == streak) {
                    return true;
                }
            } else {
                current = 0;
            }
            startX += addToX;
            startY += addToY;
        }
        return false;
    }

    /**
     * Validates if the given coordinates are within the board boundaries.
     *
     * @param board The game board
     * @param row Row coordinate to validate
     * @param col Column coordinate to validate
     * @return true if coordinates are valid, false otherwise
     */
    private boolean isValidCord(Board board, int row, int col) {
        return 0 <= row && row < board.getSize() && 0 <= col && col < board.getSize();
    }
}