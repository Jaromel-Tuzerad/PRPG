package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Hexer IV: Lidl edition");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(300);
        primaryStage.getScene().getStylesheets().add("gui/hivle.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
