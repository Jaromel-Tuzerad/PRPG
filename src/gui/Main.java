package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.Entity;
import resources.Map;
import resources.Player;

import java.util.ArrayList;

public class Main extends Application {

    static Player player;
    static Map currentMap;
    static ArrayList<Entity> entities;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GamePanel.fxml"));
        primaryStage.setTitle("Vitcher IV: Lidl edition");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        player = new Player("Sharpain", 4, 4);
        currentMap = new Map(0);
        entities = new ArrayList<>();
        launch(args);
    }
}
