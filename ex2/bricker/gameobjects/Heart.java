package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a heart object in the Bricker game.
 * Extends GameObject to provide collision handling and rendering functionality.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Heart extends GameObject {
    /**
     * LifePanel instance to manage the number of lives.
     */
    private LifePanel lifeGraphic;

    /**
     * Paddle instance representing the main paddle.
     */
    private Paddle mainPaddle;

    /**
     * Collection of game objects.
     */
    private GameObjectCollection collection;

    /**
     * Constructs a new Heart instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param lifeGraphic   LifePanel instance to manage the number of lives.
     * @param mainPaddle    Paddle instance representing the main paddle.
     * @param collection    Collection of game objects.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, LifePanel lifeGraphic, Paddle mainPaddle, GameObjectCollection collection) {
        super(topLeftCorner, dimensions, renderable);
        this.lifeGraphic = lifeGraphic;
        this.mainPaddle = mainPaddle;
        this.collection = collection;
    }

    /**
     * Handles the collision event when the heart collides with another game object.
     * Adds a new heart to the life panel and removes the heart object from the game.
     *
     * @param other      The other game object involved in the collision.
     * @param collision  The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.lifeGraphic.addNewHeart();
        collection.removeGameObject(this);
    }

    /**
     * Determines whether the heart should collide with another game object.
     * Only collides with the main paddle.
     *
     * @param other The other game object to check for collision.
     * @return True if the heart should collide with the other game object, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.equals(mainPaddle);
    }
}