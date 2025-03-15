package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents the flora in the game, including the creation of trees within a specified range.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class Flora {
    /**
     * The width of each tree.
     */
    private final int treeWidth;
    /**
     * A function to get the height at a given x-coordinate.
     */
    private final Function<Float, Float> heightAtX;
    /**
     * A consumer to handle the event when a tree is eaten.
     */
    private final Consumer<Float> onEat;


    /**
     * Constructs a Flora object.
     *
     * @param treeWidth The width of each tree.
     * @param heightAtX A function to get the height at a given x-coordinate.
     * @param onEat A consumer to handle the event when a tree is eaten.
     */
    public Flora(int treeWidth, Function<Float, Float> heightAtX, Consumer<Float> onEat) {
        this.heightAtX = heightAtX;
        this.onEat = onEat;
        this.treeWidth = treeWidth;
    }

    /**
     * Creates trees within the specified range.
     *
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return A list of created trees.
     */
    public List<Tree> createInRange(int minX, int maxX){
        List<Tree> treeList = new ArrayList<>();
        Random random = new Random(Objects.hash(minX,maxX));
        for(float i = minX; i < maxX; i+= treeWidth){
            if(random.nextBoolean()){
                continue;
            }
            Tree tree = new Tree(i, heightAtX.apply(i), onEat, treeWidth);
            treeList.add(tree);
        }
        return treeList;
    }
}