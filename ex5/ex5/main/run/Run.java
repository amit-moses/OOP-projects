package ex5.main.run;

import ex5.main.symbols.FunctionMatcher;
import ex5.main.symbols.ParamsScopeMatcher;
import ex5.main.utils.Utils;
import ex5.main.valid.Checker;
import ex5.main.valid.SJavacException;
import ex5.main.valid.ValidFunctionDeclaration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * Class responsible for running the validation process on the provided code.
 * It uses a list of checkers to validate each line of code and ensures that
 * functions are properly declared and closed.
 *
 * @autor Emmanuelle Schnitzer
 * @autor Amit Moses
 */
public class Run {
    private List<Checker> checkers;
    private final ValidFunctionDeclaration validateIsFunction;
    private final FunctionMatcher functionMatcher;
    private final ParamsScopeMatcher paramsScopeMatcher;
    private static final String ILLEGAL_CODE_ERROR = "Illegal code %s";
    private static final String RETURN_ERROR = "Last line in function must be return, %s";

    private boolean error;

    /**
     * Constructor for the Run class.
     *
     * @param checkers List of checkers to validate each line of code.
     * @param validateIsFunction Validator for function declarations.
     */
    public Run(List<Checker> checkers, ValidFunctionDeclaration validateIsFunction) {
        this.checkers = checkers;
        this.functionMatcher = new FunctionMatcher();
        this.paramsScopeMatcher = new ParamsScopeMatcher();

        this.validateIsFunction = validateIsFunction;

        this.error = false;
        paramsScopeMatcher.addScope();
    }

    /**
     * Changes the list of checkers and sets the error flag.
     *
     * @param checker New list of checkers.
     * @param error Error flag indicating whether to throw exceptions on validation failure.
     */
    public void changeChecker(List<Checker> checker, boolean error) {
        checkers = checker;
        this.error = error;
    }

    /**
     * Runs the validation process on the provided BufferedReader.
     *
     * @param reader BufferedReader to read the code from.
     * @throws SJavacException if validation fails.
     * @throws IOException if an I/O error occurs.
     */
    public void run(BufferedReader reader) throws SJavacException, IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (validateIsFunction.isValid(line, paramsScopeMatcher, functionMatcher)) {
                if (!validateIsFunction.lastLineValid(checkFunction(reader))) {
                    throw new SJavacException(String.format(RETURN_ERROR,line));
                }
            } else if (!validateLine(line)) {
                throw new SJavacException(String.format(ILLEGAL_CODE_ERROR,line));
            }
        }
    }

    /**
     * Validates a single line of code using the list of checkers.
     *
     * @param line Line of code to validate.
     * @return true if the line is valid, false otherwise.
     * @throws SJavacException if validation fails.
     */
    private boolean validateLine(String line) throws SJavacException {
        if(line == null){
            return false;
        }

        for (Checker checker : checkers) {
            if (checker.isValid(line, paramsScopeMatcher, functionMatcher)) {
                return true;
            }
        }
        if(paramsScopeMatcher.getScopeSize() == 1){
            throw new SJavacException(String.format(ILLEGAL_CODE_ERROR,line));
        }
        return !error;
    }

    /**
     * Checks the function declaration and validates its body.
     *
     * @param reader BufferedReader to read the function body from.
     * @return The last line of the function body.
     * @throws SJavacException if validation fails.
     * @throws IOException if an I/O error occurs.
     */
    private String checkFunction(BufferedReader reader) throws SJavacException, IOException {
        String line;
        String tmpLine = null;
        while (validateLine(line = reader.readLine())) {
            if (Utils.START_SCOPE.matcher(line).matches()) {
                paramsScopeMatcher.addScope();
                checkFunction(reader);
            } else if (Utils.END_SCOPE.matcher(line).matches()) {
                paramsScopeMatcher.removeLast();
                return tmpLine;
            }
            if (!line.trim().isEmpty()) {
                tmpLine = line;
            }
        }
        throw new SJavacException(String.format(ILLEGAL_CODE_ERROR,line));
    }
}