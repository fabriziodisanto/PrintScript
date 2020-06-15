package expressions.types;

import data.values.DataTypeValue;
import errors.InterpreterError;
import expressions.Expression;
import expressions.ExpressionVisitor;
import token.Token;

public class BinaryExpression extends Expression {

    private Expression leftExpression;
    private Token operator;
    private Expression rightExpression;

    public BinaryExpression(Expression leftExpression, Token operator, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    @Override
    public <T> DataTypeValue accept(ExpressionVisitor<T> visitor) throws InterpreterError {
        return visitor.visitBinaryExpression(this);
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }
}