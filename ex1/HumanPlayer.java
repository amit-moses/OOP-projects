/**
 * Represents a human player in the game.
 * Handles user input for making moves on the board.
 *
 * Features:
 * - Processes user input for moves
 * - Validates move coordinates
 * - Provides feedback for invalid moves
 * - Supports retry on invalid input
 *
 * @author Amit Moses
 */
public class HumanPlayer implements Player {
    /** Format string for requesting player input */
    private static final String INPUT_FORMAT = "Player %s, type coordinates:";

    /** Prompt for requesting new input after invalid move */
    private static final String NEW_INPUT = " Please choose a valid position:";

    /** Error message for invalid coordinates */
    private static final String ERROR_INVALID = "Invalid mark position.";

    /** Error message for occupied position */
    private static final String ERROR_TAKEN = "Mark position is already occupied.";

    /**
     * Creates a new human player instance.
     */
    public HumanPlayer(){}

    /**
     * Handles a player's turn by getting input coordinates and placing mark.
     * Continues to request input until a valid move is made.
     *
     * @param board The game board
     * @param mark The player's mark (X or O)
     */
    public void playTurn(Board board, Mark mark){
        int[] res = inputCords(String.format(INPUT_FORMAT, mark.toString()));
        while(! board.putMark(mark, res[0], res[1])){
            String errMsg =  isValidCord(board, res[0], res[1]) ? ERROR_TAKEN : ERROR_INVALID;
            res = inputCords(errMsg + NEW_INPUT);
        }
    }

    /**
     * Gets coordinate input from user.
     * Processes input as a single integer and splits into row/column.
     * Example: Input 12 becomes row 1, column 2
     *
     * @param message Prompt message for user
     * @return Array containing [row, col] coordinates
     */
    private int[] inputCords(String message){
        System.out.println(message);
        int res = KeyboardInput.readInt();
        return new int[]{res / 10, res % 10};
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