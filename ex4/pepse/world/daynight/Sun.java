package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Manages the sun in the game, including creating the sun object and its movement.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Sun {
    private static final int SUN_SIZE = 100;
    private static final float LOCATION_FACTOR = 0.5f;
    private static final float INIT_FACTOR = 2.0F / 3.0F;
    private static final float START_ANGLE = 0;
    private static final float END_ANGLE = 360;
    private static final String SUN_TAG = "sun";

    /**
     * Default constructor for the Sun class.
     */
    public Sun() {}

    /**
     * Creates a sun game object that moves in a circular path to simulate the sun's movement.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The length of the sun's movement cycle.
     * @return A GameObject representing the sun.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        OvalRenderable sunRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(windowDimensions.mult(LOCATION_FACTOR),
                Vector2.ONES.mult(SUN_SIZE), sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        float groundHeightAtX0 = windowDimensions.y() * INIT_FACTOR;
        Vector2 initialSunCenter = windowDimensions.mult(LOCATION_FACTOR);
        Vector2 cycleCenter = Vector2.of(windowDimensions.x() * LOCATION_FACTOR, groundHeightAtX0);
        new Transition<>(sun,
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter)
                        .rotated(angle)
                        .add(cycleCenter)),
                START_ANGLE,
                END_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_LOOP, null);
        return sun;
    }
}