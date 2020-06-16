package statement.expression.types;

import data.values.DataTypeValue;
import errors.InterpreterError;
import errors.VariableError;
import statement.StatementVisitor;
import statement.expression.Expression;
import token.Token;

public class AssignExpression extends Expression {

    private Token name;
    private Expression value;

    public AssignExpression(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError, VariableError {
        return visitor.visitAssignExpression(this);
    }

    public Token getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

}