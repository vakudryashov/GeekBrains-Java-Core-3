package Lesson_02.client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

public class Connection implements Closeable {
    private final String host = "localhost";
    private final int port = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Controller controller;
    public String nick;
    private String password;
    public boolean firstLogin = true;

    public Connection(Controller controller) {
        this.controller = controller;
    }

    public void open(){
        controller.disconnect(true);
        try {
            socket = new Socket("localhost", port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(this::readInputStream).start();
            controller.disconnect(false);
        }catch(IOException e){
            System.out.println("Не удалось установить соединение с сервером "+host+":"+port);
            controller.runCommand("error","Не удалось установить соединение с сервером "+host+":"+port);
            reconnect();
        }
    }

    private void reconnect(){
        closeAll();
        if (controller.closed()) return;
        try{
            Thread.sleep(20000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        open();
    }

    private void readInputStream(){
        try{
            while(true){
                    String msg = in.readUTF();
                    parseMessage(msg);
            }
        }catch(UTFDataFormatException dfe){
            System.out.println("Пришла строка с кривым символом в конце. Ну и ладно.");
        }catch(EOFException | SocketException notError){
            System.out.println("Соединение с сервером потеряно");
            controller.runCommand("/serverMsg","Соединение с сервером потеряно");
            controller.disconnect(true);
            reconnect();
        }catch(IOException e){
            e.printStackTrace();
            controller.runCommand("/errorMsg", Arrays.toString(e.getStackTrace()));
            reconnect();
        }
    }

    public void send(String cmd, String msg){
        if (cmd.equals("/nickname")){
            nick = msg;
        }else if (cmd.equals("/password")){
            password = msg;
        }
        msg = cmd + " " + msg;
        try{
            out.writeUTF(msg);
            out.flush();
        }catch(IOException e){
            controller.runCommand("/error","Не удалось отправить сообщение");
        }
    }

    public void changeNick(){ password = null; }

    private void parseMessage(String msg){
        String[] part = msg.split(" ", 2);
        String cmd = part[0];
        String text = part.length>1 ? part[1] : "";
        if (cmd.equals("/accessDeny")){
            nick = null;
            password = null;
        }else if (cmd.equals("/requestNickname") && nick != null){
            send("/nickname", nick);
            return;
        }else if (cmd.equals("/requestPassword") && password != null){
            send("/password", password);
            return;
        }
        controller.runCommand(cmd,text);
    }

    private void closeAll(Closeable...items){
        try {
            for (Closeable item : items) {
                if (item != null) item.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void close(){
        if (!controller.closed()) controller.runCommand("exit");
        closeAll(in, out, socket);
    }
}
