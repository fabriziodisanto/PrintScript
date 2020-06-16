package variables;

import data.values.DataTypeValue;
import errors.VariableError;

public class Variable {

    private boolean isConst;
    private DataTypeValue value;

    public Variable(boolean isConst, DataTypeValue value) {
        this.isConst = isConst;
        this.value = value;
    }

    public boolean isConst() {
        return isConst;
    }

    public DataTypeValue getValue() {
        return value;
    }

    public void setValue(DataTypeValue value) throws VariableError {
        if(isConst) throw new VariableError("Const variable cannot update its value");
        this.value = value;
    }
}
