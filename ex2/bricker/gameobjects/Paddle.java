package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents the paddle in the Bricker game.
 * Extends GameObject to provide movement and rendering functionality.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Paddle extends GameObject {
    /**
     * Listener for user input to control the paddle.
     */
    private final UserInputListener inputListener;

    /**
     * Speed of the paddle's movement.
     */
    private float movementSpeed;

    /**
     * Right boundary for paddle movement.
     */
    private float borderRight;

    /**
     * Left boundary for paddle movement.
     */
    private float borderLeft;

    /**
     * Constructs a new Paddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener Listener for user input to control the paddle.
     * @param borderRight   Right boundary for paddle movement.
     * @param borderLeft    Left boundary for paddle movement.
     * @param movementSpeed Speed of paddle.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, float borderRight, float borderLeft, float movementSpeed) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.borderRight = borderRight;
        this.borderLeft = borderLeft;
        this.movementSpeed = movementSpeed;
    }

    /**
     * Updates the paddle's position based on user input and ensures it stays within boundaries.
     *
     * @param deltaTime Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movement = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movement = movement.add(Vector2.RIGHT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movement = movement.add(Vector2.LEFT);
        }
        if (getTopLeftCorner().x() <= borderLeft) {
            movement = movement.add(Vector2.RIGHT);
        }
        if (getTopLeftCorner().x() >= borderRight) {
            movement = movement.add(Vector2.LEFT);
        }
        setVelocity(movement.mult(movementSpeed));
    }
}