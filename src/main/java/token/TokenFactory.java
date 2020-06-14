package token;

import data.DataTypeValue;

public interface TokenFactory {
    Token build(TokenType type, DataTypeValue value, int lineNumber, int colPositionStart, int colPositionEnd);
}
