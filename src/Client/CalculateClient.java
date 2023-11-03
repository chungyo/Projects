package Client;
import java.io.*;
import java.net.*;
import java.util.*;

public class CalculateClient {

    public static void main(String[] args) throws Exception {
        // 서버 정보를 파일에서 불러오기
        String serverIP = "localhost";
        int port = 1234;
        File configFile = new File("server_info.dat");

        if (configFile.exists()) {
            try (Scanner scanner = new Scanner(configFile)) {
                serverIP = scanner.nextLine();
                port = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Failed to load server information. Using default values.");
            }
        }

        try (Socket socket = new Socket(serverIP, port);
             var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var writer = new PrintWriter(socket.getOutputStream(), true);
             var scanner = new Scanner(System.in)) {

            System.out.println("Connected with Server: " + serverIP + ":" + port);
            while (true) {
                System.out.println("Enter an arithmetic operation or type 'exit' to quit (e.g. 1 + 2): !!spacing is mandatory!!");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }

                writer.println(input);
                String response = reader.readLine();
                System.out.println("Server Response: " + response);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
