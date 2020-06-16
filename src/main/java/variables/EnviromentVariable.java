package variables;

import data.values.DataTypeValue;
import errors.VariableError;

public interface EnviromentVariable {
    void defineConstVariable(String name, DataTypeValue value) throws VariableError;
    void defineVariable(String name, DataTypeValue value) throws VariableError;
    DataTypeValue getValue(String name) throws VariableError;
    void putValue(String name, DataTypeValue value) throws VariableError;
    boolean exists(String name);
}
