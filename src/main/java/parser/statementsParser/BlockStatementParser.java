//package parser.statementsParser;
//
//import errors.ParserError;
//import parser.statementsParser.expressionsParser.ExpressionParser;
//import statement.Statement;
//import statement.importStatement.ImportStatement;
//import token.Token;
//import token.TokenType;
//
//import java.util.Collections;
//import java.util.List;
//
//public class BlockStatementParser extends StatementParser {
//
//    private ExpressionParser expressionParser;
//
//    public BlockStatementParser(ExpressionParser expressionParser) {
//        this.expressionParser = expressionParser;
//    }
//
//
//    @Override
//    public Statement parse(List<Token> tokens) throws ParserError {
//        return new ImportStatement(expressionParser.parse(Collections.singletonList(tokens.get(1))));
//    }
//
//    @Override
//    public boolean matchThisTokens(List<Token> tokens) {
//        if (tokens.size() == 3) return tokens.get(0).getType() == TokenType.IMPORT
//                && tokens.get(1).getType() == TokenType.STRING
//                && tokens.get(2).getType() == TokenType.SEMICOLON;
//        if (tokens.size() == 4) return tokens.get(0).getType() == TokenType.IMPORT
//                && tokens.get(1).getType() == TokenType.STRING
//                && tokens.get(2).getType() == TokenType.SEMICOLON
//                && tokens.get(3).getType() == TokenType.EOF;
//        return false;
//    }
//}
