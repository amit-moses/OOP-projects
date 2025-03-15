package ex5.main.utils;

/**
 * Enum representing various data types.
 * Provides methods to get the type as a string and to retrieve a Type enum from a string.
 * The supported types are: int, String, boolean, double, and char.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public enum Type {
    /**
     * Represents the int data type.
     */
    INT("int"),

    /**
     * Represents the String data type.
     */
    STRING("String"),

    /**
     * Represents the boolean data type.
     */
    BOOLEAN("boolean"),

    /**
     * Represents the double data type.
     */
    DOUBLE("double"),

    /**
     * Represents the char data type.
     */
    CHAR("char");

    private final String type;

    /**
     * Constructor for the Type enum.
     *
     * @param type the string representation of the type
     */
    Type(String type) {
        this.type = type;
    }

    /**
     * Gets the string representation of the type.
     *
     * @return the string representation of the type
     */
    public String getType(){
        return this.type;
    }

    /**
     * Retrieves the Type enum corresponding to the given string.
     *
     * @param type the string representation of the type
     * @return the corresponding Type enum, or null if the type is not recognized
     */
    public static Type getType(String type){
        return switch (type){
            case "int" -> INT;
            case "String" -> STRING;
            case "boolean" -> BOOLEAN;
            case "double" -> DOUBLE;
            case "char" -> CHAR;
            default -> null;
        };
    }
}