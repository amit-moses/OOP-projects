package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Represents the sky in the game, including its creation and behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Sky {
    private static final String TAG = "sky";
    private final static Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private final List<Cloud> clouds = new ArrayList<>();
    private static final float DROP_INITIAL_TRANSITION_VALUE = 1;
    private static final float DROP_FINAL_TRANSITION_VALUE = 0;
    private static final float DROP_TRANSITION_TIME = 1;

    /**
     * Creates a sky game object.
     *
     * @param windowDimensions The dimensions of the window.
     * @return A GameObject representing the sky.
     */
    public static GameObject create(Vector2 windowDimensions){
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(TAG);
        return sky;
    }

    /**
     * Updates the cloud's state, adding or removing it from the list of clouds.
     *
     * @param cloud The cloud to update.
     * @param toAdd Whether to add or remove the cloud.
     */
    public void updateCloud(Cloud cloud, boolean toAdd){
        if (toAdd){
            clouds.add(cloud);
        } else {
            clouds.remove(cloud);
        }
    }

    /**
     * Creates raindrops from the clouds and adds them to the game objects collection.
     *
     * @param deleteDrop A consumer to handle the event when a raindrop is deleted.
     */
    public List<GameObject> rain(Consumer<GameObject> deleteDrop){
        List<GameObject> drops = new ArrayList<>();
        for(Cloud cloud: clouds){
            cloud.rain().forEach(drop -> {
                drops.add(drop);
                new Transition<Float>(drop,
                        drop.renderer()::setOpaqueness,
                        DROP_INITIAL_TRANSITION_VALUE,
                        DROP_FINAL_TRANSITION_VALUE,
                        Transition.CUBIC_INTERPOLATOR_FLOAT,
                        DROP_TRANSITION_TIME,
                        Transition.TransitionType.TRANSITION_ONCE,
                        () -> deleteDrop.accept(drop));
            });

        }
        return drops;
    }
}