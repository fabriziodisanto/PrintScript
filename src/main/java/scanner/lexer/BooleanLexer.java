package scanner.lexer;

import data.BooleanValue;
import errors.LexerError;
import token.Token;
import token.factory.TokenFactory;
import token.TokenType;

import java.util.Map;

import static token.TokenType.*;

public class BooleanLexer extends AbstractLexer {

    private Map<String, TokenType> booleanKeywordsMap;

    //we have this booleanKeywords map because maybe in the future we would like to add while, elif, etc
    public BooleanLexer(StringBuffer codeSource, TokenFactory tokenFactory, Map<String, TokenType> booleanKeywords) {
        super.codeSource = codeSource;
        super.lineNumber = 1;
        super.currentPosition = 0;
        super.colPositionStart = 0;
        super.colPositionEnd = 0;
        super.tokenFactory = tokenFactory;
        this.booleanKeywordsMap = booleanKeywords;
        super.lexerType = LexerType.ALPHA;
    }

    public Token scanToken() throws LexerError {
        char c = consumeNextChar();
        while (super.removeNotUsedChars(c)){
            c = consumeNextChar();
        }
        if (isAlpha(c)) {
            return consumeBoolean();
        }
        throw new LexerError(lineNumber, colPositionStart, currentPosition);
    }

    private Token consumeBoolean() throws LexerError {
        while (isAlpha(checkFollowingToken(0))) consumeNextChar();
        String text = codeSource.substring(startPosition, currentPosition);
        TokenType type = booleanKeywordsMap.get(text);
        if (type == null) {
            throw new LexerError(lineNumber, colPositionStart, currentPosition);
        } else if(type == TRUE || type == FALSE) {
            return tokenFactory.build(type, new BooleanValue(Boolean.valueOf(text)), lineNumber, colPositionStart, colPositionEnd);
        }
        return tokenFactory.build(type, null, lineNumber, colPositionStart, colPositionEnd);

    }
}

