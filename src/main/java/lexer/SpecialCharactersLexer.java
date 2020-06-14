package lexer;

import errors.LexerError;
import token.Token;
import token.TokenFactory;
import token.TokenType;

import java.util.Map;

import static token.TokenType.*;

public class SpecialCharactersLexer extends AbstractLexer{

    private Map<String, TokenType> specialCharKeywordsMap;


    public SpecialCharactersLexer(StringBuffer codeSource, TokenFactory tokenFactory, Map<String, TokenType> specialCharKeywordsMap) {
        super.codeSource = codeSource;
        super.lineNumber = 1;
        super.currentPosition = 0;
        super.colPositionStart = 0;
        super.colPositionEnd = 0;
        super.tokenFactory = tokenFactory;
        this.specialCharKeywordsMap = specialCharKeywordsMap;
    }

    public Token scanToken() throws LexerError {
        char c = consumeNextChar();
        while (super.removeNotUsedChars(c)){
            c = consumeNextChar();
        }
        return consumeSpecialChar(c);
    }

    private Token consumeSpecialChar(char c) throws LexerError {
        TokenType type = specialCharKeywordsMap.get(String.valueOf(c));
        if (type == null) {
            throw new LexerError(lineNumber, colPositionStart, currentPosition);
        }
        if (type == LESS) {
            return tokenFactory.build(checkIfNextTokenIsThis('=') ? LESS_EQUAL : LESS,
                    null, lineNumber, colPositionStart, colPositionEnd);
        } else if (type == GREATER) {
            return tokenFactory.build(checkIfNextTokenIsThis('=') ? GREATER_EQUAL : GREATER,
                    null, lineNumber, colPositionStart, colPositionEnd);
        } else {
            return tokenFactory.build(type, null, lineNumber, colPositionStart, colPositionEnd);
        }
    }
}

