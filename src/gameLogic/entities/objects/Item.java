package gameLogic.entities.objects;

import gameLogic.entities.objects.GameObject;
import gameLogic.inventory.InventoryItem;

public class Item extends GameObject {

    private InventoryItem item;

    public Item(String displayName, InventoryItem item) {
        super(displayName, item.getDisplayName() + " is laying on the ground");
        this.item = item;
    }

    public InventoryItem getItem() {
        return this.item;
    }

}
