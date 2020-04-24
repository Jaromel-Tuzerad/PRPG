package prpg.gameLogic.entities.objects;

import prpg.gameLogic.items.InventoryItem;

public class Item extends GameObject {

    private InventoryItem item;

    public Item(String name, InventoryItem item) {
        super(name, name + " is laying on the ground");
        this.item = item;
    }

    @Override
    public String getDisplayName() {
        return "[I] " + this.name;
    }

    @Override
    public String getActionName() {
        return "Pick up";
    }

    public InventoryItem getItem() {
        return this.item;
    }


}
