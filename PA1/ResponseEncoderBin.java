import java.io.*;

public class ResponseEncoderBin implements ResponseEncoder, ResponseBinConst {

  private String encoding;

  public ResponseEncoderBin() {
    encoding = DEFAULT_ENCODING;
  }

  public ResponseEncoderBin(String encoding) {
    this.encoding = encoding;
  }

  public byte[] encode(Response response) throws Exception {
  
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(buf);

    out.writeByte(response.total_message_length);

    out.writeInt(response.result);

    out.writeByte(response.error_code);

    out.writeShort(response.request_id);

    out.flush();
    out.close();

    return buf.toByteArray(); 
  }
}
