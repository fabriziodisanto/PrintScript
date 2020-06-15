package expressions;

import data.values.DataTypeValue;
import errors.InterpreterError;
import expressions.types.*;

public interface ExpressionVisitor<T> {
    DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError;
    DataTypeValue visitAssignExpression(AssignExpression expression);
    DataTypeValue visitGroupExpression(GroupExpression expression) throws InterpreterError;
    DataTypeValue visitLiteralExpression(LiteralExpression expression);
    DataTypeValue visitUnaryExpression(UnaryExpression expression) throws InterpreterError;
    DataTypeValue visitVariableExpression(VariableExpression expression);
}
