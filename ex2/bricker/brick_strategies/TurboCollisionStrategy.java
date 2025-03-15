package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Path;
import danogl.GameObject;

/**
 * A collision strategy that activates a turbo mode for the main ball.
 * Implements the CollisionStrategy interface to define custom collision behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class TurboCollisionStrategy implements CollisionStrategy {
    /**
     * The number of collisions required to activate turbo mode.
     */
    private static final int TURBO_COUNT = 6;

    /**
     * The game manager responsible for managing game objects.
     */
    private BrickerGameManager manager;

    /**
     * The next collision strategy to be executed.
     */
    private CollisionStrategy nextStrategy;

    /**
     * Constructs a TurboCollisionStrategy with a game manager and next strategy.
     *
     * @param manager The game manager responsible for managing game objects.
     * @param nextStrategy The next collision strategy to be executed.
     */
    public TurboCollisionStrategy(BrickerGameManager manager, CollisionStrategy nextStrategy) {
        this.manager = manager;
        this.nextStrategy = nextStrategy;
    }

    /**
     * Sets the turbo mode for the main ball if the collision count condition is met.
     */
    private void setTurbo() {
        boolean isTurbo = manager.getMainBall().getCollisionCounter() <= manager.getMainBall().getResetCounter();
        if (isTurbo) {
            return;
        }

        manager.getMainBall().setResetCounter(manager.getMainBall().getCollisionCounter() + TURBO_COUNT);
        manager.getMainBall().renderer().setRenderable(manager.getImage(Path.TURBO_IMAGE, true));
        manager.getMainBall().applySpeed();
    }

    /**
     * Handles the collision by potentially activating turbo mode for the main ball.
     *
     * @param thisObj The primary collision object.
     * @param otherObj The secondary collision object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        nextStrategy.onCollision(thisObj, otherObj);
        setTurbo();
    }
}