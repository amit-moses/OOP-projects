package bricker.main;

/**
 * Enum representing various asset paths used in the Bricker game.
 * Provides methods to retrieve the path as a string.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public enum Path {
    HEART_IMAGE("assets/heart.png"),
    BRICK_IMAGE("assets/brick.png"),
    BALL_IMAGE("assets/ball.png"),
    TURBO_IMAGE("assets/redball.png"),
    PADDLE_IMAGE("assets/paddle.png"),
    BALL_SOUND("assets/blop.wav"),
    PACK_IMAGE("assets/mockBall.png"),
    BACKGROUND_IMAGE("assets/DARK_BG2_small.jpeg");

    /**
     * The path to the asset.
     */
    private String path;

    /**
     * Constructs a new Path enum with the specified path.
     *
     * @param path The path to the asset.
     */
    Path(String path) {
        this.path = path;
    }

    /**
     * Returns the path as a string.
     *
     * @return The path to the asset.
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the path as a string.
     *
     * @return The path to the asset.
     */
    @Override
    public String toString() {
        return path;
    }
}