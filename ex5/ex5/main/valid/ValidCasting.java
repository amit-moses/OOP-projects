package ex5.main.valid;

import ex5.main.symbols.FunctionMatcher;
import ex5.main.symbols.ParamsScopeMatcher;
import ex5.main.utils.Type;
import ex5.main.utils.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Class for validating variable casting in the SJavac program.
 * This class implements the Checker interface and provides logic
 * to check if a variable can be cast to another type.
 * It uses a predefined map of valid type castings and checks for various casting errors.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class ValidCasting implements Checker {
    /**
     * Map of valid type castings.
     * The key is the source type, and the value is a list of target
     * types that the source type can be cast to.
     */
    private final HashMap<Type, List<Type>> castingMap;

    /**
     * Error message for invalid casting.
     */
    private static final String INVALID_CASTING = "Cannot cast variable %s to %s";

    /**
     * Error message for uninitialized variable assignment.
     */
    private static final String UNINITIALIZED_ERROR = "Cannot assign to uninitialized variable %s";
    /**
     * Error message for assignment to a final variable.
     */
    private static final String FINAL_ERROR = "Cannot assign a value to a final variable %s";

    /**
     * Error message for invalid symbol.
     */
    private static final String INVALID_SYMBOL_ERROR = "Cannot find a symbol %s";

    /**
     * Flag indicating whether to throw exceptions on validation errors.
     */
    private final boolean toThrow;

    /**
     * Number of parameters expected in a casting operation.
     */
    private static final int NUMBER_OF_PARAMETERS = 2;

    /**
     * Constructs a new ValidCasting instance with the specified error handling behavior.
     *
     * @param toThrow whether to throw exceptions on validation errors
     */
    public ValidCasting(boolean toThrow) {
        this.toThrow = toThrow;
        castingMap = new HashMap<>();
        castingMap.put(Type.INT, List.of(Type.INT));
        castingMap.put(Type.DOUBLE, List.of(Type.INT, Type.DOUBLE));
        castingMap.put(Type.STRING, List.of(Type.STRING));
        castingMap.put(Type.BOOLEAN, List.of(Type.INT, Type.DOUBLE, Type.BOOLEAN));
        castingMap.put(Type.CHAR, List.of(Type.CHAR));
    }

    /**
     * Checks if the given line is valid according to the SJavac rules for variable casting.
     *
     * @param line the line to check
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param functionMatcher the matcher for function declarations
     * @return true if the line is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    @Override
    public boolean isValid(String line, ParamsScopeMatcher paramsScopeMatcher,
                           FunctionMatcher functionMatcher) throws SJavacException {
        line = line.trim();
        return casting(line, paramsScopeMatcher, toThrow);
    }

    /**
     * Validates the casting operation in the given string.
     *
     * @param strToCheck the string to check
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param toThrow whether to throw exceptions on validation errors
     * @return true if the casting operation is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    private boolean casting(String strToCheck, ParamsScopeMatcher paramsScopeMatcher,
                            boolean toThrow) throws SJavacException {
        String[] parts = strToCheck.split(Utils.EQUAL_CHECK);
        if (parts.length != NUMBER_OF_PARAMETERS) {
            return false;
        }
        String varName = parts[0].trim();
        ParamsScopeMatcher.Variable variable1 = paramsScopeMatcher.getVariable(varName);
        if (variable1 == null) {
            if (toThrow) {
                throw new SJavacException(String.format(INVALID_SYMBOL_ERROR, varName));
            }
            return false;
        }

        if (variable1.isFinal()) {
            if (toThrow) {
                throw new SJavacException(String.format(FINAL_ERROR, varName));
            }
            return false;
        }

        if (Utils.getPattern(variable1.getType()).matcher(parts[1].trim()).matches()) {
            variable1.setInitialized(paramsScopeMatcher.getScopeSize() == 1);
            return true;
        }

        ParamsScopeMatcher.Variable variable2 = paramsScopeMatcher.getVariable(parts[1].trim());
        if (variable2 == null) {
            if (toThrow) {
                throw new SJavacException(String.format(INVALID_SYMBOL_ERROR, parts[1].trim()));
            }
            return false;
        }

        if (!variable2.isInitialized()) {
            if (toThrow) {
                throw new SJavacException(String.format(UNINITIALIZED_ERROR, parts[1].trim()));
            }
            return false;
        }

        if (castingMap.get(variable1.getType()).contains(variable2.getType())) {
            variable1.setInitialized(paramsScopeMatcher.getScopeSize() == 1);
            return true;
        }
        if (toThrow) {
            throw new SJavacException(String.format(INVALID_CASTING, varName, variable2.getType()));
        }
        return false;
    }
}