package gui;

import exceptions.ExceptionAlert;
import gameLogic.inventory.Equipment;
import gameLogic.inventory.Food;
import gameLogic.inventory.InventoryItem;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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

    @FXML
    private Label labelStrength;
    @FXML
    private Label labelDexterity;
    @FXML
    private Label labelIntelligence;

    @FXML
    private Label labelItemDescription;

    // Returns back to map
    @FXML
    private void returnToMap() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("GamePanel.fxml"));
            primaryStage.setTitle("Hexer IV: Lidl edition");
            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(600);
            primaryStage.getScene().getStylesheets().add("gui/hivle.css");
            primaryStage.show();
            listViewInventory.getScene().getWindow().hide();
        } catch(Exception ex2) {
            System.out.println(ex2.getMessage());
        }
    }

    // Methods on buttons for deequipping items
    @FXML
    private void deequipHead() {
        try {
            Main.player.deequipItem(InventoryItem.ItemType.HEADWEAR);
            refreshGUI();
        } catch(ExceptionAlert e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void deequipBody() {
        try {
            Main.player.deequipItem(InventoryItem.ItemType.BODYWEAR);
            refreshGUI();
        } catch(ExceptionAlert e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void deequipWeapon() {
        try {
            Main.player.deequipItem(InventoryItem.ItemType.WEAPON);
            refreshGUI();
        } catch(ExceptionAlert e) {
            GamePanelController.callAlert(e);
        }

    }

    @FXML
    private void deequipLeg() {
        try {
            Main.player.deequipItem(InventoryItem.ItemType.LEGWEAR);
            refreshGUI();
        } catch(ExceptionAlert e) {
            GamePanelController.callAlert(e);
        }

    }

    // Refreshes stats
    private void refreshStats() {
        labelStrength.setText(String.valueOf(Main.player.getTotalStrength()));
        labelDexterity.setText(String.valueOf(Main.player.getTotalDexterity()));
        labelIntelligence.setText(String.valueOf(Main.player.getTotalIntelligence()));
    }

    // Refreshes inventory items
    private void refreshItems() {
        inventoryItems.clear();
        inventoryItems.addAll(Main.player.getInventory());
        listViewInventory.setItems(inventoryItems);
        listViewInventory.refresh();
    }

    // Refreshes equipment
    private void refreshEquipment() {
        if(Main.player.getEquippedHeadArmor() != null) {
            labelHead.setText(Main.player.getEquippedHeadArmor().getDisplayName());
            buttonDeequipHead.setVisible(true);
        } else {
            labelHead.setText("N/A");
            buttonDeequipHead.setVisible(false);
        }
        if(Main.player.getEquippedBodyArmor() != null) {
            labelBody.setText(Main.player.getEquippedBodyArmor().getDisplayName());
            buttonDeequipBody.setVisible(true);
        } else {
            labelBody.setText("N/A");
            buttonDeequipBody.setVisible(false);
        }
        if(Main.player.getEquippedLegArmor() != null) {
            labelLeg.setText(Main.player.getEquippedLegArmor().getDisplayName());
            buttonDeequipLeg.setVisible(true);
        } else {
            labelLeg.setText("N/A");
            buttonDeequipLeg.setVisible(false);
        }
        if(Main.player.getEquippedWeapon() != null) {
            labelWeapon.setText(Main.player.getEquippedWeapon().getDisplayName());
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
        } else {
            if (item instanceof Equipment) {
                inventoryItemActions.add("Equip");
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
                    Main.player.eat((Food) item);
                    break;
                case "Equip":
                    Main.player.equipItem((Equipment)item);
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
            displayActionsForInventoryItem(newValue);
        });

        listViewInventoryActions.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            try {
                executeActionOnItem(newValue, (InventoryItem) listViewInventory.getSelectionModel().getSelectedItem());
                refreshGUI();
            } catch (ExceptionAlert exceptionAlert) {
                GamePanelController.callAlert(exceptionAlert);
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
