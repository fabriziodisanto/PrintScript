package statement.importStatement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.Statement;
import statement.StatementVisitor;
import statement.expression.Expression;

public class ImportStatement implements Statement {

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError {
        return visitor.visitImportStatement(this);
    }

    private Expression route;

    public ImportStatement(Expression route) {
        this.route = route;
    }

    public Expression getRoute() {
        return route;
    }
}
