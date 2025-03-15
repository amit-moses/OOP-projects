package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Vector2;

/**
 * A collision strategy that creates a heart when a collision occurs.
 * Optionally deletes the object that triggered the collision.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class AddLifeCollisionStrategy implements CollisionStrategy {
    /**
     * The game manager that handles game state and objects.
     */
    private BrickerGameManager manager;

    /**
     * The speed at which the heart falls.
     */
    private static final float HEART_SPEED = 100;

    /**
     * The next collision strategy to be executed.
     */
    private CollisionStrategy nextStrategy;

    /**
     * Constructs a new AddLifeCollisionStrategy.
     *
     * @param manager The game manager that handles game state and objects.
     * @param nextStrategy The next collision strategy to be executed.
     */
    public AddLifeCollisionStrategy(BrickerGameManager manager, CollisionStrategy nextStrategy) {
        this.manager = manager;
        this.nextStrategy = nextStrategy;
    }

    /**
     * Creates a new heart at the specified location and adds it to the game.
     *
     * @param place The location where the heart should be created.
     */
    private void makeNewHeart(Vector2 place) {
        Heart heart = manager.getNewHeart();
        heart.setVelocity(Vector2.DOWN.mult(HEART_SPEED));
        heart.setCenter(place);
        manager.addObj(heart);
    }

    /**
     * Handles the collision by potentially removing the object and creating a heart.
     *
     * @param thisObj The primary object involved in the collision.
     * @param otherObj The object to potentially remove and replace with a heart.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        nextStrategy.onCollision(thisObj, otherObj);
        makeNewHeart(otherObj.getCenter());
    }
}