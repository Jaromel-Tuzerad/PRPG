package prpg.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import prpg.gameLogic.quests.Quest;

import java.net.URL;
import java.util.ResourceBundle;

public class JournalController implements Initializable {

    public static final int JOURNAL_WIDTH = 650;
    public static final int JOURNAL_HEIGHT = 630;

    @FXML
    private ListView listViewQuests;
    private ObservableList<Quest> quests;

    @FXML
    private TextArea textAreaQuestDescription;

    public void setQuestDescription(Quest quest) {
        textAreaQuestDescription.setText(quest.getDescription());
    }

    // Returns back to map
    @FXML
    private void returnToMap() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/gui/GamePanel.fxml"));
            primaryStage.setTitle(Main.gameTitle);
            primaryStage.setScene(new Scene(root, GamePanelController.GAME_PANEL_WIDTH, GamePanelController.GAME_PANEL_HEIGHT));
            primaryStage.setMinWidth(GamePanelController.GAME_PANEL_WIDTH);
            primaryStage.setMinHeight(GamePanelController.GAME_PANEL_HEIGHT);
            primaryStage.getScene().getStylesheets().add("/gui/hivle.css");
            primaryStage.show();
            listViewQuests.getScene().getWindow().hide();
        } catch(Exception ex2) {
            System.out.println(ex2.getMessage());
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        quests = FXCollections.observableArrayList();
        quests.addAll(GamePanelController.currentGame.getCurrentPlayer().getQuestJournal());
        listViewQuests.setItems(quests);
        listViewQuests.refresh();

        // Sets the ChangeListener for the selected item in listView - sets what happens when an item is selected
        listViewQuests.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Quest>) (observable, oldValue, newValue) -> {
        // What is supposed to happen when the selected item changes
            if(newValue != null) {Platform.runLater(() -> setQuestDescription(newValue));}
        });

        //Sets the manner in which objects in listViews is displayed
        listViewQuests.setCellFactory(lv -> new ListCell<Quest>() {
            @Override
            public void updateItem(Quest quest, boolean empty) {
                super.updateItem(quest, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = quest.getTitle();
                    setText(text);
                }
            }
        });

    }

}
