package expressions;

import data.DataTypeValue;
import errors.InterpreterError;

public abstract class Expression {

    public abstract <T> DataTypeValue accept(ExpressionVisitor<T> visitor) throws InterpreterError;
}
