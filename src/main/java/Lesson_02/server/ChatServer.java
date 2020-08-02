package Lesson_02.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatServer {
    private int port = 8189;
    public static Map<String, User> userList = new ConcurrentHashMap<>();
    public static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final Logger LOGGER = LogManager.getLogger(ChatServer.class);

    public ChatServer() {}

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        LOGGER.info("Сервер запущен. Прослушивается порт " + port);
        while(true){
            Socket socket = serverSocket.accept();
            LOGGER.info("Соединение установлено. ip: " + socket.getInetAddress());
            executorService.submit(new ChatConnection(socket));
        }
    }
}
