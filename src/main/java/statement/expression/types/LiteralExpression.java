package statement.expression.types;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.StatementVisitor;
import statement.expression.Expression;
import statement.expression.ExpressionVisitor;

public class LiteralExpression extends Expression {

    private DataTypeValue value;

    public LiteralExpression(DataTypeValue value) {
        this.value = value;
    }

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError {
        return visitor.visitLiteralExpression(this);
    }

    public DataTypeValue getValue() {
        return value;
    }

}
