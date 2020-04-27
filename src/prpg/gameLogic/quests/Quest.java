package prpg.gameLogic.quests;

import prpg.gameLogic.entities.mobs.NPC;
import prpg.gameLogic.items.InventoryItem;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Quest implements Serializable {

    public enum QuestType {
        KILL,
        FETCH
    }

    protected QuestType type;
    protected String title;
    protected NPC questgiver;
    protected int rewardXP;
    protected int rewardGold;
    protected ArrayList<InventoryItem> rewardItems;

    public Quest(QuestType type, String title, NPC questgiver, int rewardXP, int rewardGold, ArrayList<InventoryItem> rewardItems) {
        this.type = type;
        this.title = title;
        this.questgiver = questgiver;
        this.rewardXP = rewardXP;
        this.rewardGold = rewardGold;
        this.rewardItems = rewardItems;
    }

    public abstract boolean isFinished();

    public abstract String getDescription();

    public abstract String getNPCText();

    public NPC getQuestGiver() {
        return this.questgiver;
    }

    public int getRewardXP() {
        return rewardXP;
    }

    public int getRewardGold() {
        return rewardGold;
    }

    public ArrayList<InventoryItem> getRewardItems() {
        return rewardItems;
    }

    public String getTitle() {
        return title;
    }

    public NPC getQuestgiver() {
        return questgiver;
    }

    public QuestType getQuestType() {
        return this.type;
    }

}
