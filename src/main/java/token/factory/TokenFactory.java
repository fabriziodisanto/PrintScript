package token.factory;

import data.DataTypeValue;
import token.Token;
import token.TokenType;

public interface TokenFactory {
    Token build(TokenType type, DataTypeValue value, int lineNumber, int colPositionStart, int colPositionEnd);
}
