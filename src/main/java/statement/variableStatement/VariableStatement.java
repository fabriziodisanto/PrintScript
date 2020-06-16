package statement.variableStatement;

import data.values.DataTypeValue;
import errors.InterpreterError;
import statement.Statement;
import statement.StatementVisitor;
import statement.expression.Expression;
import token.Token;

public class VariableStatement implements Statement {

    @Override
    public DataTypeValue accept(StatementVisitor visitor) throws InterpreterError {
        return visitor.visitVariableStatement(this);
    }

    private Token letOrConst;
    private Token name;
    private Token type;
    private Expression value;

    public VariableStatement(Token letOrConst, Token name, Token type, Expression value) {
        this.letOrConst = letOrConst;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Token getLetOrConst() {
        return letOrConst;
    }

    public Token getName() {
        return name;
    }

    public Token getType() {
        return type;
    }

    public Expression getValue() {
        return value;
    }
}
