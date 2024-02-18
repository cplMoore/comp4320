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

                // Process and display response
                ByteArrayInputStream bais = new ByteArrayInputStream(responseData);
                DataInputStream dis = new DataInputStream(bais);
                int result = dis.readInt();
                int errorCode = dis.readInt();
                int responseRequestId = dis.readInt();
                System.out.println("Response: Request ID=" + responseRequestId + ", Result=" + result + ", Error Code=" + errorCode);
                System.out.println("Round trip time: " + roundTripTime + " ms");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
