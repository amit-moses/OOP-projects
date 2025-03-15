package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Defines a strategy for handling collisions between game objects.
 * Allows for different behaviors when game objects collide.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public interface CollisionStrategy {
    /**
     * Defines the action to take when a collision occurs between two game objects.
     *
     * @param thisObj The primary object involved in the collision.
     * @param otherObj The secondary object involved in the collision.
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}