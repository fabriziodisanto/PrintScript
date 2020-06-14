package token;

import data.DataTypeValue;

public class TokenFactoryImpl implements TokenFactory {

    @Override
    public Token build(TokenType type, DataTypeValue value, int lineNumber, int colPositionStart, int colPositionEnd) {
        return new Token(type, value, lineNumber, colPositionStart, colPositionEnd);
    }
}
