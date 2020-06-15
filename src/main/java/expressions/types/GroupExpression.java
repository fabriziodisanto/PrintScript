package expressions.types;

import data.values.DataTypeValue;
import errors.InterpreterError;
import expressions.Expression;
import expressions.ExpressionVisitor;

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
