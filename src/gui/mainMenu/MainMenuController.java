package gui.mainMenu;

import gui.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button buttonStartGame;
    @FXML
    private Button buttonExit;

    @FXML
    public void startGame(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../gamePanel/GamePanel.fxml"));
        Stage gameStage = new Stage();
        gameStage.setTitle("Hexer IV: Lidl edition");
        gameStage.setScene(new Scene(root, Main.GAME_PANEL_WIDTH, Main.GAME_PANEL_HEIGHT));
        gameStage.setMinWidth(Main.GAME_PANEL_WIDTH);
        gameStage.setMinHeight(Main.GAME_PANEL_HEIGHT);
        gameStage.setMaxWidth(Main.GAME_PANEL_WIDTH);
        gameStage.setMaxHeight(Main.GAME_PANEL_HEIGHT);
        gameStage.getScene().getStylesheets().add("gui/hivle.css");
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
