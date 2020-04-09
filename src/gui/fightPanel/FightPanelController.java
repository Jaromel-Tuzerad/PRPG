package gui.fightPanel;

import exceptions.ExceptionAlert;
import exceptions.MobDiedException;
import gameLogic.entities.mobs.Mob;
import gameLogic.inventory.InventoryItem;
import gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static gui.gamePanel.GamePanelController.*;
import static gameLogic.RandomFunctions.*;

public class FightPanelController implements Initializable {

    // Global
    @FXML
    private GridPane gridPaneGlobal;

    // Player info (0, 0)
    @FXML
    private Label labelNamePlayer;
    @FXML
    private Label labelPlayerHealth;
    @FXML
    private ProgressBar progressBarPlayerHealth;

    // Monster info (1, 0)
    @FXML
    private Label labelNameMonster;
    @FXML
    private Label labelMonsterHealth;
    @FXML
    private ProgressBar progressBarMonsterHealth;

    // Battle log (0, 1)
    @FXML
    private TextArea textAreaLog;

    // Attacks (0, 2)
    @FXML
    private Label labelAttackFastChance;
    @FXML
    private Label labelAttackFastDamage;
    @FXML
    private Label labelAttackNormalChance;
    @FXML
    private Label labelAttackNormalDamage;
    @FXML
    private Label labelAttackStrongChance;
    @FXML
    private Label labelAttackStrongDamage;

    private enum AttackType {
        FAST,
        NORMAL,
        STRONG
    }

    @FXML
    private void fastAttack() {
        attack(AttackType.FAST);
    }

    @FXML
    private void normalAttack() {
        attack(AttackType.NORMAL);
    }

    @FXML
    private void strongAttack() {
        attack(AttackType.STRONG);
    }

