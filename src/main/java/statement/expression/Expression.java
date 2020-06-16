package statement.expression;

import data.values.DataTypeValue;
import errors.InterpreterError;
import errors.VariableError;
import statement.Statement;
import statement.StatementVisitor;

public abstract class Expression implements Statement {

    public abstract DataTypeValue accept(StatementVisitor visitor) throws InterpreterError, VariableError;
}
