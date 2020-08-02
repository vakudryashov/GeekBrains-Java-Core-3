package Lesson_02.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setX(100);
        stage.setY(50);
        stage.setWidth(800);
        stage.setHeight(600);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Клиент чата");
        stage.setScene(scene);
        stage.show();

        Controller controller = loader.getController();
        stage.setOnCloseRequest(e -> {
            controller.runCommand("/exit","Goodbye");
        });
    }
    public void run(){ launch(); }
}
