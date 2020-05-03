package prpg.gameLogic.entities.objects;

import prpg.exceptions.AlertException;
import prpg.gameLogic.entities.Storing;
import prpg.gameLogic.items.InventoryItem;

import java.util.ArrayList;

public class Shop extends GameObject implements Storing {

    private int typeId;
    private ArrayList<InventoryItem> inventory;
    private int restockCost;

    public Shop(int typeId, String name, String description, int restockCost) {
        super(name, description);
        this.typeId = typeId;
        this.restockCost = restockCost;
        this.inventory = new ArrayList<>();
    }

    @Override
    public String getDisplayName() {
        return "[S] " + this.name;
    }

    @Override
    public String getActionName() {
        return "Shop";
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
    public void removeFromInventory(InventoryItem item) throws AlertException {
        if(this.inventory.contains(item)) {
            this.inventory.remove(item);
        } else {
            throw new AlertException("Inventory error", "An error occured while trying to remove an item", "The shop does not have the specified item in it's inventory");
        }
    }

    @Override
    public ArrayList<InventoryItem> getInventory() {
        return inventory;
    }

    public int getRestockCost() {
        return this.restockCost;
    }

    public int getTypeId() {
        return typeId;
    }


}
