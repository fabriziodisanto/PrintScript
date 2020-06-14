package scanner.lexer;

import errors.LexerError;
import token.Token;

public interface Lexer {
    Token scanToken() throws LexerError;
}
