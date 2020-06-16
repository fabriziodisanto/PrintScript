import errors.*;
import parser.statementsParser.ImportParser;
import parser.statementsParser.PrintParser;
import parser.statementsParser.StatementParser;
import parser.statementsParser.VariableParser;
import parser.statementsParser.expressionsParser.ExpressionParser;
import statement.Statement;
import interpreter.Interpreter;
import interpreter.InterpreterImpl;
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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class InterpreterTests {

    private Map<String, TokenType> keywords = getKeywords();
    private Map<String, TokenType> getKeywords() {
        HashMap<String, TokenType> keywords = new HashMap<>();
        keywords.put("const",   TokenType.CONST);
        keywords.put("import",  TokenType.IMPORT);
        keywords.put("let",     TokenType.LET);
        keywords.put("print", TokenType.PRINT);
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
        booleanWords.put("else",    TokenType.ELSE);
        booleanWords.put("false",   TokenType.FALSE);
        booleanWords.put("if",      TokenType.IF);
        booleanWords.put("true",    TokenType.TRUE);
        booleanWords.put("boolean", TokenType.BOOLEAN);
        return booleanWords;
    }


    private Map<Integer, AbstractExpressionParser> expressionParserMap = getExpressionParserMap();
    private Map<Integer, AbstractExpressionParser> getExpressionParserMap() {
        HashMap<Integer, AbstractExpressionParser> expressionsParserMap = new HashMap<>();
//        expressionsParserMap.put(6, new GroupingParser());
        expressionsParserMap.put(1, new ComparisonParser());
        expressionsParserMap.put(2, new AdditionParser());
        expressionsParserMap.put(3, new MultiplicationParser());
//        expressionsParserMap.put(4, new UnaryParser());
        expressionsParserMap.put(5, new PrimaryParser());
        return expressionsParserMap;
    }

    private Map<Integer, StatementParser> statementParserMap = getStatementParserMap();
    private Map<Integer, StatementParser> getStatementParserMap() {
        HashMap<Integer, StatementParser> statementParserMap = new HashMap<>();
        statementParserMap.put(1, new ImportParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(2, new PrintParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(3, new VariableParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(5, new ExpressionParser(expressionParserMap));
        return statementParserMap;
    }

    @Test(expected = InterpreterError.class)
    public void test001_interpretInvalidCodeSourceFails() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 + false >= 63;");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl(), new LexerProvider(lexersPrecedenceMap));
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl();
        interpreter.interpret(statementStream);
    }

    @Test
    public void test002_interpretMathSumWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("print(5 + 4 - 32); print(37 + 19);");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl(), new LexerProvider(lexersPrecedenceMap));
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();


        Interpreter interpreter = new InterpreterImpl();
        interpreter.interpret(statementStream);
//        todo assert
//        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
//        DataTypeValue result2 = interpreter.visitBinaryExpression((BinaryExpression) expression2);
//
//        assertEquals(-23.0, result.getValue());
//        assertEquals(56.0, result2.getValue());
    }

    @Test
    public void test003_interpretMathMultiplyWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("print(5 * 4 / 2);");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl(), new LexerProvider(lexersPrecedenceMap));
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl();
        interpreter.interpret(statementStream);
//        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
//        assertEquals(10.0, result.getValue());
//        todo assert
    }

    @Test
    public void test004_interpretComparisonWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("print(5 > 4 / 2);");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl(), new LexerProvider(lexersPrecedenceMap));
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl();
        interpreter.interpret(statementStream);

//        Expression expression = expressionList.get(0);
//        Interpreter interpreter = new InterpreterImpl();
//        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
//        assertEquals(true, result.getValue());
//        todo assert
    }

    @Test
    public void test005_interpretComparisonEqualWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("print(5 <= 4 / 2 + 3);");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl(), new LexerProvider(lexersPrecedenceMap));
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl();
        interpreter.interpret(statementStream);

//        Expression expression = expressionList.get(0);
//        Interpreter interpreter = new InterpreterImpl();
//        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
//        assertEquals(true, result.getValue());
    }

    @Test
    public void test005_interpretStringAppendWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("print(\"hola \" + \"que tal \" + 43);");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, booleanLexer);
        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl(), new LexerProvider(lexersPrecedenceMap));
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl();
        interpreter.interpret(statementStream);
//
//        Expression expression = expressionList.get(0);
//        Interpreter interpreter = new InterpreterImpl();
//        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
//        assertEquals("hola que tal 43.0", result.getValue());
//        todo assert
    }
}
