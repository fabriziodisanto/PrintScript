import errors.LexerError;
import errors.ParserError;
import expressions.Expression;
import parser.ParserImpl;
import parser.expressionsParser.*;
import scanner.lexer.*;
import org.junit.Test;
import scanner.Scanner;
import scanner.ScannerImpl;
import token.Token;
import token.factory.TokenFactoryImpl;
import token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserTests {

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


//    todo - unary ?
//    todo  ()
    @Test
    public void test001_scanExampleCodeSource() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 + false >= 63;");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        ArrayList<AbstractLexer> lexersList = new ArrayList<>();
        lexersList.add(booleanLexer);
        lexersList.add(identifierAndKeywordsLexer);
        lexersList.add(numberLexer);
        lexersList.add(specialCharactersLexer);
        lexersList.add(stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersList, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
        Expression expression = expressionStream.collect(Collectors.toList()).get(0);

//        todo assert? maybe print tree?
    }

    @Test(expected = ParserError.class)
    public void test002_scanFailsWithInvalidExpression() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 ++ false >= 63;");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        ArrayList<AbstractLexer> lexersList = new ArrayList<>();
        lexersList.add(booleanLexer);
        lexersList.add(identifierAndKeywordsLexer);
        lexersList.add(numberLexer);
        lexersList.add(specialCharactersLexer);
        lexersList.add(stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersList, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
    }

    @Test(expected = ParserError.class)
    public void test003_scanFailsWithInvalidExpression() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 6+ false = 63;");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        ArrayList<AbstractLexer> lexersList = new ArrayList<>();
        lexersList.add(booleanLexer);
        lexersList.add(identifierAndKeywordsLexer);
        lexersList.add(numberLexer);
        lexersList.add(specialCharactersLexer);
        lexersList.add(stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersList, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
    }

    @Test(expected = ParserError.class)
    public void test004_scanFailsWithInvalidExpression() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 6+ false = 63");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        ArrayList<AbstractLexer> lexersList = new ArrayList<>();
        lexersList.add(booleanLexer);
        lexersList.add(identifierAndKeywordsLexer);
        lexersList.add(numberLexer);
        lexersList.add(specialCharactersLexer);
        lexersList.add(stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersList, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
    }

    @Test
    public void test005_scanExampleMultiLineCodeSource() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 + false >= 63;\n 5 - 3 + \"hola\";");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        ArrayList<AbstractLexer> lexersList = new ArrayList<>();
        lexersList.add(booleanLexer);
        lexersList.add(identifierAndKeywordsLexer);
        lexersList.add(numberLexer);
        lexersList.add(specialCharactersLexer);
        lexersList.add(stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersList, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        ParserImpl parser = new ParserImpl(tokens, expressionParserMap);
        Stream<Expression> expressionStream = parser.analyze();
        List<Expression> expressionList = expressionStream.collect(Collectors.toList());

//        todo assert? maybe print tree?
    }
}
