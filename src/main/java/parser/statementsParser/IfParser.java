//package parser.statementsParser;
//
//import errors.ParserError;
//import parser.statementsParser.expressionsParser.ExpressionParser;
//import statement.Statement;
//import statement.ifStatement.IfStatement;
//import token.Token;
//import token.TokenType;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class IfParser extends StatementParser {
//
//    private ExpressionParser expressionParser;
//
//    public IfParser(ExpressionParser expressionParser) {
//        this.expressionParser = expressionParser;
//    }
//
//    @Override
//    public Statement parse(List<Token> tokens) throws ParserError {
//        return new IfStatement(expressionParser.parse(getTokensBetweenThisTwo(tokens, TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN)),
//                super.parse(getTokensBetweenThisTwo(tokens, TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE)));
//    }
//
//    @Override
//    public boolean matchThisTokens(List<Token> tokens) {
//        if (tokens.size() < 5) return false;
//        ArrayList<TokenType> tokenTypes = new ArrayList<>();
//        return containsAllTheseTokensTypes(tokens, addAll(tokenTypes,TokenType.IF, TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN,
//                TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE));
//    }
//}