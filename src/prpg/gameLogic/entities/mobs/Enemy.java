package prpg.gameLogic.entities.mobs;

import prpg.exceptions.AlertException;
import prpg.gameLogic.entities.Storing;
import prpg.gameLogic.items.InventoryItem;

import java.util.ArrayList;

public class Enemy extends Mob implements Storing {

    private int enemyTypeID;
    private int yieldedXP;
    private int yieldedGold;
    private ArrayList<InventoryItem> inventory;

    public Enemy(int enemyTypeID, String name, String description, int maxHealth, int level, int strength, int dexterity, int defense, int yieldedXP, int yieldedGold) {
        super(name, description, maxHealth, level, strength, dexterity, defense);
        this.enemyTypeID = enemyTypeID;
        this.inventory = new ArrayList<>();
        this.yieldedXP = (int) (5 + 8 * Math.pow(1.5, this.level));
        this.yieldedGold = yieldedGold;
    }

    @Override
    public String getDisplayName() {
        return "[E] " + name + " (" + level + ")";
    }

    @Override
    public String getActionName() {
        return "Fight";
    }

    @Override
    public void removeFromInventory(InventoryItem item) throws AlertException {
        if(this.inventory.contains(item)) {
            this.inventory.remove(item);
        } else {
            throw new AlertException("Inventory error", "An error occured while trying to remove an item from enemy's inventory", "The enemy does not have the specified item in it's inventory");
        }
    }

    @Override
    public void addToInventory(InventoryItem item) {
        this.inventory.add(item);
    }

    @Override
    public void addToInventory(ArrayList<InventoryItem> items) {
        this.inventory.addAll(items);
    }

    @Override
    public ArrayList<InventoryItem> getInventory() {
        return inventory;
    }

    public int getEnemyTypeID() {
        return enemyTypeID;
    }

    public int getYieldedXP() {
        return yieldedXP;
    }

    public int getYieldedGold() {
        return yieldedGold;
    }

}
