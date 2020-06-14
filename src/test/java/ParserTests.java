import errors.LexerError;
import expressions.Expression;
import expressions.factory.ExpressionFactoryImpl;
import scanner.lexer.*;
import org.junit.Before;
import org.junit.Test;
import parser.ParserImpl;
import scanner.Scanner;
import scanner.ScannerImpl;
import token.Token;
import token.factory.TokenFactoryImpl;
import token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class ParserTests {

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

//    todo assert
    @Test
    public void test001_scanExampleCodeSource() throws LexerError {
        StringBuffer stringBuffer = new StringBuffer("(-5 * 4 + (false)) >= 63;");
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

//        ParserImpl parser = new ParserImpl(new ExpressionFactoryImpl(), tokens);
//        Expression expression = parser.parse();
//        System.out.println("hola");
    }
}
