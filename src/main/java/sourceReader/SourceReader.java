package sourceReader;

import java.io.IOException;

public interface SourceReader {
    StringBuffer readFromPath(String path) throws IOException;
}
