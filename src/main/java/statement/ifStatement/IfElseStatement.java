package statement.ifStatement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.Statement;
import statement.StatementVisitor;
import statement.expression.Expression;

public class IfElseStatement extends IfStatement {

    private Statement falseStatement;

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError {
        return null;
    }

    public IfElseStatement(Expression condition, Statement trueStatement, Statement falseStatement) {
        super(condition, trueStatement);
        this.falseStatement = falseStatement;
    }

    public Statement getFalseStatement() {
        return falseStatement;
    }
}
