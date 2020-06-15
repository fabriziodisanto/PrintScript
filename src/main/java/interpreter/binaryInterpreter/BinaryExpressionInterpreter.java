package interpreter.binaryInterpreter;

import data.types.NumberType;
import data.types.StringType;
import data.values.DataTypeValue;
import errors.InterpreterError;
import token.Token;

public abstract class BinaryExpressionInterpreter {
    public abstract DataTypeValue interpret(DataTypeValue left, Token operator, DataTypeValue right) throws InterpreterError;

    boolean checkNumberOrStringOperators(DataTypeValue left, DataTypeValue right, Token operator) throws InterpreterError {
        if ((isNumberType(left) || isStringType(left)) && (isNumberType(right) || isStringType(right))) return true;
        throw new InterpreterError("Types can not execute this operation ", operator.getType(), operator.getLineNumber(),
                operator.getColPositionStart(), operator.getColPositionEnd());
    }

    private boolean isNumberType(DataTypeValue data) {
        return data.getType() == NumberType.getInstance();
    }

    private boolean isStringType(DataTypeValue data) {
        return data.getType() == StringType.getInstance();
    }

    boolean checkNumberOperators(DataTypeValue left, DataTypeValue right, Token operator) throws InterpreterError {
        if (isNumberType(left) && isNumberType(right)) return true;
        else throw new InterpreterError("Types can not execute this operation ", operator.getType(),
                operator.getLineNumber(), operator.getColPositionStart(),
                operator.getColPositionEnd());
    }
}
