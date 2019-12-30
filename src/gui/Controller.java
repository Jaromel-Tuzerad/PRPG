package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import gameResources.Entity;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //Global
    @FXML
    private GridPane gridPaneGlobal;

    //Status bars and labels (0, 0)
    @FXML
    private ProgressBar progressBarHealth;
    @FXML
    private ProgressBar progressBarHunger;
    @FXML
    private ProgressBar progressBarEnergy;
    @FXML
    private ProgressBar progressBarMana;

    @FXML
    private Label labelHealth;
    @FXML
    private Label labelHunger;
    @FXML
    private Label labelEnergy;
    @FXML
    private Label labelMana;

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
        StringBuilder tileDescription = new StringBuilder(Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getDescription());
        if(!Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities().isEmpty()) {
            for(Entity e : Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities()) {
                tileDescription.append("\n").append(e.getDescription());
            }
        }
        LabelTileDescription.setText(tileDescription.toString());
    }

    @FXML
    private void moveNorth() {
        if(Main.player.getY() > 0) {
            movePlayer(0, -1);
        }
    }

    @FXML
    private void moveSouth() {
        if(Main.player.getY() < 8) {
            movePlayer(0, 1);
        }
    }

    @FXML
    private void moveEast() {
        if(Main.player.getX() < 8) {
            movePlayer(1, 0);
        }
    }

    @FXML
    private void moveWest() {
        if(Main.player.getX() > 0) {
            movePlayer(-1, 0);
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        //Map render
        reloadMap();

        //Setting description for player's initial starting position
        StringBuilder tileDescription = new StringBuilder(Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getDescription());
        if(!Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities().isEmpty()) {
            for(Entity e : Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities()) {
                tileDescription.append("\n").append(e.getDescription());
            }
        }
        LabelTileDescription.setText(tileDescription.toString());

        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());

        //Setting the initial status bars percentage
        double healthPercentage = (double) Main.player.getHealth() / Main.player.getMaxHealth();
        progressBarHealth.setProgress(healthPercentage);
        labelHealth.setText("Health (" + (int) healthPercentage*100 + " %)");
        progressBarHunger.setProgress((double) Main.player.getHunger()/100);
        labelHunger.setText("Hunger (" + Main.player.getHunger() + " %)");
        progressBarEnergy.setProgress(((double) (Main.player.getEnergy())) / (double) (Main.player.getMaxEnergy()));
        labelEnergy.setText("Energy (" + Main.player.getEnergy() + "/" + Main.player.getMaxEnergy() + ")");
        progressBarMana.setProgress(((double) (Main.player.getEnergy())) / (double) (Main.player.getMaxEnergy()));
        labelMana.setText("Mana (" + Main.player.getMana() + "/" + Main.player.getMaxMana() + ")");

        gridPaneGlobal.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    moveNorth();
                    break;
                case A:
                    moveWest();
                    break;
                case S:
                    moveSouth();
                    break;
                case D:
                    moveEast();
                    break;
            }
        });
    }

}
