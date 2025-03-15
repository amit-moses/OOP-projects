package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a brick game object with custom collision strategies.
 * Extends GameObject to provide brick-specific collision behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Brick extends GameObject {
    /**
     * The collision strategy to be executed when the brick collides with another object.
     */
    private CollisionStrategy collisionStrategy;

    /**
     * Constructs a new Brick with specified properties and collision strategy.
     *
     * @param topLeftCorner Initial position of the brick.
     * @param dimensions Size of the brick.
     * @param renderable Visual representation of the brick.
     * @param collisionStrategy Strategy to execute on collision.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Determines if the brick can collide with another game object.
     * Only allows collisions with Ball objects.
     *
     * @param other Potential colliding object.
     * @return True if the object is a Ball, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Ball;
    }

    /**
     * Triggers the brick's collision strategy when a Ball collides.
     * Calls the specific collision strategy's onCollision method.
     *
     * @param other Colliding game object.
     * @param collision Collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.collisionStrategy.onCollision(other, this);
    }
}