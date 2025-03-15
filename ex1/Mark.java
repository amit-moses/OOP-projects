/**
 * Enumeration representing possible states of a board position.
 * Represents the three possible states in the game:
 * - BLANK: Empty position
 * - X: X player's mark
 * - O: O player's mark
 *
 * @author Amit Moses
 */
enum Mark{
    /** Empty position, X player's mark, O player's mark */
    BLANK, X, O;

    /**
     * Converts mark to string representation.
     * BLANK returns null, others return their name.
     *
     * @return String representation of mark, or null for BLANK
     */
    @Override
    public String toString(){
        if (this == Mark.BLANK){
            return null;
        }
        return this.name();
    }
}