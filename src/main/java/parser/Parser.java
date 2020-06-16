package parser;

import errors.ParserError;
import statement.Statement;

import java.util.stream.Stream;

public interface Parser {
    Stream<Statement> analyze() throws ParserError;
}
