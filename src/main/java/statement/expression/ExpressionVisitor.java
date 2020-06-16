package statement.expression;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.StatementVisitor;
import statement.expression.types.*;

public interface ExpressionVisitor extends StatementVisitor {
    DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError;
    DataTypeValue visitAssignExpression(AssignExpression expression);
    DataTypeValue visitGroupExpression(GroupExpression expression) throws InterpreterError;
    DataTypeValue visitLiteralExpression(LiteralExpression expression);
    DataTypeValue visitUnaryExpression(UnaryExpression expression) throws InterpreterError;
    DataTypeValue visitVariableExpression(VariableExpression expression);
}
