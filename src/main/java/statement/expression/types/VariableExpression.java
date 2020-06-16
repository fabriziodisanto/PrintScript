package statement.expression.types;

import data.values.DataTypeValue;
import errors.VariableError;
import statement.StatementVisitor;
import statement.expression.Expression;
import token.Token;

public class VariableExpression extends Expression {

    private Token name;

    public VariableExpression(Token name) {
        this.name = name;
    }

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws VariableError {
        return visitor.visitVariableExpression(this);
    }

    public Token getName() {
        return name;
    }
}
