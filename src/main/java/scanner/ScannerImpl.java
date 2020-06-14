package scanner;

import errors.LexerError;
import lexer.AbstractLexer;
import token.Token;
import token.TokenFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static token.TokenType.EOF;

//mhmh extenderlo del abstract lexer?
public class ScannerImpl implements Scanner{

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
//        TODO importante, boolean lexer antes que el keyword, sino los convierte en identifiers
        this.lexerList = lexerList;
        this.tokenFactory = tokenFactory;
    }

    private Boolean isEndOfSource() {
        return codeSource == null || currentPosition >= codeSource.length();
    }

    public Stream<Token> analyze() throws LexerError {
        while (!isEndOfSource()) {
//            todo mhm queremos try catchearlo?
//            try {
                Token token = scanToken();
                addTokenToStream(token);
//            }
//            catch (LexerError lexerError) {
//                System.out.println(lexerError.getMessage());
////                todo mmhmh queremos exitear?
//                System.exit(1);
//            }
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
        for (AbstractLexer lexer : lexerList) {
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


}
