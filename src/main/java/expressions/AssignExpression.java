package expressions;

import data.DataTypeValue;
import token.Token;

public class AssignExpression extends Expression{

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