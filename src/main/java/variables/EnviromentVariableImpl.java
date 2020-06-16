package variables;

import data.values.DataTypeValue;
import errors.VariableError;

import java.util.HashMap;

public class EnviromentVariableImpl implements EnviromentVariable {

    private HashMap<String, Variable> variablesMap;
    private EnviromentVariable outside;


//    todo ver cual no uso
    public EnviromentVariableImpl() {
        this.variablesMap = new HashMap<>();
        this.outside = null;
    }

    public EnviromentVariableImpl(EnviromentVariable outside) {
        this.variablesMap = new HashMap<>();
        this.outside = outside;
    }

    public EnviromentVariableImpl(HashMap<String, Variable> variablesMap, EnviromentVariable inside) {
        this.variablesMap = variablesMap;
        this.outside = outside;
    }

    public EnviromentVariableImpl(HashMap<String, Variable> variablesMap) {
        this.variablesMap = variablesMap;
        this.outside = null;
    }

    @Override
    public void defineConstVariable(String name, DataTypeValue value) throws VariableError {
        if (exists(name)) throw new VariableError("Variable with name " + name + " was already declared");
        variablesMap.put(name, new Variable(true, value));
    }

    @Override
    public void defineVariable(String name, DataTypeValue value) throws VariableError {
        if (exists(name)) throw new VariableError("Variable with name " + name + " was already declared");
        variablesMap.put(name, new Variable(false, value));
    }

    public boolean exists(String name){
        boolean found = false;
        for (String varName : variablesMap.keySet()) {
            if(varName.equals(name)) {
                found = true;
                break;
            }
        }
        if (!found && outside == null) return false;
        if (!found) return outside.exists(name);
        return true;
    }

    @Override
    public DataTypeValue getValue(String name) throws VariableError {
        if(exists(name)) {
            Variable var = variablesMap.get(name);
            return var.getValue();
        }
        throw new VariableError("Variable with name " + name + " was not declared");
    }

    @Override
    public void putValue(String name, DataTypeValue value) throws VariableError {
        if(exists(name)){
            Variable var = variablesMap.get(name);
            if(var == null) var = new Variable(false, value);
            else var.setValue(value);
            variablesMap.put(name, var);
        }
//        if (!found && outside == null) throw new VariableError("Variable with name " + name + " was not declared");
//        if (!found) {
//            outside.putValue(name, value);
//            return;
//        }

    }
}
