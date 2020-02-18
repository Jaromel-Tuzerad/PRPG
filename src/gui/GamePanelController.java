package gui;

import exceptions.ExceptionAlert;
import gameLogic.entities.*;
import gameLogic.inventory.Equipment;
import gameLogic.inventory.Food;
import gameLogic.inventory.InventoryItem;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mapping.Map;

import java.net.URL;
import java.util.ResourceBundle;

public class GamePanelController implements Initializable {

    // Global
    @FXML
    private GridPane gridPaneGlobal;
    // TODO - find a use for turns
    private int turnsPassed;

    // Status bars and labels (0, 0)
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

    // Game map (0, 1)
    @FXML
    private GridPane gridPaneMap;

    // Move buttons (0, 2)
    // TODO - obsolete?
    @FXML
    private Button buttonNorth;
    @FXML
    private Button buttonEast;
    @FXML
    private Button buttonSouth;
    @FXML
    private Button buttonWest;
    
    // Tile description (1, 1)
    @FXML
    private Label LabelTileDescription;

    // Entities and actions (1, 2)
    @FXML
    private ListView listViewEntities;
    private ObservableList<Entity> tileEntities;
    @FXML
    private ListView listViewEntityActions;
    private ObservableList<String> entityActions;

    @FXML
    private void moveNorth() {
        if (Main.player.getY() > 0) {
            movePlayer(0, -1);
        }
    }

    @FXML
    private void moveSouth() {
        if (Main.player.getY() < 8) {
            movePlayer(0, 1);
        }
    }

    @FXML
    private void moveEast() {
        if (Main.player.getX() < 8) {
            movePlayer(1, 0);
        }
    }

    @FXML
    private void moveWest() {
        if (Main.player.getX() > 0) {
            movePlayer(-1, 0);
        }
    }

