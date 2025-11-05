
import java.io.*;
import java.net.*;

public class ChatCliente {
    public static void main(String[] args) throws Exception {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("IP do servidor: ");
        String ip = console.readLine();
        System.out.print("Porta: ");
        int port = Integer.parseInt(console.readLine());

        Socket socket = new Socket(ip, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (Exception e) {}
        }).start();

        String input;
        while ((input = console.readLine()) != null) {
            out.println(input);
            if (input.equals("/exit")) break;
        }

        socket.close();
    }
}
