/**
 * Interface defining the contract for game players.
 * All player types (human and automated) must implement this interface.
 *
 * This interface ensures that:
 * - All players can make moves on the board
 * - Game logic can treat all player types uniformly
 * - New player types can be easily added
 *
 * @author Amit Moses
 */
interface Player {
    /**
     * Makes a move on the board.
     * Implementation determines how the move is chosen.
     *
     * @param board The game board to play on
     * @param mark The mark (X or O) to place
     */
    void playTurn(Board board, Mark mark);
}