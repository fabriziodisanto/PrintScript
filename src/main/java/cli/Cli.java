package cli;

import errors.InterpreterError;
import errors.LexerError;
import errors.ParserError;
import errors.VariableError;
import interpreter.InterpreterImpl;
import parser.ParserImpl;
import parser.statementsParser.ImportParser;
import parser.statementsParser.PrintParser;
import parser.statementsParser.StatementParser;
import parser.statementsParser.VariableDeclarationParser;
import parser.statementsParser.expressionsParser.ExpressionParser;
import parser.statementsParser.expressionsParser.types.*;
import picocli.CommandLine;
import scanner.LexerProvider;
import scanner.Scanner;
import scanner.ScannerImpl;
import scanner.lexer.*;
import sourceReader.SourceReader;
import sourceReader.SourceReaderImpl;
import statement.Statement;
import token.Token;
import token.TokenType;
import token.factory.TokenFactoryImpl;
import variables.EnviromentVariableImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@CommandLine.Command
public class Cli implements Runnable {

    @CommandLine.Option(names = "-c", description = "consts")
    private boolean hasConsts;

    @CommandLine.Option(names = "-b", description = "booleans", required = true)
    private boolean hasBooleans;

    @CommandLine.Option(names = {"-f", "--file"}, description = "file path", required = true)
    private String filePath;

    @CommandLine.Option(names = {"-r", "--run"}, description = "file path", required = true)
    String runMode;

    public static void main(String[] args) {
        CommandLine.run(new Cli(), args);
    }

    @Override
    public void run() {
        System.out.println("Welcome to PrintScript");

        if(!(runMode.equals("compile") || runMode.equals("interpret"))){
            System.out.println("Invalid run mode");
            System.exit(1);
        }

        Map<String, TokenType> specialChars = getSpecialChars();
        Map<String, TokenType> keywords = getKeywords();
        if (hasConsts) {
            keywords.put("const", TokenType.CONST);
        }

        Map<Integer, AbstractExpressionParser> expressionParserMap = getExpressionParserMap();
        Map<Integer, StatementParser> statementParserMap = getStatementParserMap(hasBooleans);

        StringLexer stringLexer = new StringLexer(new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(new TokenFactoryImpl());
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();

        lexersPrecedenceMap.put(2, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        if (hasBooleans) {
            Map<String, TokenType> booleanWords = getBooleanWords();
            BooleanLexer booleanLexer = new BooleanLexer(new TokenFactoryImpl(), booleanWords);
            lexersPrecedenceMap.put(1, booleanLexer);
        }

        SourceReader sourceReader = new SourceReaderImpl();

        Scanner scanner = null;
        try {
            scanner = new ScannerImpl(filePath, lexersPrecedenceMap, new TokenFactoryImpl(), sourceReader);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Invalid file path");
            System.exit(1);
        }

        Stream<Token> tokens = null;
        try {
            tokens = scanner.analyze();
        } catch (LexerError lexerError) {
            lexerError.printStackTrace();
            System.out.println(lexerError.getMessage());
            System.exit(1);
        }

        ParserImpl parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = null;
        try {
            statementStream = parser.analyze();
        } catch (ParserError parserError) {
            System.out.println(parserError.getMessage());
            System.exit(1);
        }
        if(runMode.equals("compile")) {
            System.out.println("COMPILED!");
            System.exit(0);
        }
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        try {
            interpreter.interpret(statementStream);
        } catch (InterpreterError | VariableError error) {
            System.out.println(error.getMessage());
            System.exit(1);
        }
    }

    private Map<String, TokenType> getKeywords() {
        HashMap<String, TokenType> keywords = new HashMap<>();
        keywords.put("import", TokenType.IMPORT);
        keywords.put("let", TokenType.LET);
        keywords.put("print", TokenType.PRINT);
        keywords.put("number", TokenType.NUMBER_VAR);
        keywords.put("string", TokenType.STRING_VAR);
        return keywords;
    }

    private Map<String, TokenType> getSpecialChars () {
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

    private Map<String, TokenType> getBooleanWords () {
        HashMap<String, TokenType> booleanWords = new HashMap<>();
        booleanWords.put("else", TokenType.ELSE);
        booleanWords.put("false", TokenType.FALSE);
        booleanWords.put("if", TokenType.IF);
        booleanWords.put("true", TokenType.TRUE);
        booleanWords.put("boolean", TokenType.BOOLEAN);
        return booleanWords;
    }

    private Map<Integer, AbstractExpressionParser> getExpressionParserMap () {
        HashMap<Integer, AbstractExpressionParser> expressionsParserMap = new HashMap<>();
        expressionsParserMap.put(0, new AssignExpressionParser());
        expressionsParserMap.put(1, new ComparisonParser());
        expressionsParserMap.put(2, new AdditionParser());
        expressionsParserMap.put(3, new MultiplicationParser());
        expressionsParserMap.put(4, new VariableParser());
        //        expressionsParserMap.put(6, new GroupingParser());
        //        expressionsParserMap.put(4, new UnaryParser());
        expressionsParserMap.put(5, new PrimaryParser());
        return expressionsParserMap;
    }

    private Map<Integer, StatementParser> getStatementParserMap (boolean hasBooleans) {
        Map<Integer, AbstractExpressionParser> expressionParserMap = getExpressionParserMap();

        HashMap<Integer, StatementParser> statementParserMap = new HashMap<>();
        statementParserMap.put(1, new ImportParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(2, new PrintParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(3, new VariableDeclarationParser(new ExpressionParser(expressionParserMap), hasBooleans));
        statementParserMap.put(5, new ExpressionParser(expressionParserMap));
        return statementParserMap;
    }

}

