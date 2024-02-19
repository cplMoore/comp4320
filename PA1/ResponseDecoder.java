import java.io.*;
import java.net.*;

public interface ResponseDecoder {
  Response decode(InputStream source) throws IOException;
}
