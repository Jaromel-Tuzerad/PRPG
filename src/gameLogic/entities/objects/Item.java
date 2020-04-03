package gameLogic.entities.objects;

import gameLogic.entities.objects.GameObject;
import gameLogic.inventory.InventoryItem;

public class Item extends GameObject {

    private InventoryItem item;

    public Item(String displayName, String description, InventoryItem item) {
        super(displayName, description);
        this.item = item;
    }

    public InventoryItem getItem() {
        return this.item;
    }

}
