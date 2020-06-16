package statement;

import data.values.DataTypeValue;
import errors.InterpreterError;

public interface Statement {

    DataTypeValue accept(StatementVisitor visitor) throws InterpreterError;

}
