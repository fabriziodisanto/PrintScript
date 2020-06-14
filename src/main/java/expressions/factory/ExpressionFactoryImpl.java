package expressions.factory;

import data.DataTypeValue;
import expressions.*;
import token.Token;

public class ExpressionFactoryImpl implements ExpressionFactory {

    @Override
    public BinaryExpression buildBinaryExpression(Expression leftExpression, Token operator, Expression rightExpression) {
        return new BinaryExpression(leftExpression, operator, rightExpression);
    }

    @Override
    public AssignExpression buildAssignExpression(Token name, Expression value) {
        return new AssignExpression(name, value);
    }

    @Override
    public GroupExpression buildGroupExpression(Expression expression) {
        return new GroupExpression(expression);
    }

    @Override
    public LiteralExpression buildLiteralExpression(DataTypeValue value) {
        return new LiteralExpression(value);
    }

    @Override
    public UnaryExpression buildUnaryExpression(Token operator, Expression rightExpression) {
        return new UnaryExpression(operator, rightExpression);
    }

    @Override
    public VariableExpression buildVariableExpression(Token name) {
        return new VariableExpression(name);
    }
}
