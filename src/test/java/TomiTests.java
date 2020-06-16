import errors.InterpreterError;
import errors.LexerError;
import errors.ParserError;
import errors.VariableError;
import interpreter.InterpreterImpl;
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
import token.factory.TokenFactoryImpl;
import token.TokenType;
import variables.EnviromentVariableImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TomiTests {

    private Map<String, TokenType> keywords = getKeywords();

    private Map<String, TokenType> getKeywords() {
        HashMap<String, TokenType> keywords = new HashMap<>();
        keywords.put("const", TokenType.CONST);
        keywords.put("import", TokenType.IMPORT);
        keywords.put("let", TokenType.LET);
        keywords.put("print", TokenType.PRINT);
        keywords.put("number", TokenType.NUMBER_VAR);
        keywords.put("string", TokenType.STRING_VAR);
        keywords.put("boolean", TokenType.BOOLEAN);
        return keywords;
    }

    private Map<String, TokenType> specialChars = getSpecialChars();

    private Map<String, TokenType> getSpecialChars() {
        HashMap<String, TokenType> specialChars = new HashMap<>();
        specialChars.put("{", TokenType.LEFT_BRACE);
        specialChars.put("}", TokenType.RIGHT_BRACE);
        specialChars.put("(", TokenType.LEFT_PAREN);
        specialChars.put(")", TokenType.RIGHT_PAREN);
        specialChars.put(".", TokenType.DOT);
        specialChars.put("-", TokenType.MINUS);
        specialChars.put("+", TokenType.PLUS);
        specialChars.put(";", TokenType.SEMICOLON);
        specialChars.put("*", TokenType.STAR);
        specialChars.put("/", TokenType.SLASH);
        specialChars.put("=", TokenType.EQUAL);
        specialChars.put(":", TokenType.COLON);
        specialChars.put("<", TokenType.LESS);
        specialChars.put(">", TokenType.GREATER);
        specialChars.put("<=", TokenType.LESS_EQUAL);
        specialChars.put(">=", TokenType.GREATER_EQUAL);
        return specialChars;
    }

    private Map<String, TokenType> booleanWords = getBooleanWords();

    private Map<String, TokenType> getBooleanWords() {
        HashMap<String, TokenType> booleanWords = new HashMap<>();
        booleanWords.put("else", TokenType.ELSE);
        booleanWords.put("false", TokenType.FALSE);
        booleanWords.put("if", TokenType.IF);
        booleanWords.put("true", TokenType.TRUE);
        booleanWords.put("boolean", TokenType.BOOLEAN);
        return booleanWords;
    }

    private Map<Integer, AbstractExpressionParser> expressionParserMap = getExpressionParserMap();

    private Map<Integer, AbstractExpressionParser> getExpressionParserMap() {
        HashMap<Integer, AbstractExpressionParser> expressionsParserMap = new HashMap<>();
//        expressionsParserMap.put(6, new GroupingParser());
        expressionsParserMap.put(0, new AssignExpressionParser());
        expressionsParserMap.put(1, new ComparisonParser());
        expressionsParserMap.put(2, new AdditionParser());
        expressionsParserMap.put(3, new MultiplicationParser());
        expressionsParserMap.put(4, new VariableParser());
//        expressionsParserMap.put(4, new UnaryParser());
        expressionsParserMap.put(5, new PrimaryParser());
        return expressionsParserMap;
    }

    private Map<Integer, StatementParser> statementParserMap = getStatementParserMap();

    private Map<Integer, StatementParser> getStatementParserMap() {
        HashMap<Integer, StatementParser> statementParserMap = new HashMap<>();
        statementParserMap.put(1, new ImportParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(2, new PrintParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(3, new VariableDeclarationParser(new ExpressionParser(expressionParserMap), true));
        statementParserMap.put(5, new ExpressionParser(expressionParserMap));
        return statementParserMap;
    }

    //    todo - unary ?
//    todo  ()
    @Test
    public void test001_variablerRassign() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = new ScannerImpl("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/variable-reassign.txt", lexersPrecedenceMap, new TokenFactoryImpl(),
                sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    @Test(expected = VariableError.class)
    public void test002_variableWithoutValues() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = new ScannerImpl("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/variable-without-values.txt", lexersPrecedenceMap, new TokenFactoryImpl(),
                sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    @Test
    public void test003_variables() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = new ScannerImpl("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/variables.txt", lexersPrecedenceMap, new TokenFactoryImpl(),
                sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    @Test(expected = ParserError.class)
    public void test004_declareVariableWithSpaces() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = new ScannerImpl("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/declare-variable-with-spaces.txt", lexersPrecedenceMap, new TokenFactoryImpl(),
                sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    @Test(expected = VariableError.class)
    public void test005_consts() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = new ScannerImpl("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/conts.txt", lexersPrecedenceMap, new TokenFactoryImpl(),
                sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    @Test(expected = ParserError.class)
    public void test006_invalidType() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = new ScannerImpl("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/invalid-type.txt", lexersPrecedenceMap, new TokenFactoryImpl(),
                sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    @Test(expected = VariableError.class)
    public void test007_variableRedeclaration() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = new ScannerImpl("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/variable-redeclaration.txt", lexersPrecedenceMap, new TokenFactoryImpl(),
                sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    @Test(expected = VariableError.class)
    public void test007_wrongType() throws LexerError, ParserError, IOException, InterpreterError, VariableError {
        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = new ScannerImpl("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/wrong-type-assignation", lexersPrecedenceMap, new TokenFactoryImpl(),
                sourceReader);
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

}
