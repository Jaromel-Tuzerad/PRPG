package gui;

import exceptions.ExceptionAlert;
import gameLogic.entities.*;
import gameLogic.inventory.InventoryItem;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class GameController implements Initializable {

    //Global
    @FXML
    private GridPane gridPaneGlobal;
    //TODO - find a use for turns
    private int turnsPassed;
    private ObservableList<Entity> tileEntities;
    private ObservableList<String> entityActions;
    private ObservableList<InventoryItem> inventoryItems;
    private ObservableList<String> inventoryItemActions;
    private Player player;
    private Map currentMap;

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
    private ListView listViewEntityActions;
    @FXML
    private ListView listViewInventory;
    @FXML
    private ListView listViewInventoryActions;

    //Calls an alert when an error arises
    public static void callAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Refresh the map
    private void reloadMap() {
        for (int i = 0; i < gridPaneMap.getChildren().size(); i++) {
            int y = i % 9;
            int x = i / 9;
            setTileChar(x, y, currentMap.getTileByCoords(x, y).getIcon());
        }
    }

    //Makes small changes to map tile icons (quicker than refreshing the whole map)
    private void setTileChar(int x, int y, char c) {
        ((Label) gridPaneMap.getChildren().get(x * 9 + y)).setText("[" + c + "]");
    }

    //Move player in a relative direction, passes a turn and refreshes the GUI
    private void movePlayer(int dX, int dY) {
        setTileChar(player.getX(), player.getY(), currentMap.getTileByCoords(player.getX(), player.getY()).getIcon());
        player.moveBy(dX, dY);
        setTileChar(player.getX(), player.getY(), player.getIcon());
        passTurn();
        refreshGUI();
    }

    @FXML
    private void moveNorth() {
        if (player.getY() > 0) {
            movePlayer(0, -1);
        }
    }

    @FXML
    private void moveSouth() {
        if (player.getY() < 8) {
            movePlayer(0, 1);
        }
    }

    @FXML
    private void moveEast() {
        if (player.getX() < 8) {
            movePlayer(1, 0);
        }
    }

    @FXML
    private void moveWest() {
        if (player.getX() > 0) {
            movePlayer(-1, 0);
        }
    }

    //This happens when a turn passes - player either moves or waits
    private void passTurn() {
        try {
            if (player.getHunger() > 0) {
                player.decreaseHunger();
            } else {
                player.starve();
            }
            turnsPassed += 1;
        } catch (ExceptionAlert e) {
            callAlert(e.getAlertTitle(), e.getAlertHeader(), e.getAlertMessage());
            gameOver();
        }
    }

    //Refreshes the progressBars and listViews
    private void refreshGUI() {

        //Refreshes progress bars
        labelHealth.setText("Health (" + (double) player.getHealth() / player.getMaxHealth() * 100 + " %)");
        progressBarHealth.setProgress(((double) player.getHealth()) / player.getMaxHealth());
        labelHunger.setText("Hunger (" + player.getHunger() + " %)");
        progressBarHunger.setProgress(((double) player.getHunger()) / 100);
        progressBarEnergy.setProgress(((double) (player.getEnergy())) / (double) (player.getMaxEnergy()));
        labelEnergy.setText("Energy (" + player.getEnergy() + "/" + player.getMaxEnergy() + ")");
        progressBarMana.setProgress(((double) (player.getEnergy())) / (double) (player.getMaxEnergy()));
        labelMana.setText("Mana (" + player.getMana() + "/" + player.getMaxMana() + ")");

        // Refreshes entities in listViewEntities
        tileEntities.clear();
        for(Entity entity : currentMap.getTileByCoords(player.getX(), player.getY()).getEntities()) {
            tileEntities.add(entity);
        }
        listViewEntities.setItems(tileEntities);
        listViewEntities.refresh();

        // Refreshes inventory items in listViewInventory
        inventoryItems.clear();
        for(InventoryItem item : player.getInventory()) {
            inventoryItems.add(item);
        }
        listViewInventory.setItems(inventoryItems);
        listViewInventory.refresh();

        // Refreshes tile description
        StringBuilder tileDescription = new StringBuilder(currentMap.getTileByCoords(player.getX(), player.getY()).getDescription());
        if (!currentMap.getTileByCoords(player.getX(), player.getY()).getEntities().isEmpty()) {
            for (Entity e : currentMap.getTileByCoords(player.getX(), player.getY()).getEntities()) {
                tileDescription.append("\n").append(e.getDescription());
            }
        }
        LabelTileDescription.setText(tileDescription.toString());
    }

    //Lists actions in listViewActions for the selected entity according to it's type
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

    // TODO - Add actions to listViewItemActions depending on item type
    /*private void displayActionsForInventoryItem(InventoryItem item) {
        entityActions.clear();
        if (item.getType().equals(Item.ItemType.FOOD)) {
            inventoryItemActions.add("Eat");
        } else {
            if (item.getType().equals(Item.ItemType.FOOD)) {
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
    }*/

    //Executes selected action
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
                    player.pickUpItem(((Item) entity).toInventoryItem());
                    currentMap.getTileByCoords(entity.getX(), entity.getY()).removeEntity(entity);
                    refreshGUI();
                    break;
            }
        }
    }

    //

    // Called when the game ends (player dies)
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

        //Initial entities and the player are created
        player = new Player(4, 4, "Sharpain");
        currentMap = new Map(0);
        currentMap.getTileByCoords(1, 2).addEntity(new NPC(1, 2, "Adam", "Adam is walking around here", '¥', 10,  0, 0, 0, 0));
        currentMap.getTileByCoords(1, 2).addEntity(new NPC(1, 2, "Michael", "Michael is walking around here", '¥', 10, 0, 0, 0, 0));
        currentMap.getTileByCoords(4, 4).addEntity(new NPC(4, 4, "Petr", "Petr is walking around here", '¥', 10, 0, 0, 0, 0));
        currentMap.getTileByCoords(4, 4).addEntity(new Item(4, 4, "Food", "Food is floating in the river", Item.ItemType.FOOD, 0, 0, 0));
        currentMap.getTileByCoords(4, 4).addEntity(new Enemy(4, 4, "Monster", "There's a Shoggoth swimming in the river", 0, 0,  0, 0, 0));
        currentMap.getTileByCoords(4, 4).addEntity(new Item(4, 4, "Food", "Food is floating in the river", Item.ItemType.FOOD, 0, 0, 0));
        currentMap.getTileByCoords(4, 4).addEntity(new Item(4, 4, "Food", "Food is floating in the river", Item.ItemType.FOOD, 0, 0, 0));

        //Map render
        reloadMap();

        // Initialize listView arrayLists
        tileEntities = FXCollections.observableArrayList();
        entityActions = FXCollections.observableArrayList();
        inventoryItems = FXCollections.observableArrayList();
        inventoryItemActions = FXCollections.observableArrayList();

        // Sets the ChangeListener for the selected item in listView - sets what happens when an item is selected
        listViewEntities.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Entity>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            displayActionsForEntity((Entity) listViewEntities.getSelectionModel().getSelectedItem());
        });
        listViewEntityActions.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            executeActionOnEntity((String) listViewEntityActions.getSelectionModel().getSelectedItem(), (Entity) listViewEntities.getSelectionModel().getSelectedItem());
        });
        listViewInventory.getSelectionModel().selectedItemProperty().addListener((ChangeListener<InventoryItem>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            //TODO Function for inventorzitem selection
        });
        listViewInventoryActions.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            //TODO function for inventory item action selection
        });

        // Resets the GUI look according to current values
        refreshGUI();

        // Sets the tile which the player is initially on to display his icon
        setTileChar(player.getX(), player.getY(), player.getIcon());

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
        listViewInventory.setCellFactory(lv -> new ListCell<InventoryItem>() {
            @Override
            public void updateItem(InventoryItem item, boolean empty) {
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
