package interpreter.binaryInterpreter;

import data.values.DataTypeValue;
import data.values.NumberValue;
import data.values.StringValue;
import errors.InterpreterError;
import token.Token;

public class SumInterpreter extends BinaryExpressionInterpreter {

    @Override
    public DataTypeValue interpret(DataTypeValue left, Token operator, DataTypeValue right) throws InterpreterError {
        try{
            checkNumberOperators(left, right, operator);
            return new NumberValue((double) left.getValue() + (double) right.getValue());
        } catch (InterpreterError ignored) {
            checkNumberOrStringOperators(left, right, operator);
            return new StringValue((left.getValue().toString()) + right.getValue().toString());
        }
    }
}
