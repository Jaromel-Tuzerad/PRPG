package prpg.gui.fightPanel;

import prpg.exceptions.AlertException;
import prpg.exceptions.MobDiedException;
import prpg.gameLogic.entities.mobs.Enemy;
import prpg.gameLogic.entities.mobs.Mob;
import prpg.gameLogic.items.InventoryItem;
import prpg.gameLogic.quests.KillQuest;
import prpg.gameLogic.quests.Quest;
import prpg.gui.Main;
import prpg.gui.gamePanel.GamePanelController;
import prpg.gui.mainMenu.MainMenuController;
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

import static prpg.gui.gamePanel.GamePanelController.*;
import static prpg.gameLogic.RandomFunctions.*;

public class FightPanelController implements Initializable {

    public static final int FIGHT_PANEL_WIDTH = 630;
    public static final int FIGHT_PANEL_HEIGHT = 600;

    public static Enemy currentEnemy;

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

    // Calculates the damage that the attacker with attackerStrength inflicts on the victim with defenderDefense
    // damage = (attackerStrength*attackerStrength) / (attackerStrength+defenderDefense)

    private int rollInflictedDamage(AttackType type, int attackerStrength, int defenderDefense) throws AlertException {
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
                throw new AlertException("Attack type error", "The selected attack type is invalid", "Selected attack type:" + type);
        }
        return (int) Math.round(damageModifier * (attackerStrength*attackerStrength) / (attackerStrength+defenderDefense));
    }

    // Calculates the chance for Mob with attackerDexterity to hit Mob with defenderDexterity
    // chanceToHit = -(chanceMarginOfType / (attackerDexterity/defenderDexterity + 1)) + maxChanceOfType

    private double calculateChanceToHit(AttackType type, int attackerDexterity, int defenderDexterity) throws AlertException {
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
                throw new AlertException("Attack type error", "The selected attack type is invalid", "Selected attack type:" + type);
        }
        return (-margin / (((double) attackerDexterity/defenderDexterity)+1) + topChance)/100;
    }

    private void attack(AttackType chosenAttackType) {
        try {
            double chanceToHit = calculateChanceToHit(chosenAttackType, player.getTotalDexterity(), currentEnemy.getDexterity());
            int damage = rollInflictedDamage(chosenAttackType, player.getTotalStrength(), currentEnemy.getDefense());
            attackMob(player, currentEnemy, damage, chanceToHit);
            if(currentEnemy.getHealth() > 0) {
                double enemyChanceToHit = calculateChanceToHit(AttackType.NORMAL, currentEnemy.getDexterity(), player.getTotalDexterity());
                int enemyDamage = rollInflictedDamage(AttackType.NORMAL, currentEnemy.getStrength(), player.getTotalDefense());
                attackMob(currentEnemy, player, enemyDamage, enemyChanceToHit);
            } else {
                throw new AlertException("Victory", "The enemy has perished!", currentEnemy.getDisplayName() + " has died! Reap the spoils!");
            }
            refreshGUI();
        } catch (AlertException alertException) {
            callAlert(alertException);
        } catch (MobDiedException mobDiedException) {
            try {
                endBattle();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch(AlertException ae) {
                callAlert(ae);
            }
        }
    }

    private void addEntryToLog(String entry) {
        textAreaLog.setText(textAreaLog.getText() + "\n" + entry);
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
        labelMonsterHealth.setText("Health (" + (int)((double) (currentEnemy.getHealth()) / currentEnemy.getMaxHealth() * 100) + " %)");
        progressBarMonsterHealth.setProgress((double) (currentEnemy.getHealth()) / currentEnemy.getMaxHealth());

    }

    // Called when the battle is over
    private void endBattle() throws IOException, AlertException {
        if(currentEnemy.getHealth() <= 0) {
            // Give player the XP and items the monster has
            StringBuilder lootMessage = new StringBuilder("Loot has dropped:\n");
            for(InventoryItem item : currentEnemy.getInventory()) {
                player.addToInventory(item);
                lootMessage.append(item.getName()).append("\n");
            }
            lootMessage.append("Experience (").append(currentEnemy.getYieldedXP()).append(")").append("\n");
            player.addExperience(currentEnemy.getYieldedXP());
            lootMessage.append("Gold (").append(currentEnemy.getYieldedGold()).append(")");
            player.addGold(currentEnemy.getYieldedGold());
            currentMap.getTileByCoords(player.getX(), player.getY()).getEntities().remove(currentEnemy);
            for(Quest q : player.getQuestJournal()) {
                if(q.getQuestType() == Quest.QuestType.KILL) {
                    if(((KillQuest) q).getEnemyType().getEnemyTypeID() == currentEnemy.getEnemyTypeID()) {
                        ((KillQuest) q).incrementKillProgress();
                    }
                }
            }
            callAlert(new AlertException("Victory", "You have won! " + currentEnemy.getDisplayName() + " is no more!", lootMessage.toString()));
            currentEnemy = null;

            // Return back to game screen
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("../gamePanel/GamePanel.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle(Main.gameTitle);
            gameStage.setScene(new Scene(root, GamePanelController.GAME_PANEL_WIDTH, GamePanelController.GAME_PANEL_HEIGHT));
            gameStage.setMinWidth(GamePanelController.GAME_PANEL_WIDTH);
            gameStage.setMinHeight(GamePanelController.GAME_PANEL_HEIGHT);
            gameStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            gameStage.show();
        } else if(player.getHealth() <= 0) {
            callAlert(new AlertException("Player is dead", player.getDisplayName() + " has been killed and eaten by " + currentEnemy.getDisplayName(), "Better luck next time!"));
            Parent root = FXMLLoader.load(getClass().getResource("../mainMenu/MainMenu.fxml"));
            Stage gameStage = new Stage();
            gameStage.setTitle(Main.gameTitle);
            gameStage.setScene(new Scene(root, MainMenuController.MAIN_MENU_WIDTH, MainMenuController.MAIN_MENU_HEIGHT));
            gameStage.setMinWidth(MainMenuController.MAIN_MENU_WIDTH);
            gameStage.setMinHeight(MainMenuController.MAIN_MENU_HEIGHT);
            gameStage.getScene().getStylesheets().add("prpg/gui/hivle.css");
            gameStage.show();
            player = null;
            currentEnemy = null;
            currentMap = null;
            Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
            stage.close();
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        // Sets the name of the player and the monster above corresponding health bars
        labelNamePlayer.setText(player.getDisplayName());
        labelNameMonster.setText(currentEnemy.getDisplayName());

        // Log start
        textAreaLog.setText("The battle has started!");

        // Looks up chances and damage values of all the attacks
        try {
            labelAttackFastChance.setText((int)(Math.round(calculateChanceToHit(AttackType.FAST, player.getTotalDexterity(), currentEnemy.getDexterity())*100)) + " %");
            labelAttackFastDamage.setText((int)Math.round(0.6 * (player.getTotalStrength()*player.getTotalStrength()) / (player.getTotalStrength()+ currentEnemy.getDefense())) + " - " + Math.round((player.getTotalStrength() * player.getTotalStrength()) / (player.getTotalStrength() + currentEnemy.getDefense())));
            labelAttackNormalChance.setText((int)(Math.round(calculateChanceToHit(AttackType.NORMAL, player.getTotalDexterity(),
                    currentEnemy.getDexterity())*100)) + " %");
            labelAttackNormalDamage.setText((int)Math.round(0.9 * (player.getTotalStrength()*player.getTotalStrength()) / (player.getTotalStrength()+ currentEnemy.getDefense())) + " - " + (int)Math.round(1.1 * (player.getTotalStrength() * player.getTotalStrength()) / (player.getTotalStrength() + currentEnemy.getDefense())));
            labelAttackStrongChance.setText((int)(Math.round(calculateChanceToHit(AttackType.STRONG, player.getTotalDexterity(), currentEnemy.getDexterity())*100)) + " %");
            labelAttackStrongDamage.setText((int)Math.round(1.4 * (player.getTotalStrength()*player.getTotalStrength()) / (player.getTotalStrength()+ currentEnemy.getDefense())) + " - " + (int)Math.round(1.6 * (player.getTotalStrength() * player.getTotalStrength()) / (player.getTotalStrength() + currentEnemy.getDefense())));
        } catch (AlertException alertException) {
            callAlert(alertException);
        }

        refreshGUI();

    }

}
