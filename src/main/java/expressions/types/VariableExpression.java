package expressions.types;

import data.values.DataTypeValue;
import expressions.Expression;
import expressions.ExpressionVisitor;
import token.Token;

public class VariableExpression extends Expression {

    private Token name;

    public VariableExpression(Token name) {
        this.name = name;
    }

    @Override
    public <T> DataTypeValue accept(ExpressionVisitor<T> visitor) {
        return visitor.visitVariableExpression(this);
    }

    public Token getName() {
        return name;
    }
}
