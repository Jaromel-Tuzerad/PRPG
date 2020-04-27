package prpg.gui.inventory;

import prpg.exceptions.AlertException;
import prpg.exceptions.MobDiedException;
import prpg.gameLogic.entities.objects.Item;
import prpg.gameLogic.items.Equipment;
import prpg.gameLogic.items.Food;
import prpg.gameLogic.items.InventoryItem;
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
import javafx.stage.Stage;
import prpg.gui.gamePanel.GamePanelController;
import prpg.gui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    public static final int INVENTORY_WIDTH = 700;
    public static final int INVENTORY_HEIGHT = 730;

    @FXML
    private ListView listViewInventory;
    private ObservableList<InventoryItem> inventoryItems;
    @FXML
    private ListView listViewInventoryActions;
    private ObservableList<String> inventoryItemActions;

    // Equipment (0, 2)
    @FXML
    private Label labelHead;
    @FXML
    private Label labelBody;
    @FXML
    private Label labelLeg;
    @FXML
    private Label labelWeapon;
    @FXML
    private Button buttonDeequipHead;
    @FXML
    private Button buttonDeequipBody;
    @FXML
    private Button buttonDeequipWeapon;
    @FXML
    private Button buttonDeequipLeg;

    // Item description (2, 1)
    @FXML
    private TextArea textAreaItemDescription;

    // Character stats (2, 2)
    @FXML
    private Label labelPlayerName;
    @FXML
    private Label labelLevel;
    @FXML
    private Label labelPoints;
    @FXML
    private Label labelStrength;
    @FXML
    private Button buttonIncreaseStrength;
    @FXML
    private Label labelDexterity;
    @FXML
    private Button buttonIncreaseDexterity;
    @FXML
    private Label labelDefense;
    @FXML
    private Button buttonIncreaseDefense;
    @FXML
    private Label labelHealth;
    @FXML
    private ProgressBar progressBarHealth;
    @FXML
    private Label labelHunger;
    @FXML
    private ProgressBar progressBarHunger;
    @FXML
    private Label labelExperience;
    @FXML
    private ProgressBar progressBarExperience;
    @FXML
    private Label labelGold;

    // Returns back to map
    @FXML
    private void returnToMap() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../gamePanel/GamePanel.fxml"));
            primaryStage.setTitle(Main.gameTitle);
            primaryStage.setScene(new Scene(root, GamePanelController.GAME_PANEL_WIDTH, GamePanelController.GAME_PANEL_HEIGHT));
            primaryStage.setMinWidth(GamePanelController.GAME_PANEL_WIDTH);
            primaryStage.setMinHeight(GamePanelController.GAME_PANEL_HEIGHT);
            primaryStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            primaryStage.show();
            listViewInventory.getScene().getWindow().hide();
        } catch(Exception ex2) {
            System.out.println(ex2.getMessage());
        }
    }

    // Methods on buttons for unequipping items
    @FXML
    private void unequipHead() {
        try {
            GamePanelController.currentGame.getCurrentPlayer().unequipItemInSlotType(InventoryItem.ItemType.HEADWEAR);
            refreshGUI();
        } catch(AlertException e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void unequipBody() {
        try {
            GamePanelController.currentGame.getCurrentPlayer().unequipItemInSlotType(InventoryItem.ItemType.BODYWEAR);
            refreshGUI();
        } catch(AlertException e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void unequipWeapon() {
        try {
            GamePanelController.currentGame.getCurrentPlayer().unequipItemInSlotType(InventoryItem.ItemType.WEAPON);
            refreshGUI();
        } catch(AlertException e) {
            GamePanelController.callAlert(e);
        }
    }

    @FXML
    private void unequipLegs() {
        try {
            GamePanelController.currentGame.getCurrentPlayer().unequipItemInSlotType(InventoryItem.ItemType.LEGWEAR);
            refreshGUI();
        } catch(AlertException e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void upgradeStrength() {
        GamePanelController.currentGame.getCurrentPlayer().increaseStrengthBy(1);
        GamePanelController.currentGame.getCurrentPlayer().decreaseStatPointsBy(1);
        refreshStats();
    }

    @FXML
    private void upgradeDexterity() {
        GamePanelController.currentGame.getCurrentPlayer().increaseDexterityBy(1);
        GamePanelController.currentGame.getCurrentPlayer().decreaseStatPointsBy(1);
        refreshStats();
    }

    @FXML
    private void upgradeDefense() {
        GamePanelController.currentGame.getCurrentPlayer().increaseDefenseBy(1);
        GamePanelController.currentGame.getCurrentPlayer().decreaseStatPointsBy(1);
        refreshStats();
    }

    // Sets an item to be described
    private void setItemToDescribe(InventoryItem item) {
        if(item != null) {
            textAreaItemDescription.setText(item.getDescription());
        } else {
            textAreaItemDescription.setText("N/A");
        }
    }

    // Refreshes stats
    private void refreshStats() {
        if(GamePanelController.currentGame.getCurrentPlayer().getStatPoints() > 0) {
            buttonIncreaseStrength.setDisable(false);
            buttonIncreaseDexterity.setDisable(false);
            buttonIncreaseDefense.setDisable(false);
        } else {
            buttonIncreaseStrength.setDisable(true);
            buttonIncreaseDexterity.setDisable(true);
            buttonIncreaseDefense.setDisable(true);
        }
        labelLevel.setText(String.valueOf(GamePanelController.currentGame.getCurrentPlayer().getLevel()));
        labelPoints.setText(String.valueOf(GamePanelController.currentGame.getCurrentPlayer().getStatPoints()));
        labelStrength.setText(String.valueOf(GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()));
        labelDexterity.setText(String.valueOf(GamePanelController.currentGame.getCurrentPlayer().getTotalDexterity()));
        labelDefense.setText(String.valueOf(GamePanelController.currentGame.getCurrentPlayer().getTotalDefense()));
        labelHealth.setText(GamePanelController.currentGame.getCurrentPlayer().getHealth() + "/" + GamePanelController.currentGame.getCurrentPlayer().getMaxHealth());
        progressBarHealth.setProgress((double) GamePanelController.currentGame.getCurrentPlayer().getHealth() / GamePanelController.currentGame.getCurrentPlayer().getMaxHealth());
        labelHunger.setText(GamePanelController.currentGame.getCurrentPlayer().getHunger() + " %");
        progressBarHunger.setProgress(((double) GamePanelController.currentGame.getCurrentPlayer().getHunger()) / 100);
        labelExperience.setText(GamePanelController.currentGame.getCurrentPlayer().getExperience() + "/" + GamePanelController.currentGame.getCurrentPlayer().getExperienceToNextLevel());
        progressBarExperience.setProgress((double) GamePanelController.currentGame.getCurrentPlayer().getExperience() / GamePanelController.currentGame.getCurrentPlayer().getExperienceToNextLevel());
        labelGold.setText(String.valueOf(GamePanelController.currentGame.getCurrentPlayer().getGold()));
    }

    // Refreshes inventory items
    private void refreshItems() {
        inventoryItems.clear();
        inventoryItems.addAll(GamePanelController.currentGame.getCurrentPlayer().getInventory());
        listViewInventory.setItems(inventoryItems);
        listViewInventory.refresh();
    }

    // Refreshes equipment
    private void refreshEquipment() {
        if(GamePanelController.currentGame.getCurrentPlayer().getEquippedHeadArmor() != null) {
            labelHead.setText(GamePanelController.currentGame.getCurrentPlayer().getEquippedHeadArmor().getDisplayName());
            buttonDeequipHead.setVisible(true);
        } else {
            labelHead.setText("N/A");
            buttonDeequipHead.setVisible(false);
        }
        if(GamePanelController.currentGame.getCurrentPlayer().getEquippedBodyArmor() != null) {
            labelBody.setText(GamePanelController.currentGame.getCurrentPlayer().getEquippedBodyArmor().getDisplayName());
            buttonDeequipBody.setVisible(true);
        } else {
            labelBody.setText("N/A");
            buttonDeequipBody.setVisible(false);
        }
        if(GamePanelController.currentGame.getCurrentPlayer().getEquippedLegArmor() != null) {
            labelLeg.setText(GamePanelController.currentGame.getCurrentPlayer().getEquippedLegArmor().getDisplayName());
            buttonDeequipLeg.setVisible(true);
        } else {
            labelLeg.setText("N/A");
            buttonDeequipLeg.setVisible(false);
        }
        if(GamePanelController.currentGame.getCurrentPlayer().getEquippedWeapon() != null) {
            labelWeapon.setText(GamePanelController.currentGame.getCurrentPlayer().getEquippedWeapon().getDisplayName());
            buttonDeequipWeapon.setVisible(true);
        } else {
            labelWeapon.setText("N/A");
            buttonDeequipWeapon.setVisible(false);
        }
    }

    // Refreshes the whole pane
    private void refreshGUI() {
        refreshEquipment();
        refreshItems();
        refreshStats();
    }

    // Adds actions to listViewItemActions depending on item type
    private void displayActionsForInventoryItem(InventoryItem item) {
        inventoryItemActions.clear();
        if (item instanceof Food) {
            inventoryItemActions.add("Eat");
            inventoryItemActions.add("Drop");
        } else {
            if (item instanceof Equipment) {
                inventoryItemActions.add("Equip");
                inventoryItemActions.add("Drop");
            }
        }
        listViewInventoryActions.setItems(inventoryItemActions);
        listViewInventoryActions.refresh();
    }

    //Executes selected action for the selected item
    private void executeActionOnItem(String actionName, InventoryItem item) throws AlertException, MobDiedException {
        if (actionName != null) {
            switch (actionName) {
                case "Eat":
                    GamePanelController.currentGame.getCurrentPlayer().eat((Food) item);
                    break;
                case "Equip":
                    GamePanelController.currentGame.getCurrentPlayer().equipItem((Equipment)item);
                    break;
                case "Drop":
                    GamePanelController.currentGame.getCurrentPlayer().removeFromInventory(item);
                    GamePanelController.currentGame.getCurrentMap().getTileByCoords(
                            GamePanelController.currentGame.getCurrentPlayer().getX(),
                            GamePanelController.currentGame.getCurrentPlayer().getY()
                    ).addEntity(new Item(item.getDisplayName(), item));
                    break;
            }
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        labelPlayerName.setText(GamePanelController.currentGame.getCurrentPlayer().getName());

        // Initialize listView arrayLists
        inventoryItems = FXCollections.observableArrayList();
        inventoryItemActions = FXCollections.observableArrayList();

        refreshGUI();

        listViewInventory.getSelectionModel().selectedItemProperty().addListener((ChangeListener<InventoryItem>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            Platform.runLater(() -> {
                displayActionsForInventoryItem(newValue);
                setItemToDescribe(newValue);
            });
        });

        listViewInventoryActions.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            Platform.runLater(() -> {
                try {
                    executeActionOnItem(newValue, (InventoryItem) listViewInventory.getSelectionModel().getSelectedItem());
                    refreshGUI();
                } catch (AlertException alertException) {
                    GamePanelController.callAlert(alertException);
                } catch(MobDiedException mobDead) {
                    System.out.println(mobDead.getMessage());
                }
            });
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
