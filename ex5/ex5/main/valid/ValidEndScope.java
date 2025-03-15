package ex5.main.valid;

import ex5.main.symbols.FunctionMatcher;
import ex5.main.symbols.ParamsScopeMatcher;
import ex5.main.utils.Utils;

/**
 * Class for validating the end of a scope in the SJavac program.
 * This class implements the Checker interface and provides logic to check if a line correctly ends a scope.
 * It ensures that the main scope is not improperly ended.
 *
 * @autor Emmanuelle Schnitzer
 * @autor Amit Moses
 */
public class ValidEndScope implements Checker {
    /**
     * Error message for invalid end of the main scope.
     */
    private static final String INVALID_END_SCOPE = "Cannot end the main scope %s";

    /**
     * Checks if the given line is valid according to the SJavac rules for ending a scope.
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
        if(!Utils.END_SCOPE.matcher(line).matches()){
            return false;
        }

        if (paramsScopeMatcher.getScopeSize() <= 1) {
            throw new SJavacException(String.format(INVALID_END_SCOPE, line));
        }
        return true;
    }
}