package Lesson_02.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private int port = 8189;
    public static Map<String, User> userList = new ConcurrentHashMap<>();

    public ChatServer() {}

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        System.out.println(format.format(new Date())+" Сервер запущен. Прослушивается порт "+port);
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println(format.format(new Date())+" Соединение установлено c ip: "+socket.getInetAddress());
            new ChatConnection(this, socket);
        }
    }
}
