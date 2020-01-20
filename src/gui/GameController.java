package gui;

import exceptions.EntityDiedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import gameLogic.Entity;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    //Global
    @FXML
    private GridPane gridPaneGlobal;
    //TODO - find a use for turns
    private int turnsPassed;
    private ObservableList<Entity> tileEntities;

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
    //TODO - obsolete?
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

    //Entities and actions (1, 2)
    @FXML
    private ListView listViewEntities;
    @FXML
    private ListView listViewActions;

    //Refresh the map
    private void reloadMap() {
        for(int i = 0; i<gridPaneMap.getChildren().size(); i++) {
            int y = i%9;
            int x = i/9;
            setTileChar(x, y, Main.currentMap.getTileByCoords(x, y).getIcon());
        }
    }

    //Makes small changes to map tile icons (quicker than refreshing the whole map)
    private void setTileChar(int x, int y, char c) {
        ((Label)gridPaneMap.getChildren().get(x*9+y)).setText("[" + c + "]");
    }

    //Move player in a relative direction, passes a turn and refreshes the GUI
    private void movePlayer(int dX, int dY) {
        setTileChar(Main.player.getX(), Main.player.getY(), Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getIcon());
        Main.player.move(dX, dY);
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());
        passTurn();
        refreshGUI();
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

    //This happens when a turn passes - player either moves or waits
    private void passTurn() {
        try {
            if (Main.player.getHunger() > 0) {
                Main.player.decreaseHunger();
            } else {
                Main.player.starve();
            }
            turnsPassed += 1;
        } catch(EntityDiedException e) {
            gameOver();
        }
    }

    //Refreshes the progressBars and listViews
    public void refreshGUI() {

        //Refreshes progress bars
        labelHealth.setText("Health (" + (double) Main.player.getHealth() / Main.player.getMaxHealth() * 100 + " %)");
        progressBarHealth.setProgress(((double) Main.player.getHealth()) / Main.player.getMaxHealth());
        labelHunger.setText("Hunger (" + Main.player.getHunger() + " %)");
        progressBarHunger.setProgress(((double) Main.player.getHunger()) / 100);
        progressBarEnergy.setProgress(((double) (Main.player.getEnergy())) / (double) (Main.player.getMaxEnergy()));
        labelEnergy.setText("Energy (" + Main.player.getEnergy() + "/" + Main.player.getMaxEnergy() + ")");
        progressBarMana.setProgress(((double) (Main.player.getEnergy())) / (double) (Main.player.getMaxEnergy()));
        labelMana.setText("Mana (" + Main.player.getMana() + "/" + Main.player.getMaxMana() + ")");

        //Refreshes entities and actions
        //TODO - add actions
        tileEntities.clear();
        for(Entity entity : Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities()) {
            tileEntities.add(entity);
            listViewEntities.setItems(tileEntities);
        }
        listViewEntities.refresh();

        //Refreshes tile description
        //TODO - Learn about the StringBuilder class
        StringBuilder tileDescription = new StringBuilder(Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getDescription());
        if(!Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities().isEmpty()) {
            for(Entity e : Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities()) {
                tileDescription.append("\n").append(e.getDescription());
            }
        }
        LabelTileDescription.setText(tileDescription.toString());

    }

    //Called when a game ends (player dies)
    public void gameOver() {
        System.out.println("You are dead, you shouldn't be moving...");
    }

    public void initialize(URL url, ResourceBundle rb) {

        //Map render
        reloadMap();

        //Initialize listView arrayLists
        tileEntities = FXCollections.observableArrayList();

        //Resets the GUI look according to current values
        refreshGUI();

        //Sets the tile which the player is initially on to display his icon
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());

        //Enables movement with the WASD keys by binding their events to buttons
        //TODO - not my code
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

        //Sets the manner in which objects in listViews is displayed
        //TODO - not my code
        listViewEntities.setCellFactory(lv -> new ListCell<Entity>() {
            @Override
            public void updateItem(Entity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = item.getDisplayName();
                    setText(text);
                }
            }
        });
    }
}
