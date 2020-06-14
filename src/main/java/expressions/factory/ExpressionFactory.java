package expressions.factory;

import data.DataTypeValue;
import expressions.*;
import token.Token;

public interface ExpressionFactory {
    BinaryExpression buildBinaryExpression(Expression leftExpression, Token operator, Expression rightExpression);
    AssignExpression buildAssignExpression(Token name, Expression value);
    GroupExpression buildGroupExpression(Expression expression);
    LiteralExpression buildLiteralExpression(DataTypeValue value);
    UnaryExpression buildUnaryExpression(Token operator, Expression rightExpression);
    VariableExpression buildVariableExpression(Token name);
}
