package ex5.main.valid;

import ex5.main.symbols.FunctionMatcher;
import ex5.main.symbols.ParamsScopeMatcher;
import ex5.main.utils.Type;
import ex5.main.utils.Utils;

import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Class for validating keywords in the SJavac program.
 * This class implements the Checker interface and provides logic to check if a line correctly
 * uses keywords such as if, while, and return.
 * It ensures that keyword usage follows the SJavac rules and handles condition validation.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class ValidKeywords implements Checker {
    /**
     * Keyword for if condition.
     */
    private static final String IF_CONDITION = "if";

    /**
     * Keyword for while condition.
     */
    private static final String WHILE_CONDITION = "while";

    /**
     * Keyword for return condition.
     */
    private static final String RETURN_CONDITION = "return";

    /**
     * String representing the condition split pattern.
     */
    private static final String CONDITION_SPLIT = "(&&|\\|\\|)";

    /**
     * String representing the first word split pattern.
     */
    private static final String FIRST_WORD_SPLIT = "\\s+";

    /**
     * String representing the true condition.
     */
    private static final String TRUE_CONDITION = "true";

    /**
     * String representing the false condition.
     */
    private static final String FALSE_CONDITION = "false";

    /**
     * Error message for invalid boolean type.
     */
    private static final String ERROR_BOOLEAN = "Variable %s is not of boolean, int, double type";

    /**
     * Error message for undeclared variable.
     */
    private static final String ERROR_NOT_DECLARED = "Variable %s is not declared";

    /**
     * Error message for uninitialized variable.
     */
    private static final String ERROR_NOT_INITIALIZED = "Variable %s is not initialized";

    /**
     * Flag indicating whether to throw exceptions on validation errors.
     */
    private final boolean toThrow;

    /**
     * Constructs a new ValidKeywords instance with the specified error handling behavior.
     *
     * @param toThrow whether to throw exceptions on validation errors
     */
    public ValidKeywords(boolean toThrow) {
        this.toThrow = toThrow;
    }

    /**
     * Checks if the given line is valid according to the SJavac rules for keywords.
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
        if (paramsScopeMatcher.getScopeSize() == 1) {
            return false;
        }
        line = line.trim();
        HashMap<String, ThrowingPredicate> validKeywords = new HashMap<>();
        validKeywords.put(IF_CONDITION, condition -> checkIf(condition, paramsScopeMatcher, toThrow));
        validKeywords.put(WHILE_CONDITION, condition -> checkWhile(condition, paramsScopeMatcher, toThrow));
        validKeywords.put(RETURN_CONDITION, ValidKeywords::checkReturn);
        String firstWord = line.split(FIRST_WORD_SPLIT)[0];
        for (String start : validKeywords.keySet()) {
            if (firstWord.startsWith(start) && validKeywords.get(start).test(line)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates the boolean condition in the given string.
     *
     * @param condition the condition to check
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param toThrow whether to throw exceptions on validation errors
     * @return true if the condition is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    private static boolean checkIfBoolean(String condition,
                                          ParamsScopeMatcher paramsScopeMatcher, boolean toThrow)
            throws SJavacException {
        String[] tokens = condition.split(CONDITION_SPLIT); // Split by boolean operators
        for (String token : tokens) {
            token = token.trim();
            if (token.equals(TRUE_CONDITION) || token.equals(FALSE_CONDITION) ||
                    Utils.NUMBER_PATTERN.matcher(token).matches()) {
                continue;
            }
            ParamsScopeMatcher.Variable variable = paramsScopeMatcher.getVariable(token);
            if (variable == null) {
                if (toThrow) {
                    throw new SJavacException(String.format(ERROR_NOT_DECLARED, token));
                }
                return false;
            }
            if (variable.getType() == Type.CHAR || variable.getType() == Type.STRING) {
                if (toThrow) {
                    throw new SJavacException(String.format(ERROR_BOOLEAN, token));
                }
                return false;
            }
            if (!variable.isInitialized()) {
                if (toThrow) {
                    throw new SJavacException(String.format(ERROR_NOT_INITIALIZED, token));
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Validates the if condition in the given string.
     *
     * @param line the line to check
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param toThrow whether to throw exceptions on validation errors
     * @return true if the if condition is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    private static boolean checkIf(String line, ParamsScopeMatcher paramsScopeMatcher, boolean toThrow)
            throws SJavacException {
        Matcher ifMatcher = Utils.IF_PATTERN.matcher(line);
        if (!ifMatcher.matches()) {
            return false;
        }
        return checkIfBoolean(ifMatcher.group(1).trim(), paramsScopeMatcher, toThrow);
    }

    /**
     * Validates the while condition in the given string.
     *
     * @param line the line to check
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param toThrow whether to throw exceptions on validation errors
     * @return true if the while condition is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    private static boolean checkWhile(String line, ParamsScopeMatcher paramsScopeMatcher, boolean toThrow)
            throws SJavacException {
        Matcher whileMatcher = Utils.WHILE_PATTERN.matcher(line);
        if (!whileMatcher.matches()) {
            return false;
        }
        return checkIfBoolean(whileMatcher.group(1).trim(), paramsScopeMatcher, toThrow);
    }

    /**
     * Validates the return statement in the given string.
     *
     * @param line the line to check
     * @return true if the return statement is valid, false otherwise
     */
    private static boolean checkReturn(String line) {
        return Utils.RETURN_PATTERN.matcher(line).matches();
    }
}