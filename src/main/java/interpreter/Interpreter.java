package interpreter;

import errors.InterpreterError;
import statement.Statement;
import statement.expression.ExpressionVisitor;

import java.util.stream.Stream;

public interface Interpreter extends ExpressionVisitor {

    void interpret(Stream<Statement> statements) throws InterpreterError;
}
