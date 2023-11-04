package Calculator;

import Calculator.ErrorType.Type;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculateServer {

    public static String calc(String str) {
        StringTokenizer st = new StringTokenizer(str, " ");
        int tokenCount = st.countTokens();

        // Argument 개수 체크
        if (tokenCount < 3) {
            return (Type.TOO_FEW_ARGUMENTS.getDescription());
        } else if (tokenCount > 3) {
            return (Type.TOO_MANY_ARGUMENTS.getDescription());
        }

        int op1 = Integer.parseInt(st.nextToken());
        String opcode = st.nextToken();
        int op2 = Integer.parseInt(st.nextToken());

        // 나누기에 대한 0 확인
        if (opcode.equals("/") && op2 == 0) {
            return (Type.DIVIDED_BY_ZERO.getDescription());
        }

        String res;
        switch (opcode) {
            case "+":
                res = Integer.toString(op1 + op2);
                break;
            case "-":
                res = Integer.toString(op1 - op2);
                break;
            case "*":
                res = Integer.toString(op1 * op2);
                break;
            case "/":
                res = Integer.toString(op1 / op2);
                break;
            default:
                return (Type.INVALID_OPERATION.getDescription());
        }
        return res;
    }


    // 멀티스레드 이용을 위한 메서드
    private static class RequestHandler implements Runnable {
        private Socket socket;
        RequestHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            System.out.println("Connected: " +  socket);
            try {
                var in = new Scanner(socket.getInputStream());
                var out = new PrintWriter(socket.getOutputStream(), true);
                while (in.hasNextLine()) {
                    String request = in.nextLine();
                    String response = calc(request);
                    out.println(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
                System.out.println("Closed: " + socket);
            }
        }
    }

    // 메인함수
    public static void main(String[] args) throws Exception {
        int port = 9999;
        ServerSocket listener = new ServerSocket(port);
        ExecutorService pool = Executors.newFixedThreadPool(20);
        System.out.println("Server.Server started on port " + port);

        while (true) {
            Socket socket = listener.accept();
            pool.execute(new RequestHandler(socket));
        }
    }
}

