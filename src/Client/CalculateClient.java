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
                System.out.println("서버 정보를 불러오는 데 실패했습니다. 기본값을 사용합니다.");
            }
        }

        try (Socket socket = new Socket(serverIP, port);
             var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var writer = new PrintWriter(socket.getOutputStream(), true);
             var scanner = new Scanner(System.in)) {

            System.out.println("서버에 연결되었습니다: " + serverIP + ":" + port);
            while (true) {
                System.out.println("산술 연산을 입력하거나 'exit'를 입력하여 종료하세요 (예: 1 + 2): !!띄어쓰기 필수!!");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }

                writer.println(input);
                String response = reader.readLine();
                System.out.println("서버 응답: " + response);
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }
}
