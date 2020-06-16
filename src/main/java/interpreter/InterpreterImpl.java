package interpreter;

import data.types.NumberType;
import data.values.DataTypeValue;
import data.values.NumberValue;
import errors.InterpreterError;
import statement.Statement;
import statement.expression.Expression;
import statement.expression.types.*;
import interpreter.binaryInterpreter.*;
import statement.ifStatement.IfStatement;
import statement.importStatement.ImportStatement;
import statement.printStatement.PrintStatement;
import statement.variableStatement.VariableStatement;
import token.Token;
import token.TokenType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InterpreterImpl implements Interpreter {

    private BinaryExpressionsInterpretersProvider provider;

    public InterpreterImpl() {
        this.provider = new BinaryExpressionsInterpretersProvider();
    }

    @Override
    public DataTypeValue visitExpressionStatement(Expression expression) throws InterpreterError {
        return evaluate(expression);
    }

    @Override
    public DataTypeValue visitIfStatement(IfStatement ifStatement) {
//        todo implement
        return null;
    }

    @Override
    public DataTypeValue visitPrintStatement(PrintStatement printStatement) throws InterpreterError {
        System.out.println(evaluate(printStatement.getExpression()).getValue());
        return null;
    }

    @Override
    public DataTypeValue visitImportStatement(ImportStatement printStatement) {
        //        todo implement
        return null;
    }

    @Override
    public DataTypeValue visitVariableStatement(VariableStatement variableStatement) {
        //        todo implement
        return null;
    }

    @Override
    public DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError {
        DataTypeValue left = evaluate(expression.getLeftExpression());
        DataTypeValue right = evaluate(expression.getRightExpression());
        Token operator = expression.getOperator();
        BinaryExpressionInterpreter binaryExpressionInterpreter = provider.get(operator.getType());
        if (binaryExpressionInterpreter == null) throw new InterpreterError("Operation " + operator.getType().toString()
                + " not supported in line " + operator.getType() + " column from " + operator.getColPositionStart()
                + " to " + operator.getColPositionEnd());
        return binaryExpressionInterpreter.interpret(left, operator, right);
    }

    private boolean isNumberType(DataTypeValue data) {
        return data.getType() == NumberType.getInstance();
    }

    @Override
    public DataTypeValue visitAssignExpression(AssignExpression expression) {
        //        todo implement
        return null;
    }

    @Override
    public DataTypeValue visitGroupExpression(GroupExpression expression) throws InterpreterError {
        return evaluate(expression.getExpression());
    }

    @Override
    public DataTypeValue visitLiteralExpression(LiteralExpression expression) {
        return expression.getValue();
    }

    @Override
    public DataTypeValue visitUnaryExpression(UnaryExpression expression) throws InterpreterError {
        DataTypeValue right = evaluate(expression.getRightExpression());

        if (expression.getOperator().getType() == TokenType.MINUS) {
            if (isNumberType(right)) return new NumberValue(-((double) right.getValue()));
            else throw new InterpreterError("Operand must be a number ", expression.getOperator().getType(),
                    expression.getOperator().getLineNumber(), expression.getOperator().getColPositionStart(),
                    expression.getOperator().getColPositionEnd());
        }
        return null;
    }

    @Override
    public DataTypeValue visitVariableExpression(VariableExpression expression) {
        //        todo implement necesaria?
        return null;
    }

    private DataTypeValue evaluate(Expression expression) throws InterpreterError {
        return expression.accept(this);
    }

    private DataTypeValue execute(Statement statement) throws InterpreterError {
        return statement.accept(this);
    }

    @Override
    public void interpret(Stream<Statement> statements) throws InterpreterError {
        List<Statement> statementsList = statements.collect(Collectors.toList());
        for (Statement statement : statementsList) {
            execute(statement);
        }
    }
}
