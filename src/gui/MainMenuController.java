package gui;

import gameLogic.Entity;
import gameLogic.NPC;
import gameLogic.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mapping.Map;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button buttonStartGame;
    @FXML
    private Button buttonExit;

    @FXML
    public void startGame(ActionEvent event) throws Exception {

        Main.player = new Player("Sharpain", 4, 4);
        Main.currentMap = new Map(0);
        Main.currentMap.getTileByCoords(1, 2).addEntity(new NPC(1, 2, "Adam", "Adam is walking around here", '¥', 10, 0, 0, 0, 0,0));
        Main.currentMap.getTileByCoords(1, 2).addEntity(new NPC(1, 2, "Michael", "Michael is walking around here", '¥', 10, 0, 0, 0, 0,0));
        Main.currentMap.getTileByCoords(4, 4).addEntity(new NPC(4, 4, "Petr", "Petr is walking around here", '¥', 10, 0, 0, 0, 0,0));
        Parent root = FXMLLoader.load(getClass().getResource("GamePanel.fxml"));

        Stage gameStage = new Stage();
        gameStage.setTitle("Hexer IV: Lidl edition");
        gameStage.setScene(new Scene(root, 600, 600));
        gameStage.setMinHeight(600);
        gameStage.setMinWidth(600);
        gameStage.getScene().getStylesheets().add("gui/hivle.css");
        gameStage.show();

        buttonStartGame.getScene().getWindow().hide();
    }

    public void exit() {
         Stage stage = (Stage) buttonExit.getScene().getWindow();
         stage.close();
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

}
