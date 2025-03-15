package ex5.main.symbols;

import ex5.main.utils.Type;
import ex5.main.valid.SJavacException;

import java.util.*;

/**
 * Class responsible for managing variable scopes and their associated variables.
 * It maintains a list of scope maps, each representing a scope with its variables.
 * Provides methods to add, remove, and retrieve variables within different scopes.
 *
 * @autor Emmanuelle Schnitzer
 * @autor Amit Moses
 */
public class ParamsScopeMatcher {
    private final ArrayList<HashMap<String, Variable>> scopeMap;
    private static final String DOUBLE_VARIABLE = "Variable %s already exists in this scope";

    /**
     * Constructor for the ParamsScopeMatcher class.
     * Initializes the list of scope maps.
     */
    public ParamsScopeMatcher(){
        scopeMap = new ArrayList<>();
    }

    /**
     * Removes the last scope from the list of scope maps.
     */
    public void removeLast(){
        scopeMap.remove(scopeMap.size()-1);
    }

    /**
     * Adds a new scope to the list of scope maps.
     */
    public void addScope(){
        scopeMap.add(new HashMap<>());
    }

    /**
     * Class representing a variable with its type, finality, initialization status, and scope.
     */
    public static class Variable{
        private final Type type;
        private boolean isFinal;
        private boolean initialized;
        private final boolean global;

        /**
         * Constructor for the Variable class.
         *
         * @param type the type of the variable
         * @param isFinal whether the variable is final
         * @param initialized whether the variable is initialized
         * @param global whether the variable is global
         */
        public Variable(Type type, boolean isFinal, boolean initialized, boolean global){
            this.type = type;
            this.isFinal = isFinal;
            this.initialized = initialized;
            this.global = global;
        }

        /**
         * Gets the type of the variable.
         *
         * @return the type of the variable
         */
        public Type getType(){
            return this.type;
        }

        /**
         * Checks if the variable is initialized.
         *
         * @return true if the variable is initialized, false otherwise
         */
        public boolean isInitialized(){
            return this.initialized;
        }

        /**
         * Checks if the variable is final.
         *
         * @return true if the variable is final, false otherwise
         */
        public boolean isFinal(){
            return this.isFinal;
        }

        /**
         * Checks if the variable is global.
         *
         * @return true if the variable is global, false otherwise
         */
        public boolean isGlobal(){
            return this.global;
        }

        /**
         * Sets the variable as final.
         */
        public void setFinal(){
            this.isFinal = true;
        }

        /**
         * Sets the variable as initialized.
         *
         * @param isGlobalScope whether the variable is in the global scope
         */
        public void setInitialized(boolean isGlobalScope){
            this.initialized = !global || isGlobalScope;
        }
    }

    /**
     * Adds a variable to the last scope in the list of scope maps.
     *
     * @param name the name of the variable
     * @param var the variable to add
     * @return true if the variable was added successfully, false otherwise
     * @throws SJavacException if the variable already exists in the last scope
     */
    public boolean addVariableToLast(String name, Variable var) throws SJavacException{
        if(scopeMap.get(scopeMap.size()-1).containsKey(name)){
            throw new SJavacException(String.format(DOUBLE_VARIABLE, name));
        }

        scopeMap.get(scopeMap.size()-1).put(name,var);
        return true;
    }

    /**
     * Gets the size of the scope map.
     *
     * @return the size of the scope map
     */
    public int getScopeSize(){
        return scopeMap.size();
    }

    /**
     * Checks if a variable exists in the last scope.
     *
     * @param name the name of the variable
     * @return true if the variable exists in the last scope, false otherwise
     */
    public boolean isVariableInLastScope(String name){
        return scopeMap.get(scopeMap.size() - 1).containsKey(name);
    }

    /**
     * Retrieves a variable by its name from the scope maps.
     *
     * @param name the name of the variable
     * @return the variable if found, null otherwise
     */
    public Variable getVariable(String name){
        for(int i = scopeMap.size()-1; i >= 0; i--){
            if(scopeMap.get(i).containsKey(name)){
                return scopeMap.get(i).get(name);
            }
        }
        return null;
    }

    /**
     * Checks if the scope map is empty.
     *
     * @return true if the scope map is empty, false otherwise
     */
    public boolean isEmpty(){
        return scopeMap.isEmpty();
    }
}