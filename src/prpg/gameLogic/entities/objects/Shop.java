package prpg.gameLogic.entities.objects;

import prpg.exceptions.ExceptionAlert;
import prpg.gameLogic.items.InventoryItem;

import java.util.ArrayList;

public class Shop extends GameObject {

    private int typeId;
    private ArrayList<InventoryItem> inventory;
    private int restockCost;

    public Shop(int typeId, String displayName, String description, int restockCost) {
        super(displayName, description);
        this.typeId = typeId;
        this.restockCost = restockCost;
        this.inventory = new ArrayList<>();
    }

    public void addItem(InventoryItem item) {
        this.inventory.add(item);
    }

    public void addItems(ArrayList<InventoryItem> items) {
        this.inventory.addAll(items);
    }

    public void removeItem(InventoryItem item) throws ExceptionAlert {
        if(this.inventory.contains(item)) {
            this.inventory.remove(item);
        } else {
            throw new ExceptionAlert("Inventory error", "An error occured while trying to remove an item", "The shop does not have the specified item in it's inventory");
        }
    }

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
