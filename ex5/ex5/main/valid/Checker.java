package ex5.main.valid;

import ex5.main.symbols.FunctionMatcher;
import ex5.main.symbols.ParamsScopeMatcher;

/**
 * Interface for checking the validity of lines in the SJavac program.
 * Implementations of this interface will provide specific validation logic for different types of lines.
 * The validation process involves checking the syntax and structure of the lines based on the SJavac rules.
 *
 * @autor Emmanuelle Schnitzer
 * @autor Amit Moses
 */
public interface Checker {
    /**
     * Checks if the given line is valid according to the SJavac rules.
     *
     * @param line the line to check
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param functionMatcher the matcher for function declarations
     * @return true if the line is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    boolean isValid(String line, ParamsScopeMatcher paramsScopeMatcher,
                    FunctionMatcher functionMatcher) throws SJavacException;
}