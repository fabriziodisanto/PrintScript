package interpreter.binaryInterpreter;

import data.values.DataTypeValue;
import data.values.NumberValue;
import errors.InterpreterError;
import token.Token;

public class MultiplyInterpreter extends BinaryExpressionInterpreter{

    @Override
    public DataTypeValue interpret(DataTypeValue left, Token operator, DataTypeValue right) throws InterpreterError {
        checkNumberOperators(left, right, operator);
        return new NumberValue((double) left.getValue() * (double) right.getValue());
    }
}
