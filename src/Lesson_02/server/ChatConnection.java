package Lesson_02.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

public class ChatConnection implements Runnable{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Queue<String> outputBuffer = new LinkedList<>();
    private boolean isOutBufferBusy = false;
    private User user;

    public ChatConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        getIOStream();
    }

    private void getIOStream(){
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            getUser();
            ChatServer.executorService.submit(this::waitAuth);
            while (true) {
                String msg = in.readUTF();
                handleMessage(msg);
            }
        }catch(EOFException | SocketException eofException){
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (user != null) user.die();
            close(in,out,socket);
        }
    }

    private void handleMessage(String msg){
        String[] part = msg.split(" ",2);
        String cmd = part[0];
        msg = part.length>1 ? part[1] : "";
        if (cmd.equals("/nickname")){
            String nickname = part[1];
            if (user != null) { user.die(); }
            user = new User(nickname, this);
            if (!user.authenticate()) user = null;
        }else if (cmd.equals("/password") && user != null && !user.authenticate(msg)){
            user = null;
        }else if (user != null){
            user.runCmd(cmd, msg);
        }
    }

    public void getUser(){
        send("/userList "+ ChatServer.userList.keySet().toString());
        send("/requestNickname");
    }

    public void send(String msg){
        outputBuffer.offer(msg);
        if (isOutBufferBusy) return;
        isOutBufferBusy = true;
        try{
            while((msg = outputBuffer.poll()) != null) {
                out.writeUTF(msg);
                out.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        isOutBufferBusy = false;
    }

    public void waitAuth(){
        long countDown = 120;
        boolean userAuthenticated;
        do{
            userAuthenticated = user != null && ChatServer.userList.containsKey(user.nick);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(countDown-- > 0 && !userAuthenticated);

        if (!userAuthenticated){
            send("/authTimeout");
            if (user !=  null) user.die();
            close(in,out,socket);
        }
    }


    public void close(Closeable...items){
        user = null;
        try {
            for (Closeable item : items) {
                if (item != null) item.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
