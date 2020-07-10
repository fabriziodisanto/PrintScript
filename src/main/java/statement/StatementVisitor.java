package statement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import errors.VariableError;
import statement.blockStatement.BlockStatement;
import statement.expression.Expression;
import statement.expression.types.*;
import statement.ifStatement.IfElseStatement;
import statement.ifStatement.IfStatement;
import statement.importStatement.ImportStatement;
import statement.printStatement.PrintStatement;
import statement.variableStatement.VariableStatement;

import java.util.List;

public interface StatementVisitor {
    DataTypeValue visitExpressionStatement(Expression expression) throws InterpreterError, VariableError;
    DataTypeValue visitIfStatement(IfStatement ifStatement) throws InterpreterError, VariableError;
    DataTypeValue visitIfElseStatement(IfElseStatement ifElseStatement) throws InterpreterError, VariableError;
    DataTypeValue visitPrintStatement(PrintStatement printStatement) throws InterpreterError, VariableError;
    DataTypeValue visitImportStatement(ImportStatement printStatement);
    DataTypeValue visitVariableStatement(VariableStatement variableStatement) throws InterpreterError, VariableError;
    DataTypeValue visitBlockStatement(BlockStatement blockStatement) throws InterpreterError, VariableError;

    DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError, VariableError;
    DataTypeValue visitAssignExpression(AssignExpression expression) throws InterpreterError, VariableError;
    DataTypeValue visitGroupExpression(GroupExpression expression) throws InterpreterError, VariableError;
    DataTypeValue visitLiteralExpression(LiteralExpression expression);
    DataTypeValue visitUnaryExpression(UnaryExpression expression) throws InterpreterError, VariableError;
    DataTypeValue visitVariableExpression(VariableExpression expression) throws VariableError;
}
