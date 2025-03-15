package pepse.world;

import danogl.GameObject;

import java.util.List;

/**
 * Interface for temporary objects that can be added or removed from the game objects collection.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public interface ComplexObject {
    /**
     * Gets the items in the temporary object.
     *
     * @return A list of items in the temporary object.
     */
    List<GameObject> getItems();

    /**
     * Checks if the temporary object is within a specified range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return True if the temporary object is within the range, false otherwise.
     */
    boolean isInSRange(float minX, float maxX);
}