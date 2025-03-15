package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Manages the Bricker game, including initialization, game objects, and game logic.
 * Extends GameManager to provide core game management functionality.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class BrickerGameManager extends GameManager {
    // Constants for game configuration
    private static final int DEFAULT_LIVES = 3;
    private static final int MAX_LIVES = 4;
    private static final int BALL_SPEED = 200;
    private static final int DEFAULT_ROWS = 7;
    private static final int DEFAULT_COLS = 8;
    private static final int WIDTH_BORDER = 10;
    private static final int HEIGHT_BRICK = 15;
    private static final int MARGIN = 5;
    private static final int PADDLE_PADDING = 30;
    private static final int STRATEGIES_RANGE = 10;
    private static final int HEART_SCALE = 15;
    private static final int TEMP_PADDLE_COUNT = 4;
    private static final float SPEED_FACTOR = 1.4F;
    private static final float CENTER_FACTOR = 0.5f;
    private static final float PADDLE_SPEED = 300;

    private static final Vector2 WINDOW_SCALE = new Vector2(700, 500);
    private static final Vector2 HEART_SIZE = new Vector2(15, 15);
    private static final Vector2 BALL_SIZE = new Vector2(30, 30);
    private static final Vector2 PADDLE_SIZE = new Vector2(200, 20);

    private static final String WIN_MSG = "You win!";
    private static final String LOSE_MSG = "You lose!";
    private static final String PLAY_AGAIN_MSG = " Play again?";
    private static final String TITLE = "Bricker Game";

    private Random rand = new Random();
    private Ball ball;
    private int rows;
    private int cols;
    private Vector2 windowDim;
    private LifePanel lifeGraphic;
    private Paddle userPaddle;
    private TempPaddle centerPaddle;
    private SoundReader soundReader;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private int countCollision;

    /**
     * Main method to start the game.
     * @param args Command line arguments for rows and columns
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            new BrickerGameManager(TITLE, WINDOW_SCALE, Integer.parseInt(args[0]), Integer.parseInt(args[1])).run();
        } else {
            new BrickerGameManager(TITLE, WINDOW_SCALE).run();
        }
    }

    /**
     * Constructs a new BrickerGameManager with default rows and columns.
     * @param title Title of the game window
     * @param windowSize Size of the game window
     */
    public BrickerGameManager(String title, Vector2 windowSize) {
        this(title, windowSize, DEFAULT_ROWS, DEFAULT_COLS);
    }

    /**
     * Constructs a new BrickerGameManager with specified rows and columns.
     * @param title Title of the game window
     * @param windowSize Size of the game window
     * @param rows Number of rows of bricks
     * @param cols Number of columns of bricks
     */
    public BrickerGameManager(String title, Vector2 windowSize, int rows, int cols) {
        super(title, windowSize);
        this.rows = rows;
        this.cols = cols;
        this.countCollision = 0;
        this.windowDim = WINDOW_SCALE;
    }

    /**
     * Reads and returns a sound from the specified path.
     * @param path Path to the sound file
     * @return Sound object
     */
    public Sound getSound(Path path) {
        return soundReader.readSound(path.getPath());
    }

    /**
     * Reads and returns an image from the specified path.
     * @param path Path to the image file
     * @param isTopLeftPixelTransparency Whether the top-left pixel is transparent
     * @return Renderable image object
     */
    public Renderable getImage(Path path, boolean isTopLeftPixelTransparency) {
        return imageReader.readImage(path.getPath(), isTopLeftPixelTransparency);
    }

    /**
     * Adds a game object to the game.
     * @param gameObject Game object to add
     */
    public void addObj(GameObject gameObject) {
        gameObjects().addGameObject(gameObject);
    }

    /**
     * Removes a game object from the game.
     * @param gameObject Game object to remove
     * @return True if the object was removed, false otherwise
     */
    public boolean removeObj(GameObject gameObject) {
        return gameObjects().removeGameObject(gameObject);
    }

    /**
     * Increments the collision count.
     */
    public void incrementCountCollision() {
        countCollision++;
    }

    /**
     * Retrieves the center paddle.
     * @return Center paddle object
     */
    public TempPaddle getCenterPaddle() {
        return centerPaddle;
    }

    /**
     * Retrieves the main game ball.
     * @return Main game ball object
     */
    public Ball getMainBall() {
        return ball;
    }

    /**
     * Creates and returns a new heart object.
     * @return New heart object
     */
    public Heart getNewHeart() {
        return new Heart(Vector2.ZERO, HEART_SIZE,
                getImage(Path.HEART_IMAGE, true),
                lifeGraphic, userPaddle, gameObjects());
    }

    /**
     * Initializes the game, setting up game objects and initial state.
     * @param imageReader Image reader for loading images
     * @param soundReader Sound reader for loading sounds
     * @param inputListener Listener for user input
     * @param windowController Controller for the game window
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.countCollision = 0;
        this.soundReader = soundReader;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDim = windowController.getWindowDimensions();

        ball = new Ball(Vector2.ZERO, BALL_SIZE,
                getImage(Path.BALL_IMAGE, true),
                getSound(Path.BALL_SOUND), SPEED_FACTOR, BALL_SPEED);

        this.gameObjects().addGameObject(ball);
        centerBall(); //sets the ball to the center of the window

        TextRenderable textRenderable = new TextRenderable("");
        GameObject lifeLabel = new GameObject(new Vector2(HEART_SCALE,
                windowDim.y() - PADDLE_PADDING), HEART_SIZE, textRenderable);
        this.gameObjects().addGameObject(lifeLabel, Layer.UI);

        Renderable heartImage = getImage(Path.HEART_IMAGE, true);
        this.lifeGraphic = new LifePanel(gameObjects(), DEFAULT_LIVES, MAX_LIVES, heartImage,
                HEART_SCALE, new Vector2(PADDLE_PADDING, windowDim.y() - PADDLE_PADDING), textRenderable);

        makePaddles();
        makeFixedObjects();
        makeBricks();
    }

    /**
     * Creates and adds paddles to the game.
     */
    private void makePaddles() {
        this.userPaddle = new Paddle(Vector2.ZERO, PADDLE_SIZE,
                getImage(Path.PADDLE_IMAGE, false),
                inputListener, windowDim.x() - PADDLE_SIZE.x() - WIDTH_BORDER, WIDTH_BORDER, PADDLE_SPEED);

        this.centerPaddle = new TempPaddle(Vector2.ZERO, PADDLE_SIZE,
                getImage(Path.PADDLE_IMAGE, false),
                inputListener, windowDim.x() - PADDLE_SIZE.x() - WIDTH_BORDER, WIDTH_BORDER,
                PADDLE_SPEED, gameObjects(), TEMP_PADDLE_COUNT, windowDim.mult(CENTER_FACTOR));

        this.gameObjects().addGameObject(userPaddle);
        userPaddle.setCenter(new Vector2(windowDim.x() / 2, windowDim.y() - PADDLE_PADDING));
    }

    /**
     * Creates and adds fixed objects (borders and background) to the game.
     */
    private void makeFixedObjects() {
        GameObject background = new GameObject(Vector2.ZERO, windowDim,
                getImage(Path.BACKGROUND_IMAGE, false));
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);

        GameObject borderTop = new GameObject(Vector2.ZERO, new Vector2(windowDim.x(), WIDTH_BORDER), null);
        GameObject borderLeft = new GameObject(Vector2.ZERO, new Vector2(WIDTH_BORDER, windowDim.y()), null);
        GameObject borderRight = new GameObject(new Vector2(windowDim.x() - WIDTH_BORDER, 0),
                new Vector2(WIDTH_BORDER, windowDim.y()), null);

        this.gameObjects().addGameObject(borderTop, Layer.STATIC_OBJECTS);
        this.gameObjects().addGameObject(borderLeft, Layer.STATIC_OBJECTS);
        this.gameObjects().addGameObject(borderRight, Layer.STATIC_OBJECTS);
    }

    /**
     * Creates and adds bricks to the game.
     */
    private void makeBricks() {
        float width = windowDim.x() - 2 * WIDTH_BORDER - (cols + 1) * MARGIN;
        float width_brick = width / cols; // Width of each brick
        Vector2 startVector = new Vector2(WIDTH_BORDER + MARGIN, HEIGHT_BRICK);
        for (int i = 0; i < rows; i++) {
            Vector2 currentVector = new Vector2(startVector.x(), startVector.y() + i * (HEIGHT_BRICK + MARGIN));
            for (int j = 0; j < cols; j++) {
                Vector2 brickPosition = new Vector2(currentVector.x() + j * (width_brick + MARGIN), currentVector.y());
                int randomNumber = rand.nextInt(STRATEGIES_RANGE);
                CollisionStrategy collisionStrategy = FactoryStrategies.createCollisionStrategy(randomNumber, this);
                GameObject brick = new Brick(brickPosition, new Vector2(width_brick, HEIGHT_BRICK),
                        getImage(Path.BRICK_IMAGE, false), collisionStrategy);
                this.gameObjects().addGameObject(brick);
            }
        }
    }

    /**
     * Ends the game with a specified message and prompts to play again.
     * @param message Message to display at the end of the game
     */
    private void endGame(String message) {
        if (windowController.openYesNoDialog(message + PLAY_AGAIN_MSG)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    /**
     * Updates the game state, checking for collisions and game over conditions.
     * @param deltaTime Time elapsed since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.rows * this.cols <= countCollision || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            endGame(WIN_MSG);
        }

        for (GameObject obj : this.gameObjects()) {
            if (obj.equals(ball)) {
                if (windowDim.y() < ball.getCenter().y()) {
                    if (this.lifeGraphic.getCurrentLives() == 1) {
                        endGame(LOSE_MSG);
                    } else {
                        this.lifeGraphic.removeHeart();
                        centerBall();
                    }
                }
            } else {
                if (windowDim.y() < obj.getCenter().y()) {
                    this.gameObjects().removeGameObject(obj);
                }
            }
        }
    }

    /**
     * Centers the ball in the game window and sets its velocity.
     */
    private void centerBall() {
        ball.setCenter(this.windowDim.mult(CENTER_FACTOR));
        float velX = rand.nextBoolean() ? ball.getSpeed() : -ball.getSpeed();
        float velY = rand.nextBoolean() ? ball.getSpeed() : -ball.getSpeed();
        //sets the velocity of the ball according to the random values
        ball.setVelocity(new Vector2(velX, velY));
    }
}