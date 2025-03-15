package ex5.main.valid;

import ex5.main.symbols.FunctionMatcher;
import ex5.main.symbols.ParamsScopeMatcher;
import ex5.main.utils.Utils;

/**
 * Class for validating variable identifiers in the SJavac program.
 * This class implements the Checker interface and provides logic to check if a line
 * correctly declares or assigns variables.
 * It ensures that variable names and assignments follow the SJavac rules.
 *
 * @autor Emmanuelle Schnitzer
 * @autor Amit Moses
 */
public class ValidIdentifier implements Checker {
    /**
     * Checker for validating variable casting.
     */
    private final Checker validCasting;

    /**
     * Flag indicating whether to throw exceptions on validation errors.
     */
    private final boolean toThrow;

    /**
     * Error message for invalid casting.
     */
    private static final String CASTING_ERROR = "Cannot cast variable";

    /**
     * Constructs a new ValidIdentifier instance with the specified error handling behavior.
     *
     * @param toThrow whether to throw exceptions on validation errors
     */
    public ValidIdentifier(boolean toThrow){
        this.toThrow = toThrow;
        this.validCasting = new ValidCasting(toThrow);
    }

    /**
     * Checks if the given line is valid according to the SJavac rules for variable identifiers.
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
        return checkSingleVariable(line, validCasting, paramsScopeMatcher, functionMatcher, toThrow);
    }

    /**
     * Validates a single variable declaration or assignment in the given string.
     *
     * @param line the line to check
     * @param casting the checker for validating variable casting
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param functionMatcher the matcher for function declarations
     * @param toThrow whether to throw exceptions on validation errors
     * @return true if the variable declaration or assignment is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    private static boolean checkSingleVariable(String line, Checker casting,
                                               ParamsScopeMatcher paramsScopeMatcher,
                                               FunctionMatcher functionMatcher,
                                               boolean toThrow) throws SJavacException {
        if (!line.endsWith(Utils.END_LINE_ARGUMENT)) {
            return false;
        }
        line = line.trim();
        line = line.substring(0, line.length() - 1);

        String[] tokens = line.split(Utils.SEPERATOR);
        for (String token : tokens) {
            token = token.trim();
            if (!token.contains(Utils.EQUAL_CHECK)) {
                return false;
            }

            String varName = token.split(Utils.EQUAL_CHECK)[0].trim();
            if (!Utils.VAR_NAME.matcher(varName).matches()) {
                return false;
            }
            if (!casting.isValid(token, paramsScopeMatcher, functionMatcher)) {
                if (toThrow) {
                    throw new SJavacException(CASTING_ERROR);
                }
                return false;
            }
        }
        return true;
    }
}