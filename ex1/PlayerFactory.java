/**
 * Factory class for creating different types of players.
 * Implements the Factory design pattern to The Single Responsibility Principle.
 *
 * Supported player types:
 * - human: Human player with keyboard input
 * - genius: Advanced AI player
 * - whatever: Random move player
 * - clever: Basic AI player
 *
 * @author Amit Moses
 */
public class PlayerFactory {
    /**
     * Creates a new PlayerFactory instance.
     */
    public PlayerFactory(){}

    /**
     * Creates a player instance based on the specified type.
     *
     * @param type String identifier for player type:
     *             "human" - Human player
     *             "genius" - Advanced player
     *             "whatever" - Random player
     *             "clever" - Basic player
     * @return Player instance of requested type, or null if type is invalid
     */
    public Player buildPlayer(String type){
        return switch (type) {
            case "human" -> new HumanPlayer();
            case "genius" -> new GeniusPlayer();
            case "whatever" -> new WhateverPlayer();
            case "clever" -> new CleverPlayer();
            default -> null;
        };
    }
}