    @FXML
    private void openInventory(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
            Stage inventoryStage = new Stage();
            inventoryStage.setTitle("Inventory");
            inventoryStage.setScene(new Scene(root, 600, 600));
            inventoryStage.setMinWidth(600);
            inventoryStage.setMinHeight(600);
            inventoryStage.getScene().getStylesheets().add("gui/hivle.css");
            inventoryStage.show();
            gridPaneGlobal.getScene().getWindow().hide();
        } catch(Exception ex1) {
            ex1.printStackTrace();
        }
    }

    @FXML
    private void saveGame() {
        // TODO - serializace
    }

    @FXML
    private void loadGame() {
        // TODO
    }

    @FXML
    private void openMainMenu() {
        // TODO
    }

    // Calls an alert when an error arises
    public static void callAlert(ExceptionAlert exc) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(exc.getAlertTitle());
        alert.setHeaderText(exc.getAlertHeader());
        alert.setContentText(exc.getAlertContent());
        alert.showAndWait();
    }

    // Refresh the map
    private void reloadMap() {
        for (int i = 0; i < gridPaneMap.getChildren().size(); i++) {
            int y = i % 9;
            int x = i / 9;
            setTileChar(x, y, Main.currentMap.getTileByCoords(x, y).getIcon());
        }
    }

    // Makes small changes to map tile icons (quicker than refreshing the whole map)
    private void setTileChar(int x, int y, char c) {
        ((Label) gridPaneMap.getChildren().get(x * 9 + y)).setText("[" + c + "]");
    }

    // Move Main.player in a relative direction, passes a turn and refreshes the GUI
    private void movePlayer(int dX, int dY) {
        setTileChar(Main.player.getX(), Main.player.getY(), Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getIcon());
        Main.player.moveBy(dX, dY);
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());
        passTurn();
        refreshGUI();
    }

    //This happens when a turn passes - Main.player either moves or waits
    private void passTurn() {
        try {
            if (Main.player.getHunger() > 0) {
                Main.player.decreaseHunger();
            } else {
                Main.player.starve();
            }
            turnsPassed += 1;
        } catch (ExceptionAlert e) {
            callAlert(e);
            gameOver();
        }
    }

    // Refreshes progress bars
    private void refreshProgressBars() {
        labelHealth.setText("Health (" + (double) Main.player.getHealth() / Main.player.getMaxHealth() * 100 + " %)");
        progressBarHealth.setProgress(((double) Main.player.getHealth()) / Main.player.getMaxHealth());
        labelHunger.setText("Hunger (" + Main.player.getHunger() + " %)");
        progressBarHunger.setProgress(((double) Main.player.getHunger()) / 100);
        progressBarEnergy.setProgress(((double) (Main.player.getEnergy())) / (double) (Main.player.getMaxEnergy()));
        labelEnergy.setText("Energy (" + Main.player.getEnergy() + "/" + Main.player.getMaxEnergy() + ")");
        progressBarMana.setProgress(((double) (Main.player.getEnergy())) / (double) (Main.player.getMaxEnergy()));
        labelMana.setText("Mana (" + Main.player.getMana() + "/" + Main.player.getMaxMana() + ")");
    }

    // Refreshes detected entities
    private void refreshEntities() {
        tileEntities.clear();
        tileEntities.addAll(Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities());
        listViewEntities.setItems(tileEntities);
        listViewEntities.refresh();
    }

    // Refreshes tile description
    private void refreshDescription() {
        StringBuilder tileDescription = new StringBuilder(Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getDescription());
        if (!Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities().isEmpty()) {
            for (Entity e : Main.currentMap.getTileByCoords(Main.player.getX(), Main.player.getY()).getEntities()) {
                tileDescription.append("\n").append(e.getDescription());
            }
        }
        LabelTileDescription.setText(tileDescription.toString());
    }

    // Refreshes the whole pane
    private void refreshGUI() {

        refreshProgressBars();
        refreshEntities();
        refreshDescription();

    }

    // Lists actions in listViewActions for the selected entity according to it's type
    private void displayActionsForEntity(Entity entity) {
        entityActions.clear();
        if (entity instanceof Enemy) {
            entityActions.add("Fight");
        } else {
            if (entity instanceof Item) {
                entityActions.add("Pick up");
            } else {
                if (entity instanceof NPC) {
                    entityActions.add("Talk");
                    entityActions.add("Fight");
                }
            }
        }
        listViewEntityActions.setItems(entityActions);
        listViewEntityActions.refresh();
    }

    //Executes selected action on specified entity
    private void executeActionOnEntity(String actionName, Entity entity) {
        if (actionName != null) {
            switch (actionName) {
                case "Talk":
                    ((NPC) entity).talk();
                    break;
                case "Fight":
                    ((Mob) entity).fight();
                    break;
                case "Pick up":
                    Main.player.pickUpItem(((Item) entity).getItem());
                    Main.currentMap.getTileByCoords(entity.getX(), entity.getY()).removeEntity(entity);
                    refreshDescription();
                    refreshEntities();
                    break;
            }
        }
    }

    // Called when the game ends (Main.player dies)
    private void gameOver() {
        Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
        stage.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Hexer IV: Lidl edition");
            gameStage.setScene(new Scene(root, 600, 600));
            gameStage.setMinHeight(600);
            gameStage.setMinWidth(600);
            gameStage.getScene().getStylesheets().add("gui/hivle.css");
            gameStage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        // Initial entities and the Main.player are created
        if(Main.player == null) {
            Main.player = new Player(4, 4, "Sharpain");
        }

        // TODO - map generation
        if(Main.currentMap == null) {
            Main.currentMap = new Map(0);
            Main.currentMap.getTileByCoords(1, 2).addEntity(new NPC(1, 2, "Adam", "Adam is walking around here", '¥', 10, 0, 0, 0, 0));
            Main.currentMap.getTileByCoords(1, 2).addEntity(new NPC(1, 2, "Michael", "Michael is walking around here", '¥', 10, 0, 0, 0, 0));
            Main.currentMap.getTileByCoords(4, 4).addEntity(new NPC(4, 4, "Petr", "Petr is walking around here", '¥', 10, 0, 0, 0, 0));
            Main.currentMap.getTileByCoords(4, 4).addEntity(new Item(4, 4, "Food", "Food is floating in the river", new Food("Food", 25)));
            Main.currentMap.getTileByCoords(4, 4).addEntity(new Enemy(4, 4, "Monster", "There's a Shoggoth swimming in the river", 0, 0, 0, 0, 0));
            Main.currentMap.getTileByCoords(4, 4).addEntity(new Item(4, 4, "Food", "Food is floating in the river", new Food("Food", 25)));
            Main.currentMap.getTileByCoords(4, 4).addEntity(new Item(4, 4, "Spoon of Doom", "Spoon of Doom is laying on the beach", new Equipment("Spoon of Doom", 25, 25, 25, InventoryItem.ItemType.WEAPON)));
        }

        // Map render
        reloadMap();

        // Initialize listView arrayLists
        tileEntities = FXCollections.observableArrayList();
        entityActions = FXCollections.observableArrayList();

        // Sets the ChangeListener for the selected item in listView - sets what happens when an item is selected

        listViewEntities.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Entity>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            displayActionsForEntity(newValue);
        });

        listViewEntityActions.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            executeActionOnEntity(newValue, (Entity) listViewEntities.getSelectionModel().getSelectedItem());
        });

        // Resets the GUI look according to current values
        refreshGUI();

        // Sets the tile which the Main.player is initially on to display his icon
        setTileChar(Main.player.getX(), Main.player.getY(), Main.player.getIcon());

        // Enables movement using the keyboard by binding key pressed events to buttons
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
