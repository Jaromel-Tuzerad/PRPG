package gui.inventory;

import exceptions.ExceptionAlert;
import gameLogic.entities.objects.Item;
import gameLogic.inventory.Equipment;
import gameLogic.inventory.Food;
import gameLogic.inventory.InventoryItem;
import gui.Main;
import gui.gamePanel.GamePanelController;
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

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

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

    // Returns back to map
    @FXML
    private void returnToMap() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../gamePanel/GamePanel.fxml"));
            primaryStage.setTitle("Hexer IV: Lidl edition");
            primaryStage.setScene(new Scene(root, Main.GAME_PANEL_WIDTH, Main.GAME_PANEL_HEIGHT));
            primaryStage.setMinWidth(Main.GAME_PANEL_WIDTH);
            primaryStage.setMinHeight(Main.GAME_PANEL_HEIGHT);
            primaryStage.getScene().getStylesheets().add("gui/hivle.css");
            primaryStage.show();
            listViewInventory.getScene().getWindow().hide();
        } catch(Exception ex2) {
            System.out.println(ex2.getMessage());
        }
    }

    // Methods on buttons for unequipping items
    @FXML
    private void deequipHead() {
        try {
            GamePanelController.player.UnequipItem(InventoryItem.ItemType.HEADWEAR);
            refreshGUI();
        } catch(ExceptionAlert e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void deequipBody() {
        try {
            GamePanelController.player.UnequipItem(InventoryItem.ItemType.BODYWEAR);
            refreshGUI();
        } catch(ExceptionAlert e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void UnequipWeapon() {
        try {
            GamePanelController.player.UnequipItem(InventoryItem.ItemType.WEAPON);
            refreshGUI();
        } catch(ExceptionAlert e) {
            GamePanelController.callAlert(e);
        }
    }

    @FXML
    private void deequipLeg() {
        try {
            GamePanelController.player.UnequipItem(InventoryItem.ItemType.LEGWEAR);
            refreshGUI();
        } catch(ExceptionAlert e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void upgradeStrength() {
        GamePanelController.player.increaseStrengthBy(1);
        GamePanelController.player.decreaseStatPointsBy(1);
        refreshStats();
    }

    @FXML
    private void upgradeDexterity() {
        GamePanelController.player.increaseDexterityBy(1);
        GamePanelController.player.decreaseStatPointsBy(1);
        refreshStats();
    }

    @FXML
    private void upgradeDefense() {
        GamePanelController.player.increaseDefenseBy(1);
        GamePanelController.player.decreaseStatPointsBy(1);
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
        if(GamePanelController.player.getStatPoints() > 0) {
            buttonIncreaseStrength.setDisable(false);
            buttonIncreaseDexterity.setDisable(false);
            buttonIncreaseDefense.setDisable(false);
        } else {
            buttonIncreaseStrength.setDisable(true);
            buttonIncreaseDexterity.setDisable(true);
            buttonIncreaseDefense.setDisable(true);
        }
        labelPoints.setText(String.valueOf(GamePanelController.player.getStatPoints()));
        labelStrength.setText(String.valueOf(GamePanelController.player.getTotalStrength()));
        labelDexterity.setText(String.valueOf(GamePanelController.player.getTotalDexterity()));
        labelDefense.setText(String.valueOf(GamePanelController.player.getTotalDefense()));
        labelHealth.setText("Health (" + (int)((double) GamePanelController.player.getHealth() / GamePanelController.player.getMaxHealth() * 100) + " %)");
        progressBarHealth.setProgress((double) GamePanelController.player.getHealth() / GamePanelController.player.getMaxHealth());
        labelHunger.setText("Hunger (" + GamePanelController.player.getHunger() + " %)");
        progressBarHunger.setProgress(((double) GamePanelController.player.getHunger()) / 100);
        labelExperience.setText(GamePanelController.player.getExperience() + "/" + GamePanelController.player.getExperienceToNextLevel());
        progressBarExperience.setProgress((double) GamePanelController.player.getExperience() / GamePanelController.player.getExperienceToNextLevel());
    }

    // Refreshes inventory items.txt
    private void refreshItems() {
        inventoryItems.clear();
        inventoryItems.addAll(GamePanelController.player.getInventory());
        listViewInventory.setItems(inventoryItems);
        listViewInventory.refresh();
    }

    // Refreshes equipment
    private void refreshEquipment() {
        if(GamePanelController.player.getEquippedHeadArmor() != null) {
            labelHead.setText(GamePanelController.player.getEquippedHeadArmor().getDisplayName());
            buttonDeequipHead.setVisible(true);
        } else {
            labelHead.setText("N/A");
            buttonDeequipHead.setVisible(false);
        }
        if(GamePanelController.player.getEquippedBodyArmor() != null) {
            labelBody.setText(GamePanelController.player.getEquippedBodyArmor().getDisplayName());
            buttonDeequipBody.setVisible(true);
        } else {
            labelBody.setText("N/A");
            buttonDeequipBody.setVisible(false);
        }
        if(GamePanelController.player.getEquippedLegArmor() != null) {
            labelLeg.setText(GamePanelController.player.getEquippedLegArmor().getDisplayName());
            buttonDeequipLeg.setVisible(true);
        } else {
            labelLeg.setText("N/A");
            buttonDeequipLeg.setVisible(false);
        }
        if(GamePanelController.player.getEquippedWeapon() != null) {
            labelWeapon.setText(GamePanelController.player.getEquippedWeapon().getDisplayName());
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
    private void executeActionOnItem(String actionName, InventoryItem item) throws ExceptionAlert {
        if (actionName != null) {
            switch (actionName) {
                case "Eat":
                    GamePanelController.player.eat((Food) item);
                    break;
                case "Equip":
                    GamePanelController.player.equipItem((Equipment)item);
                    break;
                case "Drop":
                    GamePanelController.player.dropItem(item);
                    GamePanelController.currentMap.getTileByCoords(
                            GamePanelController.player.getX(),
                            GamePanelController.player.getY()
                    ).addEntity(new Item(item.getDisplayName(), item));
                    break;
            }
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

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
                } catch (ExceptionAlert exceptionAlert) {
                    GamePanelController.callAlert(exceptionAlert);
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
