package scanner.lexer;

import data.values.StringValue;
import errors.LexerError;
import token.Token;
import token.factory.TokenFactory;

import static token.TokenType.*;

public class StringLexer extends AbstractLexer{

    public StringLexer(TokenFactory tokenFactory) {
        super.lineNumber = 1;
        super.currentPosition = 0;
        super.colPositionStart = 0;
        super.colPositionEnd = 0;
        super.tokenFactory = tokenFactory;
        super.lexerType = LexerType.SPECIAL_CHAR;
    }

    public Token scanToken() throws LexerError {
        char c = consumeNextChar();
        while (super.removeNotUsedChars(c)){
            c = consumeNextChar();
        }
        if (c == '\"') {
            return consumeString();
        }
        throw new LexerError(lineNumber, colPositionStart, currentPosition);
    }

    private Token consumeString() throws LexerError {
        while (checkFollowingToken(0) != '"' && !isEndOfSource()) {
            if (checkFollowingToken(0) == '\n') {
                lineNumber++;
                colPositionEnd = 0;
            }
            consumeNextChar();
        }
        if (isEndOfSource()) {
            throw new LexerError(lineNumber, colPositionStart, currentPosition);
        }

        // The closing ".
        consumeNextChar();

        // Trim the surrounding quotes.
        String value = codeSource.substring(startPosition + 1, currentPosition - 1);
        return tokenFactory.build(STRING, new StringValue(value), lineNumber, colPositionStart, colPositionEnd);

    }

}

