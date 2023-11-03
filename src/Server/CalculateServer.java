package Server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculateServer {

    public static String calc(String exp) {
        StringTokenizer st = new StringTokenizer(exp, " ");
        if (st.countTokens() != 3)
            return "error";
        String res = "";
        int op1 = Integer.parseInt(st.nextToken());
        String opcode = st.nextToken();
        int op2 = Integer.parseInt(st.nextToken());
        // 나누기에 대한 0 확인
        if (opcode.equals("/") && op2 == 0) {
            return "Error message: divided by zero";
        }
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
                res = Integer.toString(op1 / op2); // 0으로 나누는 것은 위에서 이미 체크
                break;
            default:
                res = "error";
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
        System.out.println("Server started on port " + port);

        while (true) {
            Socket socket = listener.accept();
            pool.execute(new RequestHandler(socket));
        }
    }
}

