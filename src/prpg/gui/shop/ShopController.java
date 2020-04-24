package prpg.gui.shop;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import prpg.exceptions.AlertException;
import prpg.exceptions.XMLException;
import prpg.gameLogic.entities.objects.Shop;
import prpg.gameLogic.items.InventoryItem;
import prpg.gameLogic.items.Tradeable;
import prpg.gui.gamePanel.GamePanelController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ShopController implements Initializable {

    public static final int SHOP_WIDTH = 500;
    public static final int SHOP_HEIGHT = 260;

    public static Shop currentShop;

    @FXML
    private Button buttonRestock;
    @FXML
    private Label labelMoney;
    @FXML
    private ListView listViewInventory;
    private ObservableList<InventoryItem> inventoryItems;
    @FXML
    private Button buttonSell;
    @FXML
    private ListView listViewShop;
    private ObservableList<InventoryItem> shopItems;
    @FXML
    private Button buttonBuy;

    // Returns back to map
    @FXML
    private void returnToMap() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../gamePanel/GamePanel.fxml"));
            primaryStage.setTitle("Hexer IV: Lidl edition");
            primaryStage.setScene(new Scene(root, GamePanelController.GAME_PANEL_WIDTH, GamePanelController.GAME_PANEL_HEIGHT));
            primaryStage.setMinWidth(GamePanelController.GAME_PANEL_WIDTH);
            primaryStage.setMinHeight(GamePanelController.GAME_PANEL_HEIGHT);
            primaryStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            primaryStage.show();
            listViewInventory.getScene().getWindow().hide();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void sell() {
        if(listViewInventory.getSelectionModel().getSelectedItem() != null) {
            sellItem((InventoryItem) listViewInventory.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void buy() {
        if(listViewShop.getSelectionModel().getSelectedItem() != null) {
            buyItem(((InventoryItem) listViewShop.getSelectionModel().getSelectedItem()));
        }
    }

    @FXML
    private void restockItems() {
        try {
            GamePanelController.player.addGold(-currentShop.getRestockCost());
            currentShop.getInventory().clear();
            currentShop.getInventory().addAll(GamePanelController.gameFactory.getNewShopStockWithId(currentShop.getTypeId()));
            refreshGUI();
        } catch(AlertException ea) {
            GamePanelController.callAlert(ea);
        } catch(XMLException xmle) {
            System.out.println(xmle.getMessage());
        }
    }

    private void sellItem(InventoryItem item) {
        try {
            currentShop.addToInventory(item);
            GamePanelController.player.removeFromInventory(item);
            // When selling an item, itemWorth is halved
            GamePanelController.player.addGold(((Tradeable)item).getWorth()/2);
            refreshGUI();
        } catch(AlertException ex) {
            GamePanelController.callAlert(ex);
        }

    }

    private void buyItem(InventoryItem item) {
        try {
            GamePanelController.player.addGold(-((Tradeable)item).getWorth());
            currentShop.removeFromInventory(item);
            GamePanelController.player.addToInventory(item);
            refreshGUI();
        } catch(AlertException ex) {
            GamePanelController.callAlert(ex);
        }
    }

    // Refreshes inventory items
    private void refreshInventoryItems() {
        inventoryItems.clear();
        for(InventoryItem item : GamePanelController.player.getInventory()) {
            if(item instanceof Tradeable) {
                inventoryItems.add(item);
            }
        }
        listViewInventory.setItems(inventoryItems);
        listViewInventory.refresh();
    }

    // Refreshes shop items
    private void refreshShopItems() {
        shopItems.clear();
        shopItems.addAll(currentShop.getInventory());
        listViewShop.setItems(shopItems);
        listViewShop.refresh();
    }

    // Refreshes the whole window
    private void refreshGUI() {
        buttonBuy.setText("Buy");
        buttonSell.setText("Sell");
        buttonRestock.setText("Restock (" + currentShop.getRestockCost() + "g)");
        labelMoney.setText(GamePanelController.player.getGold() + "g");
        refreshInventoryItems();
        refreshShopItems();
    }

    public void initialize(URL url, ResourceBundle rb) {

        // Initialize listView arrayLists
        shopItems = FXCollections.observableArrayList();
        inventoryItems = FXCollections.observableArrayList();

        // Sets the ChangeListener for the selected item in listView - sets what happens when an item is selected
        listViewInventory.getSelectionModel().selectedItemProperty().addListener((ChangeListener<InventoryItem>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            if(newValue != null) {
                Platform.runLater(() -> buttonSell.setText("Sell (" + ((Tradeable) newValue).getWorth() / 2 + "g)"));
            }
        });
        listViewShop.getSelectionModel().selectedItemProperty().addListener((ChangeListener<InventoryItem>) (observable, oldValue, newValue) -> {
            // What is supposed to happen when the selected item changes
            if(newValue != null) {
                Platform.runLater(() -> buttonBuy.setText("Buy (" + ((Tradeable) newValue).getWorth() + "g)") );
            }
        });

        //Sets the manner in which objects in listViews is displayed
        listViewShop.setCellFactory(lv -> new ListCell<InventoryItem>() {
            @Override
            public void updateItem(InventoryItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = item.getName();
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
                    String text = item.getName();
                    setText(text);
                }
            }
        });

        refreshGUI();

    }

}
