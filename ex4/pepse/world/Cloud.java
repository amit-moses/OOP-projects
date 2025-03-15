package pepse.world;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Represents a cloud in the game, including its creation, movement, and behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Cloud implements ComplexObject, MovingObject {
    /**
     * The tag for the cloud.
     */
    public static final String TAG = "cloud";


    private static final Random random = new Random();
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);

    private static final int BLOCK_SIZE = 20;
    private static final int SPEED = 100;
    private static final List<List<Integer>> CLOUD_POS1 = List.of(
            List.of(0, 1, 1, 0, 0, 0),
            List.of(1, 1, 1, 0, 1, 0),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0)
    );

    private static final int TOP_LEFT = 2;
    private static final int DROP_SIZE = 8;
    private static final float DROP_GRAVITY = 600;

    /**
     * The list of blocks that make up the cloud.
     */
    private final List<GameObject> blocks = new ArrayList<>();

    /**
     * The width of the cloud.
     */
    public static final int WIDTH = CLOUD_POS1.get(0).size() * BLOCK_SIZE;
    /**
     * Constructs a cloud object.
     *
     * @param startPosition The starting position of the cloud.
     */
    public Cloud(Vector2 startPosition) {
        createCloud(startPosition);
    }

    /**
     * Creates a cloud object.
     *
     * @param startPosition The starting position of the cloud.
     * @return A Cloud object.
     */
    public static Cloud create(Vector2 startPosition) {
        return new Cloud(startPosition);
    }


    /**
     * Sets the movement of a block in the cloud.
     *
     * @param rnd The random velocity for the block.
     * @
     */
    @Override
    public void setMovement(float rnd, Vector2 direction, Runnable resetLocation) {
        for (GameObject block : blocks) {
            block.setVelocity(direction.mult(SPEED));
            new Transition<>(block, pos ->
                    block.transform().setVelocityY(pos),
                    rnd,
                    -rnd,
                    Transition.CUBIC_INTERPOLATOR_FLOAT, Math.abs(rnd),
                    Transition.TransitionType.TRANSITION_LOOP,
                    resetLocation);
        }
    }

    /**
     * Moves the cloud to a new position.
     *
     * @param startPosition The starting position of the cloud.
     */
    @Override
    public void move(Vector2 startPosition) {
        startPosition = startPosition.subtract(Vector2.of(WIDTH, 0));
        int m = 0;
        for (int i = 0; i < CLOUD_POS1.size(); i++) {
            for (int j = 0; j < CLOUD_POS1.get(i).size(); j++) {
                if (CLOUD_POS1.get(i).get(j) == 1) {
                    blocks.get(m).setTopLeftCorner(startPosition.add(Vector2.of(j * BLOCK_SIZE,
                            i * BLOCK_SIZE)));
                    m++;
                }
            }
        }
    }



    /**
     * Creates a raindrop game object.
     *
     * @param vecPos The position of the raindrop.
     * @return A GameObject representing the raindrop.
     */
    private GameObject makeDrop(Vector2 vecPos) {
        OvalRenderable dropRenderable = new OvalRenderable(Color.BLUE);
        GameObject drop = new GameObject(vecPos,
                Vector2.ONES.mult(DROP_SIZE), dropRenderable);
        drop.transform().setAccelerationY(DROP_GRAVITY);
        drop.setVelocity(Vector2.RIGHT.mult(SPEED));
        return drop;
    }

    /**
     * Creates raindrops from the cloud.
     *
     * @return A list of raindrop game objects.
     */
    public List<GameObject> rain() {
        List<GameObject> drops = new ArrayList<>();
        for (GameObject block : blocks) {
            if (random.nextBoolean()) {
                drops.add(makeDrop(block.getCenter()));
            }
        }
        return drops;
    }

    /**
     * Creates a block in the cloud.
     *
     * @param blockPosition The position of the block.
     */
    private void createBlock(Vector2 blockPosition) {
        Block block = new Block(blockPosition,
                new RectangleRenderable(ColorSupplier.approximateMonoColor(
                        BASE_CLOUD_COLOR))
        );
        block.setTag(TAG);
        blocks.add(block);
    }

    /**
     * Creates the cloud at the specified starting position.
     *
     * @param startPosition The starting position of the cloud.
     */
    private void createCloud(Vector2 startPosition) {
        startPosition = startPosition.subtract(Vector2.of(WIDTH, 0));
        for (int i = 0; i < CLOUD_POS1.size(); i++) {
            for (int j = 0; j < CLOUD_POS1.get(i).size(); j++) {
                if (CLOUD_POS1.get(i).get(j) == 1) {
                    createBlock(startPosition.add(Vector2.of(j * BLOCK_SIZE,
                            i * BLOCK_SIZE)));
                }
            }
        }
    }

    /**
     * Gets the blocks that make up the cloud.
     *
     * @return A list of GameObjects representing the blocks.
     */
    @Override
    public List<GameObject> getItems() {
        return blocks;
    }

    /**
     * Checks if the cloud is within a specified range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return True if the cloud is within the range, false otherwise.
     */
    @Override
    public boolean isInSRange(float minX, float maxX) {
        if (blocks.isEmpty()) {
            return false;
        }

        float left = blocks.get(TOP_LEFT).getTopLeftCorner().x();
        return (minX <= left && left <= maxX) ||
                (minX <= left+WIDTH && left+WIDTH <= maxX);
    }
}