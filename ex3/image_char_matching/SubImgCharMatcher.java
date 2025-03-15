package image_char_matching;

import java.util.*;

/**
 * The SubImgCharMatcher class is responsible for matching sub-images to characters
 * based on their brightness. It maintains mappings between characters and their
 * brightness values, and provides methods to retrieve characters
 * based on the brightness of sub-images.
 *
 *  @author Emmanuelle Schnitzer
 *  @author Amit Moses
 */
public class SubImgCharMatcher {
    /**
     * The default rounding value for brightness to character mapping.
     */
    private static final int ABS_ROUND = 2;

    /**
     * The floor rounding value for brightness to character mapping.
     */
    private static final int FLOOR_ROUND = 1;

    /**
     * The ceiling rounding value for brightness to character mapping.
     */
    private static final int CEIL_ROUND = 0;



    /**
     * The error message for an invalid type of rounding.
     */
    private static final String ERROR_ROUND = "Invalid typeRound";

    /**
     * The type of rounding to use when matching brightness values to characters.
     */
    private int typeRound;

    /**
     * A map that associates characters with their brightness values.
     */
    private final HashMap<Character, Double> charToBrightnessSet;

    /**
     * A map that associates brightness values with priority queues of characters.
     */
    private final TreeMap<Double, PriorityQueue<Character>> brightnessToCharMap;

    /**
     * The maximum brightness value among the characters.
     */
    private double maxBrightness;

    /**
     * The minimum brightness value among the characters.
     */
    private double minBrightness;

    /**
     * A boolean value that indicates whether the brightness values should be normalized.
     */
    private boolean normalize;

    /**
     * Constructs a SubImgCharMatcher object with the specified character set.
     * Initializes the mappings between characters and their brightness values.
     *
     * @param charset the array of characters to be used for matching
     */
    public SubImgCharMatcher(char [] charset){
        this.typeRound = ABS_ROUND;
        this.normalize = false;

        this.charToBrightnessSet = new HashMap<>();

        // use comparator to sort the brightness values
        this.brightnessToCharMap = new TreeMap<>((b, a) ->
                Double.compare(b, normalize ? normalizeBrightness(a) : a));

        this.maxBrightness = Double.MIN_VALUE;
        this.minBrightness = Double.MAX_VALUE;
        for (char c : charset) {
            addChar(c);
        }
    }

