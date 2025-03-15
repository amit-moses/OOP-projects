package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

import java.util.Random;

/**
 * A complex collision strategy that combines multiple collision behaviors.
 * Randomly selects and applies 2-3 different strategies upon collision.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class DoubleBehaviorCollisionStrategy implements CollisionStrategy {
    /**
     * The next collision strategy to be executed.
     */
    private CollisionStrategy nextStrategy;

    /**
     * The game manager responsible for managing game objects.
     */
    private BrickerGameManager manager;

    /**
     * Random number generator for selecting behaviors.
     */
    private Random rand = new Random();

    /**
     * Constructs a DoubleBehaviorCollisionStrategy and initializes random behaviors.
     *
     * @param manager The game manager responsible for game object management.
     */
    public DoubleBehaviorCollisionStrategy(BrickerGameManager manager) {
        this.manager = manager;
        setBehaviors();
    }

    /**
     * Sets random collision behaviors for the strategy.
     */
    private void setBehaviors() {
        int randomNum2 = rand.nextInt(1, FactoryStrategies.getStrategyNum());
        CollisionStrategy behavior1 = FactoryStrategies.createCollisionStrategy(randomNum2, manager);
        int randomNum1 = rand.nextInt(FactoryStrategies.getStrategyNum());
        if (randomNum1 == 0) {
            int randomNum3 = rand.nextInt(1, FactoryStrategies.getStrategyNum());
            int randomNum4 = rand.nextInt(1, FactoryStrategies.getStrategyNum());
            CollisionStrategy behavior2 = FactoryStrategies.createCollisionStrategy(randomNum3, manager, behavior1);
            this.nextStrategy = FactoryStrategies.createCollisionStrategy(randomNum4, manager, behavior2);
        } else {
            this.nextStrategy = FactoryStrategies.createCollisionStrategy(randomNum1, manager, behavior1);
        }
    }

    /**
     * Applies multiple collision strategies and removes the colliding object.
     *
     * @param thisObj The primary object involved in the collision.
     * @param otherObj The object to be removed after applying behaviors.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        nextStrategy.onCollision(thisObj, otherObj);
    }
}