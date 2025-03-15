package ex5.main.valid;

import ex5.main.symbols.FunctionMatcher;
import ex5.main.symbols.ParamsScopeMatcher;
import ex5.main.utils.Type;
import ex5.main.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Class for validating function declarations in the SJavac program.
 * This class implements the Checker interface and provides logic to
 * check if a line correctly declares a function.
 * It ensures that function declarations follow the SJavac rules and handles parameter validation.
 *
 * @autor Emmanuelle Schnitzer
 * @autor Amit Moses
 */
public class ValidFunctionDeclaration implements Checker {
    /**
     * Flag indicating whether to throw exceptions on validation errors.
     */
    private boolean toThrow;

    /**
     * Legal size for parameter parts.
     */
    private static final int LEGAL_SIZE = 2;

    /**
     * Error message for invalid parameter list.
     */
    private static final String INVALID_PARAM_LIST = "Param list of function is not valid";

    /**
     * Error message for double declaration of a variable.
     */
    private static final String DOUBLE_DECLARATION = "Variable %s already exists in this scope";

    /**
     * Constructs a new ValidFunctionDeclaration instance with the specified error handling behavior.
     *
     * @param toThrow whether to throw exceptions on validation errors
     */
    public ValidFunctionDeclaration(boolean toThrow){
        this.toThrow = toThrow;
    }

    /**
     * Changes the error handling behavior of this instance.
     *
     * @param toThrow whether to throw exceptions on validation errors
     */
    public void changeThrow(boolean toThrow){
        this.toThrow = toThrow;
    }

    /**
     * Checks if the given line is valid according to the SJavac rules for function declarations.
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
        return checkFunction(line, paramsScopeMatcher, functionMatcher, toThrow);
    }

    /**
     * Creates a map of parameters from the given parameter string.
     *
     * @param params the parameter string
     * @return a map of parameter names to variables, or null if the parameter list is invalid
     */
    private static Map<String, ParamsScopeMatcher.Variable> makeParamList(String params) {
        if(!Utils.ARGUMENTS.matcher(params).matches()){
            return null;
        }

        Map<String, ParamsScopeMatcher.Variable> paramMap = new HashMap<>();
        if (params.isEmpty()) {
            return paramMap;
        }

        String[] tokens = params.split(Utils.SEPERATOR);
        for (String token : tokens) {
            token = token.trim();
            String[] parts = token.split(Utils.FIRST_WORD_SPLIT);

            boolean isFinal = token.startsWith(Utils.FINAL);
            if ((isFinal && parts.length != LEGAL_SIZE + 1) || (!isFinal && parts.length != LEGAL_SIZE)) {
                return null;
            }

            Type type = Type.getType(parts[isFinal ? 1 : 0].trim());
            if (type == null) {
                return null;
            }

            ParamsScopeMatcher.Variable variable = new ParamsScopeMatcher.Variable(type,
                    isFinal, true, false);
            String varName = parts[isFinal ? LEGAL_SIZE : 1].trim();
            if (paramMap.containsKey(varName) || !Utils.VAR_NAME.matcher(varName).matches()) {
                return null;
            }
            paramMap.put(varName, variable);
        }
        return paramMap;
    }

    /**
     * Validates the function declaration in the given string.
     *
     * @param line the line to check
     * @param paramsScopeMatcher the matcher for parameter scopes
     * @param functionMatcher the matcher for function declarations
     * @param toThrow whether to throw exceptions on validation errors
     * @return true if the function declaration is valid, false otherwise
     * @throws SJavacException if a validation error occurs
     */
    private static boolean checkFunction(String line, ParamsScopeMatcher paramsScopeMatcher,
                                         FunctionMatcher functionMatcher,
                                         boolean toThrow) throws SJavacException {
        line = line.trim();
        Matcher functionR = Utils.FUNCTION_PATTERN.matcher(line);
        if (!functionR.matches()) {
            return false;
        }
        Map<String, ParamsScopeMatcher.Variable> paramMap = makeParamList(functionR.group(2).trim());
        if (paramMap == null) {
            if(toThrow){
                throw new SJavacException(INVALID_PARAM_LIST);
            }
            return false;
        }

        List<Type> paramList = new ArrayList<>();
        for (ParamsScopeMatcher.Variable variable : paramMap.values()) {
            paramList.add(variable.getType());
        }

        String funcName = functionR.group(1).trim();

        functionMatcher.addFunctionDecleration(funcName, paramList);
        paramsScopeMatcher.addScope();
        for (String key : paramMap.keySet()) {
            if (!paramsScopeMatcher.addVariableToLast(key, paramMap.get(key))) {
                if(toThrow){
                    throw new SJavacException(String.format(DOUBLE_DECLARATION, key));
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the last line of a function is valid according to the SJavac rules.
     *
     * @param line the line to check
     * @return true if the last line is valid, false otherwise
     */
    public boolean lastLineValid(String line) {
        return line != null && Utils.RETURN_PATTERN.matcher(line.trim()).matches();
    }
}