package cli;

import parser.statementsParser.ImportParser;
import parser.statementsParser.PrintParser;
import parser.statementsParser.StatementParser;
import parser.statementsParser.VariableDeclarationParser;
import parser.statementsParser.expressionsParser.ExpressionParser;
import parser.statementsParser.expressionsParser.types.*;
import token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, TokenType> getKeywords() {
        HashMap<String, TokenType> keywords = new HashMap<>();
        keywords.put("import", TokenType.IMPORT);
        keywords.put("let", TokenType.LET);
        keywords.put("print", TokenType.PRINT);
        keywords.put("number", TokenType.NUMBER_VAR);
        keywords.put("string", TokenType.STRING_VAR);
        return keywords;
    }

    public static Map<String, TokenType> getSpecialChars () {
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

    public static Map<String, TokenType> getBooleanWords () {
        HashMap<String, TokenType> booleanWords = new HashMap<>();
        booleanWords.put("else", TokenType.ELSE);
        booleanWords.put("false", TokenType.FALSE);
        booleanWords.put("if", TokenType.IF);
        booleanWords.put("true", TokenType.TRUE);
        booleanWords.put("boolean", TokenType.BOOLEAN);
        return booleanWords;
    }

    public static Map<Integer, AbstractExpressionParser> getExpressionParserMap () {
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

    public static Map<Integer, StatementParser> getStatementParserMap (boolean hasBooleans) {
        Map<Integer, AbstractExpressionParser> expressionParserMap = getExpressionParserMap();

        HashMap<Integer, StatementParser> statementParserMap = new HashMap<>();
        statementParserMap.put(1, new ImportParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(2, new PrintParser(new ExpressionParser(expressionParserMap)));
        statementParserMap.put(3, new VariableDeclarationParser(new ExpressionParser(expressionParserMap), hasBooleans));
        statementParserMap.put(5, new ExpressionParser(expressionParserMap));
        return statementParserMap;
    }

}
