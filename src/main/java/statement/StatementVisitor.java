package statement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.expression.Expression;
import statement.expression.types.*;
import statement.ifStatement.IfStatement;
import statement.importStatement.ImportStatement;
import statement.printStatement.PrintStatement;
import statement.variableStatement.VariableStatement;

public interface StatementVisitor {
    DataTypeValue visitExpressionStatement(Expression expression) throws InterpreterError;
    DataTypeValue visitIfStatement(IfStatement ifStatement);
    DataTypeValue visitPrintStatement(PrintStatement printStatement) throws InterpreterError;
    DataTypeValue visitImportStatement(ImportStatement printStatement);
    DataTypeValue visitVariableStatement(VariableStatement variableStatement);

    DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError;
    DataTypeValue visitAssignExpression(AssignExpression expression);
    DataTypeValue visitGroupExpression(GroupExpression expression) throws InterpreterError;
    DataTypeValue visitLiteralExpression(LiteralExpression expression);
    DataTypeValue visitUnaryExpression(UnaryExpression expression) throws InterpreterError;
    DataTypeValue visitVariableExpression(VariableExpression expression);
}
