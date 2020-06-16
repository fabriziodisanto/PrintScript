package scanner;

import errors.LexerError;
import scanner.lexer.AbstractLexer;
import scanner.lexer.LexerType;
import sourceReader.SourceReader;
import token.Token;
import token.factory.TokenFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

import static token.TokenType.EOF;

public class ScannerImpl implements Scanner {

    private SourceReader sourceReader;
    private String fileName;
    private StringBuffer codeSource;
    private int lineNumber = 1;
    private int currentPosition = 0;
    private int colPositionStart = 0;
    private int colPositionEnd = 0;
    private Stream<Token> tokenList;
    private TokenFactory tokenFactory;
    private  Map<Integer, AbstractLexer> lexerPrecedenceMap;
    private LexerProvider provider;

//    todo, hacer que el code source venga del metodo de leer desde el filename
    public ScannerImpl(String fileName, Map<Integer, AbstractLexer> lexerPrecedenceMap,
                       TokenFactory tokenFactory, LexerProvider provider, SourceReader sourceReader) throws IOException {
        this.sourceReader = sourceReader;
        this.fileName = fileName;
        this.codeSource = sourceReader.readFromPath(fileName);
        this.tokenList = new ArrayList<Token>().stream();
        this.lexerPrecedenceMap = setCodeSource(lexerPrecedenceMap, this.codeSource);
        this.tokenFactory = tokenFactory;
        this.provider = provider;
    }

    private Map<Integer, AbstractLexer> setCodeSource(Map<Integer, AbstractLexer> lexerPrecedenceMap, StringBuffer codeSource) {
        for (AbstractLexer lexer : lexerPrecedenceMap.values()) {
            lexer.setCodeSource(codeSource);
        }
        return lexerPrecedenceMap;
    }

    private Boolean isEndOfSource() {
        return codeSource == null || currentPosition >= codeSource.length();
    }

    public Stream<Token> analyze() throws LexerError {
        while (!isEndOfSource()) {
            Token token = scanToken();
            addTokenToStream(token);
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
        LexerType lexerType = null;
        while(lexerType == null) lexerType = getNextCharLexerType(checkNextChar());
        for (AbstractLexer lexer : provider.get(lexerType).values()) {
            try {
                Token token = lexer.scanToken();
                setAllLexersPositions(lexer.getAllPositions());
                return token;
            } catch (LexerError ignored) {

            }
        }
        throw new LexerError(fileName, lineNumber, colPositionStart, currentPosition);
    }

    private void setAllLexersPositions(int[] positions) {
        this.lineNumber = positions[0];
        this.currentPosition = positions[1];
        this.colPositionStart = positions[2];
        this.colPositionEnd = positions[2];
        for (AbstractLexer lexer : lexerPrecedenceMap.values()) {
            lexer.setAllPositions(lineNumber, currentPosition, colPositionEnd);
        }
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
