import data.values.DataTypeValue;
import errors.*;
import expressions.Expression;
import expressions.types.BinaryExpression;
import interpreter.Interpreter;
import interpreter.InterpreterImpl;
import parser.ParserImpl;
import parser.expressionsParser.*;
import parser.expressionsParser.types.*;
import scanner.LexerProvider;
import scanner.lexer.*;
import org.junit.Test;
import scanner.Scanner;
import scanner.ScannerImpl;
import token.Token;
import token.factory.TokenFactoryImpl;
import token.TokenType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class InterpreterTests {

    private Map<String, TokenType> keywords = getKeywords();
    private Map<String, TokenType> getKeywords() {
        HashMap<String, TokenType> keywords = new HashMap<>();
        keywords.put("const",   TokenType.CONST);
        keywords.put("import",  TokenType.IMPORT);
        keywords.put("let",     TokenType.LET);
        keywords.put("println", TokenType.PRINTLN);
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
        return booleanWords;
    }

    private Map<Integer, ExpressionParser> expressionParserMap = getExpressionParserMap();
    private Map<Integer, ExpressionParser> getExpressionParserMap() {
        HashMap<Integer, ExpressionParser> expressionsParserMap = new HashMap<>();
//        expressionsParserMap.put(6, new GroupingParser());
        expressionsParserMap.put(1, new ComparisonParser());
        expressionsParserMap.put(2, new AdditionParser());
        expressionsParserMap.put(3, new MultiplicationParser());
        expressionsParserMap.put(4, new UnaryParser());
        expressionsParserMap.put(5, new PrimaryParser());
        return expressionsParserMap;
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

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
        Expression expression = expressionStream.collect(Collectors.toList()).get(0);

        Interpreter interpreter = new InterpreterImpl();
        interpreter.visitBinaryExpression((BinaryExpression) expression);
    }

    @Test
    public void test002_interpretMathSumWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("5 + 4 - 32; 37 + 19;");
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

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
        List<Expression> expressionList = expressionStream.collect(Collectors.toList());

        Expression expression = expressionList.get(0);
        Expression expression2 = expressionList.get(1);

        Interpreter interpreter = new InterpreterImpl();
        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
        DataTypeValue result2 = interpreter.visitBinaryExpression((BinaryExpression) expression2);

        assertEquals(-23.0, result.getValue());
        assertEquals(56.0, result2.getValue());
    }

    @Test
    public void test003_interpretMathMultiplyWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 / 2;");
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

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
        List<Expression> expressionList = expressionStream.collect(Collectors.toList());

        Expression expression = expressionList.get(0);
        Interpreter interpreter = new InterpreterImpl();
        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
        assertEquals(10.0, result.getValue());
    }

    @Test
    public void test004_interpretComparisonWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("5 > 4 / 2;");
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

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
        List<Expression> expressionList = expressionStream.collect(Collectors.toList());

        Expression expression = expressionList.get(0);
        Interpreter interpreter = new InterpreterImpl();
        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
        assertEquals(true, result.getValue());
    }

    @Test
    public void test005_interpretComparisonEqualWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("5 <= 4 / 2 + 3;");
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

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
        List<Expression> expressionList = expressionStream.collect(Collectors.toList());

        Expression expression = expressionList.get(0);
        Interpreter interpreter = new InterpreterImpl();
        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
        assertEquals(true, result.getValue());
    }

    @Test
    public void test005_interpretStringAppendWorks() throws LexerError, ParserError, InterpreterError {
        StringBuffer stringBuffer = new StringBuffer("\"hola \" + \"que tal \" + 43;");
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

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
        List<Expression> expressionList = expressionStream.collect(Collectors.toList());

        Expression expression = expressionList.get(0);
        Interpreter interpreter = new InterpreterImpl();
        DataTypeValue result = interpreter.visitBinaryExpression((BinaryExpression) expression);
        assertEquals("hola que tal 43.0", result.getValue());
    }
}
