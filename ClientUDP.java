import java.io.*;
import java.net.*;

public class ClientUDP {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java ClientUDP <server_hostname> <port_number>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            InetAddress serverAddress = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // Ask user for inputs
                System.out.print("Enter opcode: ");
                int opcode = Integer.parseInt(userInput.readLine());
                System.out.print("Enter Operand 1: ");
                int operand1 = Integer.parseInt(userInput.readLine());
                System.out.print("Enter Operand 2: ");
                int operand2 = Integer.parseInt(userInput.readLine());
                System.out.print("Enter Request ID: ");
                int requestId = Integer.parseInt(userInput.readLine());

                // creates start time
                long startTime = System.currentTimeMillis();

                // Create message request
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeInt(opcode);
                dos.writeInt(operand1);
                dos.writeInt(operand2);
                dos.writeInt(requestId);
                byte[] requestData = baos.toByteArray();

                // Display request in hexadecimal
                System.out.println("Request in hexadecimal: ");
                for (byte b : requestData) {
                    System.out.printf("%02X ", b);
                }
                System.out.println();

                // Send request to server
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, serverAddress, port);
                socket.send(requestPacket);

                // Receive response from server
                byte[] responseData = new byte[1024];
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
                socket.receive(responsePacket);

                // Measure end time
                long endTime = System.currentTimeMillis();
                long roundTripTime = endTime - startTime;

                // Display response in hexadecimal
                System.out.println("Response in hexadecimal: ");
                for (int i = 0; i < responsePacket.getLength(); i++) {
                    System.out.printf("%02X ", responseData[i]);
                }
                System.out.println();

                // Process and display response
                ByteArrayInputStream bais = new ByteArrayInputStream(responseData);
                DataInputStream dis = new DataInputStream(bais);
                int result = dis.readInt();
                int errorCode = dis.readInt();
                int responseRequestId = dis.readInt();
                System.out.println("Response: Request ID=" + responseRequestId + ", Result=" + result + ", Error Code=" + errorCode);
                if (errorCode == 0) {
                    System.out.println("Error Code: Ok");
                }

                // Display round trip time
                System.out.println("Round trip time: " + roundTripTime + " ms");

                // Prompt the user for a new request
                System.out.print("Do you want to make another request? (yes/no): ");
                String response = userInput.readLine();
                if (!response.equalsIgnoreCase("yes")) {
                    break; // Exit the loop if the user doesn't want to make another request
                }
            }
            socket.close(); // Close the socket when done
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
