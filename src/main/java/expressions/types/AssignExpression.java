package expressions.types;

import data.values.DataTypeValue;
import expressions.Expression;
import expressions.ExpressionVisitor;
import token.Token;

public class AssignExpression extends Expression {

    private Token name;
    private Expression value;

    public AssignExpression(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> DataTypeValue accept(ExpressionVisitor<T> visitor) {
        return visitor.visitAssignExpression(this);
    }

    public Token getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }
}