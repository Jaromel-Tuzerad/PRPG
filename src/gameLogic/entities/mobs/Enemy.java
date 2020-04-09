package gameLogic.entities.mobs;

import gameLogic.inventory.InventoryItem;

import java.util.ArrayList;

public class Enemy extends Mob {

    private int yieldedXP;
    private ArrayList<InventoryItem> droppedItems;

    public Enemy(String name, String description, int maxHealth, int level, int strength, int dexterity, int defense, ArrayList<InventoryItem> droppedItems, int yieldedXP) {
        super(name, description, maxHealth, level, strength, dexterity, defense);
        this.droppedItems = new ArrayList<>();
        this.droppedItems.addAll(droppedItems);
        this.yieldedXP = yieldedXP;
    }

    public int getYieldedXP() {
        return yieldedXP;
    }

    public ArrayList<InventoryItem> getDroppedItems() {
        return droppedItems;
    }

}
