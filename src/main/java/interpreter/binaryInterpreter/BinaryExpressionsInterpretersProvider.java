package interpreter.binaryInterpreter;

import token.TokenType;

import java.util.HashMap;

public class BinaryExpressionsInterpretersProvider {

    private static final HashMap<TokenType, BinaryExpressionInterpreter> binaryExpressionInterpreterMap = new HashMap<>();

    public BinaryExpressionInterpreter get(TokenType tokenType) {
        if (binaryExpressionInterpreterMap.isEmpty()) fill();
        return binaryExpressionInterpreterMap.get(tokenType);
    }

    private void fill() {
        binaryExpressionInterpreterMap.put(TokenType.MINUS, new SubstractInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.SLASH, new DivideInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.STAR, new MultiplyInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.PLUS, new SumInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.GREATER, new GreaterInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.GREATER_EQUAL, new GreaterEqualInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.LESS, new LessInterpreter());
        binaryExpressionInterpreterMap.put(TokenType.LESS_EQUAL, new LessEqualInterpreter());
    }
}
