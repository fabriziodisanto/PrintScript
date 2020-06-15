package token.factory;

import data.values.DataTypeValue;
import token.Token;
import token.TokenType;

public class TokenFactoryImpl implements TokenFactory {

    @Override
    public Token build(TokenType type, DataTypeValue value, int lineNumber, int colPositionStart, int colPositionEnd) {
        return new Token(type, value, lineNumber, colPositionStart, colPositionEnd);
    }
}
