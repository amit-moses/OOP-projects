package pepse.world;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;

import java.util.List;

/**
 * Represents a bird in the game, including its movement and behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Bird extends GameObject implements ComplexObject, MovingObject {
    /**
     * The tag for the bird.
     */
    public static final String TAG = "bird";

    private static final float FRAME_DURATION = 0.1F;
    private static final int SPEED = 100;

    private static final String[] BIRD_FLY= new String[]{"assets/bird_1.png","assets/bird_2.png",
            "assets/bird_3.png", "assets/bird_4.png"};
    private static final Vector2 SIZE = Vector2.of(25,30);

    /**
     * Constructs a bird object.
     *
     * @param topLeftCorner The top-left corner of the bird.
     * @param imageReader Reads images for rendering.
     */
    public Bird(Vector2 topLeftCorner, ImageReader imageReader) {
        super(topLeftCorner, SIZE, new AnimationRenderable(BIRD_FLY,
                imageReader,true,FRAME_DURATION));
        renderer().setIsFlippedHorizontally(true);
        setTag(TAG);
    }

    /**
     * Creates a bird game object.
     *
     * @param pos The position of the bird.
     * @param imageReader Reads images for rendering.
     * @return A Bird object.
     */
    public static Bird create(Vector2 pos, ImageReader imageReader){
        return new Bird(pos, imageReader);
    }

    /**
     * Gets the items in the game.
     *
     * @return A list of GameObjects.
     */
    @Override
    public List<GameObject> getItems() {
        return List.of(this);
    }

    /**
     * Sets the movement of the bird.
     *
     * @param rnd The random value to set the movement.
     * @param direction The direction of the movement.
     * @param resetLocation The action to reset the location.
     */
    @Override
    public void setMovement(float rnd, Vector2 direction, Runnable resetLocation) {
        setVelocity(direction.mult(SPEED));
        new Transition<>(this, pos ->
                transform().setVelocityY(pos),
                rnd,
                -rnd,
                Transition.CUBIC_INTERPOLATOR_FLOAT, Math.abs(rnd),
                Transition.TransitionType.TRANSITION_LOOP, resetLocation);
    }

    /**
     * Moves the bird to a new position.
     *
     * @param startPosition The starting position of the bird.
     */
    @Override
    public void move(Vector2 startPosition) {
        setTopLeftCorner(startPosition);
    }

    /**
     * Checks if the bird is within a specified range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return True if the bird is within the range, false otherwise.
     */
    @Override
    public boolean isInSRange(float minX, float maxX) {
        float left = getTopLeftCorner().x();
        float width = getDimensions().x();

        return (minX <= left && left <= maxX) ||
                (minX <= left+width && left+width <= maxX);
    }
}