import errors.LexerError;
import scanner.LexerProvider;
import scanner.lexer.*;
import org.junit.Before;
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

import static org.junit.Assert.assertEquals;

public class ScannerTests {

    private Map<Integer, TokenType> indexAndTokenTypeMap;
    private Map<Integer, Integer[]> indexAndTokenPositionsMap;

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


    @Before
    public void setUpTests(){
        indexAndTokenTypeMap = new HashMap<>();
        indexAndTokenPositionsMap = new HashMap<>();
    }

    @Test
    public void test001_scanExampleCodeSource() throws LexerError {
        StringBuffer stringBuffer = new StringBuffer("let variable = 5 * 4;\nprint variable");
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
        List<Token> tokens = scanner.analyze().collect(Collectors.toList());
        indexAndTokenTypeMap.put(0, TokenType.LET);
        indexAndTokenTypeMap.put(1, TokenType.IDENTIFIER);
        indexAndTokenTypeMap.put(2, TokenType.EQUAL);
        indexAndTokenTypeMap.put(3, TokenType.NUMBER);
        indexAndTokenTypeMap.put(4, TokenType.STAR);
        indexAndTokenTypeMap.put(5, TokenType.NUMBER);
        indexAndTokenTypeMap.put(6, TokenType.SEMICOLON);
        indexAndTokenTypeMap.put(7, TokenType.PRINT);
        indexAndTokenTypeMap.put(8, TokenType.IDENTIFIER);
        indexAndTokenTypeMap.put(9, TokenType.EOF);
        assertTokenType(tokens, indexAndTokenTypeMap);

        assertEquals(10, tokens.size());

        Integer[] positions = {1,0,3};
        indexAndTokenPositionsMap.put(0, positions);
        positions = new Integer[]{1, 4, 12};
        indexAndTokenPositionsMap.put(1, positions);
        positions = new Integer[]{1, 13, 14};
        indexAndTokenPositionsMap.put(2, positions);
        positions = new Integer[]{1, 15, 16};
        indexAndTokenPositionsMap.put(3, positions);
        positions = new Integer[]{1, 17, 18};
        indexAndTokenPositionsMap.put(4, positions);
        positions = new Integer[]{1, 19, 20};
        indexAndTokenPositionsMap.put(5, positions);
        positions = new Integer[]{1, 20, 21};
        indexAndTokenPositionsMap.put(6, positions);
        positions = new Integer[]{2, 0, 5};
        indexAndTokenPositionsMap.put(7, positions);
        positions = new Integer[]{2, 6, 14};
        indexAndTokenPositionsMap.put(8, positions);
        positions = new Integer[]{2, 14, 15};
        indexAndTokenPositionsMap.put(9, positions);
        assertTokenLineNumberAndColPositions(tokens, indexAndTokenPositionsMap);

        assertEquals(5.0, tokens.get(3).getValue().getValue());
        assertEquals(4.0, tokens.get(5).getValue().getValue());
    }

    @Test
    public void test002_scanExampleCodeSource2() throws LexerError {
        StringBuffer stringBuffer = new StringBuffer("const variable = 5.2 * \"string\" false");
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
        List<Token> tokens = scanner.analyze().collect(Collectors.toList());
        indexAndTokenTypeMap.put(0, TokenType.CONST);
        indexAndTokenTypeMap.put(1, TokenType.IDENTIFIER);
        indexAndTokenTypeMap.put(2, TokenType.EQUAL);
        indexAndTokenTypeMap.put(3, TokenType.NUMBER);
        indexAndTokenTypeMap.put(4, TokenType.STAR);
        indexAndTokenTypeMap.put(5, TokenType.STRING);
        indexAndTokenTypeMap.put(6, TokenType.FALSE);
        indexAndTokenTypeMap.put(7, TokenType.EOF);
        assertTokenType(tokens, indexAndTokenTypeMap);

        assertEquals(8, tokens.size());

        Integer[] positions = {1,0,5};
        indexAndTokenPositionsMap.put(0, positions);
        positions = new Integer[]{1, 6, 14};
        indexAndTokenPositionsMap.put(1, positions);
        positions = new Integer[]{1, 15, 16};
        indexAndTokenPositionsMap.put(2, positions);
        positions = new Integer[]{1, 17, 20};
        indexAndTokenPositionsMap.put(3, positions);
        positions = new Integer[]{1, 21, 22};
        indexAndTokenPositionsMap.put(4, positions);
        positions = new Integer[]{1, 23, 31};
        indexAndTokenPositionsMap.put(5, positions);
        positions = new Integer[]{1, 32, 37};
        indexAndTokenPositionsMap.put(6, positions);
        positions = new Integer[]{1, 37, 38};
        indexAndTokenPositionsMap.put(7, positions);
        assertTokenLineNumberAndColPositions(tokens, indexAndTokenPositionsMap);

        assertEquals(5.2, tokens.get(3).getValue().getValue());
        assertEquals("string", tokens.get(5).getValue().getValue());
        assertEquals(false, tokens.get(6).getValue().getValue());
    }

