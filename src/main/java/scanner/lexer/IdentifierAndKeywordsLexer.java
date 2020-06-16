package scanner.lexer;

import data.values.StringValue;
import errors.LexerError;
import token.Token;
import token.factory.TokenFactory;
import token.TokenType;

import java.util.Map;

import static token.TokenType.*;

public class IdentifierAndKeywordsLexer extends AbstractLexer{

    private Map<String, TokenType> keywordsMap;

    public IdentifierAndKeywordsLexer(TokenFactory tokenFactory, Map<String, TokenType> keywordsMap) {
        super.lineNumber = 1;
        super.startPosition = 0;
        super.currentPosition = 0;
        super.colPositionStart = 0;
        super.colPositionEnd = 0;
        super.tokenFactory = tokenFactory;
        this.keywordsMap = keywordsMap;
        super.lexerType = LexerType.ALPHA;
    }

    public IdentifierAndKeywordsLexer(StringBuffer codeSource, TokenFactory tokenFactory, Map<String, TokenType> keywordsMap) {
        super.lineNumber = 1;
        super.currentPosition = 0;
        super.colPositionStart = 0;
        super.colPositionEnd = 0;
        super.tokenFactory = tokenFactory;
        this.keywordsMap = keywordsMap;
        super.codeSource = codeSource;
        super.lexerType = LexerType.ALPHA;
    }


    public Token scanToken() throws LexerError {
        char c = consumeNextChar();
        while (super.removeNotUsedChars(c)){
            c = consumeNextChar();
        }
        if (isAlpha(c)) {
            return consumeKeywordOrIdentifier();
        }
        throw new LexerError(lineNumber, colPositionStart, currentPosition);
    }

    private Token consumeKeywordOrIdentifier() {
        while (isAlphaNumeric(checkFollowingToken(0))) consumeNextChar();
        String text = codeSource.substring(startPosition, currentPosition);
        TokenType type = keywordsMap.get(text);
        if (type == null) type = IDENTIFIER;
        StringValue value = null;
        if (type == IDENTIFIER) value = new StringValue(text);
        return tokenFactory.build(type, value, lineNumber, colPositionStart, colPositionEnd);
    }
}
