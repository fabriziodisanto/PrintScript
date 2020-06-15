package interpreter;

import data.types.NumberType;
import data.types.StringType;
import data.values.BooleanValue;
import data.values.DataTypeValue;
import data.values.NumberValue;
import data.values.StringValue;
import errors.InterpreterError;
import expressions.*;
import expressions.types.*;
import token.Token;
import token.TokenType;

public class InterpreterImpl<T> implements Interpreter<T> {

    @Override
    public DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError {
        DataTypeValue left = evaluate(expression.getLeftExpression());
        DataTypeValue right = evaluate(expression.getRightExpression());
        Token operator = expression.getOperator();
//        todo sacar el switch
        switch (operator.getType()) {
            case MINUS:
                checkNumberOperators(left, right, operator);
                return new NumberValue((double) left.getValue() - (double) right.getValue());
            case SLASH:
                checkNumberOperators(left, right, operator);
                return new NumberValue((double) left.getValue() / (double) right.getValue());
            case STAR:
                checkNumberOperators(left, right, operator);
                return new NumberValue((double) left.getValue() * (double) right.getValue());
            case PLUS:
                if (checkNumberOperators(left, right, operator)) {
                    return new NumberValue((double) left.getValue() + (double) right.getValue());
                }
                else if (checkNumberOrStringOperators(left, right, operator)){
                    return new StringValue("".concat((String) left.getValue()).concat((String) right.getValue()));
                }
            case GREATER:
                checkNumberOperators(left, right, operator);
                return new BooleanValue((double) left.getValue() > (double) right.getValue());
            case GREATER_EQUAL:
                checkNumberOperators(left, right, operator);
                return new BooleanValue((double) left.getValue() >= (double) right.getValue());
            case LESS:
                checkNumberOperators(left, right, operator);
                return new BooleanValue((double) left.getValue() < (double) right.getValue());
            case LESS_EQUAL:
                return new BooleanValue((double) left.getValue() <= (double) right.getValue());
        }
        return null;
    }

    private boolean checkNumberOrStringOperators(DataTypeValue left, DataTypeValue right, Token operator) throws InterpreterError {
        if ((isNumberType(left) || isStringType(left)) && (isNumberType(right) || isStringType(right))) return true;
        throw new InterpreterError("Strings can not execute this operation "
                + operator.getType().toString()
                + " in line " + operator.getLineNumber()
                + " column from " + operator.getColPositionStart()
                + " to " + operator.getColPositionEnd());
    }

    private boolean isNumberType(DataTypeValue data) {
        return data.getType() == NumberType.getInstance();
    }

    private boolean isStringType(DataTypeValue data) {
        return data.getType() == StringType.getInstance();
    }

    private boolean checkNumberOperators(DataTypeValue left, DataTypeValue right, Token operator) throws InterpreterError {
        if (isNumberType(left) && isNumberType(right)) return true;
        throw new InterpreterError("Booleans or Strings can not execute this operation "
                + operator.getType().toString()
                + " in line " + operator.getLineNumber()
                + " column from " + operator.getColPositionStart()
                + " to " + operator.getColPositionEnd());
    }

    @Override
    public DataTypeValue visitAssignExpression(AssignExpression expression) {
        return null;
    }

    @Override
    public DataTypeValue visitGroupExpression(GroupExpression expression) throws InterpreterError {
        return evaluate(expression.getExpression());
    }

    @Override
    public DataTypeValue visitLiteralExpression(LiteralExpression expression) {
        return expression.getValue();
    }

    @Override
    public DataTypeValue visitUnaryExpression(UnaryExpression expression) throws InterpreterError {
        DataTypeValue right = evaluate(expression.getRightExpression());

        if (expression.getOperator().getType() == TokenType.MINUS) {
            if (isNumberType(right)) return new NumberValue(-((double) right.getValue()));
            else throw new InterpreterError("Operand must be a number"
                    + " in line " + expression.getOperator().getLineNumber()
                    + " column from " + expression.getOperator().getColPositionStart()
                    + " to " + expression.getOperator().getColPositionEnd());
        }
        return null;
    }

    @Override
    public DataTypeValue visitVariableExpression(VariableExpression expression) {
        return null;
    }

    private DataTypeValue evaluate(Expression expression) throws InterpreterError {
        return expression.accept(this);
    }
}
