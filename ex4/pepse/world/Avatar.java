package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

/**
 * Represents the player's avatar in the game,
 * including movement, energy management, and animations.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Avatar extends GameObject {
    private static final float VELOCITY_X = 250;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 250;
    private static final String[] inPlace= new String[]{"assets/idle_0.png","assets/idle_1.png",
            "assets/idle_2.png","assets/idle_3.png"};
    private static final String[] moving = new String[]{"assets/run_0.png","assets/run_1.png",
            "assets/run_2.png","assets/run_3.png", "assets/run_4.png","assets/run_5.png"};
    private static final String[] jumping = new String[]{"assets/jump_0.png","assets/jump_1.png",
            "assets/jump_2.png","assets/jump_3.png"};
    private static final float FRAME_DURATION = 0.1F;
    private static final String AVATAR_IMAGE = "assets/idle_0.png";
    private static final int MAX_ENERGY = 100;
    private static final float WALK_ENERGY = 0.5f;
    private static final int JUMP_ENERGY = 10;
    private static final float ENERGY_RELAX = 1f;

    /**
     * The current energy state of the avatar.
     */
    private float energyState;
    /**
     * Runnable to execute when the avatar jumps.
     */
    private final Runnable onJump;
    /**
     * Listens for user input.
     */
    private final UserInputListener inputListener;
    /**
     * Reads images for rendering.
     */
    private final ImageReader imageReader;

    /**
     * Tag for identifying the avatar.
     */
    public static final String AVATAR_TAG = "avatar";
    /**
     * The size of the avatar.
     */
    public static final Vector2 SIZE = Vector2.of(55,65);

    /**
     * Constructs an avatar object.
     *
     * @param topLeftCorner The top-left corner of the avatar.
     * @param inputListener Listens for user input.
     * @param imageReader Reads images for rendering.
     * @param onJump Runnable to execute when the avatar jumps.
     */
    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader,
                  Runnable onJump) {
        super(topLeftCorner, SIZE, imageReader.readImage(AVATAR_IMAGE,true));
        this.energyState = MAX_ENERGY;
        this.imageReader = imageReader;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        setTag(AVATAR_TAG);
        this.onJump = onJump;
    }

    /**
     * Gets the current energy state of the avatar.
     *
     * @return The current energy state.
     */
    public float getEnergyState() {
        return energyState;
    }

    /**
     * Sets the energy state of the avatar.
     *
     * @param energyState The new energy state.
     */
    public void setEnergyState(float energyState) {
        this.energyState = Math.min(MAX_ENERGY, Math.max(0, energyState));
    }

    /**
     * Updates the avatar's state, including movement, energy consumption, and animations.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            renderer().setIsFlippedHorizontally(true);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            renderer().setIsFlippedHorizontally(false);
        }
        if(WALK_ENERGY <= energyState && xVel != 0){
            setEnergyState(energyState - WALK_ENERGY);
            renderer().setRenderable(new AnimationRenderable(moving,imageReader,true,FRAME_DURATION));
        }

        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && transform().getVelocity().y() == 0
                && JUMP_ENERGY <= energyState){
            transform().setVelocityY(VELOCITY_Y);
            setEnergyState(energyState - JUMP_ENERGY);
            renderer().setRenderable(new AnimationRenderable(jumping,imageReader,true,FRAME_DURATION));
            onJump.run();
        }
        if(getVelocity().isZero()){
            renderer().setRenderable(new AnimationRenderable(inPlace,imageReader,true,FRAME_DURATION));
            setEnergyState(energyState + ENERGY_RELAX);
        }
        if (energyState < Math.min(JUMP_ENERGY, WALK_ENERGY)){
            transform().setVelocityX(0);
            renderer().setRenderable(new AnimationRenderable(inPlace,imageReader,true,FRAME_DURATION));

        }
    }

    /**
     * Handles collision events for the avatar.
     *
     * @param other The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision){
        super.onCollisionEnter(other,collision);
        if(other.getTag().equals(Block.BLOCK_TAG)){
            this.transform().setVelocityY(0);
        }

    }
}