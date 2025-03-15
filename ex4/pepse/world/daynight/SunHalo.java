package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Manages the sun halo in the game, including creating the sun halo object and its behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class SunHalo {
    private static final Color SuN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int SUN_HALO_SIZE = 200;
    private static final String SUN_HALO_TAG = "sunHalo";

    /**
     * Default constructor for the SunHalo class.
     */
    public SunHalo(){}

    /**
     * Creates a sun halo game object that follows the sun's position.
     *
     * @param sun The sun game object to follow.
     * @return A GameObject representing the sun halo.
     */
    public static GameObject create(GameObject sun){
        OvalRenderable sunRenderable = new OvalRenderable(SuN_HALO_COLOR);
        GameObject sunHalo = new GameObject(Vector2.ZERO, Vector2.ONES.mult(SUN_HALO_SIZE), sunRenderable);
        sunHalo.setCenter(sun.getCenter());
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}