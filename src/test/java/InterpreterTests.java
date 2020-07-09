import cli.Utils;
import data.values.DataTypeValue;
import errors.*;
import parser.Parser;
import parser.statementsParser.StatementParser;
import statement.Statement;
import interpreter.Interpreter;
import interpreter.InterpreterImpl;
import parser.ParserImpl;
import scanner.lexer.*;
import org.junit.Test;
import scanner.Scanner;
import scanner.ScannerImpl;
import token.Token;
import token.factory.TokenFactoryImpl;
import token.TokenType;
import variables.EnviromentVariableImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class InterpreterTests {

    private Map<String, TokenType> keywords = Utils.getKeywords();
    private Map<String, TokenType> specialChars = Utils.getSpecialChars();
    private Map<String, TokenType> booleanWords = Utils.getBooleanWords();
    private Map<Integer, StatementParser> statementParserMap = Utils.getStatementParserMap(true);

    @Test(expected = InterpreterError.class)
    public void test001_interpretInvalidCodeSourceFails() throws LexerError, ParserError, InterpreterError, VariableError {
        StringBuffer stringBuffer = new StringBuffer("5 * 4 + false >= 63;");
//        esto no es del tests, llevarlo afuera
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
    }

    @Test
    public void test002_interpretMathSumWorks() throws LexerError, ParserError, InterpreterError, VariableError {
        StringBuffer stringBuffer = new StringBuffer("print(5 + 4 - 32); 37 + 19;");
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        List<DataTypeValue> values = interpreter.interpret(statementStream);
        assertEquals("-23.0", values.get(0).getValue());
        assertEquals(56.0, values.get(1).getValue());
    }

    @Test
    public void test003_interpretMathMultiplyWorks() throws LexerError, ParserError, InterpreterError, VariableError {
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        List<DataTypeValue> values = interpreter.interpret(statementStream);
        assertEquals("10.0", values.get(0).getValue());
    }

    @Test
    public void test004_interpretComparisonWorks() throws LexerError, ParserError, InterpreterError, VariableError {
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        List<DataTypeValue> values = interpreter.interpret(statementStream);
        assertEquals("true", values.get(0).getValue());
    }

    @Test
    public void test005_interpretComparisonEqualWorks() throws LexerError, ParserError, InterpreterError, VariableError {
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        List<DataTypeValue> values = interpreter.interpret(statementStream);
        assertEquals(true, values.get(0).getValue());
    }

    @Test
    public void test005_interpretStringAppendWorks() throws LexerError, ParserError, InterpreterError, VariableError {
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        List<DataTypeValue> values = interpreter.interpret(statementStream);
        assertEquals("hola que tal 43.0", values.get(0).getValue());
    }

    @Test
    public void test006_interpretVariableDeclarationStatement() throws LexerError, ParserError, InterpreterError, VariableError {
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();

        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
        assertEquals(5.0, interpreter.getEnviromentVariable().getValue("variable").getValue());
    }

    @Test
    public void test007_interpretVariableDeclarationNotInitializedStatement() throws LexerError, ParserError, InterpreterError, VariableError {
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();


        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
        assertEquals(null, interpreter.getEnviromentVariable().getValue("variable").getValue());
    }

    @Test
    public void test008_interpretVariableDeclarationStatement() throws LexerError, ParserError, InterpreterError, VariableError {
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
        assertEquals(false, interpreter.getEnviromentVariable().getValue("var").getValue());
    }

    @Test
    public void test009_interpretVariableDeclarationStatement() throws LexerError, ParserError, InterpreterError, VariableError {
        StringBuffer stringBuffer = new StringBuffer("let a: number = 3;\n a = 9 + 2;\nprint(a);");
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
        Stream<Statement> statementStream = parser.analyze();
        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
        interpreter.interpret(statementStream);
        assertEquals(11.0, interpreter.getEnviromentVariable().getValue("a").getValue());
    }

    @Test(expected = VariableError.class)
    public void test009_interpretConstModificationFails() throws LexerError, ParserError, InterpreterError, VariableError {
        StringBuffer stringBuffer = new StringBuffer("const a: number = 3;\n a = 9 + 2;\nprint(a);");
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

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        Stream<Token> tokens = scanner.analyze();

        Parser parser = new ParserImpl(tokens, statementParserMap);
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
