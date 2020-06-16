import cli.Utils;
import errors.InterpreterError;
import errors.LexerError;
import errors.ParserError;
import errors.VariableError;
import interpreter.InterpreterImpl;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import parser.statementsParser.ImportParser;
import parser.statementsParser.PrintParser;
import parser.statementsParser.StatementParser;
import parser.statementsParser.VariableDeclarationParser;
import parser.statementsParser.expressionsParser.ExpressionParser;
import sourceReader.SourceReader;
import sourceReader.SourceReaderImpl;
import statement.Statement;
import parser.ParserImpl;
import parser.statementsParser.expressionsParser.types.*;
import scanner.LexerProvider;
import scanner.lexer.*;
import org.junit.Test;
import scanner.Scanner;
import scanner.ScannerImpl;
import token.Token;
import token.factory.TokenFactory;
import token.factory.TokenFactoryImpl;
import token.TokenType;
import variables.EnviromentVariableImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TomiTests {

    private Map<String, TokenType> keywords = Utils.getKeywords();
    private Map<String, TokenType> specialChars = Utils.getSpecialChars();
    private Map<String, TokenType> booleanWords = Utils.getBooleanWords();
    private Map<Integer, StatementParser> statementParserMap = Utils.getStatementParserMap(true);

    @Test
    public void test001_variablesReAssign() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        TokenFactory tokenFactory = new TokenFactoryImpl();
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = getLexerPrecedenceMap(
                new StringLexer(tokenFactory), new NumberLexer(tokenFactory),
                new BooleanLexer(tokenFactory, booleanWords), new IdentifierAndKeywordsLexer(tokenFactory, keywords),
                new SpecialCharactersLexer(tokenFactory, specialChars));

        SourceReader sourceReader = new SourceReaderImpl();

        String path = "/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/variable-reassign.txt";
        run(lexersPrecedenceMap, sourceReader, path);
    }

    @Test
    public void test002_variableWithoutValues() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        TokenFactory tokenFactory = new TokenFactoryImpl();
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = getLexerPrecedenceMap(
                new StringLexer(tokenFactory), new NumberLexer(tokenFactory),
                new BooleanLexer(tokenFactory, booleanWords), new IdentifierAndKeywordsLexer(tokenFactory, keywords),
                new SpecialCharactersLexer(tokenFactory, specialChars));
        SourceReader sourceReader = new SourceReaderImpl();

        run(lexersPrecedenceMap, sourceReader, "/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/variable-without-values.txt");
    }

    @Test
    public void test003_variables() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        TokenFactory tokenFactory = new TokenFactoryImpl();
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = getLexerPrecedenceMap(
                new StringLexer(tokenFactory), new NumberLexer(tokenFactory),
                new BooleanLexer(tokenFactory, booleanWords), new IdentifierAndKeywordsLexer(tokenFactory, keywords),
                new SpecialCharactersLexer(tokenFactory, specialChars));
        SourceReader sourceReader = new SourceReaderImpl();

        run(lexersPrecedenceMap, sourceReader, "/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/variables.txt");
    }

    @Test(expected = ParserError.class)
    public void test004_declareVariableWithSpaces() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        TokenFactory tokenFactory = new TokenFactoryImpl();
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = getLexerPrecedenceMap(
                new StringLexer(tokenFactory), new NumberLexer(tokenFactory),
                new BooleanLexer(tokenFactory, booleanWords), new IdentifierAndKeywordsLexer(tokenFactory, keywords),
                new SpecialCharactersLexer(tokenFactory, specialChars));
        SourceReader sourceReader = new SourceReaderImpl();

        run(lexersPrecedenceMap, sourceReader, "/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/declare-variable-with-spaces.txt");
    }

    @Test(expected = VariableError.class)
    public void test005_consts() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        TokenFactory tokenFactory = new TokenFactoryImpl();
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = getLexerPrecedenceMap(
                new StringLexer(tokenFactory), new NumberLexer(tokenFactory),
                new BooleanLexer(tokenFactory, booleanWords), new IdentifierAndKeywordsLexer(tokenFactory, keywords),
                new SpecialCharactersLexer(tokenFactory, specialChars));
        SourceReader sourceReader = new SourceReaderImpl();

        run(lexersPrecedenceMap, sourceReader, "/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/conts.txt");
    }

    @Test(expected = ParserError.class)
    public void test006_invalidType() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        TokenFactory tokenFactory = new TokenFactoryImpl();
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = getLexerPrecedenceMap(
                new StringLexer(tokenFactory), new NumberLexer(tokenFactory),
                new BooleanLexer(tokenFactory, booleanWords), new IdentifierAndKeywordsLexer(tokenFactory, keywords),
                new SpecialCharactersLexer(tokenFactory, specialChars));
        SourceReader sourceReader = new SourceReaderImpl();

        run(lexersPrecedenceMap, sourceReader, "/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/invalid-type.txt");
    }

    @Test(expected = VariableError.class)
    public void test007_variableRedeclaration() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        TokenFactory tokenFactory = new TokenFactoryImpl();
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = getLexerPrecedenceMap(
                new StringLexer(tokenFactory), new NumberLexer(tokenFactory),
                new BooleanLexer(tokenFactory, booleanWords), new IdentifierAndKeywordsLexer(tokenFactory, keywords),
                new SpecialCharactersLexer(tokenFactory, specialChars));
        SourceReader sourceReader = new SourceReaderImpl();

        run(lexersPrecedenceMap, sourceReader, "/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/variable-redeclaration.txt");
    }

    @Test(expected = VariableError.class)
    public void test007_wrongType() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        TokenFactory tokenFactory = new TokenFactoryImpl();
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = getLexerPrecedenceMap(
                new StringLexer(tokenFactory), new NumberLexer(tokenFactory),
                new BooleanLexer(tokenFactory, booleanWords), new IdentifierAndKeywordsLexer(tokenFactory, keywords),
                new SpecialCharactersLexer(tokenFactory, specialChars));
        SourceReader sourceReader = new SourceReaderImpl();

        run(lexersPrecedenceMap, sourceReader, "/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/wrong-type-assignation");
    }

    private void run(HashMap<Integer, AbstractLexer> lexersPrecedenceMap, SourceReader sourceReader, String path) throws IOException, LexerError, ParserError, InterpreterError, VariableError {
        Scanner scanner = new ScannerImpl(path, lexersPrecedenceMap, new TokenFactoryImpl(), sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    private HashMap<Integer, AbstractLexer> getLexerPrecedenceMap(StringLexer stringLexer, NumberLexer numberLexer, BooleanLexer booleanLexer, IdentifierAndKeywordsLexer identifierAndKeywordsLexer, SpecialCharactersLexer specialCharactersLexer) {
        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);
        return lexersPrecedenceMap;
    }

}
