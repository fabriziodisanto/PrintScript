package expressions;

import data.DataTypeValue;
import errors.InterpreterError;

public class GroupExpression extends Expression {

    private Expression expression;

    public GroupExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <T> DataTypeValue accept(ExpressionVisitor<T> visitor) throws InterpreterError {
        return visitor.visitGroupExpression(this);
    }

    public Expression getExpression() {
        return expression;
    }
}
