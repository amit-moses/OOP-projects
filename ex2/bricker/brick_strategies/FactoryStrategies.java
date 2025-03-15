package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

/**
 * A factory class for creating various collision strategies.
 * This class provides a method to create different types of collision strategies
 * based on a random number input.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class FactoryStrategies {
    // Constants for the different collision strategies.
    private static final int DOUBLE_BEHAVIOR = 0;
    private static final int ADD_LIFE = 1;
    private static final int TURBO = 2;
    private static final int MORE_PADDLE = 3;
    private static final int MORE_BALLS = 4;

    // Number of strategies available.
    private static final int STRATEGY_NUM = 5;

    /**
     * Creates a collision strategy based on the provided random number.
     * If the next strategy is null, it defaults to BasicCollisionStrategy.
     *
     * @param numRandom The random number used to determine the collision strategy.
     * @param manager The game manager responsible for managing game objects.
     * @param nextStrategy The next collision strategy to be executed, can be null.
     * @return The created collision strategy.
     */
    public static CollisionStrategy createCollisionStrategy(int numRandom,
                                                            BrickerGameManager manager, CollisionStrategy nextStrategy) {
        return switch (numRandom) {
            case DOUBLE_BEHAVIOR -> new DoubleBehaviorCollisionStrategy(manager);
            case ADD_LIFE -> new AddLifeCollisionStrategy(manager, nextStrategy);
            case TURBO -> new TurboCollisionStrategy(manager, nextStrategy);
            case MORE_PADDLE -> new MorePaddleCollisionStrategy(manager, nextStrategy);
            case MORE_BALLS -> new MoreBallsCollisionStrategy(manager, nextStrategy);
            default -> nextStrategy;
        };
    }

    /**
     * Creates a collision strategy based on the provided random number.
     * Defaults to BasicCollisionStrategy if nextStrategy is null.
     *
     * @param numRandom The random number used to determine the collision strategy.
     * @param manager The game manager responsible for managing game objects.
     * @return The created collision strategy.
     */
    public static CollisionStrategy createCollisionStrategy(int numRandom, BrickerGameManager manager){
        return createCollisionStrategy(numRandom, manager, new BasicCollisionStrategy(manager));
    }

    /**
     * Returns the number of strategies available.
     *
     * @return The number of strategies available.
     */
    public static int getStrategyNum() {
        return STRATEGY_NUM;
    }
}