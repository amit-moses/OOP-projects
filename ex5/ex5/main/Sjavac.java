package ex5.main;

import ex5.main.run.Run;
import ex5.main.valid.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Main class for the SJavac program.
 * This class is responsible for validating and running the SJavac program.
 * It includes methods for command validation, program execution, and SJavac file validation.
 * The program checks for valid SJavac syntax and structure.
 *
 * @autor Emmanuelle Schnitzer
 * @autor Amit Moses
 */
public class Sjavac {
    private static final String INVALID_FILE_ERROR = "2";
    private static final String VALID_SJAVAC = "0";
    private static final String INVALID_SJAVAC = "1";
    private static final String FILE_ERROR = "Unable to open file";
    private static final String NUMBER_ARGUMENTS_ERROR = "Illegal number of arguments for the program";
    private static final String FILE_FORMAT_ERROR = "Wrong file format";
    private static final String FILE_FINISH = ".sjava";

    /**
     * Main method for the SJavac program.
     * Validates the command-line arguments and runs the SJavac program.
     *
     * @param args the command-line arguments
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        try {
            isLegalCommand(args);
            runProgram(args[0]);
            System.out.println(VALID_SJAVAC);
        } catch (IOException e) {
            System.out.println(INVALID_FILE_ERROR);
            System.out.println(e.getMessage());
        } catch (SJavacException e) {
            System.out.println(INVALID_SJAVAC);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Runs the SJavac program with the specified file path.
     *
     * @param path the path to the SJavac file
     * @throws IOException if an I/O error occurs
     * @throws SJavacException if a SJavac validation error occurs
     */
    private static void runProgram(String path) throws IOException, SJavacException {
        Sjavac sjavac = new Sjavac();
        sjavac.validateSJavac(path);
    }

    /**
     * Validates the SJavac file at the specified path.
     * Performs two validation runs with different sets of checkers.
     *
     * @param path the path to the SJavac file
     * @throws SJavacException if a SJavac validation error occurs
     * @throws IOException if an I/O error occurs
     */
    private void validateSJavac(String path) throws SJavacException, IOException {
        List<Checker> firstRun = List.of(
                new ValidEndScope(),
                new ValidNotation(),
                new ValidVariables(false),
                new ValidIdentifier(false)
        );

        List<Checker> secondRun = List.of(
                new ValidEndScope(),
                new ValidNotation(),
                new ValidIdentifier(true),
                new ValidIdentifierScope(true),
                new ValidKeywords(true),
                new ValidVariablesScope(true)
        );

        ValidFunctionDeclaration validFunctionDeclaration = new ValidFunctionDeclaration(false);
        try (FileReader fileReader1 = new FileReader(path);
             FileReader fileReader2 = new FileReader(path)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader1);
            Run run = new Run(firstRun, validFunctionDeclaration);
            run.run(bufferedReader);

            bufferedReader = new BufferedReader(fileReader2);
            validFunctionDeclaration.changeThrow(true);
            run.changeChecker(secondRun, true);

            run.run(bufferedReader);

        } catch (IOException e) {
            throw new IOException(FILE_ERROR);
        }
    }

    /**
     * Validates the command-line arguments for the SJavac program.
     *
     * @param args the command-line arguments
     * @throws IOException if the number of arguments is incorrect or the file format is invalid
     */
    private static void isLegalCommand(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IOException(NUMBER_ARGUMENTS_ERROR);
        }
        if (!args[0].endsWith(FILE_FINISH)) {
            throw new IOException(FILE_FORMAT_ERROR);
        }
    }
}