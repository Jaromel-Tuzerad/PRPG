package gui;

import gameLogic.NPC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mapping.Map;
import gameLogic.Player;

public class Main extends Application {

    static Player player;
    static Map currentMap;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Hexer IV: Lidl edition");
        primaryStage.setScene(new Scene(root, 200, 200));
        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(200);
        primaryStage.getScene().getStylesheets().add("gui/hivle.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
