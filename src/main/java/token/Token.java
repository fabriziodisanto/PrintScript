package token;

import data.DataTypeValue;

public class Token {
    private TokenType type;
    private DataTypeValue value;
    private int lineNumber;
    private int colPositionStart;
    private int colPositionEnd;

    public Token(TokenType type, DataTypeValue value, int lineNumber, int colPositionStart, int colPositionEnd) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
        this.colPositionStart = colPositionStart;
        this.colPositionEnd = colPositionEnd;
    }

    public TokenType getType() {
        return type;
    }

    public DataTypeValue getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColPositionStart() {
        return colPositionStart;
    }

    public int getColPositionEnd() {
        return colPositionEnd;
    }
}
