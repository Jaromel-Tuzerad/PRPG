package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.Map;
import resources.NPC;
import resources.Player;

public class Main extends Application {

    static Player player;
    static Map currentMap;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GamePanel.fxml"));
        primaryStage.setTitle("Hexer IV: Lidl edition");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        player = new Player("Sharpain", 4, 4);
        currentMap = new Map(0);
        currentMap.getTileByCoords(1, 2).addEntity(new NPC(1, 2, "Adam"));
        currentMap.getTileByCoords(1, 2).addEntity(new NPC(1, 2, "Michael"));
        currentMap.getTileByCoords(4, 4).addEntity(new NPC(4, 4, "Petr"));
        launch(args);
    }
}
