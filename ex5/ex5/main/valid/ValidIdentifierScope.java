package ex5.main.valid;

import ex5.main.symbols.FunctionMatcher;
import ex5.main.symbols.ParamsScopeMatcher;
import ex5.main.utils.Type;
import ex5.main.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Class for validating variable identifiers and function calls within a scope in the SJavac program.
 * This class extends ValidIdentifier and provides additional logic
 * to check if a line correctly calls a function.
 * It ensures that variable names, assignments, and function calls follow the SJavac rules.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class ValidIdentifierScope extends ValidIdentifier {
    /**
     * Flag indicating whether to throw exceptions on validation errors.
     */
    private final boolean toThrow;

    /**
     * Error message for type not found.
     */
    private static final String TYPE_ERROR = "Can't find type of variable %s";

    /**
     * Error message for function call with wrong parameters.
     */
    private static final String WRONG_PARAMETERS_ERROR = "Function call with wrong parameters";

    /**
     * Error message for uninitialized variable.
     */
    private static final String INITIALIZATION_ERROR = "Variable not initialized %s";

    /**
     * Group index for function parameters in the regex matcher.
     */
    private static final int FUNCTION_PARAMETERS_GROUP = 2;

    /**
     * Constructs a new ValidIdentifierScope instance with the specified error handling behavior.
     *
     * @param toThrow whether to throw exceptions on validation errors
     */
    public ValidIdentifierScope(boolean toThrow){
        super(toThrow);
        this.toThrow = toThrow;
    }

    /**
     * Checks if the given line is valid according to the SJavac rules
     * for variable identifiers and function calls.
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
        return super.isValid(line, paramsScopeMatcher, functionMatcher) || checkLaterFunction(line,
                paramsScopeMatcher, functionMatcher, toThrow);
    }

    /**
     * Validates a function call in the given string.
     *
     * @param line the line to check
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param functionMatcher the matcher for function declarations
     * @param toThrow whether to throw exceptions on validation errors
     * @return true if the function call is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    private static boolean checkLaterFunction(String line, ParamsScopeMatcher paramsScopeMatcher,
                                              FunctionMatcher functionMatcher,
                                              boolean toThrow) throws SJavacException {
        // Can't call a function from the main scope
        if (paramsScopeMatcher.getScopeSize() <= 1) {
            return false;
        }

        line = line.trim();
        Matcher functionR = Utils.FUNCTION_CALL_PATTERN.matcher(line);
        if (!functionR.matches()) {
            return false;
        }

        List<Type> types = allExist(functionR.group(FUNCTION_PARAMETERS_GROUP).trim(),
                paramsScopeMatcher, toThrow);
        if (types == null) {
            return false;
        }
        boolean ok = functionMatcher.isEqualObj(functionR.group(1).trim(), types);
        if (!ok && toThrow) {
            throw new SJavacException(WRONG_PARAMETERS_ERROR);
        }
        return ok;
    }

    /**
     * Checks if all variables in the parameter string exist and are initialized.
     *
     * @param paramsStr the parameter string
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param toThrow whether to throw exceptions on validation errors
     * @return a list of types of the variables, or null if any variable is not found or not initialized
     * @throws SJavacException if a validation error occurs
     */
    private static List<Type> allExist(String paramsStr, ParamsScopeMatcher paramsScopeMatcher,
                                       boolean toThrow) throws SJavacException {
        List<Type> types = new ArrayList<>();
        if (paramsStr.isEmpty()) {
            return types;
        }

        String[] tokens = paramsStr.split(Utils.SEPERATOR);
        for (String token : tokens) {
            ParamsScopeMatcher.Variable variable = paramsScopeMatcher.getVariable(token.trim());
            if (variable == null) {
                Type type = Utils.getStrType(token.trim());
                if (type != null) {
                    types.add(type);
                } else {
                    if (toThrow) {
                        throw new SJavacException(String.format(TYPE_ERROR, token));
                    }
                    return null;
                }
            } else {
                if (!variable.isInitialized()) {
                    if (toThrow) {
                        throw new SJavacException(String.format(INITIALIZATION_ERROR, token));
                    }
                    return null;
                }
                types.add(variable.getType());
            }
        }
        return types;
    }
}