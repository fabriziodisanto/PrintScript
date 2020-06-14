import errors.LexerError;
import lexer.*;
import org.junit.Before;
import org.junit.Test;
import scanner.Scanner;
import scanner.ScannerImpl;
import token.Token;
import token.TokenFactoryImpl;
import token.TokenType;

import java.util.ArrayList;
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


    @Before
    public void setUpTests(){
        indexAndTokenTypeMap = new HashMap<>();
        indexAndTokenPositionsMap = new HashMap<>();
    }

    @Test
    public void test001_scanExampleCodeSource() throws LexerError {
        StringBuffer stringBuffer = new StringBuffer("let variable = 5 * 4;\nprintln variable");
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
        List<Token> tokens = scanner.analyze().collect(Collectors.toList());
        indexAndTokenTypeMap.put(0, TokenType.LET);
        indexAndTokenTypeMap.put(1, TokenType.IDENTIFIER);
        indexAndTokenTypeMap.put(2, TokenType.EQUAL);
        indexAndTokenTypeMap.put(3, TokenType.NUMBER);
        indexAndTokenTypeMap.put(4, TokenType.STAR);
        indexAndTokenTypeMap.put(5, TokenType.NUMBER);
        indexAndTokenTypeMap.put(6, TokenType.SEMICOLON);
        indexAndTokenTypeMap.put(7, TokenType.PRINTLN);
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
        positions = new Integer[]{2, 0, 7};
        indexAndTokenPositionsMap.put(7, positions);
        positions = new Integer[]{2, 8, 16};
        indexAndTokenPositionsMap.put(8, positions);
        positions = new Integer[]{2, 16, 17};
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

        ArrayList<AbstractLexer> lexersList = new ArrayList<>();
        lexersList.add(booleanLexer);
        lexersList.add(identifierAndKeywordsLexer);
        lexersList.add(numberLexer);
        lexersList.add(specialCharactersLexer);
        lexersList.add(stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersList, new TokenFactoryImpl());
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
    public void test002_scanFailsWithInvalidSpecialChar() throws LexerError {
        StringBuffer stringBuffer = new StringBuffer("@");
        StringLexer stringLexer = new StringLexer(stringBuffer, new TokenFactoryImpl());
        NumberLexer numberLexer = new NumberLexer(stringBuffer, new TokenFactoryImpl());
        IdentifierAndKeywordsLexer identifierAndKeywordsLexer = new IdentifierAndKeywordsLexer(stringBuffer, new TokenFactoryImpl(), keywords);
        SpecialCharactersLexer specialCharactersLexer = new SpecialCharactersLexer(stringBuffer, new TokenFactoryImpl(), specialChars);

        ArrayList<AbstractLexer> lexersList = new ArrayList<>();
        lexersList.add(identifierAndKeywordsLexer);
        lexersList.add(numberLexer);
        lexersList.add(specialCharactersLexer);
        lexersList.add(stringLexer);

        Scanner scanner = new ScannerImpl("textFile", stringBuffer, lexersList, new TokenFactoryImpl());
        scanner.analyze().collect(Collectors.toList());
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

//    @Test
//    public void test001_lexEmptySource(){
//        SpecialCharactersLexer lexer = new SpecialCharactersLexer(new StringBuffer(" "), new TokenFactoryImpl(), keywords);
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 1);
//    }
//
//    @Test
//    public void test002_lexStringSource(){
//        Lexer lexer = new StringLexer(new StringBuffer("\"I am a String\""), new TokenFactoryImpl());
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.STRING);
//        indexAndTokenTypeMap.put(1, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 2);
//
//        Integer[] positions = {1,0,15};
//        indexAndTokenPositionsMap.put(0, positions);
//        assertTokenLineNumberAndColPositions(tokens, indexAndTokenPositionsMap);
//
//        assertEquals("I am a String", tokens.get(0).getValue().getValue());
//    }
//
//    @Test
//    public void test003_lexMultiStringSource(){
//        Lexer lexer = new StringLexer(new StringBuffer("\"I am a String\"\n\"And I am another String\""), new TokenFactoryImpl());
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.STRING);
//        indexAndTokenTypeMap.put(1, TokenType.STRING);
//        indexAndTokenTypeMap.put(2, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 3);
//
//        Integer[] positions = {1,0,15};
//        indexAndTokenPositionsMap.put(0, positions);
//        positions = new Integer[]{2, 0, 25};
//        indexAndTokenPositionsMap.put(1, positions);
//        assertTokenLineNumberAndColPositions(tokens, indexAndTokenPositionsMap);
//
//        assertEquals("I am a String", tokens.get(0).getValue().getValue());
//        assertEquals("And I am another String", tokens.get(1).getValue().getValue());
//    }
//
//    @Test
//    public void test00X_lexMultiLineStringSource(){
//        Lexer lexer = new StringLexer(new StringBuffer("\"I am a\nMultiLineString\""), new TokenFactoryImpl());
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.STRING);
//        indexAndTokenTypeMap.put(1, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 2);
//        Integer[] positions = {2,0,17};
//        indexAndTokenPositionsMap.put(0, positions);
//        assertTokenLineNumberAndColPositions(tokens, indexAndTokenPositionsMap);
//
//        assertEquals("I am a\nMultiLineString", tokens.get(0).getValue().getValue());
//    }
//
//    @Test
//    public void test00X_lexMultiMultiLineStringSource(){
//        Lexer lexer = new StringLexer(new StringBuffer("\"I am a\nMultiLineString\" \"And i am another\nstring\""), new TokenFactoryImpl());
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.STRING);
//        indexAndTokenTypeMap.put(1, TokenType.STRING);
//        indexAndTokenTypeMap.put(2, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 3);
//        Integer[] positions = {2,0,17};
//        indexAndTokenPositionsMap.put(0, positions);
//        positions = new Integer[]{3, 18, 8};
//        indexAndTokenPositionsMap.put(1, positions);
//        assertTokenLineNumberAndColPositions(tokens, indexAndTokenPositionsMap);
//
//        assertEquals("I am a\nMultiLineString", tokens.get(0).getValue().getValue());
//        assertEquals("And i am another\nstring", tokens.get(1).getValue().getValue());
//
//    }
//
//    @Test
//    public void test004_lexIntegerNumberSource(){
//        Lexer lexer = new NumberLexer(new StringBuffer("23"), new TokenFactoryImpl());
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.NUMBER);
//        indexAndTokenTypeMap.put(1, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 2);
//
//        assertEquals(23.0, tokens.get(0).getValue().getValue());
//
//    }
//
//    @Test
//    public void test004_lexDecimalNumberSource(){
//        Lexer lexer = new NumberLexer(new StringBuffer("23.7"), new TokenFactoryImpl());
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.NUMBER);
//        indexAndTokenTypeMap.put(1, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 2);
//
//        assertEquals(23.7, tokens.get(0).getValue().getValue());
//
//    }
//
//    @Test
//    public void test005_lexDecimalNumberDotAnotherNumberSource(){
//        Lexer lexer = new NumberLexer(new StringBuffer("23.7.1998"), new TokenFactoryImpl());
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.NUMBER);
//        indexAndTokenTypeMap.put(1, TokenType.DOT);
//        indexAndTokenTypeMap.put(2, TokenType.NUMBER);
//        indexAndTokenTypeMap.put(3, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 4);
//
//        assertEquals(23.7, tokens.get(0).getValue().getValue());
//        assertEquals(1998.0, tokens.get(2).getValue().getValue());
//
//    }
//
//    @Test
//    public void test006_lexKeywordsSource(){
//        Lexer lexer = new IdentifierAndKeywordsLexer(new StringBuffer("const import let println"), new TokenFactoryImpl(), keywords);
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.CONST);
//        indexAndTokenTypeMap.put(1, TokenType.IMPORT);
//        indexAndTokenTypeMap.put(2, TokenType.LET);
//        indexAndTokenTypeMap.put(3, TokenType.PRINTLN);
//        indexAndTokenTypeMap.put(4, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 5);
//    }
//
//    @Test
//    public void test00X_lexBooleanWordsSource(){
//        Lexer lexer = new BooleanLexer(new StringBuffer("else false if true"), new TokenFactoryImpl(), booleanWords);
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.ELSE);
//        indexAndTokenTypeMap.put(1, TokenType.FALSE);
//        indexAndTokenTypeMap.put(2, TokenType.IF);
//        indexAndTokenTypeMap.put(3, TokenType.TRUE);
//        indexAndTokenTypeMap.put(4, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 5);
//
//        assertEquals(false, tokens.get(1).getValue().getValue());
//        assertEquals(true, tokens.get(3).getValue().getValue());
//    }
//
//    @Test
//    public void test007_lexIdentifierSource(){
//        Lexer lexer = new IdentifierAndKeywordsLexer(new StringBuffer("identifier"), new TokenFactoryImpl(), keywords);
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.IDENTIFIER);
//        indexAndTokenTypeMap.put(1, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 2);
//    }
//
//    @Test
//    public void test008_lexMultiIdentifierSource(){
//        Lexer lexer = new IdentifierAndKeywordsLexer(new StringBuffer("identifier1 identifier_2"), new TokenFactoryImpl(), keywords);
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.IDENTIFIER);
//        indexAndTokenTypeMap.put(1, TokenType.IDENTIFIER);
//        indexAndTokenTypeMap.put(2, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 3);
//    }
//
//    @Test
//    public void test009_lexOtherTokensSource(){
//        Lexer lexer = new SpecialCharactersLexer(new StringBuffer("{}().-+;*/=:< > <= >="), new TokenFactoryImpl(), specialChars);
//        List<Token> tokens = lexer.analyze().collect(Collectors.toList());
//        indexAndTokenTypeMap.put(0, TokenType.LEFT_BRACE);
//        indexAndTokenTypeMap.put(1, TokenType.RIGHT_BRACE);
//        indexAndTokenTypeMap.put(2, TokenType.LEFT_PAREN);
//        indexAndTokenTypeMap.put(3, TokenType.RIGHT_PAREN);
//        indexAndTokenTypeMap.put(4, TokenType.DOT);
//        indexAndTokenTypeMap.put(5, TokenType.MINUS);
//        indexAndTokenTypeMap.put(6, TokenType.PLUS);
//        indexAndTokenTypeMap.put(7, TokenType.SEMICOLON);
//        indexAndTokenTypeMap.put(8, TokenType.STAR);
//        indexAndTokenTypeMap.put(9, TokenType.SLASH);
//        indexAndTokenTypeMap.put(10, TokenType.EQUAL);
//        indexAndTokenTypeMap.put(11, TokenType.COLON);
//        indexAndTokenTypeMap.put(12, TokenType.LESS);
//        indexAndTokenTypeMap.put(13, TokenType.GREATER);
//        indexAndTokenTypeMap.put(14, TokenType.LESS_EQUAL);
//        indexAndTokenTypeMap.put(15, TokenType.GREATER_EQUAL);
//        indexAndTokenTypeMap.put(16, TokenType.EOF);
//        assertTokenType(tokens, indexAndTokenTypeMap);
//
//        assertEquals(tokens.size(), 17);
//    }
}
