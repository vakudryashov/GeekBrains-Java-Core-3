package Lesson_02.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerApp {
    private static final Logger LOGGER = LogManager.getLogger(ServerApp.class);
    public static void main(String[] args) {
        try {
            new ChatServer().start();
        }catch (Exception e){
            LOGGER.fatal(e.getStackTrace());
        }
    }
}
