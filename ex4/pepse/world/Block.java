package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a block in the game, including its size and immovable properties.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Block extends GameObject {
    /**
     * The size of the block.
     */
    public static final int SIZE = 30;
    /**
     * Tag for identifying the block.
     */
    public static final String BLOCK_TAG = "block";

    /**
     * Constructs a block object.
     *
     * @param topLeftCorner The top-left corner of the block.
     * @param renderable The renderable to use for the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(BLOCK_TAG);
    }
}