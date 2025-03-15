package pepse.world;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the terrain in the game, including its creation and behavior.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int BOTTOM_MARGIN = 8;
    private static final float SCREEN_HEIGHT_FRACTION = 2.0F / 3.0F;
    private static final int NOISE_FACTOR = 240;
    /**
     * The dimensions of the window.
     */
    private final Vector2 windowDimensions;
    /**
     * The ground height at x = 0.
     */
    private final float groundHeightAtX0;
    /**
     * The noise generator for terrain height.
     */
    private final NoiseGenerator noiseGenerator;
    /**
     * The list of blocks that make up the land.
     */
    private final List<Block> land;

    /**
     * Constructs a terrain object.
     *
     * @param windowDimensions The dimensions of the window.
     * @param seed The seed for the noise generator.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = windowDimensions.y() * SCREEN_HEIGHT_FRACTION;
        this.noiseGenerator = new NoiseGenerator(seed, (int)groundHeightAtX0);
        this.land = new ArrayList<>();
    }

    /**
     * Gets the ground height at the specified x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The ground height at the specified x-coordinate.
     */
    public float groundHeightAt(float x) {
        return (float) (groundHeightAtX0 + noiseGenerator.noise(x, NOISE_FACTOR));
    }

    /**
     * Gets the list of blocks that make up the land.
     *
     * @return The list of blocks.
     */
    public List<Block> getLand() {
        return land;
    }

    /**
     * Removes the specified blocks from the land.
     *
     * @param blocks The blocks to remove.
     */
    public void removeLand(List<Block> blocks) {
        land.removeAll(blocks);
    }

    /**
     * Creates terrain blocks within the specified range.
     *
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return The list of created blocks.
     */
    public List<Block> createInRange(int minX, int maxX) {
        minX -= (minX % Block.SIZE);
        List<Block> blocks = new ArrayList<>();
        for (int x = minX; x < maxX; x += Block.SIZE) {
            float groundHeight = groundHeightAt(x); // Random height within [0, maxHeight)
            float maxHeight = windowDimensions.y() + BOTTOM_MARGIN * Block.SIZE;
            for (float y = groundHeight; y < maxHeight; y += Block.SIZE) {
                Block newBlock = new Block(Vector2.of(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                blocks.add(newBlock);
                land.add(newBlock);
            }
        }
        return blocks;
    }
}