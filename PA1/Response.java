/* Response class for UDP server.
 * Author: Jacob Moore jwm0083@auburn.edu
 */

public class Response {
	
  public int total_message_length;
  public int result;
  public int error_code;
  public int request_id;

  public Response(int total_message_length, int result, int error_code, int request_id) {
    this.total_message_length     = total_message_length;
    this.result 		  = result;
    this.error_code		  = error_code;
    this.request_id		  = request_id;
  }
  
  public String toString() {
    final String EOLN = java.lang.System.getProperty("line.separator");
    String value = "Total Message Length: " + total_message_length + EOLN +
  	           "Result: " + result + EOLN +
		   "Error Code: " + error_code + EOLN +
		   "Request ID: " + request_id + EOLN;

    return value;	
  }

}
