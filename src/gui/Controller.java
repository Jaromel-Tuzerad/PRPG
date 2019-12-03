package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import resources.Map;
import resources.Player;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Player player;

    @FXML
    private GridPane gridPaneMap;

    private void loadMap(Map map) {
        for(int i = 0; i<gridPaneMap.getChildren().size(); i++) {
            int row = i%9;
            int col = i/9;
            ((Label)gridPaneMap.getChildren().get(i)).setText("[" + String.valueOf(map.getTileByCoords(row, col).getIcon()) + "]");
        }
    }

    @FXML
    private void moveNorth() {
        ((Label)gridPaneMap.getChildren().get(player.getX()*9+player.getY())).setText("[" + String.valueOf(player.getIcon()) + "]");
        player.move(0, -1);
    }

    public void initialize(URL url, ResourceBundle rb) {
        loadMap(new Map(0));
        player = new Player("Sharpain");
        ((Label)gridPaneMap.getChildren().get(40)).setText("[" + String.valueOf(player.getIcon()) + "]");
    }

}
