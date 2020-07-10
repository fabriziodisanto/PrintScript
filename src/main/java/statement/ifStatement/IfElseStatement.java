package statement.ifStatement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import errors.VariableError;
import statement.Statement;
import statement.StatementVisitor;
import statement.expression.Expression;

public class IfElseStatement extends IfStatement {

    private Statement falseStatement;

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError, VariableError {
        return visitor.visitIfElseStatement(this);
    }

    public IfElseStatement(Expression condition, Statement trueStatement, Statement falseStatement) {
        super(condition, trueStatement);
        this.falseStatement = falseStatement;
    }

    public Statement getFalseStatement() {
        return falseStatement;
    }
}