    @Test(expected = LexerError.class)
    public void test003_scanFailsWithInvalidSpecialChar() throws LexerError {
        StringBuffer stringBuffer = new StringBuffer("@");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(1, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(2, numberLexer);
        lexersPrecedenceMap.put(3, specialCharactersLexer);
        lexersPrecedenceMap.put(4, stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        scanner.analyze().collect(Collectors.toList());
    }

    @Test
    public void test004_scanWithMorePrecedenceInIdentifierLexFalseAsIdentifier() throws LexerError {
        StringBuffer stringBuffer = new StringBuffer("const variable = 5.2 * \"string\" false");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        BooleanLexer booleanLexer = new BooleanLexer(stringBuffer, new TokenFactoryImpl(), booleanWords);
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        HashMap<Integer, AbstractLexer> lexersPrecedenceMap = new HashMap<>();
        lexersPrecedenceMap.put(2, booleanLexer);
        lexersPrecedenceMap.put(1, identifierAndKeywordsLexer);
        lexersPrecedenceMap.put(3, numberLexer);
        lexersPrecedenceMap.put(4, specialCharactersLexer);
        lexersPrecedenceMap.put(5, stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersPrecedenceMap, new TokenFactoryImpl());
        List<Token> tokens = scanner.analyze().collect(Collectors.toList());
        indexAndTokenTypeMap.put(0, TokenType.CONST);
        indexAndTokenTypeMap.put(1, TokenType.IDENTIFIER);
        indexAndTokenTypeMap.put(2, TokenType.EQUAL);
        indexAndTokenTypeMap.put(3, TokenType.NUMBER);
        indexAndTokenTypeMap.put(4, TokenType.STAR);
        indexAndTokenTypeMap.put(5, TokenType.STRING);
        indexAndTokenTypeMap.put(6, TokenType.IDENTIFIER);
        indexAndTokenTypeMap.put(7, TokenType.EOF);
        assertTokenType(tokens, indexAndTokenTypeMap);

        assertEquals(8, tokens.size());

        Integer[] positions = {1,0,5};
        indexAndTokenPositionsMap.put(0, positions);
        positions = new Integer[]{1, 6, 14};
        indexAndTokenPositionsMap.put(1, positions);
        positions = new Integer[]{1, 15, 16};
        indexAndTokenPositionsMap.put(2, positions);
        positions = new Integer[]{1, 17, 20};
        indexAndTokenPositionsMap.put(3, positions);
        positions = new Integer[]{1, 21, 22};
        indexAndTokenPositionsMap.put(4, positions);
        positions = new Integer[]{1, 23, 31};
        indexAndTokenPositionsMap.put(5, positions);
        positions = new Integer[]{1, 32, 37};
        indexAndTokenPositionsMap.put(6, positions);
        positions = new Integer[]{1, 37, 38};
        indexAndTokenPositionsMap.put(7, positions);
        assertTokenLineNumberAndColPositions(tokens, indexAndTokenPositionsMap);

        assertEquals(5.2, tokens.get(3).getValue().getValue());
        assertEquals("string", tokens.get(5).getValue().getValue());
    }

    private void assertTokenType(List<Token> tokens, Map<Integer, TokenType> indexAndTokenTypeMap){
        for (Map.Entry<Integer,TokenType> entry : indexAndTokenTypeMap.entrySet())
            assertEquals(entry.getValue(), tokens.get(entry.getKey()).getType());
    }

    private void assertTokenLineNumberAndColPositions(List<Token> tokens, Map<Integer, Integer[]> indexAndTokenPositionsMap){
        for (Map.Entry<Integer,Integer[]> entry : indexAndTokenPositionsMap.entrySet()) {
            assertEquals((int) entry.getValue()[0], tokens.get(entry.getKey()).getLineNumber());
            assertEquals((int) entry.getValue()[1], tokens.get(entry.getKey()).getColPositionStart());
            assertEquals((int) entry.getValue()[2], tokens.get(entry.getKey()).getColPositionEnd());
        }
    }
}
