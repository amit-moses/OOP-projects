package ex5.main.utils;

import java.util.regex.Pattern;

/**
 * Utility class providing various patterns and methods for pattern matching and type identification.
 * This class includes patterns for matching variable names, function declarations, and different data types.
 * It also provides methods to retrieve patterns based on data types and to
 * identify the type of a given string.
 *
 * @author Emmanuelle Schnitzer
 * @autor Amit Moses
 */
public class Utils {
    /**
     * Pattern for matching final variables.
     */
    public static final Pattern FINAL_PATTERN = Pattern.compile("final\\s+.*");

    /**
     * Pattern for matching function declarations.
     */
    public static final Pattern FUNCTION_PATTERN = Pattern.compile(
            "void\\s+([a-zA-Z][a-zA-Z_0-9]*)\\s*\\((.*)\\)\\s*\\{");

    /**
     * String representing the end of a line argument.
     */
    public static final String END_LINE_ARGUMENT = ";";

    /**
     * String representing the equality check.
     */
    public static final String EQUAL_CHECK = "=";

    /**
     * String representing the separator.
     */
    public static final String SEPERATOR = ",";

    /**
     * String representing the final keyword.
     */
    public static final String FINAL = "final";

    /**
     * String representing the first word split pattern.
     */
    public static final String FIRST_WORD_SPLIT = "\\s+";

    /**
     * Pattern for matching variable names.
     */
    public static final Pattern VAR_NAME = Pattern.compile("([a-zA-Z][a-zA-Z_0-9]*)|(_+[a-zA-Z_0-9]+)");

    /**
     * Pattern for matching function calls.
     */
    public static final Pattern FUNCTION_CALL_PATTERN = Pattern.compile(
            "([a-zA-Z][a-zA-Z_0-9]*)\\s*\\((.*)\\) *;");

    /**
     * Pattern for matching numbers.
     */
    public static final Pattern NUMBER_PATTERN = Pattern.compile("[+-]?[0-9]*.?[0-9]+");

    /**
     * Pattern for matching if statements.
     */
    public static final Pattern IF_PATTERN = Pattern.compile("if\\s*\\((.*)\\)\\s*\\{");

    /**
     * Pattern for matching while statements.
     */
    public static final Pattern WHILE_PATTERN = Pattern.compile("while\\s*\\((.*)\\)\\s*\\{");

    /**
     * Pattern for matching return statements.
     */
    public static final Pattern RETURN_PATTERN = Pattern.compile("return\\s*;");

    /**
     * Pattern for matching arguments.
     */
    public static final Pattern ARGUMENTS = Pattern.compile("^([^,]+)*(\\s*,[^,]+)*$");

    /**
     * Pattern for matching integer values.
     */
    public static final Pattern INT_PATTERN = Pattern.compile("[+-]?[0-9]+");

    /**
     * Pattern for matching string values.
     */
    public static final Pattern STRING_PATTERN = Pattern.compile("\"[^\"]*\"");

    /**
     * Pattern for matching boolean values.
     */
    public static final Pattern BOOLEAN_PATTERN = Pattern.compile("(true|false|[+-]?[0-9]*.?[0-9]+)");

    /**
     * Pattern for matching double values.
     */
    public static final Pattern DOUBLE_PATTERN = Pattern.compile("[+-]?[0-9]*\\.?[0-9]+");

    /**
     * Pattern for matching char values.
     */
    public static final Pattern CHAR_PATTERN = Pattern.compile("'.'");

    /**
     * Pattern for matching the start of a scope.
     */
    public static final Pattern START_SCOPE = Pattern.compile("^(?!//).+\\{\\s*");

    /**
     * Pattern for matching the end of a scope.
     */
    public static final Pattern END_SCOPE = Pattern.compile("^\\s*}\\s*$");

    /**
     * Retrieves the pattern corresponding to the given data type.
     *
     * @param type the data type
     * @return the pattern corresponding to the data type
     */
    public static Pattern getPattern(Type type){
        return switch (type) {
            case INT -> INT_PATTERN;
            case STRING -> STRING_PATTERN;
            case BOOLEAN -> BOOLEAN_PATTERN;
            case CHAR -> CHAR_PATTERN;
            case DOUBLE -> DOUBLE_PATTERN;
        };
    }

    /**
     * Identifies the type of a given string based on predefined patterns.
     *
     * @param line the string to identify
     * @return the corresponding data type, or null if no match is found
     */
    public static Type getStrType(String line){
        for(Type type : Type.values()){
            Pattern pattern = Utils.getPattern(type);
            if(pattern.matcher(line).matches()){
                return type;
            }
        }
        return null;
    }
}