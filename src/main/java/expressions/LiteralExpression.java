package expressions;

import data.DataTypeValue;

public class LiteralExpression extends Expression {

    private DataTypeValue value;

    public LiteralExpression(DataTypeValue value) {
        this.value = value;
    }

    @Override
    public <T> DataTypeValue accept(ExpressionVisitor<T> visitor) {
        return visitor.visitLiteralExpression(this);
    }

    public DataTypeValue getValue() {
        return value;
    }
}
