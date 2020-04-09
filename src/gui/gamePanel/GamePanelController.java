package gui.gamePanel;

import exceptions.ExceptionAlert;
import exceptions.MobDiedException;
import gameLogic.EntityFactory;
import gameLogic.RandomFunctions;
import gameLogic.entities.mobs.Enemy;
import gameLogic.entities.Entity;
import gameLogic.entities.mobs.NPC;
import gameLogic.entities.mobs.Player;
import gameLogic.entities.objects.Item;
import gameLogic.inventory.Equipment;
import gameLogic.inventory.Food;
import gameLogic.inventory.InventoryItem;
import gui.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mapping.Map;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GamePanelController implements Initializable {

    public static Player player;
    public static Map currentMap;
    public static Enemy fightingEnemy;
    public static EntityFactory entFact;
    
    // Global
    @FXML
    private GridPane gridPaneGlobal;
    private int turnsPassed;

    // Status bars and labels (0, 1)
    @FXML
    private ProgressBar progressBarHealth;
    @FXML
    private ProgressBar progressBarHunger;
    @FXML
    private ProgressBar progressBarExperience;

    @FXML
    private Label labelHealth;
    @FXML
    private Label labelHunger;
    @FXML
    private Label labelExperience;

    // Game map (0, 2)
    @FXML
    private GridPane gridPaneMap;

    // Move buttons (0, 3)
    // TODO - obsolete?
    @FXML
    private Button buttonNorth;
    @FXML
    private Button buttonEast;
    @FXML
    private Button buttonSouth;
    @FXML
    private Button buttonWest;
    
    // Tile description (1, 2)
    @FXML
    private TextArea textAreaDescription;

    // Entities and actions (1, 3)
    @FXML
    private ListView listViewEntities;
    private ObservableList<Entity> tileEntities;
    @FXML
    private ListView listViewEntityActions;
    private ObservableList<String> entityActions;

    @FXML
    private void moveNorth() {
        try {
            if (player.getY() > 0) {
                movePlayer(0, -1);
            }
        } catch (MobDiedException mobDiedException) {
            callAlert(new ExceptionAlert("Player is dead", player.getDisplayName() + " has starved to death", "Better luck next time!"));
            gameOver();
        }
    }

    @FXML
    private void moveSouth() {
        try {
            if (player.getY() < 8) {
                movePlayer(0, 1);
            }
        } catch (MobDiedException mobDiedException) {
            callAlert(new ExceptionAlert("Player is dead", player.getDisplayName() + " has starved to death", "Better luck next time!"));
            gameOver();
        }
    }

    @FXML
    private void moveEast() {
        try {
            if (player.getX() < 8) {
                movePlayer(1, 0);
            }
        } catch (MobDiedException mobDiedException) {
            callAlert(new ExceptionAlert("Player is dead", player.getDisplayName() + " has starved to death", "Better luck next time!"));
            gameOver();
        }
    }

    @FXML
    private void moveWest() {
        try {
            if (player.getX() > 0) {
                movePlayer(-1, 0);
            }
        } catch (MobDiedException mobDiedException) {
            callAlert(new ExceptionAlert("Player is dead", player.getDisplayName() + " has starved to death", "Better luck next time!"));
            gameOver();
        }
    }

    @FXML
    private void waitTurn() throws MobDiedException {
        passTurn();
        refreshGUI();
    }

    @FXML
    private void openInventory() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../inventory/Inventory.fxml"));
            Stage inventoryStage = new Stage();
            inventoryStage.setTitle("Inventory");
            inventoryStage.setScene(new Scene(root, Main.INVENTORY_WIDTH, Main.INVENTORY_HEIGHT));
            inventoryStage.setMinWidth(Main.INVENTORY_WIDTH);
            inventoryStage.setMinHeight(Main.INVENTORY_HEIGHT);
            inventoryStage.setMaxWidth(Main.INVENTORY_WIDTH);
            inventoryStage.setMaxHeight(Main.INVENTORY_HEIGHT);
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
        gameOver();
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
            setTileChar(x, y, currentMap.getTileByCoords(x, y).getIcon());
        }
    }

    // Makes small changes to map tile icons (quicker than refreshing the whole map)
    private void setTileChar(int x, int y, char c) {
        ((Label) gridPaneMap.getChildren().get(x * 9 + y)).setText("[" + c + "]");
    }

    // Move GamePanelController.player in a relative direction, passes a turn and refreshes the GUI
    private void movePlayer(int dX, int dY) throws MobDiedException {
        setTileChar(player.getX(), player.getY(), currentMap.getTileByCoords(player.getX(), player.getY()).getIcon());
        player.moveBy(dX, dY);
        setTileChar(player.getX(), player.getY(), player.getIcon());
        waitTurn();
    }

    //This happens when a turn passes - GamePanelController.player either moves or waits
    private void passTurn() throws MobDiedException{
        if (player.getHunger() > 0) {
            player.decreaseHunger();
        } else {
            player.starve();
        }
        if(turnsPassed % 10 == 0) {
            int tileX = RandomFunctions.getRandomNumberInRange(0, 8);
            int tileY = RandomFunctions.getRandomNumberInRange(0, 8);
            currentMap.getTileByCoords(tileX, tileY).addEntity(entFact.getRandomEnemy());
        }
        turnsPassed += 1;
    }

    // Refreshes progress bars
    private void refreshProgressBars() {
        labelHealth.setText("Health (" + (int)((double) player.getHealth() / player.getMaxHealth() * 100) + " %)");
        progressBarHealth.setProgress((double) player.getHealth() / player.getMaxHealth());
        labelHunger.setText("Hunger (" + player.getHunger() + " %)");
        progressBarHunger.setProgress(((double) player.getHunger()) / 100);
        labelExperience.setText("Experience (" + (int)((double) player.getExperience() / player.getExperienceToNextLevel() * 100) + " %)");
        progressBarExperience.setProgress((double) player.getExperience() / player.getExperienceToNextLevel());
    }

    // Refreshes detected entities
    private void refreshEntities() {
        tileEntities.clear();
        tileEntities.addAll(currentMap.getTileByCoords(player.getX(), player.getY()).getEntities());
        listViewEntities.setItems(tileEntities);
        listViewEntities.refresh();
    }

    // Refreshes tile description
    private void refreshDescription() {
        StringBuilder tileDescription = new StringBuilder(currentMap.getTileByCoords(player.getX(), player.getY()).getDescription());
        if (!currentMap.getTileByCoords(player.getX(), player.getY()).getEntities().isEmpty()) {
            for (Entity e : currentMap.getTileByCoords(player.getX(), player.getY()).getEntities()) {
                tileDescription.append("\n").append(e.getDescription());
            }
        }
        textAreaDescription.setText(tileDescription.toString());
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
                    fight((Enemy) entity);
                    break;
                case "Pick up":
                    player.pickUpItem(((Item) entity).getItem());
                    currentMap.getTileByCoords(player.getX(), player.getY()).removeEntity(entity);
                    refreshDescription();
                    refreshEntities();
                    break;
            }
        }
    }

    // Starts fighting with an enemy
    private void fight(Enemy enemy) {
        fightingEnemy = enemy;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fightPanel/FightPanel.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Hexer IV: Lidl edition");
            gameStage.setScene(new Scene(root, Main.FIGHT_PANEL_WIDTH, Main.FIGHT_PANEL_HEIGHT));
            gameStage.setMinWidth(Main.FIGHT_PANEL_WIDTH);
            gameStage.setMinHeight(Main.FIGHT_PANEL_HEIGHT);
            gameStage.setMaxWidth(Main.FIGHT_PANEL_WIDTH);
            gameStage.setMaxHeight(Main.FIGHT_PANEL_HEIGHT);
            gameStage.getScene().getStylesheets().add("gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Called when the game ends (GamePanelController.player dies)
    private void gameOver() {
        try {
            player = null;
            fightingEnemy = null;
            currentMap = null;
            Parent root = FXMLLoader.load(getClass().getResource("../mainMenu/MainMenu.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Hexer IV: Lidl edition");
            gameStage.setScene(new Scene(root, 600, 600));
            gameStage.setMinHeight(600);
            gameStage.setMinWidth(600);
            gameStage.getScene().getStylesheets().add("gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        if(entFact == null) {
            try {
                entFact = new EntityFactory();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        // Initial entities and the GamePanelController.player are created
        if(player == null) {
            player = new Player(4, 4, "Sharpain", 5, 5, 5);
        }

        // TODO - map generation
        if(currentMap == null) {
            currentMap = new Map(0);
            for(int i = 0; i < 10; i++) {
                currentMap.getTileByCoords(RandomFunctions.getRandomNumberInRange(0, 8), RandomFunctions.getRandomNumberInRange(0, 8)).addEntity(entFact.getRandomEnemy());
            }
            currentMap.getTileByCoords(1, 2).addEntity(new NPC("Adam", "Adam is walking around here", '¥', 10));
            currentMap.getTileByCoords(1, 2).addEntity(new NPC("Michael", "Michael is walking around here", '¥', 10));
            currentMap.getTileByCoords(4, 4).addEntity(new NPC("Petr", "Petr is walking around here", '¥', 10));
            currentMap.getTileByCoords(4, 4).addEntity(new Item("Food", new Food("Food", 25)));
            currentMap.getTileByCoords(4, 4).addEntity(new Item("Food", new Food("Food", 25)));
            currentMap.getTileByCoords(4, 4).addEntity(new Item("Spoon of Doom", new Equipment("Spoon of Doom", 25, 25, 25, InventoryItem.ItemType.WEAPON)));
            currentMap.getTileByCoords(4, 4).addEntity(new Item("Fiddlestick", new Equipment("Fiddlestick", -3, -3, 99, InventoryItem.ItemType.WEAPON)));

        }

        // Map render
        reloadMap();

        // Initialize listView arrayLists
        tileEntities = FXCollections.observableArrayList();
        entityActions = FXCollections.observableArrayList();

        // Sets the ChangeListener for the selected item in listView - sets what happens when an item is selected

        listViewEntities.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Entity>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            Platform.runLater(() -> displayActionsForEntity(newValue) );
        });

        listViewEntityActions.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            Platform.runLater(() -> executeActionOnEntity(newValue, (Entity) listViewEntities.getSelectionModel().getSelectedItem()) );
        });

        // Resets the GUI look according to current values
        refreshGUI();

        // Sets the tile which the GamePanelController.player is initially on to display his icon
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
