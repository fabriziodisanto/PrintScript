package interpreter;

import data.types.NumberType;
import data.values.DataTypeValue;
import data.values.NumberValue;
import errors.InterpreterError;
import expressions.*;
import expressions.types.*;
import interpreter.binaryInterpreter.*;
import token.Token;
import token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class InterpreterImpl implements Interpreter {

    private Map<TokenType, BinaryExpressionInterpreter> binaryExpressionInterpreterMap;

    public InterpreterImpl() {
        this.binaryExpressionInterpreterMap = getBinaryExpressionInterpreterMap();
    }

    private Map<TokenType, BinaryExpressionInterpreter> getBinaryExpressionInterpreterMap() {
        HashMap<TokenType, BinaryExpressionInterpreter> binaryExpressionInterpreterMap = new HashMap<>();
        binaryExpressionInterpreterMap.put(TokenType.MINUS, new SubstractInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.SLASH, new DivideInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.STAR, new MultiplyInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.PLUS, new SumInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.GREATER, new GreaterInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.GREATER_EQUAL, new GreaterEqualInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.LESS, new LessInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.LESS_EQUAL, new LessEqualInterpreter());
        return binaryExpressionInterpreterMap;
    }

    @Override
    public DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError {
        DataTypeValue left = evaluate(expression.getLeftExpression());
        DataTypeValue right = evaluate(expression.getRightExpression());
        Token operator = expression.getOperator();
        BinaryExpressionInterpreter binaryExpressionInterpreter = binaryExpressionInterpreterMap.get(operator.getType());
        if (binaryExpressionInterpreter == null) throw new InterpreterError("Operation " + operator.getType().toString()
                + " not supported in line " + operator.getType() + " column from " + operator.getColPositionStart()
                + " to " + operator.getColPositionEnd());
        return binaryExpressionInterpreter.interpret(left, operator, right);
    }

    private boolean isNumberType(DataTypeValue data) {
        return data.getType() == NumberType.getInstance();
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
            else throw new InterpreterError("Operand must be a number ", expression.getOperator().getType(),
                    expression.getOperator().getLineNumber(), expression.getOperator().getColPositionStart(),
                    expression.getOperator().getColPositionEnd());
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
