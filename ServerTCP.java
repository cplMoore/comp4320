import java.io.*;
import java.net.*;

public class ServerTCP {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: prog ServerTCP portnumber");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is running on port " + portNumber + "...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("New connection from " + clientSocket.getInetAddress());

                    // Read request from client
                    DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                    int tml = input.readInt(); // Total Message Length
                    byte opCode = input.readByte(); // Operation Code
                    int operand1 = input.readInt(); // Operand 1
                    int operand2 = input.readInt(); // Operand 2
                    short requestId = input.readShort(); // Request ID
                    short opNameLength = input.readShort(); // Operation Name Length
                    byte[] opNameBytes = new byte[opNameLength];
                    input.readFully(opNameBytes);
                    String opName = new String(opNameBytes, "UTF-16BE");

                    // Display request in hexadecimal format
                    System.out.print("Request in Hex: ");
                    System.out.print(String.format("%02X ", tml));
                    System.out.print(String.format("%02X ", opCode));
                    System.out.print(String.format("%08X ", operand1));
                    System.out.print(String.format("%08X ", operand2));
                    System.out.print(String.format("%04X ", requestId));
                    System.out.print(String.format("%04X ", opNameLength));
                    for (byte b : opNameBytes) {
                        System.out.print(String.format("%02X ", b));
                    }
                    System.out.println();

                    // Display request in a user-friendly manner
                    System.out.println("Request ID: " + requestId);
                    System.out.println("Operation: " + opName);
                    System.out.println("Operands: " + operand1 + ", " + operand2);

                    // Perform operation based on opCode
                    int result = 0;
                    int errorCode = 0;
                    switch (opCode) {
                        case 0: // Addition
                            result = operand1 + operand2;
                            break;
                        case 1: // Subtraction
                            result = operand1 - operand2;
                            break;
                        case 2: // Bitwise OR
                            result = operand1 | operand2;
                            break;
                        case 3: // Bitwise AND
                            result = operand1 & operand2;
                            break;
                        case 4: // Division
                            if (operand2 != 0) {
                                result = operand1 / operand2;
                            } else {
                                errorCode = 1; // Division by zero error
                            }
                            break;
                        case 5: // Multiplication
                            result = operand1 * operand2;
                            break;
                        default:
                            errorCode = 2; // Invalid operation code
                            break;
                    }

                    // Send response to client
                    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
                    output.writeInt(9); // Total Message Length for response
                    output.writeInt(result); // Result
                    output.writeByte(errorCode); // Error Code
                    output.writeShort(requestId); // Request ID
                    output.flush();
                    System.out.println("Response sent to client.");
                } catch (IOException e) {
                    System.err.println("Error handling client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating server socket: " + e.getMessage());
        }
    }
}
