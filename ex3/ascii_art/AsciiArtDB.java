package ascii_art;

import java.util.LinkedList;

/**
 * The AsciiArtDB class is a singleton that manages the state of the AsciiArtAlgorithm.
 * It allows committing snapshots of the algorithm's state and rolling back to the last saved state.
 *
 *  @author Emmanuelle Schnitzer
 *  @author Amit Moses
 */
public class AsciiArtDB {
    /**
     * The single instance of the AsciiArtDB.
     */
    private static AsciiArtDB instance;

    /**
     * A list of saved snapshots of the AsciiArtAlgorithm's state.
     */
    private final LinkedList<AsciiArtAlgorithm.Snapshot> savedState;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the list of saved snapshots.
     */
    private AsciiArtDB() {
        this.savedState = new LinkedList<>();
    }

    /**
     * Returns the single instance of the AsciiArtDB.
     * If the instance does not exist, it creates a new one.
     *
     * @return the single instance of the AsciiArtDB
     */
    public static AsciiArtDB getInstance() {
        if (instance == null) {
            instance = new AsciiArtDB();
        }
        return instance;
    }

    /**
     * Commits a snapshot of the AsciiArtAlgorithm's state to the database.
     *
     * @param snapshot the snapshot to commit
     */
    public void commit(AsciiArtAlgorithm.Snapshot snapshot) {
        savedState.add(snapshot);
    }

    /**
     * Rolls back to the last saved snapshot of the AsciiArtAlgorithm's state.
     *
     * @return the last saved snapshot, or null if no snapshots are saved
     */
    public AsciiArtAlgorithm.Snapshot rollback() {
        return savedState.isEmpty() ? null : savedState.getLast();
    }
}