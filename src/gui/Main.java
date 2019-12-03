package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        int height = 600;
        int width = 600;

        Parent root = FXMLLoader.load(getClass().getResource("GamePanel.fxml"));
        primaryStage.setTitle("Witcher 4: Lidl edition");
        primaryStage.setScene(new Scene(root, height, width));
        primaryStage.setMinHeight(height);
        primaryStage.setMinWidth(width);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
