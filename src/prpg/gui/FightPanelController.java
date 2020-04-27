package prpg.gui;

import prpg.exceptions.AlertException;
import prpg.exceptions.MobDiedException;
import prpg.gameLogic.entities.mobs.Mob;
import prpg.gameLogic.items.InventoryItem;
import prpg.gameLogic.quests.KillQuest;
import prpg.gameLogic.quests.Quest;
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

import static prpg.gui.GamePanelController.*;
import static prpg.gameLogic.RandomFunctions.*;

public class FightPanelController implements Initializable {

    public static final int FIGHT_PANEL_WIDTH = 630;
    public static final int FIGHT_PANEL_HEIGHT = 600;

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
            double chanceToHit = calculateChanceToHit(chosenAttackType, GamePanelController.currentGame.getCurrentPlayer().getTotalDexterity(), GamePanelController.currentGame.getCurrentEnemy().getDexterity());
            int damage = rollInflictedDamage(chosenAttackType, GamePanelController.currentGame.getCurrentPlayer().getTotalStrength(), GamePanelController.currentGame.getCurrentEnemy().getDefense());
            attackMob(GamePanelController.currentGame.getCurrentPlayer(), GamePanelController.currentGame.getCurrentEnemy(), damage, chanceToHit);
            if(GamePanelController.currentGame.getCurrentEnemy().getHealth() > 0) {
                double enemyChanceToHit = calculateChanceToHit(AttackType.NORMAL, GamePanelController.currentGame.getCurrentEnemy().getDexterity(), GamePanelController.currentGame.getCurrentPlayer().getTotalDexterity());
                int enemyDamage = rollInflictedDamage(AttackType.NORMAL, GamePanelController.currentGame.getCurrentEnemy().getStrength(), GamePanelController.currentGame.getCurrentPlayer().getTotalDefense());
                attackMob(GamePanelController.currentGame.getCurrentEnemy(), GamePanelController.currentGame.getCurrentPlayer(), enemyDamage, enemyChanceToHit);
            } else {
                throw new AlertException("Victory", "The enemy has perished!", GamePanelController.currentGame.getCurrentEnemy().getDisplayName() + " has died! Reap the spoils!");
            }
            refreshGUI();
        } catch(AlertException alertException) {
            callAlert(alertException);
        } catch(MobDiedException mde){
            try {
                if(GamePanelController.currentGame.getCurrentEnemy().getHealth() <= 0) {
                    endBattle();
                } else if(GamePanelController.currentGame.getCurrentPlayer().getHealth() <= 0) {
                    gameOver();
                }
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
        labelPlayerHealth.setText("Health (" + (int)((double) (GamePanelController.currentGame.getCurrentPlayer().getHealth()) / GamePanelController.currentGame.getCurrentPlayer().getMaxHealth() * 100) + " %)");
        progressBarPlayerHealth.setProgress(((double) GamePanelController.currentGame.getCurrentPlayer().getHealth() / GamePanelController.currentGame.getCurrentPlayer().getMaxHealth()));
        labelMonsterHealth.setText("Health (" + (int)((double) (GamePanelController.currentGame.getCurrentEnemy().getHealth()) / GamePanelController.currentGame.getCurrentEnemy().getMaxHealth() * 100) + " %)");
        progressBarMonsterHealth.setProgress((double) (GamePanelController.currentGame.getCurrentEnemy().getHealth()) / GamePanelController.currentGame.getCurrentEnemy().getMaxHealth());

    }

    // Called when the battle is over
    private void endBattle() throws IOException, AlertException {
        // Give the player the XP and items the monster has
        StringBuilder lootMessage = new StringBuilder("Loot has dropped:\n");
        for(InventoryItem item : GamePanelController.currentGame.getCurrentEnemy().getInventory()) {
            GamePanelController.currentGame.getCurrentPlayer().addToInventory(item);
            lootMessage.append(item.getName()).append("\n");
        }
        lootMessage.append("Experience (").append(GamePanelController.currentGame.getCurrentEnemy().getYieldedXP()).append(")").append("\n");
        GamePanelController.currentGame.getCurrentPlayer().addExperience(GamePanelController.currentGame.getCurrentEnemy().getYieldedXP());
        lootMessage.append("Gold (").append(GamePanelController.currentGame.getCurrentEnemy().getYieldedGold()).append(")");
        GamePanelController.currentGame.getCurrentPlayer().addGold(GamePanelController.currentGame.getCurrentEnemy().getYieldedGold());
        GamePanelController.currentGame.getCurrentMap().getTileByCoords(GamePanelController.currentGame.getCurrentPlayer().getX(), GamePanelController.currentGame.getCurrentPlayer().getY()).getEntities().remove(GamePanelController.currentGame.getCurrentEnemy());
        for(Quest q : GamePanelController.currentGame.getCurrentPlayer().getQuestJournal()) {
            if(q.getQuestType() == Quest.QuestType.KILL) {
                if(((KillQuest) q).getEnemyType().getEnemyTypeID() == GamePanelController.currentGame.getCurrentEnemy().getEnemyTypeID()) {
                    ((KillQuest) q).incrementKillProgress();
                }
            }
        }
        callAlert(new AlertException("Victory", "You have won! " + GamePanelController.currentGame.getCurrentEnemy().getDisplayName() + " is no more!", lootMessage.toString()));
        GamePanelController.currentGame.setCurrentEnemy(null);

        // Return back to game screen
        Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("/gui/GamePanel.fxml"));
        Stage gameStage = new Stage();
        gameStage.setTitle(Main.gameTitle);
        gameStage.setScene(new Scene(root, GamePanelController.GAME_PANEL_WIDTH, GamePanelController.GAME_PANEL_HEIGHT));
        gameStage.setMinWidth(GamePanelController.GAME_PANEL_WIDTH);
        gameStage.setMinHeight(GamePanelController.GAME_PANEL_HEIGHT);
        gameStage.getScene().getStylesheets().add("/gui/hivle.css");
        gameStage.show();
    }

    public void gameOver() throws IOException {
        callAlert(new AlertException("Player is dead",
                GamePanelController.currentGame.getCurrentPlayer().getDisplayName() + " has been killed and eaten by " + GamePanelController.currentGame.getCurrentEnemy().getDisplayName(),
                "Better luck next time!"));
        Parent root = FXMLLoader.load(getClass().getResource("/gui/MainMenu.fxml"));
        Stage gameStage = new Stage();
        gameStage.setTitle(Main.gameTitle);
        gameStage.setScene(new Scene(root, MainMenuController.MAIN_MENU_WIDTH, MainMenuController.MAIN_MENU_HEIGHT));
        gameStage.setMinWidth(MainMenuController.MAIN_MENU_WIDTH);
        gameStage.setMinHeight(MainMenuController.MAIN_MENU_HEIGHT);
        gameStage.getScene().getStylesheets().add("/gui/hivle.css");
        gameStage.show();
        GamePanelController.currentGame = null;
        Stage stage = (Stage) gridPaneGlobal.getScene().getWindow();
        stage.close();
    }

    public void initialize(URL url, ResourceBundle rb) {

        // Sets the name of the GamePanelController.currentGame.getCurrentPlayer() and the monster above corresponding health bars
        labelNamePlayer.setText(GamePanelController.currentGame.getCurrentPlayer().getDisplayName());
        labelNameMonster.setText(GamePanelController.currentGame.getCurrentEnemy().getDisplayName());

        // Log start
        textAreaLog.setText("The battle has started!");

        // Looks up chances and damage values of all the attacks
        try {
            labelAttackFastChance.setText((int)(Math.round(calculateChanceToHit(AttackType.FAST, GamePanelController.currentGame.getCurrentPlayer().getTotalDexterity(), GamePanelController.currentGame.getCurrentEnemy().getDexterity())*100)) + " %");
            labelAttackFastDamage.setText((int)Math.round(0.6 * (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()*GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()) / (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()+ GamePanelController.currentGame.getCurrentEnemy().getDefense())) + " - " + Math.round((GamePanelController.currentGame.getCurrentPlayer().getTotalStrength() * GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()) / (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength() + GamePanelController.currentGame.getCurrentEnemy().getDefense())));
            labelAttackNormalChance.setText((int)(Math.round(calculateChanceToHit(AttackType.NORMAL, GamePanelController.currentGame.getCurrentPlayer().getTotalDexterity(),
                    GamePanelController.currentGame.getCurrentEnemy().getDexterity())*100)) + " %");
            labelAttackNormalDamage.setText((int)Math.round(0.9 * (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()*GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()) / (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()+ GamePanelController.currentGame.getCurrentEnemy().getDefense())) + " - " + (int)Math.round(1.1 * (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength() * GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()) / (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength() + GamePanelController.currentGame.getCurrentEnemy().getDefense())));
            labelAttackStrongChance.setText((int)(Math.round(calculateChanceToHit(AttackType.STRONG, GamePanelController.currentGame.getCurrentPlayer().getTotalDexterity(), GamePanelController.currentGame.getCurrentEnemy().getDexterity())*100)) + " %");
            labelAttackStrongDamage.setText((int)Math.round(1.4 * (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()*GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()) / (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()+ GamePanelController.currentGame.getCurrentEnemy().getDefense())) + " - " + (int)Math.round(1.6 * (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength() * GamePanelController.currentGame.getCurrentPlayer().getTotalStrength()) / (GamePanelController.currentGame.getCurrentPlayer().getTotalStrength() + GamePanelController.currentGame.getCurrentEnemy().getDefense())));
        } catch (AlertException alertException) {
            callAlert(alertException);
        }

        refreshGUI();

    }

}
