package prpg.gameLogic.entities.mobs;

import prpg.gameLogic.items.InventoryItem;

import java.util.ArrayList;

public class Enemy extends Mob {

    private int yieldedXP;
    private int yieldedGold;
    private ArrayList<InventoryItem> droppedItems;

    public Enemy(String name, String description, int maxHealth, int level, int strength, int dexterity, int defense, ArrayList<InventoryItem> droppedItems, int yieldedXP, int yieldedGold) {
        super("[E] " + name + " (" + level + ")", description, maxHealth, level, strength, dexterity, defense);
        this.droppedItems = new ArrayList<>();
        this.droppedItems.addAll(droppedItems);
        this.yieldedXP = yieldedXP;
        this.yieldedGold = yieldedGold;
    }

    public int getYieldedXP() {
        return yieldedXP;
    }

    public int getYieldedGold() {
        return yieldedGold;
    }

    public ArrayList<InventoryItem> getDroppedItems() {
        return droppedItems;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "yieldedXP=" + yieldedXP +
                ", yieldedGold=" + yieldedGold +
                ", droppedItems=" + droppedItems +
                ", health=" + health +
                ", maxHealth=" + maxHealth +
                ", level=" + level +
                ", strength=" + strength +
                ", dexterity=" + dexterity +
                ", defense=" + defense +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
