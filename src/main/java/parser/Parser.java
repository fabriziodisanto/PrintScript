package parser;

import errors.ParserError;
import statement.Statement;

import java.util.stream.Stream;

public interface Parser {
//    no es legible la firma del metodo, no se sabe como funciona
    Stream<Statement> analyze() throws ParserError;
}
