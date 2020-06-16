package scanner.lexer;

import data.values.NumberValue;
import errors.LexerError;
import token.Token;
import token.TokenType;
import token.factory.TokenFactory;

import java.util.Map;

import static token.TokenType.*;

public class NumberLexer extends AbstractLexer{

    public NumberLexer(TokenFactory tokenFactory) {
        super.lineNumber = 1;
        super.currentPosition = 0;
        super.colPositionStart = 0;
        super.colPositionEnd = 0;
        super.tokenFactory = tokenFactory;
        super.lexerType = LexerType.DIGIT;
    }

    public NumberLexer(StringBuffer codeSource, TokenFactory tokenFactory) {
        super.lineNumber = 1;
        super.currentPosition = 0;
        super.colPositionStart = 0;
        super.colPositionEnd = 0;
        super.tokenFactory = tokenFactory;
        super.codeSource = codeSource;
        super.lexerType = LexerType.DIGIT;
    }

    public Token scanToken() throws LexerError {
        char c = consumeNextChar();
        while (super.removeNotUsedChars(c)){
            c = consumeNextChar();
        }
        if (isDigit(c)) {
            return consumeNumber();
        }else{
            throw new LexerError(lineNumber, colPositionStart, currentPosition);
        }
    }


    private Token consumeNumber() {
        while (isDigit(checkFollowingToken(0))) consumeNextChar();
        // Look for a fractional part.
        if (checkFollowingToken(0) == '.' && isDigit(checkFollowingToken(1))) {
            // Consume the "."
            consumeNextChar();
            while (isDigit(checkFollowingToken(0))) consumeNextChar();
        }
        Double value = Double.parseDouble(codeSource.substring(startPosition, currentPosition));
        return tokenFactory.build(NUMBER, new NumberValue(value), lineNumber, colPositionStart, colPositionEnd);

    }
}

