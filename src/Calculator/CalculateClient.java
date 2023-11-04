package Calculator;

import Calculator.ErrorType.Type;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CalculateClient {

    public static void main(String[] args) {
        // 서버 정보를 파일에서 불러오기
        String serverIP = "localhost";
        int port = 1234;
        File configFile = new File("server_info.dat");

        // 서버 정보가 담긴 파일이 존재한다면, 그 파일 사용. 아닐시 default값 사용
        if (configFile.exists()) {
            try (Scanner scanner = new Scanner(configFile)) {
                serverIP = scanner.nextLine();
                port = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Failed to load server information. Using default values.");
            }
        }
        else {
            System.out.println("Configuration file not found. Using default values.");
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
                if (response.startsWith("Error: ")) {
                    // 서버에서 보낸 에러 메시지를 추출합니다.
                    String errorCodeStr = response.substring("Error: ".length());
                    try {
                        int errorCode = Integer.parseInt(errorCodeStr);
                        System.out.println("Error: " + Type.getDescriptionFromCode(errorCode));
                    } catch (NumberFormatException e) {
                        System.out.println("Server sent an invalid error code.");
                    }
                } else {
                    // 정상적인 연산 결과를 출력합니다.
                    System.out.println("Server Response: " + response);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
