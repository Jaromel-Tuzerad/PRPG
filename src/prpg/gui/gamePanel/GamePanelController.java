package prpg.gui.gamePanel;

import prpg.exceptions.ExceptionAlert;
import prpg.exceptions.MobDiedException;
import prpg.exceptions.XMLException;
import prpg.gameLogic.GameFactory;
import prpg.gameLogic.RandomFunctions;
import prpg.gameLogic.entities.mobs.Enemy;
import prpg.gameLogic.entities.Entity;
import prpg.gameLogic.entities.mobs.NPC;
import prpg.gameLogic.entities.mobs.Player;
import prpg.gameLogic.entities.objects.Item;
import prpg.gameLogic.entities.objects.Shop;
import prpg.gameLogic.items.Equipment;
import prpg.gameLogic.items.Food;
import prpg.gameLogic.items.InventoryItem;
import prpg.gui.fightPanel.FightPanelController;
import prpg.gui.inventory.InventoryController;
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
import prpg.gui.shop.ShopController;
import prpg.mapping.Map;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GamePanelController implements Initializable {

    public static final int GAME_PANEL_WIDTH = 650;
    public static final int GAME_PANEL_HEIGHT = 630;

    public static Player player;
    public static Map currentMap;

    public static GameFactory gameFactory;
    
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
    private void waitTurn() {
        try {
            passTurn();
            refreshGUI();
        } catch(XMLException xmle) {
            System.out.println(xmle.getMessage());
        } catch(MobDiedException mde) {
            callAlert(new ExceptionAlert("Player is dead", player.getDisplayName() + " has starved to death", "Better luck next time!"));
            gameOver();
        }

    }

    @FXML
    private void openInventory() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../inventory/Inventory.fxml"));
            Stage inventoryStage = new Stage();
            inventoryStage.setTitle("Inventory");
            inventoryStage.setScene(new Scene(root, InventoryController.INVENTORY_WIDTH, InventoryController.INVENTORY_HEIGHT));
            inventoryStage.setMinWidth(InventoryController.INVENTORY_WIDTH);
            inventoryStage.setMinHeight(InventoryController.INVENTORY_HEIGHT);
            inventoryStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            inventoryStage.show();
            gridPaneGlobal.getScene().getWindow().hide();
        } catch(Exception ex1) {
            ex1.printStackTrace();
        }
    }

    @FXML
    private void saveGame() {
        // TODO - serialize
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
            if(currentMap.getTileByCoords(x, y).isExplored()) {
                setTileChar(x, y, currentMap.getTileByCoords(x, y).getIcon());
            } else {
                setTileChar(x, y, '?');
            }
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
        currentMap.getTileByCoords(player.getX(), player.getY()).setExplored(true);
        setTileChar(player.getX(), player.getY(), player.getIcon());
        waitTurn();
    }

    //This happens when a turn passes - GamePanelController.player either moves or waits
    private void passTurn() throws MobDiedException, XMLException {
        if (player.getHunger() > 0) {
            player.decreaseHunger();
        } else {
            player.starve();
        }
        if(turnsPassed % 10 == 0) {
            int tileX = RandomFunctions.getRandomNumberInRange(0, 8);
            int tileY = RandomFunctions.getRandomNumberInRange(0, 8);
            int level = RandomFunctions.getRandomNumberInRange(player.getLevel()-1, player.getLevel()+1);
            currentMap.getTileByCoords(tileX, tileY).addEntity(gameFactory.getRandomEnemyOfLevel(level));
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
        StringBuilder tileDescription = new StringBuilder(currentMap.getTileByCoords(player.getX(), player.getY()).getName()).append("\n");
        tileDescription.append(currentMap.getTileByCoords(player.getX(), player.getY()).getDescription());
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
        } else if (entity instanceof Item) {
            entityActions.add("Pick up");
        } else if (entity instanceof NPC) {
            entityActions.add("Talk");
        } else if(entity instanceof Shop) {
            entityActions.add("Shop");
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
                    player.addItem(((Item) entity).getItem());
                    currentMap.getTileByCoords(player.getX(), player.getY()).removeEntity(entity);
                    refreshDescription();
                    refreshEntities();
                    break;
                case "Shop":
                    openShop((Shop) entity);
            }
        }
    }

    // Starts fighting with an enemy
    private void fight(Enemy enemy) {
        FightPanelController.currentEnemy = enemy;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fightPanel/FightPanel.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Hexer IV: Lydl Edyschön");
            gameStage.setScene(new Scene(root, FightPanelController.FIGHT_PANEL_WIDTH, FightPanelController.FIGHT_PANEL_HEIGHT));
            gameStage.setMinWidth(FightPanelController.FIGHT_PANEL_WIDTH);
            gameStage.setMinHeight(FightPanelController.FIGHT_PANEL_HEIGHT);
            gameStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Called when the game ends (The player dies)
    private void gameOver() {
        player = null;
        FightPanelController.currentEnemy = null;
        currentMap = null;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fightPanel/Shop.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Hexer IV: Lydl Edyschön");
            gameStage.setScene(new Scene(root, FightPanelController.FIGHT_PANEL_WIDTH, FightPanelController.FIGHT_PANEL_HEIGHT));
            gameStage.setMinWidth(FightPanelController.FIGHT_PANEL_WIDTH);
            gameStage.setMinHeight(FightPanelController.FIGHT_PANEL_HEIGHT);
            gameStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Opens shop menu
    private void openShop(Shop shop) {
        ShopController.currentShop = shop;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../shop/Shop.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Hexer IV: Lydl Edyschön");
            gameStage.setScene(new Scene(root, ShopController.SHOP_WIDTH, ShopController.SHOP_HEIGHT));
            gameStage.setMinWidth(ShopController.SHOP_WIDTH);
            gameStage.setMinHeight(ShopController.SHOP_HEIGHT);
            gameStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        if(gameFactory == null) {
            try {
                gameFactory = new GameFactory();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        // Initial entities and the GamePanelController.player are created
        if(player == null) {
            player = new Player(4, 4, "Sharpain", 5, 5, 5);
        }

        if(currentMap == null) {
            try {
                currentMap = new Map();
                for(int i = 0; i < 10; i++) {
                    currentMap.getTileByCoords(RandomFunctions.getRandomNumberInRange(0, 8), RandomFunctions.getRandomNumberInRange(0, 8)).addEntity(gameFactory.getRandomEnemyOfLevel(RandomFunctions.getRandomNumberInRange(0, 1)));
                }
                currentMap.getTileByCoords(4, 4).addEntity(new Item("Food", new Food("Food", 25)));
                currentMap.getTileByCoords(4, 4).addEntity(new Item("Food", new Food("Food", 25)));
                currentMap.getTileByCoords(4, 4).addEntity(gameFactory.getShopWithID(0));
                currentMap.getTileByCoords(4, 4).addEntity(new Item("Spoon of Doom", new Equipment("Spoon of Doom", 25, 25, 25, InventoryItem.ItemType.WEAPON)));
            } catch(XMLException xmle) {
                System.out.println(xmle.getMessage());
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
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
            public void updateItem(Entity entity, boolean empty) {
                super.updateItem(entity, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = entity.getDisplayName();
                    setText(text);
                }
            }
        });

    }
}
