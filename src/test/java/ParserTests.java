import errors.LexerError;
import errors.ParserError;
import parser.statementsParser.ImportParser;
import parser.statementsParser.PrintParser;
import parser.statementsParser.StatementParser;
import parser.statementsParser.VariableDeclarationParser;
import parser.statementsParser.expressionsParser.ExpressionParser;
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
        keywords.put("print", TokenType.PRINT);
        keywords.put("number",  TokenType.NUMBER_VAR);
        keywords.put("string",     TokenType.STRING_VAR);
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
        expressionsParserMap.put(0, new AssignExpressionParser());
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
        statementParserMap.put(3, new VariableDeclarationParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(5, new ExpressionParser(expressionParserMap));
        return statementParserMap;
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
        List<Statement> statements = statementStream.collect(Collectors.toList());

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
    }

    @Test(expected = ParserError.class)
    public void test003_scanFailsWithInvalidExpression() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 6+ false = 63;");
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
    }

    @Test(expected = ParserError.class)
    public void test004_scanFailsWithInvalidExpression() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 6+ false = 63");
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
    }

    @Test
    public void test005_scanExampleMultiLineCodeSource() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 + false >= 63;\n 5 - 3 + \"hola\";");
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
        List<Statement> statements = statementStream.collect(Collectors.toList());

//        todo assert? maybe print tree?
    }

    @Test
    public void test006_scanVariableDeclarationStatement() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("let variable: number = 5;");
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
        List<Statement> statements = statementStream.collect(Collectors.toList());

//        todo assert? maybe print tree?
    }

    @Test
    public void test007_scanVariableDeclarationNotInitializedStatement() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("let variable: string;");
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
        List<Statement> statements = statementStream.collect(Collectors.toList());

//        todo assert? maybe print tree?
    }

    @Test
    public void test008_scanVariableDeclarationStatement() throws LexerError, ParserError {
        StringBuffer stringBuffer = new StringBuffer("let var: boolean = false;\n print(var);");
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
        List<Statement> statements = statementStream.collect(Collectors.toList());

//        todo assert? maybe print tree?
    }

////    todo a = a + b falla
//    @Test
//    public void test009_scanVariableDeclarationStatement() throws LexerError, ParserError {
//        StringBuffer stringBuffer = new StringBuffer("let a: number = 2;\n const b: number = 5;\n a = a + b;");
//        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
//        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
//        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
//        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
//        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);
//
//        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
//        lexersPrecedenceMap.put(1, booleanLexer);
//        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
//        lexersPrecedenceMap.put(3, numberLexer);
//        lexersPrecedenceMap.put(4, specialCharactersLexer);
//        lexersPrecedenceMap.put(5, stringLexer);
//
//        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl(), new LexerProvider(lexersPrecedenceMap));
//        Stream<Token> tokens = scanner.analyze();
//
//        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
//        Stream<Statement> statementStream = parser.analyze();
//        List<Statement> statements = statementStream.collect(Collectors.toList());
//
////        todo assert? maybe print tree?
//    }


}
