package scanner;

import errors.LexerError;
import scanner.lexer.AbstractLexer;
import scanner.lexer.LexerType;
import token.Token;
import token.factory.TokenFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static token.TokenType.EOF;

//mhmh extenderlo del abstract scanner.lexer?
public class ScannerImpl implements Scanner {

    // todo private SourceReader sourceReader;
    private String fileName;
    private StringBuffer codeSource;
    private int lineNumber = 1;
    private int currentPosition = 0;
    private int colPositionStart = 0;
    private int colPositionEnd = 0;
    private Stream<Token> tokenList;
    private TokenFactory tokenFactory;
    private List<AbstractLexer> lexerList;

//    todo, hacer que el code source venga del metodo de leer desde el filename
    public ScannerImpl(String fileName, StringBuffer codeSource, List<AbstractLexer> lexerList, TokenFactory tokenFactory) {
        this.fileName = fileName;
        this.codeSource = codeSource;
        this.tokenList = new ArrayList<Token>().stream();
//        TODO importante, boolean scanner.lexer antes que el keyword, sino los convierte en identifiers
        this.lexerList = lexerList;
        this.tokenFactory = tokenFactory;
    }

    private Boolean isEndOfSource() {
        return codeSource == null || currentPosition >= codeSource.length();
    }

    public Stream<Token> analyze() throws LexerError {
        while (!isEndOfSource()) {
//            todo mhm queremos try catchearlo?
            try {
                Token token = scanToken();
                addTokenToStream(token);
            }
            catch (LexerError lexerError) {
                System.out.println(lexerError.getMessage());
//                todo mmhmh queremos exitear?
                System.exit(1);
            }
        }
        colPositionStart = colPositionEnd;
        addTokenToStream(tokenFactory.build(EOF, null, lineNumber, colPositionStart, ++colPositionEnd));
        return tokenList;
    }

    private void addTokenToStream(Token token) {
        tokenList = Stream.concat(tokenList, Stream.of(token));
    }

    @Override
    public StringBuffer readSource(String sourcePath) {
//        todo implement
        return null;
    }

    private Token scanToken() throws LexerError {
        Token token = null;
        LexerType lexerType = null;
        while(lexerType == null) lexerType = getNextCharLexerType(checkNextChar());
        for (AbstractLexer lexer : filterByLexerType(lexerType)) {
            try {
                token = lexer.scanToken();
                setAllLexersPositions(lexer.getAllPositions());
                return token;
            } catch (LexerError ignored) { }
        }
        throw new LexerError(fileName, lineNumber, colPositionStart, currentPosition);
    }

    private void setAllLexersPositions(int[] positions) {
        this.lineNumber = positions[0];
        this.currentPosition = positions[1];
        this.colPositionStart = positions[2];
        this.colPositionEnd = positions[2];
        for (AbstractLexer lexer : lexerList) {
            lexer.setAllPositions(lineNumber, currentPosition, colPositionEnd);
        }
    }

    private List<AbstractLexer> filterByLexerType(LexerType lexerType){
        List<AbstractLexer> filteredLexers = new ArrayList<>();
        lexerList.forEach((lexer) -> {if(lexer.getLexerType() == lexerType) filteredLexers.add(lexer);});
        return filteredLexers;
    }

    private char checkNextChar(){
        return codeSource.charAt(currentPosition);
    }

    private LexerType getNextCharLexerType(char character){
        if (character >= '0' && character <= '9') return LexerType.DIGIT;
        else if ((character >= 'a' && character <= 'z') ||
            (character >= 'A' && character <= 'Z') ||
            character == '_') return LexerType.ALPHA;
        else if(character == ' ' || character == '\r' || character == '\t') {
            colPositionStart++;
            currentPosition++;
            updatePositions();
            return null;
        } else if (character == '\n'){
            lineNumber++;
            currentPosition++;
            colPositionStart = 0;
            colPositionEnd = 0;
            updatePositions();
            return null;
        }
        else return LexerType.SPECIAL_CHAR;
    }

    private void updatePositions() {
        int[] positions = new int[3];
        positions[0] = this.lineNumber;
        positions[1] = this.currentPosition;
        positions[2] = this.colPositionStart;
        setAllLexersPositions(positions);
    }

}
