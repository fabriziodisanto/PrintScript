package expressions;

import data.DataTypeValue;

public class GroupExpression extends Expression {

    private Expression expression;

    public GroupExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <T> DataTypeValue accept(ExpressionVisitor<T> visitor) {
        return visitor.visitGroupExpression(this);
    }

    public Expression getExpression() {
        return expression;
    }
}
