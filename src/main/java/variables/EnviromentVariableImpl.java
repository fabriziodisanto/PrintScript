package variables;

import data.values.DataTypeValue;
import errors.VariableError;

import java.util.HashMap;

public class EnviromentVariableImpl implements EnviromentVariable {

    private HashMap<String, Variable> variablesMap;

    public EnviromentVariableImpl() {
        this.variablesMap = new HashMap<>();
    }

    public EnviromentVariableImpl(HashMap<String, Variable> variablesMap) {
        this.variablesMap = variablesMap;
    }

    @Override
    public void defineConstVariable(String name, DataTypeValue value) throws VariableError {
        if(getValue(name) != null) throw new VariableError("Variable with name " + name + " was already declared");
        variablesMap.put(name, new Variable(true, value));
    }

    @Override
    public void defineVariable(String name, DataTypeValue value) throws VariableError {
        if(getValue(name) != null) throw new VariableError("Variable with name " + name + " was already declared");
        variablesMap.put(name, new Variable(false, value));
    }

    @Override
    public DataTypeValue getValue(String name) {
        Variable var = variablesMap.get(name);
        if(var != null) return var.getValue();
        return null;
    }

    @Override
    public void putValue(String name, DataTypeValue value) throws VariableError {
        Variable var = variablesMap.get(name);
        if(var == null) throw new VariableError("Variable with name " + name + " was not declared");
        var.setValue(value);
    }
}
