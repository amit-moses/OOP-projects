package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Manages the night cycle in the game, including creating the night effect and
 * transitioning its opacity.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Night {
    private static final Float MIDNIGHT_OPACITY = 0.65f;
    private static final String NIGHT_TAG = "background color";
    private static final Float INITIAL_TRANSITION_VALUE = 0.F;
    private static final int CYCLE_FACTOR = 2;
    private static final int BLACK = 2;

    /**
     * Default constructor for the Night class.
     */
    public Night(){}

    /**
     * Creates a night effect game object that transitions its opacity to simulate a night cycle.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The length of the night cycle.
     * @return A GameObject representing the night effect.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        RectangleRenderable backgroundObj = new RectangleRenderable(new Color(BLACK));
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, backgroundObj);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<Float>(night,
                night.renderer()::setOpaqueness,
                INITIAL_TRANSITION_VALUE, MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength / CYCLE_FACTOR,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}