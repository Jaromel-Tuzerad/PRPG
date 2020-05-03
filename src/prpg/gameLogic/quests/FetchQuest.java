package prpg.gameLogic.quests;

import prpg.gameLogic.entities.mobs.NPC;
import prpg.gameLogic.items.InventoryItem;
import prpg.gameLogic.items.QuestItem;

import java.util.ArrayList;

public class FetchQuest extends Quest {

    private QuestItem requiredItem;
    private boolean inInventory;
    private int posX;
    private int posY;

    public FetchQuest(String title, NPC questgiver, ArrayList<InventoryItem> rewardItems, QuestItem requiredItem, int posX, int posY) {
        super(QuestType.FETCH, title, questgiver, (int) (20*Math.pow(1.5, questgiver.getLevel())), (int) (20*Math.pow(1.5, questgiver.getLevel())), rewardItems);
        this.requiredItem = requiredItem;
        this.inInventory = false;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public boolean isFinished() {
        return this.inInventory;
    }

    @Override
    public String getDescription() {
        // Before the quest is complete
        if(!isFinished()) {
            return "You still need to obtain the " + this.requiredItem.getName() + "!" +
                    "\nIt should be at [" + this.posX + ", " + this.posY + "]!";
        // After the requirements are met
        } else {
            return "You have found the " + this.requiredItem.getName() + "!" +
                    "\nGo return it and get your reward from " + this.questgiver.getName() + "!";
        }
    }

    @Override
    public String getNPCText() {
        return "Hail, o brave adventurer! I have a quest of the utmost importance for you." +
                "\nBring me the " + this.requiredItem.getName() + " which is at [" + this.posX + ", " + this.posY + "]";
    }

    public QuestItem getRequiredItem() {
        return requiredItem;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setInInventory(boolean state) {
        this.inInventory = state;
    }

}
