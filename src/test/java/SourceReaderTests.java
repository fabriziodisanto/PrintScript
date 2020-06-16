import org.junit.Test;
import sourceReader.SourceReader;
import sourceReader.SourceReaderImpl;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SourceReaderTests {

//    todo que funcione el relative path
    @Test
    public void test001_sourceReaderWorks() throws IOException {
        SourceReader sourceReader = new SourceReaderImpl();
        StringBuffer stringBuffer = sourceReader.readFromPath("/Users/fabriziodisanto/sandbox/Printscript/src/test/java/testSources/emptySource.txt");
        assertEquals("probando source reader\n\n\n2;\n", stringBuffer.toString());
    }
}
