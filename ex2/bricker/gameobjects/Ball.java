package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball game object with collision and movement capabilities.
 * Extends GameObject to provide basic game object functionality.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Ball extends GameObject {
    /**
     * Counter for resetting the ball's state.
     */
    private int resetCounter;

    /**
     * Counter for tracking collisions.
     */
    private int collisionCounter;

    /**
     * The visual representation of the ball.
     */
    private Renderable imgBall;

    /**
     * The sound played on collision.
     */
    private final Sound collisionSound;

    /**
     * Factor by which the ball's speed is multiplied.
     */
    private float speedFactor;

    /**
     * The current speed of the ball.
     */
    private float speed;

    /**
     * Constructs a new Ball with specified properties.
     *
     * @param topLeftCorner Initial position of the ball.
     * @param dimensions Size of the ball.
     * @param renderable Visual representation of the ball.
     * @param collisionSound Sound played on collision.
     * @param speedFactor Initial speed factor of the ball.
     * @param speed Initial speed of the ball.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound, float speedFactor, float speed) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.speedFactor = speedFactor;
        this.imgBall = renderable;
        this.speed = speed;

        this.collisionCounter = 0;
        this.resetCounter = -1;
    }

    /**
     * Gets the current speed of the ball.
     *
     * @return The current speed of the ball.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Increases the ball's speed by the speed factor.
     */
    public void applySpeed() {
        setVelocity(getVelocity().mult(speedFactor));
        speed *= speedFactor;
    }

    /**
     * Decreases the ball's speed by the speed factor.
     */
    public void unApplySpeed() {
        setVelocity(getVelocity().mult(1 / speedFactor));
        speed /= speedFactor;
    }

    /**
     * Sets the reset counter for the ball.
     *
     * @param resetCounter The reset counter value.
     */
    public void setResetCounter(int resetCounter) {
        this.resetCounter = resetCounter;
    }

    /**
     * Gets the current collision counter value.
     *
     * @return The current collision counter value.
     */
    public int getCollisionCounter() {
        return this.collisionCounter;
    }

    /**
     * Gets the reset counter value.
     *
     * @return The reset counter value.
     */
    public int getResetCounter() {
        return resetCounter;
    }

    /**
     * Resets the ball's state, including its speed and visual representation.
     */
    private void onReset() {
        this.renderer().setRenderable(imgBall);
        unApplySpeed();
    }

    /**
     * Handles ball collision behavior.
     * Reverses ball velocity and plays collision sound.
     * Ignores collisions with other Ball objects.
     *
     * @param other Colliding game object.
     * @param collision Collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        // Call the parent method to handle the collision.
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball) {
            return;
        }

        if (collisionCounter == resetCounter) {
            onReset();
        }

        // Reverse the velocity of the ball.
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionCounter++;
    }
}