package gameLogic.entities;

import gameLogic.inventory.InventoryItem;

public class Item extends GameObject {

    private InventoryItem item;

    public Item(int x, int y, String displayName, String description, InventoryItem item) {
        super(x, y, displayName, description);
        this.item = item;
    }

    public InventoryItem getItem() {
        return this.item;
    }

}
