package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Represents a fruit in the game, including its creation, behavior, and interactions.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Fruit extends GameObject {
    private final static float ENERGY = 10;
    private static final int FADE_IN_OUT_TIME = 0;
    private static final float HALF_FACTOR = 0.5F;
    private static final int APPEAR_FREQUENCY = 30;
    private static final int MARGIN_SIZE = 3;
    private static final int RANDOM_FRUIT = 10;

    /**
     * A consumer to handle the event when the fruit is eaten.
     */
    private final Consumer<Float> onEat;

    /**
     * Indicates whether the fruit has been eaten.
     */
    private boolean eaten;

    /**
     * The size of the fruit.
     */
    public static final int SIZE = 14;

    /**
     * Constructs a fruit object.
     *
     * @param topLeftCorner The top-left corner position of the fruit.
     * @param onEat A consumer to handle the event when the fruit is eaten.
     * @param color The color of the fruit.
     */
    public Fruit(Vector2 topLeftCorner, Consumer<Float> onEat, Color color) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), new OvalRenderable(color));
        this.onEat = onEat;
        this.eaten = false;
        setTag(Tree.TAG);
    }

    /**
     * Creates fruits at the specified position.
     *
     * @param groundX The x-coordinate of the ground.
     * @param sizeTrunk The size of the trunk.
     * @param trunckPos The position of the trunk.
     * @param width The width of the tree.
     * @param onEat A consumer to handle the event when the fruit is eaten.
     * @param seed The seed for the random generator.
     * @return A list of created fruits.
     */
    public static List<GameObject> createFruitAt(float groundX, float sizeTrunk, float trunckPos, float width,
                                                 Consumer<Float> onEat, int seed) {
        Random random = new Random(seed);
        List<GameObject> fruitList = new ArrayList<>();
        float heightLeafs = sizeTrunk * HALF_FACTOR;
        float widthLeafs = width * HALF_FACTOR;

        for (float i = trunckPos + heightLeafs * HALF_FACTOR; i > trunckPos - heightLeafs; i -= SIZE) {
            for (float j = groundX - widthLeafs; j < groundX + widthLeafs; j += SIZE + MARGIN_SIZE) {
                if (random.nextInt(RANDOM_FRUIT) == 0) {
                    fruitList.add(new Fruit(Vector2.of(j, i), onEat,
                            random.nextBoolean() ? Color.RED : Color.YELLOW));
                }
            }
            widthLeafs -= SIZE;
        }
        return fruitList;
    }

    /**
     * Determines whether the fruit should collide with another game object.
     *
     * @param other The other game object.
     * @return True if the fruit should collide with the other game object, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Avatar.AVATAR_TAG);
    }

    /**
     * Handles the event when the fruit collides with another game object.
     *
     * @param other The other game object.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (eaten) {
            return;
        }
        renderer().fadeOut(FADE_IN_OUT_TIME, () -> onEat.accept(ENERGY));
        eaten = true;
        new ScheduledTask(this, APPEAR_FREQUENCY, false,
                () -> renderer().fadeIn(FADE_IN_OUT_TIME, () -> eaten = false));
    }
}