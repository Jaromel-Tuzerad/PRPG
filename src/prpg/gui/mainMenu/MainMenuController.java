package prpg.gui.mainMenu;

import prpg.gui.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import prpg.gui.gamePanel.GamePanelController;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    public static final int MAIN_MENU_WIDTH = 200;
    public static final int MAIN_MENU_HEIGHT = 400;

    @FXML
    private Button buttonStartGame;
    @FXML
    private Button buttonExit;

    @FXML
    public void startGame(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../gamePanel/GamePanel.fxml"));
        Stage gameStage = new Stage();
        gameStage.setTitle("Hexer IV: Lydl Edyschön");
        gameStage.setScene(new Scene(root, GamePanelController.GAME_PANEL_WIDTH, GamePanelController.GAME_PANEL_HEIGHT));
        gameStage.setMinWidth(GamePanelController.GAME_PANEL_WIDTH);
        gameStage.setMinHeight(GamePanelController.GAME_PANEL_HEIGHT);
        gameStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
        gameStage.show();
        exit();
    }

    @FXML
    public void exit() {
         Stage stage = (Stage) buttonExit.getScene().getWindow();
         stage.close();
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

}