    /**
     * Normalizes the brightness value to a range between 0 and 1.
     *
     * @param brightness the brightness value to normalize
     * @return the normalized brightness value
     */
    private double normalizeBrightness(double brightness) {
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    /**
     * Returns the type of rounding used when matching brightness values to characters.
     */
    public void setTypeRound(int typeRound) {
        this.typeRound = typeRound;
    }

    /**
     * Checks if the specified character is present in the map.
     *
     * @param c the character to check
     * @return true if the character is present, false otherwise
     */
    public boolean isCharContain(char c){
        return charToBrightnessSet.containsKey(c);
    }

    /**
     * Returns the number of characters in the map.
     *
     * @return the number of characters in the map
     */
    public int getSize(){
        return charToBrightnessSet.size();
    }

    /**
     * Retrieves the character from the specified map entry.
     *
     * @param pr the map entry containing the brightness value and priority queue of characters
     * @return the character from the priority queue, or a space character if the queue is empty
     */
    private char getChar(Map.Entry<Double, PriorityQueue<Character>> pr){
        if(pr!=null && pr.getValue() != null && !pr.getValue().isEmpty()){
            return pr.getValue().peek();
        }
        return ' ';
    }

    /**
     * Retrieves the character that best matches the specified brightness
     * value, using the specified rounding type.
     *
     * @param ceil the map entry with the ceiling brightness value
     * @param floor the map entry with the floor brightness value
     * @param brightness the brightness value to match
     * @return the character that best matches the brightness value
     */
    private char getRound(Map.Entry<Double, PriorityQueue<Character>> ceil,
                         Map.Entry<Double, PriorityQueue<Character>> floor,
                         double brightness){

        if(ceil == null || floor == null){
            return ceil != null ? getChar(ceil) : getChar(floor);
        }
        double floorDistance = Math.abs(normalizeBrightness(floor.getKey()) - brightness);
        double ceilingDistance = Math.abs(normalizeBrightness(ceil.getKey()) - brightness);
        return floorDistance <= ceilingDistance ? getChar(floor) : getChar(ceil);
    }

    /**
     * Retrieves the character that best matches the specified brightness value,
     * using the specified rounding type.
     *
     * @param brightness the brightness value to match
     * @return the character that best matches the brightness value
     */
    public char getCharByImageBrightness(double brightness){
        normalize = true;
        Map.Entry<Double, PriorityQueue<Character>> ceil = brightnessToCharMap.ceilingEntry(brightness);
        Map.Entry<Double, PriorityQueue<Character>> floor = brightnessToCharMap.floorEntry(brightness);

        return switch (typeRound) {
            case CEIL_ROUND -> ceil != null ? getChar(ceil) : floor != null ? getChar(floor) : ' ';
            case FLOOR_ROUND -> floor != null ? getChar(floor) : ceil != null ? getChar(ceil) : ' ';
            case ABS_ROUND -> getRound(ceil, floor, brightness);
            default -> throw new IllegalArgumentException(ERROR_ROUND);
        };
    }

    /**
     * Returns the set of characters in the map.
     *
     * @return the set of characters in the map
     */
    public Set<Character> getCharacters(){
        return charToBrightnessSet.keySet();
    }

    /**
     * Calculates the brightness of the specified character.
     *
     * @param c the character to calculate the brightness for
     * @return the brightness value of the character
     */
    private static double subBrightnessByChar(char c){
        boolean[][] boolMatrix = CharConverter.convertToBoolArray(c);
        int counter = 0;
        for(int i = 0; i < boolMatrix.length; i++){
            for(int j = 0; j < boolMatrix[i].length; j++){
                if(boolMatrix[i][j]){
                    counter++;
                }
            }
        }
        return (double)counter/(boolMatrix.length * boolMatrix[0].length);
    }

    /**
     * Adds the specified character to the map and calculates its brightness.
     *
     * @param c the character to add
     */
    public void addChar(char c){
        normalize = false;
        if(isCharContain(c)){
            return;
        }
        double brightness = subBrightnessByChar(c);
        if(brightnessToCharMap.containsKey(brightness)){
            brightnessToCharMap.get(brightness).add(c);
        } else {
            PriorityQueue<Character> charQueue = new PriorityQueue<>();
            charQueue.add(c);
            brightnessToCharMap.put(brightness, charQueue);
        }

        charToBrightnessSet.put(c, brightness);

        if(brightnessToCharMap.size() == 1){
            minBrightness = brightness;
            maxBrightness = brightness;
            return;
        }

        if(brightness < minBrightness){
            minBrightness = brightness;
        } else if(brightness > maxBrightness){
            maxBrightness = brightness;
        }
    }

    /**
     * Removes the specified character from the map.
     *
     * @param c the character to remove
     */
    public void removeChar(char c){
        normalize = false;
        if(!isCharContain(c)){
            return;
        }

        double brightness = charToBrightnessSet.get(c);
        PriorityQueue<Character> charQueue = brightnessToCharMap.get(brightness);
        charQueue.remove(c);
        if(charQueue.isEmpty()){
            brightnessToCharMap.remove(brightness);
        }
        charToBrightnessSet.remove(c);

        if (brightnessToCharMap.isEmpty() || charToBrightnessSet.isEmpty()) {
            minBrightness = Double.MAX_VALUE;
            maxBrightness = Double.MIN_VALUE;
            return;
        }

        if(brightness == minBrightness){
            minBrightness = brightnessToCharMap.firstKey();
        }
        if(brightness == maxBrightness){
            maxBrightness = brightnessToCharMap.lastKey();
        }
    }
}