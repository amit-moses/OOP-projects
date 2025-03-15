package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.*;

import java.util.*;

/**
 * Manages the Pepse game, including initialization and updates.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class PepseGameManager extends GameManager {
    private static final String FONT_TYPE = "Arial";
    private static final int FRAMERATE = 20;
    private static final float FREQUENCY_MOVE = 10F;
    private static final Vector2 TEXT_POSITION = Vector2.ONES.mult(15);
    private static final Vector2 TEXT_SIZE = Vector2.of(50, 20);
    private static final float CAMERA_FACTOR = 0.5f;
    private static final float NIGHT_CYCLE = 30;
    private static final int SEED_NUMBER = 9;
    private static final int CLOUD_ENTRY_POSITION = 100;
    private static final String ENERGY_TEXT = "Energy: %.2f%%";
    private static final int TREE_WIDTH = 10 * Block.SIZE;
    private static final int BIRD_LAYER = Layer.BACKGROUND + 2;
    private static final int CLOUD_LAYER = Layer.BACKGROUND + 1;

    /**
     * Controls the game window, including rendering and user input.
     */
    private WindowController windowController;

    /**
     * List of temporary objects currently in the game.
     */
    private List<ComplexObject> currentObjects;

    /**
     * Manages the terrain generation and properties.
     */
    private Terrain land;

    /**
     * Represents the player's avatar in the game.
     */
    private Avatar avatar;

    /**
     * Manages the flora (trees and plants) in the game.
     */
    private Flora flora;

    /**
     * Controls the camera view in the game.
     */
    private Camera camera;

    /**
     * Reads images for rendering game objects.
     */
    private ImageReader imageReader;

    /**
     * Manages the sky and weather effects in the game.
     */
    private Sky skyManager;

    /**
     * The right boundary of the current game world.
     */
    private int rightBound;

    /**
     * The left boundary of the current game world.
     */
    private int leftBound;



    /**
     * Main method to run the Pepse game.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game, setting up the environment, avatar, and other game objects.
     *
     * @param imageReader     Reads images for rendering.
     * @param soundReader     Reads sounds for playback.
     * @param inputListener   Listens for user input.
     * @param windowController Controls the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(FRAMERATE);

        this.windowController = windowController;
        this.currentObjects = new ArrayList<>();
        this.land = new Terrain(windowController.getWindowDimensions(), SEED_NUMBER);
        this.flora = new Flora(TREE_WIDTH, land::groundHeightAt,
                energy -> avatar.setEnergyState(avatar.getEnergyState() + energy));
        this.leftBound = -getMaxTreesInScreen() * TREE_WIDTH;
        this.rightBound = getMaxTreesInScreen() * TREE_WIDTH;
        this.skyManager = new Sky();
        this.imageReader = imageReader;

        float positionX = windowController.getWindowDimensions().x() * CAMERA_FACTOR;
        Vector2 initialAvatarPosition = Vector2.of(positionX,
                land.groundHeightAt(positionX) - Avatar.SIZE.y());
        this.avatar = new Avatar(initialAvatarPosition, inputListener,
                imageReader, this::onAvatarJump);

        this.camera = new Camera(avatar,
                windowController.getWindowDimensions().mult(CAMERA_FACTOR).subtract(initialAvatarPosition),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions());
        setCamera(camera);

        createBackgroundGame();
        extendWorld(leftBound, rightBound);
        gameObjects().addGameObject(avatar);
    }

    /**
     * Creates the background elements of the game, including the sky, night cycle, sun, and sun halo.
     */
    private void createBackgroundGame() {
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        GameObject night = Night.create(windowController.getWindowDimensions(), NIGHT_CYCLE);
        gameObjects().addGameObject(night, Layer.UI);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), NIGHT_CYCLE);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);
        TextRenderable textRenderable = new TextRenderable(ENERGY_TEXT, FONT_TYPE, true, true);
        GameObject energyText = new GameObject(TEXT_POSITION, TEXT_SIZE, textRenderable);
        gameObjects().addGameObject(energyText, Layer.UI);
        energyText.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        avatar.addComponent(deltaTime ->
                textRenderable.setString(String.format(ENERGY_TEXT, avatar.getEnergyState())));
        createCloud();
        createBird();
    }

    /**
     * Gets the left screen boundary in world coordinates.
     *
     * @return The left screen boundary.
     */
    private float getLeftScreen() {
        return camera.screenToWorldCoords(
                windowController.getWindowDimensions()).x() - windowController.getWindowDimensions().x();
    }

    /**
     * Gets the right screen boundary in world coordinates.
     *
     * @return The right screen boundary.
     */
    private float getRightScreen() {
        return camera.screenToWorldCoords(windowController.getWindowDimensions()).x();
    }

    /**
     * Creates terrain blocks within the specified range.
     *
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     */
    private void createTerrain(float minX, float maxX) {
        land.createInRange((int) minX, (int) maxX).forEach(block ->
                gameObjects().addGameObject(block, Layer.STATIC_OBJECTS));
    }

    /**
     * Creates trees within the specified range.
     *
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     */
    private void createTrees(float minX, float maxX) {
        for (Tree tree : this.flora.createInRange((int) minX, (int) maxX)) {
            currentObjects.add(tree);
            updateComplexObject(tree, true);
        }
    }

    /**
     * Deletes objects that are out of the current screen range.
     */
    private void deleteObjects() {
        // Delete temporary objects that are out of the screen
        List<ComplexObject> toDelete = new ArrayList<>();
        for (ComplexObject obj : currentObjects) {
            if (!obj.isInSRange(leftBound, rightBound)) {
                updateComplexObject(obj, false);
                toDelete.add(obj);
            }
        }
        currentObjects.removeAll(toDelete);

        // Delete blocks that are out of the screen
        List<Block> toDeleteGameObjects = new ArrayList<>();
        for(Block block : land.getLand()){
            if(block.getTopLeftCorner().x() + block.getDimensions().x() < leftBound ||
                    block.getTopLeftCorner().x() > rightBound){
                gameObjects().removeGameObject(block, Layer.STATIC_OBJECTS);
                toDeleteGameObjects.add(block);
            }
        }
        land.removeLand(toDeleteGameObjects);
    }

    /**
     * Extends the world by creating terrain and trees within the specified range.
     *
     * @param start The start x-coordinate.
     * @param end   The end x-coordinate.
     */
    private void extendWorld(int start, int end) {
        createTerrain(start, end);
        createTrees(start, end);
    }

    /**
     * Updates the game state, extending the world and deleting objects as needed.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int range = getMaxTreesInScreen() * TREE_WIDTH;

        if (rightBound <= getRightScreen()) {
            extendWorld(rightBound, rightBound + range);
            rightBound += range;
            leftBound += range;
            deleteObjects();
        }

        if (getLeftScreen() <= leftBound) {
            extendWorld(leftBound - range, leftBound);
            leftBound -= range;
            rightBound -= range;
            deleteObjects();
        }
    }

    /**
     * Gets the maximum number of trees that can fit on the screen.
     *
     * @return The maximum number of trees.
     */
    private int getMaxTreesInScreen() {
        return (int) Math.ceil(windowController.getWindowDimensions().x() / TREE_WIDTH);
    }

    /**
     * Creates a cloud at the left screen boundary.
     */
    private void createCloud() {
        Vector2 posCloud = Vector2.of(getLeftScreen(), Math.min(CLOUD_ENTRY_POSITION,
                land.groundHeightAt(getLeftScreen())));
        Cloud cloud = Cloud.create(posCloud);
        cloud.setMovement(FREQUENCY_MOVE, Vector2.RIGHT, ()->{
            if(!cloud.isInSRange(getLeftScreen(), getRightScreen())){
                Vector2 posAgain = Vector2.of(getLeftScreen(), Math.min(CLOUD_ENTRY_POSITION,
                        land.groundHeightAt(getLeftScreen())));
                cloud.move(posAgain);
            }
        });
        skyManager.updateCloud(cloud, true);
        updateComplexObject(cloud, true);
    }

    /**
     * Creates a bird at the right screen boundary.
     */
    private void createBird() {
        Vector2 posCloud = Vector2.of(getRightScreen(), Math.min(CLOUD_ENTRY_POSITION,
                land.groundHeightAt(getRightScreen())));
        Bird bird = Bird.create(posCloud, imageReader);
        bird.setMovement(FREQUENCY_MOVE, Vector2.LEFT, ()->{
            if(!bird.isInSRange(getLeftScreen(), getRightScreen())){
                Vector2 posAgain = Vector2.of(getRightScreen(), Math.min(CLOUD_ENTRY_POSITION,
                        land.groundHeightAt(getRightScreen())));
                bird.move(posAgain);
            }
        });
        updateComplexObject(bird, true);
    }

    /**
     * Executes the action when the avatar jumps.
     */
    private void onAvatarJump() {
        List<GameObject> drops = skyManager.rain(drop ->
                gameObjects().removeGameObject(drop, Layer.BACKGROUND));
        drops.forEach(drop -> gameObjects().addGameObject(drop, Layer.BACKGROUND));
    }

    /**
     * Updates the complex object in the game.
     *
     * @param tmp The temporary object to update.
     * @param toAdd True if the object should be added, false if it should be removed.
     */
    private void updateComplexObject(ComplexObject tmp, boolean toAdd) {
        if(toAdd){
            tmp.getItems().forEach(item ->
                    gameObjects().addGameObject(item, getLayer(item)));
        } else {
            tmp.getItems().forEach(item ->
                    gameObjects().removeGameObject(item, getLayer(item)));
        }
    }

    /**
     * Gets the layer of the game object.
     *
     * @param object The game object.
     * @return The layer of the game object.
     */
    private int getLayer(GameObject object) {
        return switch (object.getTag()) {
            case Bird.TAG -> BIRD_LAYER;
            case Cloud.TAG -> CLOUD_LAYER;
            case Block.BLOCK_TAG -> Layer.STATIC_OBJECTS;
            default -> Layer.DEFAULT;
        };
    }
}