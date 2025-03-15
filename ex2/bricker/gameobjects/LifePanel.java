package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Manages the life panel in the Bricker game, including displaying and updating the number of lives.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class LifePanel {
    // Constants for the number of lives that change the text color.
    private static final int RED_LIFE = 1;
    private static final int YELLOW_LIFE = 2;


    /**
     * Margin between heart objects in the life panel.
     */
    private static final int MARGIN = 10;

    /**
     * Size of each heart object.
     */
    private int sizeObject;

    /**
     * Collection of game objects.
     */
    private GameObjectCollection collection;

    /**
     * Renderable image for the heart.
     */
    private Renderable heartImage;

    /**
     * Current position for placing the next heart.
     */
    private Vector2 currentPosition;

    /**
     * Array to store heart game objects.
     */
    private GameObject[] heartArr;

    /**
     * Current number of lives.
     */
    private int currentLives;

    /**
     * Text renderable for displaying the number of lives.
     */
    private TextRenderable textRenderable;

    /**
     * Constructs a new LifePanel instance.
     *
     * @param collection      Collection of game objects.
     * @param initialLives    Initial number of lives.
     * @param maxLives        Maximum number of lives.
     * @param heartImage      Renderable image for the heart.
     * @param sizeObject      Size of each heart object.
     * @param startPosition   Starting position for the first heart.
     * @param textRenderable  Text renderable for displaying the number of lives.
     */
    public LifePanel(GameObjectCollection collection,
                     int initialLives, int maxLives,
                     Renderable heartImage,
                     int sizeObject,
                     Vector2 startPosition,
                     TextRenderable textRenderable) {
        this.collection = collection;
        this.heartImage = heartImage;
        this.sizeObject = sizeObject;
        this.currentPosition = startPosition;
        this.heartArr = new GameObject[maxLives];
        this.textRenderable = textRenderable;
        this.currentLives = 0;

        for (int i = 0; i < initialLives; i++) {
            addNewHeart();
        }
    }

    /**
     * Returns the current number of lives.
     *
     * @return Current number of lives.
     */
    public int getCurrentLives() {
        return currentLives;
    }

    /**
     * Adds a new heart to the life panel.
     */
    public void addNewHeart() {
        if (this.heartArr.length == currentLives) {
            return;
        }

        GameObject newHeart = new GameObject(this.currentPosition,
                new Vector2(this.sizeObject, this.sizeObject), this.heartImage);
        this.currentPosition = new Vector2(this.currentPosition.x() + this.sizeObject + MARGIN, this.currentPosition.y());
        this.heartArr[currentLives++] = newHeart;
        this.collection.addGameObject(newHeart, Layer.UI);
        updateNumber();
    }

    /**
     * Removes a heart from the life panel.
     */
    public void removeHeart() {
        if (currentLives != 0) {
            this.collection.removeGameObject(this.heartArr[currentLives - 1], Layer.UI);
            this.currentPosition = new Vector2(this.currentPosition.x() - this.sizeObject - MARGIN, this.currentPosition.y());
            currentLives--;
            updateNumber();
        }
    }

    /**
     * Updates the number of lives displayed and changes the text color based on the number of lives.
     */
    private void updateNumber() {
        this.textRenderable.setString(String.valueOf(currentLives));
        switch (currentLives) {
            case RED_LIFE:
                textRenderable.setColor(Color.red);
                break;
            case YELLOW_LIFE:
                textRenderable.setColor(Color.yellow);
                break;
            default:
                textRenderable.setColor(Color.green);
        }
    }
}