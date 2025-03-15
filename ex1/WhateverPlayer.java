import java.util.Random;

/**
 * A simple player implementation that makes random moves.
 * This player serves as a baseline implementation and can be used for testing
 * or as an opponent for more sophisticated players.
 *
 * Strategy: Simply chooses a random available position on the board.
 *
 * @author Amit Moses
 */
public class WhateverPlayer implements Player {
    private Random rand = new Random();

    /**
     * Constructs a new WhateverPlayer.
     */
    public WhateverPlayer() {}

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
     * Executes the player's turn using a probabilistic strategy:
     * - 100% chance of making a random move
     *
     * @param board The game board
     * @param mark The player's mark (X or O)
     */
    public void playTurn(Board board, Mark mark) {
        markRandomMove(board, mark);
    }
}