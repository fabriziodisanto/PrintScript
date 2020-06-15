package interpreter.binaryInterpreter;

import data.values.BooleanValue;
import data.values.DataTypeValue;
import errors.InterpreterError;
import token.Token;

public class LessEqualInterpreter extends BinaryExpressionInterpreter {

    @Override
    public DataTypeValue interpret(DataTypeValue left, Token operator, DataTypeValue right) throws InterpreterError {
        checkNumberOperators(left, right, operator);
        return new BooleanValue((double) left.getValue() <= (double) right.getValue());
    }
}
