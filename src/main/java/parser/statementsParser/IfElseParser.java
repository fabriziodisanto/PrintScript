//package parser.statementsParser;
//
//import token.Token;
//import token.TokenType;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class IfElseParser extends StatementParser {
//
//
////    private ExpressionParser expressionParser;
////
////    public IfElseParser(ExpressionParser expressionParser) {
////        this.expressionParser = expressionParser;
////    }
////
////    TODO STACKEAR BRACES PARA ENCONTRAR LOS PARES CORRECTOS
////    @Override
////    public Statement parse(List<Token> tokens) throws ParserError {
////        return new IfElseStatement(expressionParser.parse(getTokensBetweenThisTwo(tokens, TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN)),
////                super.parse(getTokensBetweenThisTwo(tokens, TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE)));
////    }
//
//
//    @Override
//    public boolean matchThisTokens(List<Token> tokens) {
//        if (tokens.size() < 5) return false;
//        ArrayList<TokenType> tokenTypes = new ArrayList<>();
//        return containsAllTheseTokensTypes(tokens, addAll(tokenTypes,TokenType.IF, TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN,
//                TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE, TokenType.ELSE));
//    }
//}