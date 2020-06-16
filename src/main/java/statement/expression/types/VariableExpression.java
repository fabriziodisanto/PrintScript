package statement.expression.types;

import data.values.DataTypeValue;
import statement.StatementVisitor;
import statement.expression.Expression;
import statement.expression.ExpressionVisitor;
import token.Token;

//todo eliminar?
public class VariableExpression extends Expression {

    private Token name;

    public VariableExpression(Token name) {
        this.name = name;
    }

    @Override
    public DataTypeValue accept(StatementVisitor visitor) {
        return visitor.visitVariableExpression(this);
    }

    public Token getName() {
        return name;
    }
}
