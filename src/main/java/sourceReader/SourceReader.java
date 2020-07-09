package sourceReader;

import java.io.IOException;

public interface SourceReader {
//    clase que devuelve mi contenido, no me muestra el path
//    abstraerse del string buffer
    StringBuffer readFromPath(String path) throws IOException;
}
