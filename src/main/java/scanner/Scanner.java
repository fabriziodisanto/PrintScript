package scanner;

import errors.LexerError;
import token.Token;

import java.util.stream.Stream;

public interface Scanner {
    Stream<Token> analyze() throws LexerError;
}