    private void attack(AttackType chosenAttackType) {
        try {
            double chanceToHit = calculateChanceToHit(chosenAttackType, player.getTotalDexterity(), fightingEnemy.getDexterity());
            int damage = calculateInflictedDamage(chosenAttackType, player.getTotalStrength());
            attackMob(player, fightingEnemy, damage, chanceToHit);
            if(fightingEnemy.getHealth() > 0) {
                double enemysChanceToHit = calculateChanceToHit(AttackType.NORMAL, fightingEnemy.getDexterity(), player.getTotalDexterity());
                int enemysDamage = calculateInflictedDamage(AttackType.NORMAL, fightingEnemy.getStrength());
                attackMob(fightingEnemy, player, enemysDamage, enemysChanceToHit);
            } else {
                throw new ExceptionAlert("Victory", "The enemy has perished!", fightingEnemy.getDisplayName() + " has died! Reap the spoils!");
            }
            refreshGUI();
        } catch (ExceptionAlert exceptionAlert) {
            callAlert(exceptionAlert);
        } catch (MobDiedException mobDiedException) {
            try {
                endBattle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addEntryToLog(String entry) {
        textAreaLog.setText(textAreaLog.getText() + "\n" + entry);
    }

    private int calculateInflictedDamage(AttackType type, int attackerStrength) throws ExceptionAlert {
        double damageModifier;
        switch(type) {
            case FAST:
                damageModifier = ThreadLocalRandom.current().nextDouble(0.6, 1);
                break;
            case NORMAL:
                damageModifier = ThreadLocalRandom.current().nextDouble(0.9, 1.1);
                break;
            case STRONG:
                damageModifier = ThreadLocalRandom.current().nextDouble(1.4, 1.6);
                break;
            default:
                throw new ExceptionAlert("Attack type error", "The selected attack type is invalid", "Selected attack type:" + type);
        }
        return (int)(damageModifier * attackerStrength);
    }

    // Calculates the chance for Mob with attackerDexterity to hit Mob with defenderDexterity
    // chanceToHit = -(chanceMarginOfType / (attackerDexterity/defenderDexterity + 1)) + maxChanceOfType

    private double calculateChanceToHit(AttackType type, int attackerDexterity, int defenderDexterity) throws ExceptionAlert {
        double margin;
        double topChance;
        switch(type) {
            case FAST:
                margin = 60;
                topChance = 130;
                break;
            case NORMAL:
                margin = 80;
                topChance = 120;
                break;
            case STRONG:
                margin = 100;
                topChance = 100;
                break;
            default:
                throw new ExceptionAlert("Attack type error", "The selected attack type is invalid", "Selected attack type:" + type);
        }
        return (-margin / (((double) attackerDexterity/defenderDexterity)+1) + topChance)/100;
    }

    private void attackMob(Mob attacker, Mob defender, int damage, double chanceToHit) throws MobDiedException {
        String logMessage;
        if(random.nextDouble() < chanceToHit) {
            logMessage = attacker.getDisplayName() + " hits for " + damage;
            defender.addHealth(-damage);
        } else {
            logMessage = attacker.getDisplayName() + " missed";
        }
        addEntryToLog(logMessage);
    }

    private void refreshGUI() {

        // Refreshes health bars
        labelPlayerHealth.setText("Health (" + (int)((double) (player.getHealth()) / player.getMaxHealth() * 100) + " %)");
        progressBarPlayerHealth.setProgress(((double) player.getHealth() / player.getMaxHealth()));
        labelMonsterHealth.setText("Health (" + (int)((double) (fightingEnemy.getHealth()) / fightingEnemy.getMaxHealth() * 100) + " %)");
        progressBarMonsterHealth.setProgress((double) (fightingEnemy.getHealth()) / fightingEnemy.getMaxHealth());

    }

    // Called when the battle is over
    private void endBattle() throws IOException {
        if(fightingEnemy.getHealth() <= 0) {
            // Give player the XP and items the monster has
            StringBuilder lootMessage = new StringBuilder("Loot has dropped:\n");
            for(InventoryItem item : fightingEnemy.getDroppedItems()) {
                player.pickUpItem(item);
                lootMessage.append(item.getDisplayName()).append("\n");
            }
            lootMessage.append("Experience (").append(fightingEnemy.getYieldedXP()).append(")");
            player.addExperience(fightingEnemy.getYieldedXP());
            currentMap.getTileByCoords(player.getX(), player.getY()).getEntities().remove(fightingEnemy);

            callAlert(new ExceptionAlert("Victory", "You have won! " + fightingEnemy.getDisplayName() + " is no more!", lootMessage.toString()));

            // Return back to game screen
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("../gamePanel/GamePanel.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Hexer IV: Lidl edition");
            gameStage.setScene(new Scene(root, Main.GAME_PANEL_WIDTH, Main.GAME_PANEL_HEIGHT));
            gameStage.setMinWidth(Main.GAME_PANEL_WIDTH);
            gameStage.setMinHeight(Main.GAME_PANEL_HEIGHT);
            gameStage.setMaxWidth(Main.GAME_PANEL_WIDTH);
            gameStage.setMaxHeight(Main.GAME_PANEL_HEIGHT);
            gameStage.getScene().getStylesheets().add("gui/hivle.css");
            gameStage.show();
        } else if(player.getHealth() <= 0) {
            callAlert(new ExceptionAlert("Player is dead", player.getDisplayName() + " has been killed and eaten by " + fightingEnemy.getDisplayName(), "Better luck next time!"));
            Parent root = FXMLLoader.load(getClass().getResource("../mainMenu/MainMenu.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle("Hexer IV: Lidl edition");
            gameStage.setScene(new Scene(root, Main.MAIN_MENU_WIDTH, Main.MAIN_MENU_HEIGHT));
            gameStage.setMinWidth(Main.MAIN_MENU_WIDTH);
            gameStage.setMinHeight(Main.MAIN_MENU_HEIGHT);
            gameStage.getScene().getStylesheets().add("gui/hivle.css");
            gameStage.show();
            player = null;
            fightingEnemy = null;
            currentMap = null;
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        // Sets the name of the player and the monster above corresponding health bars
        labelNamePlayer.setText(player.getDisplayName());
        labelNameMonster.setText(fightingEnemy.getDisplayName());

        // Log start
        textAreaLog.setText("The battle has started!");

        // Looks up chances and damage values of all the attacks
        try {
            labelAttackFastChance.setText((int)(calculateChanceToHit(AttackType.FAST, player.getTotalDexterity(), fightingEnemy.getDexterity())*100) + " %");
            labelAttackFastDamage.setText(0.6 * player.getTotalStrength() + " - " + player.getTotalStrength());
            labelAttackNormalChance.setText((int)(calculateChanceToHit(AttackType.NORMAL, player.getTotalDexterity(), fightingEnemy.getDexterity())*100) + " %");
            labelAttackNormalDamage.setText(0.9 * player.getTotalStrength() + " - " + 1.1 * player.getTotalStrength());
            labelAttackStrongChance.setText((int)(calculateChanceToHit(AttackType.STRONG, player.getTotalDexterity(), fightingEnemy.getDexterity())*100) + " %");
            labelAttackStrongDamage.setText(1.4 * player.getTotalStrength() + " - " + 1.6 * player.getTotalStrength());
        } catch (ExceptionAlert exceptionAlert) {
            callAlert(exceptionAlert);
        }

        refreshGUI();

    }

}
