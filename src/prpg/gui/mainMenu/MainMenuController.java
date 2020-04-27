package prpg.gui.mainMenu;

import prpg.exceptions.AlertException;
import prpg.gameLogic.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import prpg.gui.gamePanel.GamePanelController;
import prpg.gui.Main;
import prpg.gui.newGamePanel.NewGamePanelController;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class MainMenuController {

    public static final int MAIN_MENU_WIDTH = 200;
    public static final int MAIN_MENU_HEIGHT = 400;

    @FXML
    private Button buttonStartGame;
    @FXML
    private Button buttonExit;

    @FXML
    public void startGame() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../newGamePanel/NewGamePanel.fxml"));
        Stage gameStage = new Stage();
        gameStage.setTitle(Main.gameTitle);
        gameStage.setScene(new Scene(root, NewGamePanelController.NEW_GAME_PANEL_WIDTH, NewGamePanelController.NEW_GAME_PANEL_HEIGHT));
        gameStage.setMinWidth(NewGamePanelController.NEW_GAME_PANEL_WIDTH);
        gameStage.setMinHeight(NewGamePanelController.NEW_GAME_PANEL_HEIGHT);
        gameStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
        gameStage.show();
        exit();
    }

    @FXML
    public void loadGame() {
        try {
            // Reading the object from a file
            File directory = new File(System.getProperty("user.home") + "/Documents/PRPG SaveGameData");
            if(!directory.isDirectory()) {
                directory.mkdirs();
            }
            FileInputStream file = new FileInputStream(System.getProperty("user.home") + "/Documents/PRPG SaveGameData/savedGameData.txt");
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            GamePanelController.currentGame = (Game) in.readObject();

            in.close();
            file.close();

            Parent root = FXMLLoader.load(getClass().getResource("../gamePanel/GamePanel.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle(Main.gameTitle);
            gameStage.setScene(new Scene(root, GamePanelController.GAME_PANEL_WIDTH, GamePanelController.GAME_PANEL_HEIGHT));
            gameStage.setMinWidth(GamePanelController.GAME_PANEL_WIDTH);
            gameStage.setMinHeight(GamePanelController.GAME_PANEL_HEIGHT);
            gameStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            gameStage.show();
            exit();
        } catch(Exception e) { GamePanelController.callAlert(new AlertException("Loading game",
                "There was an error while trying to load a previously saved game",
                "The saved game is either corrupted or does not exist. " +
                "\nPlease start a new game"));
            System.out.println(e.getMessage());}
    }

    @FXML
    public void exit() {
         Stage stage = (Stage) buttonExit.getScene().getWindow();
         stage.close();
    }

}
