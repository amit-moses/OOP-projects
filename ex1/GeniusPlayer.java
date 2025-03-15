import java.util.Random;

/**
 * Implements an advanced player for the tic-tac-toe game that combines both defensive
 * and offensive strategies. This player is considered "genius" because it evaluates both
 * its own potential winning moves and blocks opponent's winning opportunities.
 *
 * Strategy details:
 * 1. Primary strategy: Looks for moves that create the longest possible streak
 * 2. Evaluates both player's and opponent's potential streaks
 * 3. Falls back to random moves when no strategic move is available
 *
 *
 * @author Amit Moses
 */
public class GeniusPlayer implements Player {
    /**
     * jumps to next cell
     */
    private static final int POSITIVE_JUMP = 1;

    /**
     * jumps to previous cell
     */
    private static final int NEGATIVE_JUMP = -1;

    /** Random number generator for fallback moves */
    private Random rand = new Random();

    /**
     * Creates a new GeniusPlayer instance.
     */
    public GeniusPlayer(){}

    /**
     * Executes the player's turn by first attempting a strategic move,
     * then falling back to a random move if no strategic move is available.
     *
     * @param board The game board
     * @param mark The player's mark (X or O)
     */
    public void playTurn(Board board, Mark mark) {
        if(makeSmartMove(board, mark)){
            return;
        }
        markRandomMove(board, mark);
    }





    /**
     * Attempts to make the most strategic move possible by evaluating both
     * offensive and defensive positions. This is the key method that differentiates
     * GeniusPlayer from CleverPlayer, as it considers both player streaks.
     *
     * @param board The game board
     * @param mark The player's mark (X or O)
     * @return true if a strategic move was made, false otherwise
     */
    private boolean makeSmartMove(Board board, Mark mark) {
        Mark opponent = mark == Mark.X ? Mark.O : Mark.X;
        // Find the available positions that are adjacent to the player's existing marks
        int bestMove = -1;
        int bestScore = 0;
        for(int i = 0; i < board.getSize() * board.getSize(); i++){
            int row = i / board.getSize();
            int col = i % board.getSize();
            if(board.getMark(row, col) != Mark.BLANK){
                continue;
            }

            //different from CleverPlayer - maximum streak between the player and the opponent
            int maximumPlayerStreak = maxStreak(board, mark, new int[]{row, col});
            int maximumOpponentStreak = maxStreak(board, opponent, new int[]{row, col});
            int score = Math.max(maximumPlayerStreak, maximumOpponentStreak);
            if(score > bestScore){
                bestMove = i;
                bestScore = score;
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
     * Used as a fallback strategy when no strategic move is found.
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