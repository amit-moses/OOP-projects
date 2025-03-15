package pepse.world.trees;

import danogl.GameObject;
import pepse.world.ComplexObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;


/**
 * Represents a tree in the game, including its creation and behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Tree implements ComplexObject {
    /**
     * The tag for the tree.
     */
    public static final String TAG = "tree";

    private static final int MIN_HEIGHT = 100;
    private static final int MAX_HEIGHT = 150;

    /**
     * The items in the tree.
     */
    private final List<GameObject> items;

    /**
     * The maximum x-coordinate of the tree.
     */
    private final float maxRight;

    /**
     * The minimum x-coordinate of the tree.
     */
    private final float maxLeft;

    /**
     * Constructs a tree object.
     *
     * @param x The x-coordinate of the tree.
     * @param y The y-coordinate of the tree.
     * @param onEat A consumer to handle the event when a fruit is eaten.
     * @param width The width of the tree.
     */
    public Tree(float x, float y, Consumer<Float> onEat,  int width) {
        this.maxRight = x + (float) width;
        this.maxLeft = x - (float) width;
        int seed = Objects.hash(x);
        Random random = new Random(seed);
        int height = random.nextInt(MIN_HEIGHT, MAX_HEIGHT);
        items = new ArrayList<>();
        items.addAll(Trunk.createTrunkAt(x, height, y));
        items.addAll(Leaf.createLeafAt(x, height, y- height, width, seed));
        items.addAll(Fruit.createFruitAt(x, height, y- height, width, onEat, seed));
    }

    /**
     * Gets the items of the tree.
     *
     * @return A list of GameObjects representing the items of the tree.
     */
    @Override
    public List<GameObject> getItems() {
        return items;
    }

    /**
     * Checks if the tree is within a specified range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return True if the tree is within the range, false otherwise.
     */
    @Override
    public boolean isInSRange(float minX, float maxX) {
        return (minX <= maxLeft && maxLeft <= maxX) ||
                (minX <= maxRight && maxRight <= maxX);
    }
}
