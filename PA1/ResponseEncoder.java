public interface ResponseEncoder {
  byte[] encode(Response response) throws Exception;
}
