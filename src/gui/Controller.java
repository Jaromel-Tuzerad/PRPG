package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import resources.Map;
import resources.Player;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Player player;
    private Map currentMap;

    @FXML
    private GridPane gridPaneMap;

    @FXML
    private Button buttonNorth;
    @FXML
    private Button buttonEast;
    @FXML
    private Button buttonSouth;
    @FXML
    private Button buttonWest;

    private void loadMap(Map map) {
        for(int i = 0; i<gridPaneMap.getChildren().size(); i++) {
            int row = i%9;
            int col = i/9;
            ((Label)gridPaneMap.getChildren().get(i)).setText("[" + String.valueOf(map.getTileByCoords(row, col).getIcon()) + "]");
        }
    }

    @FXML
    private void moveNorth() {
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + currentMap.getTileByCoords(player.getX(), player.getY()).getIcon() + "]");
        player.move(0, -1);
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + String.valueOf(player.getIcon()) + "]");
        switch (player.getY()) {
            case 0:
                buttonNorth.setDisable(true);
            case 7:
                buttonSouth.setDisable(false);
        }
    }

    @FXML
    private void moveSouth() {
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + currentMap.getTileByCoords(player.getX(), player.getY()).getIcon() + "]");
        player.move(0, 1);
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + String.valueOf(player.getIcon()) + "]");
        switch (player.getY()) {
            case 1:
                buttonNorth.setDisable(false);
            case 8:
                buttonSouth.setDisable(true);
        }
    }

    @FXML
    private void moveEast() {
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + currentMap.getTileByCoords(player.getX(), player.getY()).getIcon() + "]");
        player.move(1, 0);
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + String.valueOf(player.getIcon()) + "]");
        switch (player.getX()) {
            case 1:
                buttonWest.setDisable(false);
            case 8:
                buttonEast.setDisable(true);
        }
    }

    @FXML
    private void moveWest() {
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + currentMap.getTileByCoords(player.getX(), player.getY()).getIcon() + "]");
        player.move(-1, 0);
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + String.valueOf(player.getIcon()) + "]");
        switch (player.getX()) {
            case 0:
                buttonWest.setDisable(true);
            case 7:
                buttonEast.setDisable(false);
        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        currentMap = new Map(0);
        loadMap(currentMap);
        player = new Player("Sharpain");
        ((Label)gridPaneMap.getChildren().get(40)).setText("[" + String.valueOf(player.getIcon()) + "]");
    }

}
