package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import bricker.main.Path;
import danogl.GameObject;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.util.Random;

/**
 * A collision strategy that generates two additional balls upon collision.
 * Implements the CollisionStrategy interface to define custom collision behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class MoreBallsCollisionStrategy implements CollisionStrategy {
    /**
     * Multiplier for the size of the new balls.
     */
    private static final float MULT = 0.75F;

    /**
     * Speed of the new balls.
     */
    private static final float BALL_SPEED = 200;

    /**
     * The game manager responsible for managing game objects.
     */
    private BrickerGameManager manager;

    /**
     * Random number generator for creating random velocities.
     */
    private Random rand = new Random();

    /**
     * The next collision strategy to be executed.
     */
    private CollisionStrategy nextStrategy;

    /**
     * Constructs a MoreBallsCollisionStrategy with a game manager and next strategy.
     *
     * @param manager The game manager responsible for managing game objects.
     * @param nextStrategy The next collision strategy to be executed.
     */
    public MoreBallsCollisionStrategy(BrickerGameManager manager, CollisionStrategy nextStrategy) {
        this.manager = manager;
        this.nextStrategy = nextStrategy;
    }

    /**
     * Handles the collision by potentially creating new balls.
     *
     * @param thisObj The primary collision object.
     * @param otherObj The secondary collision object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        nextStrategy.onCollision(thisObj, otherObj);
        makeNewBalls(otherObj, thisObj);
    }

    /**
     * Generates a random velocity vector for a new ball.
     *
     * @return A Vector2 representing a randomized ball velocity.
     */
    private Vector2 getRandDirection() {
        double angle = this.rand.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * BALL_SPEED;
        float velocityY = (float) Math.sin(angle) * BALL_SPEED;
        return new Vector2(velocityX, velocityY);
    }

    /**
     * Creates two new balls at the collision point with random velocities.
     *
     * @param otherObj The object at the collision point.
     * @param thisObj The object initiating the collision.
     */
    private void makeNewBalls(GameObject otherObj, GameObject thisObj) {
        Vector2 place = otherObj.getCenter();
        Renderable ballImage = manager.getImage(Path.PACK_IMAGE, true);
        Sound collisionSound = manager.getSound(Path.BALL_SOUND);
        Ball puck1 = new Ball(Vector2.ZERO, thisObj.getDimensions().mult(MULT),
                ballImage, collisionSound, 1, BALL_SPEED);
        Ball puck2 = new Ball(Vector2.ZERO, thisObj.getDimensions().mult(MULT),
                ballImage, collisionSound, 1, BALL_SPEED);
        puck1.setVelocity(getRandDirection());
        puck2.setVelocity(getRandDirection());
        puck1.setCenter(place);
        puck2.setCenter(place);
        this.manager.addObj(puck1);
        this.manager.addObj(puck2);
    }
}