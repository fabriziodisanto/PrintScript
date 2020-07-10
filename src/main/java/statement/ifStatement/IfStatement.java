package statement.ifStatement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import errors.VariableError;
import statement.Statement;
import statement.StatementVisitor;
import statement.expression.Expression;

public class IfStatement implements Statement {

    private Expression condition;
    private Statement trueStatement;

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError, VariableError {
        return visitor.visitIfStatement(this);
    }

    public IfStatement(Expression condition, Statement trueStatement) {
        this.condition = condition;
        this.trueStatement = trueStatement;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getTrueStatement() {
        return trueStatement;
    }
}
