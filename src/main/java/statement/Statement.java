package statement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import errors.VariableError;

public interface Statement {

    DataTypeValue accept(StatementVisitor visitor) throws InterpreterError, VariableError;

}
