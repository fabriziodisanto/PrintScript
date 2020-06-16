package statement.printStatement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.Statement;
import statement.StatementVisitor;
import statement.expression.Expression;

public class PrintStatement implements Statement {

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError {
        return visitor.visitPrintStatement(this);
    }

    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
