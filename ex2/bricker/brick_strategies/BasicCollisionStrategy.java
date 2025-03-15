package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A basic collision strategy that simply removes the colliding object from the game.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    /**
     * The game manager responsible for managing game objects.
     */
    private BrickerGameManager manager;

    /**
     * Constructs a BasicCollisionStrategy with a game manager.
     *
     * @param manager The game manager responsible for managing game objects.
     */
    public BasicCollisionStrategy(BrickerGameManager manager) {
        this.manager = manager;
    }

    /**
     * Removes the other object from the game upon collision.
     *
     * @param thisObj The primary object involved in the collision.
     * @param otherObj The object to be removed from the game.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(this.manager.removeObj(otherObj)){
            manager.incrementCountCollision();
        }

    }
}