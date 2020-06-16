package statement.expression.types;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.StatementVisitor;
import statement.expression.Expression;
import statement.expression.ExpressionVisitor;
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
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError {
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