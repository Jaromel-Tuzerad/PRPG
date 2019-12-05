package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import resources.Entity;

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

    //Tile description (1, 1)
    @FXML
    private Label LabelTileDescription;

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

    private void movePlayer(int dX, int dY) {
        setTileChar(Main.player.getX(), Main.player.getY(), Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getIcon());
        Main.player.move(dX, dY);
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());
        String tileDescription = Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getDescription();
        if(!Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities().isEmpty()) {
            for(Entity e : Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities()) {
                tileDescription += ("\n" + e.getDescription());
            }
        }
        LabelTileDescription.setText(tileDescription);
    }

    @FXML
    private void moveNorth() {
        movePlayer(0, -1);
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
        movePlayer(0, 1);
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
        movePlayer(1, 0);
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
        movePlayer(-1, 0);
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
        StringBuilder tileDescription = new StringBuilder(Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getDescription());
        if(!Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities().isEmpty()) {
            for(Entity e : Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities()) {
                tileDescription.append("\n").append(e.getDescription());
            }
        }
        LabelTileDescription.setText(tileDescription.toString());
        setTileChar(4, 4, Main.player.getIcon());
    }

}
