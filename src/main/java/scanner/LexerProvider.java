package scanner;

import scanner.lexer.AbstractLexer;
import scanner.lexer.LexerType;

import java.util.*;

public class LexerProvider {

    private Map<Integer, AbstractLexer> lexerMap;

    private HashMap<LexerType, Map<Integer, AbstractLexer>> lexerProviderMap = new HashMap<>();

    public LexerProvider(Map<Integer, AbstractLexer> lexerMap) {
        this.lexerMap = lexerMap;
    }


    public Map<Integer, AbstractLexer> get(LexerType lexerType) {
        if (lexerProviderMap.isEmpty()) fill();
        return lexerProviderMap.get(lexerType);
    }

    private void fill() {
        lexerProviderMap.put(LexerType.SPECIAL_CHAR, filterByLexerType(LexerType.SPECIAL_CHAR));
        lexerProviderMap.put(LexerType.ALPHA, filterByLexerType(LexerType.ALPHA));
        lexerProviderMap.put(LexerType.DIGIT, filterByLexerType(LexerType.DIGIT));
    }

    private Map<Integer, AbstractLexer> filterByLexerType(LexerType lexerType){
        Map<Integer, AbstractLexer> filteredMap = new HashMap<>();
        for (Map.Entry<Integer, AbstractLexer> entry : lexerMap.entrySet()) {
            if (entry.getValue().getLexerType() == lexerType) filteredMap.put(entry.getKey(), entry.getValue());
        }
        return filteredMap;
    }
}
