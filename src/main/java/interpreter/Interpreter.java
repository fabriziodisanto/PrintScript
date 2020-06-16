package interpreter;

import data.values.DataTypeValue;
import errors.InterpreterError;
import errors.VariableError;
import statement.Statement;
import statement.expression.ExpressionVisitor;

import java.util.List;
import java.util.stream.Stream;

public interface Interpreter extends ExpressionVisitor {

    List<DataTypeValue> interpret(Stream<Statement> statements) throws InterpreterError, VariableError;
}
