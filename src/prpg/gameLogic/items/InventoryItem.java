package prpg.gameLogic.items;

import prpg.gameLogic.entities.objects.Item;

public abstract class InventoryItem {

    public enum ItemType {
        QUEST,
        BODYWEAR,
        HEADWEAR,
        LEGWEAR,
        WEAPON,
        FOOD
    }

    protected String name;
    protected ItemType type;

    public InventoryItem(String name, ItemType type) {
        this.name = name;
        this.type = type;
    }

    public abstract String getDisplayName();

    public abstract String getDescription();

    public Item toItem() {
        return new Item(this.name, this);
    }

    public ItemType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

}
