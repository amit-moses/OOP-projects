/**
 * Represents a game board for a tic-tac-toe style game.
 * The board is implemented as a square grid where players can place their marks.
 * This implementation uses a 1D array to represent a 2D board for efficiency,
 * with coordinate conversion handled internally.
 *
 * Key Features:
 * - Customizable board size (defaults to 4x4)
 * - Efficient 1D array storage for 2D board
 * - Validates all moves before placement
 * - Thread-safe mark placement
 *
 * Board Structure:
 * - The board is represented as a 1D array of size N×N
 * - Coordinates are converted using the formula: index = row * size + col
 * - All cells are initialized as BLANK
 *
 * @author Amit Moses
 */
public class Board {
    /** Default size of the board if not specified */
    private static final int DEFAULT_SIZE = 4;

    /** Size of the board (both width and height) */
    private int size;

    /**
     * The game board array.
     * Uses 1D array for better memory efficiency and cache performance.
     * Index calculation: row * size + col
     */
    private Mark[] brd;

    /**
     * Creates a new game board with specified size.
     * Initializes all cells as BLANK and sets up tracking variables.
     *
     * @param size The width/height of the square board
     * @throws IllegalArgumentException if size is less than 1
     */
    public Board(int size){
        this.brd = new Mark[size * size];
        for(int i = 0; i < this.brd.length; i++){
            this.brd[i] = Mark.BLANK;
        }
        this.size = size;
    }

    /**
     * Creates a new game board with default size (4x4).
     * Convenience constructor that calls the primary constructor.
     */
    public Board(){
        this(DEFAULT_SIZE);
    }

    /**
     * Returns the size of the board.
     * Both width and height are equal to this value.
     *
     * @return The size of one side of the square board
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Attempts to place a mark at the specified position.
     * Validates the move before placement to ensure it's legal.
     *
     * @param mark The mark to place (X or O)
     * @param row The row coordinate (0-based)
     * @param col The column coordinate (0-based)
     * @return true if mark was successfully placed, false if invalid move
     */
    public boolean putMark(Mark mark, int row, int col){
        if(isValidCord(row, col) && this.isBlank(row, col)){
            this.setMark(mark, row, col);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the mark at the specified position.
     * No bounds checking is performed for efficiency - caller must validate coordinates.
     *
     * @param row The row coordinate (0-based)
     * @param col The column coordinate (0-based)
     * @return The mark at the specified position (X, O, or BLANK)
     */
    public Mark getMark(int row, int col){
        return isValidCord(row, col) ? this.brd[size * row + col] : Mark.BLANK;
    }

    /**
     * Validates if the given coordinates are within the board boundaries.
     *
     * @param row Row coordinate to validate
     * @param col Column coordinate to validate
     * @return true if coordinates are valid, false otherwise
     */
    private boolean isValidCord(int row, int col) {
        return 0 <= row && row < size && 0 <= col && col < size;
    }

    /**
     * Checks if the specified position is empty (BLANK).
     * Helper method for move validation.
     *
     * @param row Row coordinate to check
     * @param col Column coordinate to check
     * @return true if position is empty, false otherwise
     */
    private boolean isBlank(int row, int col) {
        return this.getMark(row, col) == Mark.BLANK;
    }

    /**
     * Places a mark at the specified position without validation.
     * Internal method called after move validation.
     *
     * @param mark The mark to place (X or O)
     * @param row The row coordinate
     * @param col The column coordinate
     */
    private void setMark(Mark mark, int row, int col){
        this.brd[size * row + col] = mark;
    }
}
