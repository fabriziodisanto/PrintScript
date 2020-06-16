package statement.expression.types;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.StatementVisitor;
import statement.expression.Expression;
import token.Token;

public class UnaryExpression extends Expression {

    private Token operator;
    private Expression rightExpression;

    public UnaryExpression(Token operator, Expression rightExpression) {
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError {
        return visitor.visitUnaryExpression(this);
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }
}
