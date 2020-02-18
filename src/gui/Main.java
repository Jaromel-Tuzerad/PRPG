package gui;

import gameLogic.entities.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mapping.Map;

public class Main extends Application {

    public static Player player;
    public static Map currentMap;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Hexer IV: Lidl edition");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.getScene().getStylesheets().add("gui/hivle.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
