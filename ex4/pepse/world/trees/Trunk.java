package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.List;

/**
 * Represents the trunk of a tree in the game, including its creation and behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Trunk extends Block {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);

    /**
     * Default constructor for the Trunk class.
     */
    public Trunk(Vector2 topLeftCorner) {
        super(topLeftCorner, new RectangleRenderable(TRUNK_COLOR));
    }

    /**
     * Creates a trunk game object at the specified position and height.
     *
     * @param i The x-coordinate of the trunk.
     * @param height The height of the trunk.
     * @param groundHeight The height of the ground.
     * @return A list of GameObjects representing the trunk.
     */
    public static List<GameObject> createTrunkAt(float i,int height, float groundHeight){
        Trunk trunk = new Trunk(Vector2.ZERO);
        trunk.setDimensions(Vector2.of(Block.SIZE, height));
        trunk.setTopLeftCorner(Vector2.of(i, groundHeight-height));
        return List.of(trunk);
    }
}
