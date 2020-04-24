package prpg.gui;

import prpg.gui.mainMenu.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static String gameTitle = "Hexer IV: Lydl Edyschön";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu/MainMenu.fxml"));
        primaryStage.setTitle("Hexer IV: Lidl edition");
        primaryStage.setScene(new Scene(root, MainMenuController.MAIN_MENU_WIDTH, MainMenuController.MAIN_MENU_HEIGHT));
        primaryStage.setMinWidth(MainMenuController.MAIN_MENU_WIDTH);
        primaryStage.setMinHeight(MainMenuController.MAIN_MENU_HEIGHT);
        primaryStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
