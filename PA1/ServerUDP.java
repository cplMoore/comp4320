/* UDP server for comp  4320 programming assignment.
 * Author: Jacob Moore jwm00883@auburn.edu.
 */

import java.net.*;
import java.io.*;
import java.nio.*;

public class ServerUDP {
  
  public static void main(String args[]) throws Exception {
    if(args.length != 1) {
      System.out.println("Usage: java ServerUDP <Port#>");
      System.exit(1);
    }

    int port = Integer.parseInt(args[0]);

   try (DatagramSocket sock = new DatagramSocket(port)) {
     System.out.println("ServerUDP is running on port" + port);

     ResponseEncoderBin response_encoder = new ResponseEncoderBin();
     ResponseDecoderBin response_decoder = new ResponseDecoderBin();

    while(true) {
	byte[] received_data = new byte[1024];
	DatagramPacket received_packet = new DatagramPacket(received_data, 
							 received_data.length);

	sock.receive(received_packet);

	System.out.print("Received Request (Hex): ");
        for(int i = 0; i < received_packet.getLength(); i++) {
	  System.out.printf("%02x ", i);
	}
	System.out.println();

	handle_request(sock, received_packet, response_encoder, response_decoder);
     }
    }
    catch (IOException e) {
        e.printStackTrace();
    }
  }
  
  private static void handle_request(DatagramSocket sock, DatagramPacket received_packet,
  ResponseEncoderBin response_encoder, ResponseDecoderBin response_decoder) {

    try {
     byte[] data = received_packet.getData();
     byte total_message_length = ByteBuffer.wrap(data, 0, 1).get();
     byte op_code = data[4];
     int op1 = ByteBuffer.wrap(data, 5, 4).getInt();
     int op2 = ByteBuffer.wrap(data, 9, 4).getInt();
     short request_id = ByteBuffer.wrap(data, 13, 2).getShort();
     byte op_name_length = ByteBuffer.wrap(data, 15, 2).get();
     String op_name = new String(data, 17, op_name_length / 2, "UTF-16BE");
     byte error_code = 0;
     int result;

     int actual_message_length =17 + op_name.length() * 2;

     if(total_message_length != actual_message_length) {
       error_code = 127;
       result = 0;
     } else {
         result = perform_operation(op_code, op1, op2);
     }
     byte[] response = create_response(total_message_length, result, error_code, request_id);

     sock.send(new DatagramPacket(response, response.length, received_packet.getAddress(),
	       received_packet.getPort()));
   } catch (BufferUnderflowException e) {
      e.printStackTrace();
     }
     catch(UnsupportedEncodingException e) {
      e.printStackTrace();
     }
     catch(IOException e) {
     e.printStackTrace();
     }
 }

private static int perform_operation(byte op_code, int op1, int op2) {
     switch (opcode) {
	case 0: // Multiplication
        	op1 * op2;
		break;
        case 1: // Division
        	if (op2 != 0) {
                	op1 / op2;
                } else {
                        errorCode = 1; // Division by zero error
                }
                break;
        case 2: // Bitwise OR
                op1 | op2;
                break;
        case 3: // Bitwise AND
                 op1 & op2;
                 break;
         case 4: // Subtraction
                op1 - op2;
                break;
        case 5: // Addition
                op1 + op2;
                break;
     	 default:
		throw new IllegalArgumentException("Invalid operation code");
    }
  }       

  private static byte[] create_response(byte total_message_length, int result, byte error_code, short request_id) {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.put(total_message_length);
    buffer.putInt(result);
    buffer.put(error_code);
    buffer.putShort(request_id);
    return buffer.array();
  }

}
