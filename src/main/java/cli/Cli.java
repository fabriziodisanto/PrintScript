package cli;

import errors.InterpreterError;
import errors.LexerError;
import errors.ParserError;
import errors.VariableError;
import interpreter.InterpreterImpl;
import parser.Parser;
import parser.ParserImpl;
import parser.statementsParser.StatementParser;
import parser.statementsParser.expressionsParser.types.*;
import picocli.CommandLine;
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

        Map<String, TokenType> specialChars = Utils.getSpecialChars();
        Map<String, TokenType> keywords = Utils.getKeywords();
        if (hasConsts) {
            keywords.put("const", TokenType.CONST);
        }

        Map<Integer, AbstractExpressionParser> expressionParserMap = Utils.getExpressionParserMap();
        Map<Integer, StatementParser> statementParserMap = Utils.getStatementParserMap(hasBooleans);

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
            Map<String, TokenType> booleanWords = Utils.getBooleanWords();
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
        Parser parser = new ParserImpl(tokens, statementParserMap);
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
}

