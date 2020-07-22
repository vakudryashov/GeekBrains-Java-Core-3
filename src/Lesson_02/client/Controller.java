package Lesson_02.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.scene.control.*;

public class Controller implements Initializable {
    private Connection connection;
    private String userCmd = "publicMsg";

    @FXML
    TextArea publicFrame;
    @FXML
    TextField userInput;
    @FXML
    ListView<String> userList;
    @FXML
    Label userInputLabel;
    @FXML
    Button buttonSend;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = new Connection(this);
        new Thread(connection::open).start();
        userList.setOnMouseClicked(this::onUserSelected);
    }

    public void disconnect(boolean isDisconnected){
        Platform.runLater(()->{
            if (isDisconnected) {
                userInputLabel.setText("Ждём...");
                userInput.setDisable(true);
                buttonSend.setDisable(true);
            }else{
                userInputLabel.setText("Всем:");
                userInput.setDisable(false);
                buttonSend.setDisable(false);
            }
        });
    }

    public void sendMessage(){
        String msg = userInput.getText();
        if (msg.startsWith("/")) msg = parseUserInput(msg);
        connection.send(userCmd, msg);
        if (userCmd.equals("/exit")){
            if (buttonSend.getText().equals("Подключиться")){
                new Thread(connection::open).start();
                userInput.setDisable(false);
                buttonSend.setText("Отправить");
            }else {
                connection.close();
                Platform.exit();
                System.exit(0);
            }
        }else {
            userCmd = "/publicMsg";
            Platform.runLater(() -> {
                userInputLabel.setText("Всем: ");
            });
            userInput.clear();
            userInput.requestFocus();
        }
    }

    public void runCommand(String cmd) { runCommand(cmd, ""); }

    public void runCommand(String cmd, String msg){
        if (cmd.equals("/userList")) {
            String  s = msg.substring(1, msg.length() - 1);
            if (s.length() == 0){
                Platform.runLater(() -> {
                    userList.getItems().clear();
                });
            }else{
                String[] nicknames = s.split(", ");
                ObservableList<String> list = FXCollections.observableArrayList(nicknames);
                Platform.runLater(() -> {
                    userList.setItems(list);
                });
            }
        }else if(cmd.equals("/addUser")){
            showMessage(msg.equals(connection.nick) ? "Ты вошёл в чат под именем "+msg : msg+" вошёл в чат");
            Platform.runLater(() -> {
                userList.getItems().add(msg);
            });
        }else if(cmd.equals("/removeUser")){
            Platform.runLater(() -> {
                showMessage(msg.equals(connection.nick) ? "Ты выходишь из чата" : msg+" вышел из чата");
                userList.getItems().removeIf(nick -> nick.equals(msg));
            });
        }else if(cmd.equals("/requestNickname")){
            showMessage("Как тебя зовут?");
            Platform.runLater(()->{ userInputLabel.setText("nickname:"); });
            userCmd = "/nickname";
        }else if(cmd.equals("/serverMsg")){
            showMessage(msg);
        }else if (cmd.equals("/exit")){
            userCmd = cmd;
            connection.close();
        }else if (cmd.equals("/requestPassword")){
            showMessage(msg);
            userCmd = "/password";
            Platform.runLater(()->{ userInputLabel.setText("password:"); });
        }else if (cmd.equals("/authTimeout")){
            showMessage("Превышено время ожидания аутентификации");
            userCmd = "/exit";
            connection.close();
            Platform.runLater(()->{ userInputLabel.setText("Отключен"); });
            userInput.setDisable(true);
            Platform.runLater(()->{ buttonSend.setText("Подключиться"); });
        }else{
            showMessage(msg);
        }
    }

    public void catchCommand(){
        String msg = userInput.getText();
        if (msg.startsWith("/w ")){
            String nick = msg.split(" ",3)[1];
            if (userList.getItems().contains(nick)){
                Platform.runLater(()->{ userInputLabel.setText("Только для "+nick+":"); });
            }else{
                Platform.runLater(()->{ userInputLabel.setText("Всем:"); });
            }
        }
    }

    private String parseUserInput(String input){
        String[] part = input.split(" ",2);
        String cmd = part[0];
        if (cmd.equals("/w")) userCmd = "/privateMsg";
        else if (cmd.equals("/nick") || cmd.equals("/nickname")){
            userCmd = "/nickname";
            connection.changeNick();
        }
        else userCmd = cmd;
        return part.length>1 ? part[1] : "";
    }

    public void showMessage(String msg){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM-HH:mm:ss");
        String now = format.format(new Date());
        publicFrame.appendText(now + " " + msg + "\n");
    }

    public boolean closed(){
        return userCmd.equals("/exit");
    }

    private void onUserSelected(javafx.scene.input.MouseEvent  event){
        if (event.getClickCount() == 2) {
            String nickname = userList.getSelectionModel().getSelectedItem();
            userInput.setText("/w " + nickname + " ");
            catchCommand();
            userInput.requestFocus();
            userInput.selectEnd();
        }
    }
}

