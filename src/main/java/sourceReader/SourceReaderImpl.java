package sourceReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SourceReaderImpl implements SourceReader {
    @Override
    public StringBuffer readFromPath(String path) throws IOException {
        return new StringBuffer(new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8));
    }
}
