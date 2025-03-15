package pepse.world;

import danogl.util.Vector2;

/**
 * Represents an object that can move in the game.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public interface MovingObject {
    /**
     * Sets the movement of the object.
     *
     * @param rnd The random value to use for the movement.
     * @param direction The direction to move the object.
     * @param resetLocation The function to call to reset the location of the object.
     */
    public void setMovement(float rnd, Vector2 direction, Runnable resetLocation);

    /**
     * Moves the object to a new position.
     *
     * @param startPosition The starting position of the object.
     */
    public void move(Vector2 startPosition);
}
