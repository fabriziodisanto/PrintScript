package statement.expression;

import data.values.DataTypeValue;
import errors.InterpreterError;
import errors.VariableError;
import statement.StatementVisitor;
import statement.expression.types.*;

public interface ExpressionVisitor extends StatementVisitor {
    DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError, VariableError;
    DataTypeValue visitAssignExpression(AssignExpression expression) throws InterpreterError, VariableError;
    DataTypeValue visitGroupExpression(GroupExpression expression) throws InterpreterError, VariableError;
    DataTypeValue visitLiteralExpression(LiteralExpression expression);
    DataTypeValue visitUnaryExpression(UnaryExpression expression) throws InterpreterError, VariableError;
    DataTypeValue visitVariableExpression(VariableExpression expression) throws VariableError;
}
