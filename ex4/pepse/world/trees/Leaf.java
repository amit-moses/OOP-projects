package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a block in the game, including its size and immovable properties.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Leaf extends GameObject {
    private static final int SIZE = 14;
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final float LEAF_ROTATION = 0.1F;
    private static final float HALF_FACTOR = 0.5F;
    private static final int SECONDS_FACTOR = 10;
    private static final int LEAF_MARGIN = 3;
    private static final float WIND_MOVEMENT = 0.9F;
    private static final int WIND_TRANSITION_TIME = 2;
    private static final float FINAL_ANGLE = 60F;


    /**
     * Constructs a leaf object.
     *
     * @param topLeftCorner The top-left corner position of the leaf.
     * @param renderable The renderable of the leaf.
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        setTag(Tree.TAG);
    }

    /**
     * set the movement of leaf.
     *
     * @param leaf The position of the leaf.
     */
    private static void danceLeaf(Leaf leaf){
        new Transition<Float>(leaf, angle -> leaf.renderer().setRenderableAngle(angle), 0F,
                FINAL_ANGLE,Transition.LINEAR_INTERPOLATOR_FLOAT, WIND_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new Transition<Vector2>(leaf, leaf::setDimensions, Vector2.ONES.mult(SIZE),
                Vector2.ONES.mult(SIZE).mult(WIND_MOVEMENT) ,
                Transition.LINEAR_INTERPOLATOR_VECTOR,WIND_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Creates a leaf game object.
     *
     * @param groundX The x position of the ground.
     * @param sizeTrunk The size of the trunk.
     * @param trunckPos The position of the trunk.
     * @param width The width of the leaf.
     * @param seed The seed for random generation.
     * @return A list of leaf objects.
     */
    public static List<GameObject> createLeafAt(float groundX, float sizeTrunk,
                                                float trunckPos, float width, int seed){
        List<GameObject> leafList = new ArrayList<>();
        float heightLeafs = sizeTrunk * HALF_FACTOR;
        float widthLeafs = width * HALF_FACTOR;
        Random random = new Random(seed);
        for (float i = trunckPos + heightLeafs*HALF_FACTOR ; i > trunckPos - heightLeafs; i-=SIZE){
           for(float j = groundX - widthLeafs; j < groundX + widthLeafs; j+=SIZE+LEAF_MARGIN){
               if(1 < random.nextInt(LEAF_MARGIN)){
                   continue;
               }

               Vector2 leafPosition = Vector2.of(j, i);
               Leaf leaf = new Leaf(leafPosition, new RectangleRenderable(LEAF_COLOR));
               float seconds = random.nextInt(SECONDS_FACTOR) *LEAF_ROTATION;
               new ScheduledTask(leaf, seconds, false, () -> danceLeaf(leaf));

               leafList.add(leaf);
           }
            widthLeafs -= SIZE;
        }

        return leafList;
    }
}
