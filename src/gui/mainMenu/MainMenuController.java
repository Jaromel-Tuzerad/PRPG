package gui.mainMenu;

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
        gameStage.setScene(new Scene(root, 650, 630));
        gameStage.setMinHeight(630);
        gameStage.setMinWidth(650);
        gameStage.setMaxHeight(630);
        gameStage.setMaxWidth(650);
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
