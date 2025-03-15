package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a temporary paddle with limited visibility and collision tracking.
 * Extends Paddle to provide additional game-specific functionality.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class TempPaddle extends Paddle {
    private Vector2 windowCenter;
    /**
     * Counter for tracking collisions.
     */
    private int collisionCounter;

    /**
     * Number of collisions until the paddle will be hidden.
     */
    private int collisionToHide;

    /**
     * Visibility status of the paddle.
     */
    private boolean isVisable;

    /**
     * Collection of game objects for paddle management.
     */
    private GameObjectCollection collection;

    /**
     * Constructs a new TempPaddle with specified properties.
     *
     * @param topLeftCorner Initial position of the paddle.
     * @param dimensions Size of the paddle.
     * @param renderable Visual representation of the paddle.
     * @param inputListener Handles user input for paddle movement.
     * @param borderRight Right movement boundary.
     * @param borderLeft Left movement boundary.
     * @param movementSpeed Speed of paddle.
     * @param collection Game object collection for paddle management.
     * @param collisionToHide Number of collisions until the paddle will be hidden.
     */
    public TempPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      UserInputListener inputListener, float borderRight, float borderLeft,
                      float movementSpeed, GameObjectCollection collection, int collisionToHide, Vector2 windowCenter) {
        super(topLeftCorner, dimensions, renderable, inputListener, borderRight, borderLeft, movementSpeed);
        this.collisionCounter = 0;
        this.isVisable = false;
        this.collection = collection;
        this.collisionToHide = collisionToHide;
        this.windowCenter = windowCenter;
    }

    /**
     * Checks if the paddle is currently visible.
     *
     * @return True if paddle is visible, false otherwise.
     */
    public boolean isVisable() {
        return this.isVisable;
    }

    /**
     * Hides the paddle and removes it from the game collection.
     */
    public void hidePaddle() {
        this.isVisable = false;
        this.collection.removeGameObject(this);
    }

    /**
     * Makes the paddle visible, resets collision counter,
     * and adds it back to the game collection.
     */
    public void showPaddle() {
        this.isVisable = true;
        setCenter(windowCenter);
        this.collisionCounter = 0;
        this.collection.addGameObject(this);
    }

    /**
     * Tracks ball collisions and manages paddle visibility.
     * Hides paddle after collisionToHide ball collisions.
     *
     * @param other Colliding game object.
     * @param collision Collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.collisionCounter++;
        if (collisionCounter >= collisionToHide) {
            this.hidePaddle();
        }
    }
}