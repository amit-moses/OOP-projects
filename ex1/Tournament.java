// Tournament.java
/**
 * Manages a tournament of multiple games between two players.
 * Tracks scores and displays final results.
 *
 * @author Amit Moses
 */

public class Tournament {
    // Constants for results display formatting
    private static final String RESULT_HEADLINE = "######### Results #########";
    private static final String RESULT_FORMAT = "Player %d, %s won: %d rounds\n";
    private static final String RESULT_TIE = "Ties: %d";

    private static final int NUM_PLAYERS = 2; // Number of players in the tournament
    // Tournament state
    private Player[] players;       // The two players participating in the tournament
    private int[] result;           // Array storing win counts [player1 wins, player2 wins]
    private int rounds;             // Total number of rounds to play
    private Renderer renderer;      // Renderer for displaying the game board

    /**
     * Creates a new tournament with specified number of rounds and players.
     *
     * @param rounds Number of rounds to play in the tournament
     * @param renderer Renderer to use for displaying the game boards
     * @param player1 First player in the tournament
     * @param player2 Second player in the tournament
     */
    public Tournament(int rounds, Renderer renderer, Player player1, Player player2){
        this.rounds = rounds;
        this.result = new int[]{0, 0};
        this.renderer = renderer;
        this.players = new Player[]{player1, player2};
    }

    /**
     * Plays all rounds of the tournament with specified board size and win condition.
     * Players alternate who goes first in each round. Results are stored internally
     * and can be displayed using {@link #displayResults}.
     *
     * @param size Size of the game board to use for all rounds
     * @param winStreak Number of marks in a row needed to win each game
     * @param playerName1 The name of the first player.
     * @param playerName2 The name of the second player.
     */
    public void playTournament(int size, int winStreak, String playerName1, String playerName2){
        for(int i = 0; i < this.rounds; i++){
            Game game = new Game(this.players[i % NUM_PLAYERS], this.players[(i + 1) % NUM_PLAYERS],
                    size, winStreak, this.renderer);
            int vic = game.run().ordinal();
            if(0 < vic){
                this.result[(i + vic - 1) % NUM_PLAYERS]++;
            }
        }
        this.displayResults(new String[]{playerName1, playerName2}, this.rounds);
    }

    /**
     * Displays the tournament results, showing how many rounds each player won
     * and how many rounds ended in ties.
     *
     * @param names Array of player names [player1 name, player2 name]
     * @param rounds Total number of rounds played
     */
    private void displayResults(String[] names, int rounds) {
        System.out.println(RESULT_HEADLINE);
        for (int i = 0; i < players.length; i++) {
            System.out.printf(RESULT_FORMAT, i + 1, names[i], result[i]);
        }
        System.out.println(String.format(RESULT_TIE, rounds - result[0] - result[1]));
    }

    /**
     * Main entry point for running a tournament from command line arguments.
     * Expected arguments:
     * args[0] - Number of rounds
     * args[1] - Board size
     * args[2] - Win streak (marks in a row needed to win)
     * args[3] - Renderer type
     * args[4] - Player 1 type
     * args[5] - Player 2 type
     *
     * @param args Command line arguments as described above
     */
    public static void main(String[] args) {
        PlayerFactory playerFactory = new PlayerFactory();
        RendererFactory rendererFactory = new RendererFactory();

        int rounds = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);

        Renderer renderer = rendererFactory.buildRenderer(args[3], size);
        Player player1 = playerFactory.buildPlayer(args[4]);
        Player player2 = playerFactory.buildPlayer(args[5]);

        Tournament tournament = new Tournament(rounds, renderer, player1, player2);
        tournament.playTournament(size, winStreak, args[4], args[5]);
    }
}