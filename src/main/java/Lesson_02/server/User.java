package Lesson_02.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User {
    public String nick;
    private String password;
    private final ChatConnection chatConnection;

    public User(String nick, ChatConnection chatConnection) {
        this.nick = nick;
        this.chatConnection = chatConnection;
    }

    public void runCmd(String cmd, String msg){
        if(cmd.equals("/publicMsg")){
            sendBroadcastMsg(String.format("/publicMsg %s: %s",nick, msg));
        }else if (cmd.equals("/privateMsg")){
            String[] chunk = msg.split(" ",2);
            String message = String.format("/publicMsg Лично от %s: %s",nick,chunk[1]);
            ChatServer.userList.get(chunk[0]).sendMsg(message);
            sendMsg("/publicMsg Лично для "+chunk[0]+": "+chunk[1]);
        }else if(cmd.equals("/exit")){
            die();
            chatConnection.close();
        }else if(cmd.equals("/register")){
            SQLiteDBService db = new SQLiteDBService();
            if (db.write(String.format("insert into users (nickname, password) values ('%s', '%s')",nick,msg))){
                password = msg;
                sendMsg("/serverMsg nickname успешно зарегистрирован");
            }else{
                sendMsg("/serverMsg Не удалось зарегистрировать nickname "+nick);
            }
        }
    }
    public boolean authenticate(){
        boolean result;
        if (ChatServer.userList.containsKey(nick)){
            sendAccessDeny(String.format("Пользователь \"%s\" уже присутствует в чате.",nick));
            chatConnection.getUser();
            result = false;
        }else if (isNickRegistered()){
            sendRequestPsw(String.format("Пользователь \"%s\" зарегистрирован. Требуется ввод пароля",nick));
            result = true;
        }else{
            result = true;
            enterChat();
        }
        return result;
    }

    public boolean authenticate(String userPassword) {
        boolean result;
        if (userPassword.equals(password)){
            result = true;
            enterChat();
        }else{
            result = false;
            sendAccessDeny("Неправильный пароль или nickname. Попробуй ещё раз");
            chatConnection.getUser();
        }
        return result;
    }

    private boolean isNickRegistered(){
        boolean result = false;
        SQLiteDBService db = new SQLiteDBService();
        ResultSet rs = db.read("select password from users where nickname='"+nick+"'");
        try {
            if (rs.next()){
                result = true;
                password = rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void enterChat(){
        sendUserList();
        ChatServer.userList.put(nick, this);
        sendAddUser(nick);
    }

    public void die(){
        ChatServer.userList.remove(nick);
        sendRemoveUser(nick);
    }

    public void sendMsg(String msg){
        chatConnection.send(msg);
    }

    public void sendAccessDeny(String msg){
        sendMsg(String.format("/accessDeny %s", msg));
    }

    public void sendRequestPsw(String msg){
        sendMsg(String.format("/requestPassword %s", msg));
    }

    private void sendUserList(){
        sendMsg("/userList "+ ChatServer.userList.keySet().toString());
    }

    private void sendAddUser(String nickname){
        sendBroadcastMsg("/addUser "+nickname);
    }

    private void sendRemoveUser(String nickname){
        sendBroadcastMsg("/removeUser "+nickname);
    }

    public void sendBroadcastMsg(String msg){
        HashMap<String, User> map = new HashMap<>(ChatServer.userList);
        for (Map.Entry<String, User> entry :map.entrySet()) {
            User anyUser = entry.getValue();
            anyUser.sendMsg(msg);
        }
    }
}
