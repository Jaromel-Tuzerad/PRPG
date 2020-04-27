package prpg.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import prpg.exceptions.XMLException;
import prpg.gameLogic.Game;

import java.io.IOException;

public class NewGamePanelController {

    public static final int NEW_GAME_PANEL_WIDTH = 250;
    public static final int NEW_GAME_PANEL_HEIGHT = 200;

    @FXML
    private TextField textFieldUsername;

    @FXML
    public void confirm() {
        try {
            GamePanelController.currentGame = new Game(textFieldUsername.getText());
            Parent root = FXMLLoader.load(getClass().getResource("/gui/GamePanel.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle(Main.gameTitle);
            gameStage.setScene(new Scene(root, GamePanelController.GAME_PANEL_WIDTH, GamePanelController.GAME_PANEL_HEIGHT));
            gameStage.setMinWidth(GamePanelController.GAME_PANEL_WIDTH);
            gameStage.setMinHeight(GamePanelController.GAME_PANEL_HEIGHT);
            gameStage.getScene().getStylesheets().add("/gui/hivle.css");
            gameStage.show();
            ((Stage) textFieldUsername.getScene().getWindow()).close();
        } catch(IOException | XMLException ioe) { System.out.println(ioe.getMessage()); }
    }

}
