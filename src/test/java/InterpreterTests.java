//import data.values.DataTypeValue;
//import errors.*;
//import parser.statementsParser.ImportParser;
//import parser.statementsParser.PrintParser;
//import parser.statementsParser.StatementParser;
//import parser.statementsParser.VariableDeclarationParser;
//import parser.statementsParser.expressionsParser.ExpressionParser;
//import statement.Statement;
//import interpreter.Interpreter;
//import interpreter.InterpreterImpl;
//import parser.ParserImpl;
//import parser.statementsParser.expressionsParser.types.*;
//import scanner.LexerProvider;
//import scanner.lexer.*;
//import org.junit.Test;
//import scanner.Scanner;
//import scanner.ScannerImpl;
//import token.Token;
//import token.factory.TokenFactoryImpl;
//import token.TokenType;
//import variables.EnviromentVariableImpl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Stream;
//
//import static org.junit.Assert.assertEquals;
//
//
//public class InterpreterTests {
//
//    private Map<String, TokenType> keywords = getKeywords();
//    private Map<String, TokenType> getKeywords() {
//        HashMap<String, TokenType> keywords = new HashMap<>();
//        keywords.put("const",   TokenType.CONST);
//        keywords.put("import",  TokenType.IMPORT);
//        keywords.put("let",     TokenType.LET);
//        keywords.put("print", TokenType.PRINT);
//        keywords.put("number",  TokenType.NUMBER_VAR);
//        keywords.put("string",     TokenType.STRING_VAR);
//        keywords.put("boolean", TokenType.BOOLEAN);
//        return keywords;
//    }
//
//    private Map<String, TokenType> specialChars = getSpecialChars();
//    private Map<String, TokenType> getSpecialChars() {
//        HashMap<String, TokenType> specialChars = new HashMap<>();
//        specialChars.put("{", TokenType.LEFT_BRACE);
//        specialChars.put("}", TokenType.RIGHT_BRACE);
//        specialChars.put("(", TokenType.LEFT_PAREN);
//        specialChars.put(")", TokenType.RIGHT_PAREN);
//        specialChars.put(".", TokenType.DOT);
//        specialChars.put("-", TokenType.MINUS);
//        specialChars.put("+", TokenType.PLUS);
//        specialChars.put(";", TokenType.SEMICOLON);
//        specialChars.put("*", TokenType.STAR);
//        specialChars.put("/", TokenType.SLASH);
//        specialChars.put("=", TokenType.EQUAL);
//        specialChars.put(":", TokenType.COLON);
//        specialChars.put("<", TokenType.LESS);
//        specialChars.put(">", TokenType.GREATER);
//        specialChars.put("<=", TokenType.LESS_EQUAL);
//        specialChars.put(">=", TokenType.GREATER_EQUAL);
//        return specialChars;
//    }
//
//    private Map<String, TokenType> booleanWords = getBooleanWords();
//    private Map<String, TokenType> getBooleanWords() {
//        HashMap<String, TokenType> booleanWords = new HashMap<>();
//        booleanWords.put("else",    TokenType.ELSE);
//        booleanWords.put("false",   TokenType.FALSE);
//        booleanWords.put("if",      TokenType.IF);
//        booleanWords.put("true",    TokenType.TRUE);
//        booleanWords.put("boolean", TokenType.BOOLEAN);
//        return booleanWords;
//    }
//
//
//    private Map<Integer, AbstractExpressionParser> expressionParserMap = getExpressionParserMap();
//    private Map<Integer, AbstractExpressionParser> getExpressionParserMap() {
//        HashMap<Integer, AbstractExpressionParser> expressionsParserMap = new HashMap<>();
////        expressionsParserMap.put(6, new GroupingParser());
//        expressionsParserMap.put(0, new AssignExpressionParser());
//        expressionsParserMap.put(1, new ComparisonParser());
//        expressionsParserMap.put(2, new AdditionParser());
//        expressionsParserMap.put(3, new MultiplicationParser());
////        expressionsParserMap.put(4, new UnaryParser());
//        expressionsParserMap.put(4, new VariableParser());
//        expressionsParserMap.put(5, new PrimaryParser());
//        return expressionsParserMap;
//    }
//
//    private Map<Integer, StatementParser> statementParserMap = getStatementParserMap();
//    private Map<Integer, StatementParser> getStatementParserMap() {
//        HashMap<Integer, StatementParser> statementParserMap = new HashMap<>();
//        statementParserMap.put(1, new ImportParser(new ExpressionParser(expressionParserMap)));
//        statementParserMap.put(2, new PrintParser(new ExpressionParser(expressionParserMap)));
//        statementParserMap.put(3, new VariableDeclarationParser(new ExpressionParser(expressionParserMap)));
//        statementParserMap.put(5, new ExpressionParser(expressionParserMap));
//        return statementParserMap;
//    }
//
//    @Test(expected = InterpreterError.class)
//    public void test001_interpretInvalidCodeSourceFails() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("5 * 4 + false >= 63;");
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
//
//        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        interpreter.interpret(statementStream);
//    }
//
//    @Test
//    public void test002_interpretMathSumWorks() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("print(5 + 4 - 32); 37 + 19;");
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
//
//        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        List<DataTypeValue> values = interpreter.interpret(statementStream);
//        assertEquals("-23.0", values.get(0).getValue());
//        assertEquals(56.0, values.get(1).getValue());
//    }
//
//    @Test
//    public void test003_interpretMathMultiplyWorks() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("print(5 * 4 / 2);");
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
//
//        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        List<DataTypeValue> values = interpreter.interpret(statementStream);
//        assertEquals("10.0", values.get(0).getValue());
////        todo assert
//    }
//
//    @Test
//    public void test004_interpretComparisonWorks() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("print(5 > 4 / 2);");
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
//
//        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        List<DataTypeValue> values = interpreter.interpret(statementStream);
//        assertEquals("true", values.get(0).getValue());
//    }
//
//    @Test
//    public void test005_interpretComparisonEqualWorks() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("5 <= 4 / 2 + 3;");
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
//
//        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        List<DataTypeValue> values = interpreter.interpret(statementStream);
//        assertEquals(true, values.get(0).getValue());
//    }
//
//    @Test
//    public void test005_interpretStringAppendWorks() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("print(\"hola \" + \"que tal \" + 43);");
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
//
//        Interpreter interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        List<DataTypeValue> values = interpreter.interpret(statementStream);
//        assertEquals("hola que tal 43.0", values.get(0).getValue());
//    }
//
//    @Test
//    public void test006_interpretVariableDeclarationStatement() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("let variable: number = 5;");
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
//
//        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        interpreter.interpret(statementStream);
//        assertEquals(5.0, interpreter.getEnviromentVariable().getValue("variable").getValue());
//    }
//
//    @Test
//    public void test007_interpretVariableDeclarationNotInitializedStatement() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("let variable: string;");
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
//
//
//        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        interpreter.interpret(statementStream);
//        assertEquals(null, interpreter.getEnviromentVariable().getValue("variable").getValue());
//    }
//
//    @Test
//    public void test008_interpretVariableDeclarationStatement() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("let var: boolean = false;\n print(var);");
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
//        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        interpreter.interpret(statementStream);
//        assertEquals(false, interpreter.getEnviromentVariable().getValue("var").getValue());
//    }
//
//    @Test
//    public void test009_interpretVariableDeclarationStatement() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("let a: number = 3;\n a = 9 + 2;\nprint(a);");
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
//        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        interpreter.interpret(statementStream);
//        assertEquals(11.0, interpreter.getEnviromentVariable().getValue("a").getValue());
//    }
//
//    @Test(expected = VariableError.class)
//    public void test009_interpretConstModificationFails() throws LexerError, ParserError, InterpreterError, VariableError {
//        StringBuffer stringBuffer = new StringBuffer("const a: number = 3;\n a = 9 + 2;\nprint(a);");
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
//        InterpreterImpl interpreter = new InterpreterImpl(new EnviromentVariableImpl());
//        interpreter.interpret(statementStream);
//    }
//
//}
