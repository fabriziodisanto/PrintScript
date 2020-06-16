package interpreter;

import data.types.BooleanType;
import data.types.NumberType;
import data.types.StringType;
import data.types.Type;
import data.values.BooleanValue;
import data.values.DataTypeValue;
import data.values.NumberValue;
import data.values.StringValue;
import errors.InterpreterError;
import errors.VariableError;
import statement.Statement;
import statement.blockStatement.BlockStatement;
import statement.expression.Expression;
import statement.expression.types.*;
import interpreter.binaryInterpreter.*;
import statement.ifStatement.IfStatement;
import statement.importStatement.ImportStatement;
import statement.printStatement.PrintStatement;
import statement.variableStatement.VariableStatement;
import token.Token;
import token.TokenType;
import variables.EnviromentVariable;
import variables.EnviromentVariableImpl;
import variables.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InterpreterImpl implements Interpreter {

    private BinaryExpressionsInterpretersProvider provider;
    private EnviromentVariable enviromentVariable;

    public InterpreterImpl(EnviromentVariable enviromentVariable) {
        this.enviromentVariable = enviromentVariable;
        this.provider = new BinaryExpressionsInterpretersProvider();
    }

    public EnviromentVariable getEnviromentVariable() {
        return enviromentVariable;
    }

    @Override
    public DataTypeValue visitExpressionStatement(Expression expression) throws InterpreterError, VariableError {
        return evaluate(expression);
    }

    @Override
    public DataTypeValue visitIfStatement(IfStatement ifStatement) {
//        todo implement
        return null;
    }

    @Override
    public DataTypeValue visitPrintStatement(PrintStatement printStatement) throws InterpreterError, VariableError {
        if(evaluate(printStatement.getExpression()).getValue() != null) {
            System.out.println(evaluate(printStatement.getExpression()).getValue());
            return new StringValue(evaluate(printStatement.getExpression()).getValue().toString());
        }
        System.out.println("null");
        return new StringValue(null);
    }

    @Override
    public DataTypeValue visitImportStatement(ImportStatement printStatement) {
        //        todo implement
        return null;
    }

    @Override
    public DataTypeValue visitVariableStatement(VariableStatement variableStatement) throws InterpreterError, VariableError {
        TokenType type = variableStatement.getType().getType();
        DataTypeValue dataTypeValue = null;
        if(variableStatement.getValue() == null) {
            if (type == TokenType.NUMBER_VAR) {
                dataTypeValue = new NumberValue(null);
            } else if (type == TokenType.BOOLEAN) {
                dataTypeValue = new BooleanValue(null);
            } else if (type == TokenType.STRING_VAR) {
                dataTypeValue = new StringValue(null);
            }
        }else{
            dataTypeValue = evaluate(variableStatement.getValue());
            checkValueIsSameAsType(variableStatement.getType(), dataTypeValue.getType());
        }
        if(variableStatement.isConst()){
            enviromentVariable.defineConstVariable(variableStatement.getName().getValue().getValue().toString(), dataTypeValue);
        }else{
            enviromentVariable.defineVariable(variableStatement.getName().getValue().getValue().toString(), dataTypeValue);
        }
        return dataTypeValue;
    }

    @Override
    public DataTypeValue visitBlockStatement(BlockStatement blockStatement) throws InterpreterError, VariableError {
        return executeBlock(blockStatement.getStatements(), new EnviromentVariableImpl(enviromentVariable));
    }

    private DataTypeValue executeBlock(List<Statement> statements, EnviromentVariableImpl enviromentVariable) throws InterpreterError, VariableError {
        EnviromentVariable previous = this.enviromentVariable;
        try {
            this.enviromentVariable = enviromentVariable;

            for (Statement statement : statements) {
                execute(statement);
            }
        } finally {
            this.enviromentVariable = previous;
        }
        return null;
    }

    private void checkValueIsSameAsType(Token expected, Type real) throws VariableError {
        if ((expected.getType() == TokenType.NUMBER && real != NumberType.getInstance())
            || (expected.getType() == TokenType.STRING && real != StringType.getInstance())
            || (expected.getType() == TokenType.BOOLEAN && real != BooleanType.getInstance()))
        throw new VariableError("Variable not from type " + expected.getType().toString()
                    + " in line " + expected.getLineNumber() + " from " + expected.getColPositionStart()
                    + " to " + expected.getColPositionEnd());
    }

    @Override
    public DataTypeValue visitBinaryExpression(BinaryExpression expression) throws InterpreterError, VariableError {
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
    public DataTypeValue visitAssignExpression(AssignExpression expression) throws InterpreterError, VariableError {
        DataTypeValue value = evaluate(expression.getValue());
        enviromentVariable.putValue(expression.getName().getValue().getValue().toString(), value);
        return value;
    }

    @Override
    public DataTypeValue visitGroupExpression(GroupExpression expression) throws InterpreterError, VariableError {
        return evaluate(expression.getExpression());
    }

    @Override
    public DataTypeValue visitLiteralExpression(LiteralExpression expression) {
        return expression.getValue();
    }

    @Override
    public DataTypeValue visitUnaryExpression(UnaryExpression expression) throws InterpreterError, VariableError {
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
    public DataTypeValue visitVariableExpression(VariableExpression expression) throws VariableError {
        Token name = expression.getName();
        DataTypeValue value = enviromentVariable.getValue(name.getValue().getValue().toString());
        if (value == null) throw new VariableError("Variable was not initialized in line " + name.getLineNumber()
                + " from " + name.getColPositionStart()
                + " to " + name.getColPositionEnd());
        return value;
    }

    private DataTypeValue evaluate(Expression expression) throws InterpreterError, VariableError {
        return expression.accept(this);
    }

    private DataTypeValue execute(Statement statement) throws InterpreterError, VariableError {
        return statement.accept(this);
    }

    @Override
    public List<DataTypeValue> interpret(Stream<Statement> statements) throws InterpreterError, VariableError {
        List<DataTypeValue> dataTypeValues = new ArrayList<>();
        List<Statement> statementsList = statements.collect(Collectors.toList());
        for (Statement statement : statementsList) {
            dataTypeValues.add(execute(statement));
        }
        return dataTypeValues;
    }
}
