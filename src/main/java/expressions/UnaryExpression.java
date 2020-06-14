package expressions;

import data.DataTypeValue;
import errors.InterpreterError;
import token.Token;

public class UnaryExpression extends Expression {

    private Token operator;
    private Expression rightExpression;

    public UnaryExpression(Token operator, Expression rightExpression) {
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    @Override
    public <T> DataTypeValue accept(ExpressionVisitor<T> visitor) throws InterpreterError {
        return visitor.visitUnaryExpression(this);
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }
}
