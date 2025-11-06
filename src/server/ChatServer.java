import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static Map<String, Set<ClientHandler>> rooms = new ConcurrentHashMap<>();
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("IP do servidor: ");
        String ip = br.readLine();
        System.out.print("Porta: ");
        int port = Integer.parseInt(br.readLine());

        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(ip, port));

        System.out.println("Servidor iniciado em " + ip + ":" + port);

        while (true) {
            Socket client = server.accept();
            new ClientHandler(client).start();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private String userName = "Usuário";
        private String room = "#geral";

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                rooms.putIfAbsent(room, ConcurrentHashMap.newKeySet());
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("Digite seu nome:");
                userName = in.readLine();
                joinRoom(room);

                sendHelp();

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.startsWith("/pm ")) {
                        privateMessage(msg);
                    } else if (msg.startsWith("/join ")) {
                        changeRoom(msg.split(" ")[1]);
                    } else if (msg.equals("/leave")) {
                        changeRoom("#geral");
                    } else if (msg.equals("/list")) {
                        listRooms();
                    } else if (msg.equals("/users")) {
                        listUsers();
                    } else if (msg.equals("/help")) {
                        sendHelp();
                    } else if (msg.equals("/exit")) {
                        leaveRoom();
                        break;
                    } else {
                        broadcast("[" + userName + "] " + msg, false);
                        writeLog(ts() + " [" + userName + "] " + msg, room);
                    }
                }
            } catch (Exception e) {}
            finally {
                leaveRoom();
                try { socket.close(); } catch (Exception e) {}
            }
        }

        private String ts() {
            return "[" + LocalDateTime.now().format(dtf) + "]";
        }

        private void joinRoom(String newRoom) {
            rooms.putIfAbsent(newRoom, ConcurrentHashMap.newKeySet());
            rooms.get(newRoom).add(this);
            room = newRoom;

            String logEntry = ts() + " " + userName + " entrou na sala " + room;
            writeLog(logEntry, room);
            broadcast(userName + " entrou na sala " + room, true);

            out.println("=== Sala atual: " + room + " ===");
        }

        private void changeRoom(String newRoom) {
            leaveRoom();
            joinRoom(newRoom);
        }

        private void leaveRoom() {
            if (rooms.containsKey(room)) {
                rooms.get(room).remove(this);

                String logEntry = ts() + " " + userName + " saiu da sala " + room;
                writeLog(logEntry, room);

                broadcast(userName + " saiu da sala " + room, false);
            }
        }

        private void listRooms() {
            out.println("Salas disponíveis:");
            for (String r : rooms.keySet()) out.println(" -> " + r);
        }

        private void listUsers() {
            out.println("Usuários em " + room + ":");
            for (ClientHandler u : rooms.get(room))
                out.println(" - " + u.userName);
        }

        private void sendHelp() {
            out.println("======== MENU DE COMANDOS ========");
            out.println("/join #sala  - Entrar em sala");
            out.println("/leave       - Voltar ao #geral");
            out.println("/list        - Listar salas");
            out.println("/users       - Mostrar usuários da sala");
            out.println("/pm nome msg - Mensagem privada");
            out.println("/help        - Mostrar comandos");
            out.println("/exit        - Sair");
            out.println("=================================");
        }

        private void privateMessage(String msg) {
            String[] parts = msg.split(" ", 3);
            if (parts.length < 3) {
                out.println("Uso: /pm <usuario> <mensagem>");
                return;
            }

            String targetUser = parts[1];
            String message = parts[2];

            for (String r : rooms.keySet()) {
                for (ClientHandler user : rooms.get(r)) {
                    if (user.userName.equalsIgnoreCase(targetUser)) {
                        user.out.println("[PM de " + userName + "] " + message);
                        out.println("[PM para " + targetUser + "] " + message);
                        String logPm = ts() + " [PM] " + userName + " -> " + targetUser + ": " + message;
                        writeLog(logPm, r);
                        return;
                    }
                }
            }

            out.println("Usuário " + targetUser + " não encontrado em nenhuma sala.");
        }

        private void broadcast(String message, boolean includeSelf) {
            for (ClientHandler h : rooms.get(room)) {
                if (!includeSelf && h == this) continue;
                h.out.println(message);
            }
        }

        private synchronized void writeLog(String msg, String roomName) {
            try {
                File folder = new File("logs");
                if (!folder.exists()) folder.mkdir();

                FileWriter fw = new FileWriter("logs/" + roomName + ".txt", true);
                fw.write(msg + System.lineSeparator());
                fw.close();
            } catch (Exception e) {}
        }
    }
}
