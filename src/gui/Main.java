package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int MAIN_MENU_WIDTH = 200;
    public static final int MAIN_MENU_HEIGHT = 400;

    public static final int GAME_PANEL_WIDTH = 650;
    public static final int GAME_PANEL_HEIGHT = 630;

    public static final int FIGHT_PANEL_WIDTH = 630;
    public static final int FIGHT_PANEL_HEIGHT = 600;

    public static final int INVENTORY_WIDTH = 619;
    public static final int INVENTORY_HEIGHT = 678;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu/MainMenu.fxml"));
        primaryStage.setTitle("Hexer IV: Lidl edition");
        primaryStage.setScene(new Scene(root, Main.MAIN_MENU_WIDTH, Main.MAIN_MENU_HEIGHT));
        primaryStage.setMinWidth(Main.MAIN_MENU_WIDTH);
        primaryStage.setMinHeight(Main.MAIN_MENU_HEIGHT);
        primaryStage.getScene().getStylesheets().add("gui/hivle.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
