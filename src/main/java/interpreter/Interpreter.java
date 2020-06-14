package interpreter;

import expressions.ExpressionVisitor;

public interface Interpreter<T> extends ExpressionVisitor<T> {
}
