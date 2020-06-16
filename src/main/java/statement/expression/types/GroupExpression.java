package statement.expression.types;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.StatementVisitor;
import statement.expression.Expression;

public class GroupExpression extends Expression {

    private Expression expression;

    public GroupExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError {
        return visitor.visitGroupExpression(this);
    }

    public Expression getExpression() {
        return expression;
    }
}
