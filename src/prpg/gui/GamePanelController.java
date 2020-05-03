package prpg.gui;

import prpg.exceptions.AlertException;
import prpg.exceptions.MobDiedException;
import prpg.exceptions.XMLException;
import prpg.gameLogic.Game;
import prpg.gameLogic.RandomFunctions;
import prpg.gameLogic.entities.mobs.Enemy;
import prpg.gameLogic.entities.Entity;
import prpg.gameLogic.entities.mobs.NPC;
import prpg.gameLogic.entities.objects.Item;
import prpg.gameLogic.entities.objects.Shop;
import prpg.gameLogic.items.InventoryItem;
import prpg.gameLogic.items.QuestItem;
import prpg.gameLogic.quests.FetchQuest;
import prpg.gameLogic.quests.Quest;
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
import prpg.gameLogic.mapping.Tile;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GamePanelController implements Initializable {

    public static final int GAME_PANEL_WIDTH = 650;
    public static final int GAME_PANEL_HEIGHT = 630;

    public static Game currentGame;

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

    // Methods for move/wait buttons
    @FXML
    private void moveNorth() {
        if (currentGame.getCurrentPlayer().getY() > 0) {
            movePlayer(0, -1);
        }
    }

    @FXML
    private void moveSouth() {
        if (currentGame.getCurrentPlayer().getY() < 8) {
            movePlayer(0, 1);
        }
    }

    @FXML
    private void moveEast() {
        if (currentGame.getCurrentPlayer().getX() < 8) {
            movePlayer(1, 0);
        }
    }

    @FXML
    private void moveWest() {
        if (currentGame.getCurrentPlayer().getX() > 0) {
            movePlayer(-1, 0);
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
            callAlert(new AlertException("Player is dead", currentGame.getCurrentPlayer().getDisplayName() + " has starved to death", "Better luck next time!"));
            endGame();
        }

    }

    // Opens the inventory and closes current window
    @FXML
    private void openInventory() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/Inventory.fxml"));
            Stage inventoryStage = new Stage();
            inventoryStage.setTitle("Inventory");
            inventoryStage.setScene(new Scene(root, InventoryController.INVENTORY_WIDTH, InventoryController.INVENTORY_HEIGHT));
            inventoryStage.setMinWidth(InventoryController.INVENTORY_WIDTH);
            inventoryStage.setMinHeight(InventoryController.INVENTORY_HEIGHT);
            inventoryStage.getScene().getStylesheets().add("/gui/hivle.css");
            inventoryStage.show();
            gridPaneGlobal.getScene().getWindow().hide();
        } catch(Exception ex1) {
            ex1.printStackTrace();
        }
    }

    // Opens the quest journal
    @FXML
    private void openJournal() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/Journal.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle(Main.gameTitle);
            gameStage.setScene(new Scene(root, JournalController.JOURNAL_WIDTH, JournalController.JOURNAL_HEIGHT));
            gameStage.setMinWidth(JournalController.JOURNAL_WIDTH);
            gameStage.setMinHeight(JournalController.JOURNAL_HEIGHT);
            gameStage.getScene().getStylesheets().add("/gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveGame() {
        // Serialization
        try {
            //Saving of object in a file
            File directory = new File(System.getProperty("user.home") + "/Documents/PRPG SaveGameData");
            if(!directory.isDirectory()) {
                directory.mkdirs();
            }
            FileOutputStream file = new FileOutputStream(System.getProperty("user.home") + "/Documents/PRPG SaveGameData/savedGameData.txt");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(currentGame);

            out.close();
            file.close();

            callAlert(new AlertException("Game",
                    "The game has been saved",
                    "Your progress has been saved to " + System.getProperty("user.home") +
                            "PRPG SaveGameData/savedGameData.txt"));
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void openMainMenu() {
        endGame();
    }

    // Calls an alert when an error arises
    public static void callAlert(AlertException exc) {
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
            if(currentGame.getCurrentMap().getTileByCoords(x, y).isExplored()) {
                setTileChar(x, y, currentGame.getCurrentMap().getTileByCoords(x, y).getIcon());
            } else {
                setTileChar(x, y, '?');
            }
        }
    }

    // Makes small changes to map tile icons (quicker than refreshing the whole map)
    private void setTileChar(int x, int y, char c) {
        ((Label) gridPaneMap.getChildren().get(x * 9 + y)).setText("[" + c + "]");
    }

    // Move GamePanelController.currentGame.getCurrentPlayer() in a relative direction, passes a turn and refreshes the GUI
    private void movePlayer(int dX, int dY) {
        setTileChar(currentGame.getCurrentPlayer().getX(), currentGame.getCurrentPlayer().getY(), currentGame.getCurrentMap().getTileByCoords(currentGame.getCurrentPlayer().getX(), currentGame.getCurrentPlayer().getY()).getIcon());
        currentGame.getCurrentPlayer().moveBy(dX, dY);
        currentGame.getCurrentMap().getTileByCoords(currentGame.getCurrentPlayer().getX(), currentGame.getCurrentPlayer().getY()).setExplored(true);
        setTileChar(currentGame.getCurrentPlayer().getX(), currentGame.getCurrentPlayer().getY(), currentGame.getCurrentPlayer().getIcon());
        waitTurn();
    }

    //This happens when a turn passes - GamePanelController.currentGame.getCurrentPlayer() either moves or waits
    private void passTurn() throws MobDiedException, XMLException {
        if (currentGame.getCurrentPlayer().getHunger() > 0) {
            currentGame.getCurrentPlayer().decreaseHunger();
        } else {
            currentGame.getCurrentPlayer().starve();
        }
        if(turnsPassed % 10 == 0) {
            int tileX = RandomFunctions.getRandomNumberInRange(0, 8);
            int tileY = RandomFunctions.getRandomNumberInRange(0, 8);
            int level = RandomFunctions.getRandomNumberInRange(currentGame.getCurrentPlayer().getLevel()-1, currentGame.getCurrentPlayer().getLevel()+1);
            currentGame.getCurrentMap().getTileByCoords(tileX, tileY).addEntity(currentGame.getGameFactory().getRandomEnemyOfLevel(level));
        }
        turnsPassed += 1;
    }

    // Refreshes progress bars
    private void refreshProgressBars() {
        labelHealth.setText("Health (" + (int)((double) currentGame.getCurrentPlayer().getHealth() / currentGame.getCurrentPlayer().getMaxHealth() * 100) + " %)");
        progressBarHealth.setProgress((double) currentGame.getCurrentPlayer().getHealth() / currentGame.getCurrentPlayer().getMaxHealth());
        labelHunger.setText("Hunger (" + currentGame.getCurrentPlayer().getHunger() + " %)");
        progressBarHunger.setProgress(((double) currentGame.getCurrentPlayer().getHunger()) / 100);
        labelExperience.setText("Experience (" + (int)((double) currentGame.getCurrentPlayer().getExperience() / currentGame.getCurrentPlayer().getExperienceToNextLevel() * 100) + " %)");
        progressBarExperience.setProgress((double) currentGame.getCurrentPlayer().getExperience() / currentGame.getCurrentPlayer().getExperienceToNextLevel());
    }

    // Refreshes detected entities
    private void refreshEntities() {
        tileEntities.clear();
        tileEntities.addAll(currentGame.getCurrentMap().getTileByCoords(currentGame.getCurrentPlayer().getX(), currentGame.getCurrentPlayer().getY()).getEntities());
        listViewEntities.setItems(tileEntities);
        listViewEntities.refresh();
    }

    // Refreshes tile description
    private void refreshTileDescription() {
        Tile tile = currentGame.getCurrentMap().getTileByCoords(currentGame.getCurrentPlayer().getX(), currentGame.getCurrentPlayer().getY());
        StringBuilder tileDescription = new StringBuilder(tile.getName()).append(" [").append(currentGame.getCurrentPlayer().getX()).append(
                ", ").append(currentGame.getCurrentPlayer().getY()).append("]").append("\n");
        tileDescription.append(tile.getDescription());
        if (!tile.getEntities().isEmpty()) {
            for (Entity e : tile.getEntities()) {
                tileDescription.append("\n").append(e.getDescription());
            }
        }
        textAreaDescription.setText(tileDescription.toString());
    }

    // Refreshes the whole pane
    private void refreshGUI() {

        refreshProgressBars();
        refreshEntities();
        refreshTileDescription();

    }

    // Lists actions in listViewActions for the selected entity according to it's type
    private void displayActionsForEntity(Entity entity) {
        entityActions.clear();
        if(entity != null) {
            entityActions.add(entity.getActionName());
            listViewEntityActions.setItems(entityActions);
        }
        listViewEntityActions.refresh();

    }

    //Executes selected action on specified entity
    private void executeActionOnEntity(String actionName, Entity entity) {
        if(actionName != null) {
            switch (actionName) {
                case "Talk":
                    try {
                        if(((NPC) entity).getActiveQuest() != null) {
                            finishQuestOfNPC((NPC) entity);
                        } else {
                            startNewQuestFor((NPC) entity);
                        }
                    } catch(XMLException e) {System.out.println(e.getMessage());}
                    break;
                case "Fight":
                    fight((Enemy) entity);
                    break;
                case "Pick up":
                    InventoryItem inventoryItem = ((Item)entity).getItem();
                    if(inventoryItem instanceof QuestItem) {
                        for(Quest q : currentGame.getCurrentPlayer().getQuestJournal()) {
                            if(q.getQuestType() == Quest.QuestType.FETCH) {
                                if(((FetchQuest) q).getRequiredItem().equals(inventoryItem)) {
                                    ((FetchQuest) q).setInInventory(true);
                                    break;
                                }
                            }
                        }
                    }
                    currentGame.getCurrentPlayer().addToInventory(inventoryItem);
                    currentGame.getCurrentMap().getTileByCoords(currentGame.getCurrentPlayer().getX(), currentGame.getCurrentPlayer().getY()).removeEntity(entity);
                    refreshTileDescription();
                    refreshEntities();
                    break;
                case "Shop":
                    openShop((Shop) entity);
                    break;
            }
        }

    }

    // Starts fighting with an enemy
    private void fight(Enemy enemy) {
        currentGame.setCurrentEnemy(enemy);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/FightPanel.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle(Main.gameTitle);
            gameStage.setScene(new Scene(root, FightPanelController.FIGHT_PANEL_WIDTH, FightPanelController.FIGHT_PANEL_HEIGHT));
            gameStage.setMinWidth(FightPanelController.FIGHT_PANEL_WIDTH);
            gameStage.setMinHeight(FightPanelController.FIGHT_PANEL_HEIGHT);
            gameStage.getScene().getStylesheets().add("/gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Called when the game ends (The currentGame.getCurrentPlayer() dies)
    private void endGame() {
        currentGame = null;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/MainMenu.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle(Main.gameTitle);
            gameStage.setScene(new Scene(root, MainMenuController.MAIN_MENU_WIDTH, MainMenuController.MAIN_MENU_HEIGHT));
            gameStage.setMinWidth(MainMenuController.MAIN_MENU_WIDTH);
            gameStage.setMinHeight(MainMenuController.MAIN_MENU_HEIGHT);
            gameStage.getScene().getStylesheets().add("/gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Opens shop menu
    private void openShop(Shop shop) {
        currentGame.setCurrentShop(shop);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/Shop.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle(Main.gameTitle);
            gameStage.setScene(new Scene(root, ShopController.SHOP_WIDTH, ShopController.SHOP_HEIGHT));
            gameStage.setMinWidth(ShopController.SHOP_WIDTH);
            gameStage.setMinHeight(ShopController.SHOP_HEIGHT);
            gameStage.getScene().getStylesheets().add("/gui/hivle.css");
            gameStage.show();
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startNewQuestFor(NPC questgiver) throws XMLException {
        Quest newQuest = currentGame.getGameFactory().getNewQuest(questgiver);
        if(newQuest instanceof FetchQuest) {
            currentGame.getCurrentMap().getTileByCoords(((FetchQuest) newQuest).getPosX(), ((FetchQuest) newQuest).getPosY()).addEntity(((FetchQuest) newQuest).getRequiredItem().toItem());
        }
        questgiver.startQuest(newQuest);
        currentGame.getCurrentPlayer().startQuest(newQuest);
        callAlert(new AlertException("Quest", "You've been given a new quest!", newQuest.getNPCText()));
    }

    public void finishQuestOfNPC(NPC questgiver) {
        if(questgiver.getActiveQuest().isFinished()) {
            try {
                currentGame.getCurrentPlayer().finishQuest(questgiver.getActiveQuest());
                StringBuilder itemsList = new StringBuilder();
                for(InventoryItem item : questgiver.getActiveQuest().getRewardItems()) {
                    itemsList.append(item.getName()).append("\n");
                }
                callAlert(new AlertException("Quest", "Quest has been finished!", "You've obtained:" +
                        "\n" + questgiver.getActiveQuest().getRewardXP() + " experience" +
                        "\n" + questgiver.getActiveQuest().getRewardGold() + " gold" +
                        "\n" + itemsList.toString()));
                questgiver.endQuest();
                refreshProgressBars();
            } catch(AlertException e) {callAlert(e);}
        } else {
            callAlert(new AlertException("Quest", "Quest is not finished!", questgiver.getActiveQuest().getDescription()));
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        if(currentGame == null) {
            callAlert(new AlertException("Game error",
                    "An error occured while trying to load current game progress", 
                    "Going back to main menu"));
            endGame();
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
            Platform.runLater(() -> executeActionOnEntity(newValue, (Entity) listViewEntities.getSelectionModel().getSelectedItem()));
        });

        // Resets the GUI look according to current values
        refreshGUI();

        // Sets the tile which the GamePanelController.currentGame.getCurrentPlayer() is initially on to display his icon
        setTileChar(currentGame.getCurrentPlayer().getX(), currentGame.getCurrentPlayer().getY(), currentGame.getCurrentPlayer().getIcon());

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
