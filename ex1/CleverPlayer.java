import java.util.Random;
/**
 * Implements an intelligent player for the tic-tac-toe game that focuses on
 * offensive strategies. This player is "clever" because it tries to create the
 * longest possible streak of its own marks, but may miss defensive opportunities.
 *
 * Strategy details:
 * 1. Primary strategy: Attempts to create longest possible streak (70% of moves)
 * 2. Secondary strategy: Random moves (30% of moves)
 * 3. Only considers its own potential streaks, ignoring opponent's threats
 *
 *
 * @author Amit Moses
 */
public class CleverPlayer implements Player {
    /** Fixed probability threshold for making a strategic move */
    private static final int STRATEGIC_MOVE_PROBABILITY = 7;

    /**
     * jumps to next cell
     */
    private static final int POSITIVE_JUMP = 1;

    /**
     * jumps to previous cell
     */
    private static final int NEGATIVE_JUMP = -1;

    /** Random number generator for move selection and fallback moves */
    private Random rand = new Random();

    /**
     * Creates a new CleverPlayer instance.
     */
    public CleverPlayer(){}

    /**
     * Executes the player's turn using a probabilistic strategy:
     * - 70% chance of making a strategic move
     * - 30% chance of making a random move
     * This randomization makes the player less predictable than GeniusPlayer.
     *
     * @param board The game board
     * @param mark The player's mark (X or O)
     */
    public void playTurn(Board board, Mark mark) {
        //different from GeniusPlayer - random move is made only 70% of the time
        if(rand.nextInt(10) < STRATEGIC_MOVE_PROBABILITY && makeSmartMove(board, mark)){
            return;
        }
        markRandomMove(board, mark);
    }





    /**
     * Attempts to make the most strategic move possible by evaluating potential
     * streak lengths. Unlike GeniusPlayer, only considers the player's own marks.
     *
     * @param board The game board
     * @param mark The player's mark (X or O)
     * @return true if a strategic move was made, false otherwise
     */
    private boolean makeSmartMove(Board board, Mark mark) {
        // Find the available positions that are adjacent to the player's existing marks
        int bestMove = -1;
        int bestScore = 0;

        for(int i = 0; i < board.getSize() * board.getSize(); i++){
            int row = i / board.getSize();
            int col = i % board.getSize();
            if(board.getMark(row, col) != Mark.BLANK){
                continue;
            }
            int maximumPlayerStreak = maxStreak(board, mark, new int[]{row, col});
            if(maximumPlayerStreak > bestScore){
                bestMove = i;
                bestScore = maximumPlayerStreak;
            }
        }
        return bestMove != -1 &&
                board.putMark(mark, bestMove/board.getSize(), bestMove%board.getSize());
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

    /**
     * Places a mark in a random empty cell on the board.
     * Used both as a fallback strategy and as part of the randomization strategy.
     *
     * @param board The game board
     * @param mark The player's mark (X or O)
     */
    private void markRandomMove(Board board, Mark mark) {
        int randomX= rand.nextInt(board.getSize());
        int randomY= rand.nextInt(board.getSize());
        while(board.getMark(randomX, randomY) != Mark.BLANK){
            randomX = rand.nextInt(board.getSize());
            randomY = rand.nextInt(board.getSize());
        }
        board.putMark(mark, randomX, randomY);
    }

    /**
     * Finds the edge position for checking diagonal streaks.
     * Moves to the topmost position of the diagonal line.
     *
     * @param board The game board
     * @param x Starting x coordinate
     * @param y Starting y coordinate
     * @param pos1 Direction modifier (-1 for left diagonal, 1 for right diagonal)
     * @return Array containing [x, y] of edge position
     */
    private int[] getEdge(Board board, int x, int y, int pos1){
        while(isValidCord(board, x + pos1 , y - 1) ){
            x += pos1;
            y -= 1;
        }
        return new int[]{x, y};
    }

    /**
     * Calculates the maximum possible streak in all directions from a given position.
     *
     * @param board The game board
     * @param mark The mark to check for streaks
     * @param toMark The position to evaluate [row, col]
     * @return Length of the maximum possible streak
     */
    private int maxStreak(Board board, Mark mark, int[] toMark) {
        int vertical = checkLine(board, mark, toMark[0], true);
        int horizontal = checkLine(board, mark, toMark[1], false);
        int diagPos = checkDiagonal(board, mark,
                getEdge(board, toMark[0], toMark[1], NEGATIVE_JUMP), NEGATIVE_JUMP);
        int diagNeg = checkDiagonal(board, mark,
                getEdge(board, toMark[0], toMark[1], POSITIVE_JUMP), POSITIVE_JUMP);

        return Math.max(Math.max(vertical, horizontal), Math.max(diagPos, diagNeg));
    }

    /**
     * Checks a line (row or column) for the maximum possible streak.
     *
     * @param board The game board
     * @param mark The mark to check for streaks
     * @param index Row/column index
     * @param isRow true to check row, false to check column
     * @return Length of the maximum streak in the line
     */
    private int checkLine(Board board, Mark mark, int index, boolean isRow) {
        int maxStreak = 0;
        int current = 0;
        for (int i = 0; i < board.getSize(); i++) {
            int row = isRow ? index : i;
            int col = isRow ? i : index;
            if (isValidCord(board, row, col) && board.getMark(row, col) == mark) {
                current++;
                if (maxStreak < current) {
                    maxStreak = current;
                }
            } else {
                current = 0;
            }
        }
        return maxStreak;
    }

    /**
     * Checks a diagonal line for the maximum possible streak.
     * Starts from the edge position and moves diagonally across the board,
     * counting consecutive marks of the same type.
     *
     * @param board The game board
     * @param mark The mark to check for streaks
     * @param start Starting position [x, y]
     * @param pos Direction modifier (-1 for left diagonal, 1 for right diagonal)
     * @return Length of the maximum streak in the diagonal
     */
    private int checkDiagonal(Board board, Mark mark, int[] start, int pos){
        int maxStreak = 0;
        int current = 0;
        while(isValidCord(board, start[0], start[1])){
            if(board.getMark(start[0], start[1]) == mark){
                current++;
                if (maxStreak < current) {
                    maxStreak = current;
                }
            } else {
                current = 0;
            }
            start[0] -= pos;
            start[1] += 1;
        }
        return maxStreak;
    }
}