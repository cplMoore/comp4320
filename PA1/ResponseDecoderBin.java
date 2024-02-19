import java.io.*;
import java.net.*;

public class ResponseDecoderBin implements ResponseDecoder, ResponseBinConst {
  private String encoding; 

  public ResponseDecoderBin() {
    encoding = DEFAULT_ENCODING;
  }

  public ResponseDecoderBin(String encoding) {
    this.encoding = encoding;
  }

  public Response decode(InputStream wire) throws IOException {
    DataInputStream src = new DataInputStream(wire);

    int total_message_length = src.readByte();

    int result = src.readInt();

    int error_code = src.readByte();

    short request_id = src.readShort();

    return new Response(total_message_length, result, error_code, request_id);
    
  }
}
