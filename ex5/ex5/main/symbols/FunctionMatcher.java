package ex5.main.symbols;

import ex5.main.utils.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for matching function declarations with their parameters.
 * It maintains a map of function names to their parameter types and provides
 * methods to add and check function declarations.
 *
 * @author Emmanuelle Schnitzer
 * @author Amit Moses
 */
public class FunctionMatcher {
    private final Map<String, List<Type>> functionMap;

    /**
     * Constructor for the FunctionMatcher class.
     * Initializes the function map.
     */
    public FunctionMatcher(){
        this.functionMap = new HashMap<>();
    }

    /**
     * Adds a function declaration to the function map.
     *
     * @param name the name of the function
     * @param params the list of parameter types for the function
     */
    public void addFunctionDecleration(String name, List<Type> params){
        functionMap.put(name,params);
    }

    /**
     * Checks if a function declaration with the given name and parameters exists in the function map.
     *
     * @param name the name of the function
     * @param params the list of parameter types to check
     * @return true if a matching function declaration exists, false otherwise
     */
    public boolean isEqualObj(String name, List<Type> params){
        List<Type> funcList = functionMap.get(name);
        if(funcList == null || funcList.size() != params.size()){
            return false;
        }

        for(int i = 0; i < funcList.size(); i++){
            if(funcList.get(i) != params.get(i)){
                return false;
            }
        }
        return true;
    }
}