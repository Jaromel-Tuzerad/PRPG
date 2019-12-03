package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    
    //Game map (0, 1)
    @FXML
    private GridPane gridPaneMap;

    //Move buttons (0, 2)
    @FXML
    private Button buttonNorth;
    @FXML
    private Button buttonEast;
    @FXML
    private Button buttonSouth;
    @FXML
    private Button buttonWest;

    private void reloadMap() {
        for(int i = 0; i<gridPaneMap.getChildren().size(); i++) {
            int y = i%9;
            int x = i/9;
            setTileChar(x, y, Main.currentMap.getTileByCoords(x, y).getIcon());
        }
    }

    //Makes small changes to map tiles (quicker than refreshing the whole map)
    private void setTileChar(int x, int y, char c) {
        ((Label)gridPaneMap.getChildren().get(x*9+y)).setText("[" + c + "]");
    }

    @FXML
    private void moveNorth() {
        setTileChar(Main.player.getX(), Main.player.getY(), Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getIcon());
        Main.player.move(0, -1);
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());
        switch (Main.player.getY()) {
            case 0:
                buttonNorth.setDisable(true);
                break;
            case 7:
                buttonSouth.setDisable(false);
                break;
        }
    }

    @FXML
    private void moveSouth() {
        setTileChar(Main.player.getX(), Main.player.getY(), Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getIcon());
        Main.player.move(0, 1);
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());
        switch (Main.player.getY()) {
            case 1:
                buttonNorth.setDisable(false);
                break;
            case 8:
                buttonSouth.setDisable(true);
                break;
        }
    }

    @FXML
    private void moveEast() {
        setTileChar(Main.player.getX(), Main.player.getY(), Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getIcon());
        Main.player.move(1, 0);
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());
        switch (Main.player.getX()) {
            case 1:
                buttonWest.setDisable(false);
                break;
            case 8:
                buttonEast.setDisable(true);
                break;
        }
    }

    @FXML
    private void moveWest() {
        setTileChar(Main.player.getX(), Main.player.getY(), Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getIcon());
        Main.player.move(-1, 0);
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());
        switch (Main.player.getX()) {
            case 0:
                buttonWest.setDisable(true);
                break;
            case 7:
                buttonEast.setDisable(false);
                break;
        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        reloadMap();
        setTileChar(4, 4, Main.player.getIcon());
    }

}
