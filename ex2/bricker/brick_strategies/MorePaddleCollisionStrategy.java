package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that reveals and positions a center paddle.
 * Implements the CollisionStrategy interface to define custom collision behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class MorePaddleCollisionStrategy implements CollisionStrategy {
    /**
     * The game manager responsible for managing game objects.
     */
    private BrickerGameManager manager;

    /**
     * The next collision strategy to be executed.
     */
    private CollisionStrategy nextStrategy;

    /**
     * Constructs a MorePaddleCollisionStrategy with a game manager and next strategy.
     *
     * @param manager The game manager responsible for managing game objects.
     * @param nextStrategy The next collision strategy to be executed.
     */
    public MorePaddleCollisionStrategy(BrickerGameManager manager, CollisionStrategy nextStrategy) {
        this.manager = manager;
        this.nextStrategy = nextStrategy;
    }

    /**
     * Handles the collision by potentially showing the center paddle.
     *
     * @param thisObj The primary collision object.
     * @param otherObj The secondary collision object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        nextStrategy.onCollision(thisObj, otherObj);
        if (!manager.getCenterPaddle().isVisable()) {
            manager.getCenterPaddle().showPaddle();
        }
    }
}