package gameLogic.entities;

import gameLogic.inventory.InventoryItem;

public class Item extends Object {

    public enum ItemType {
        QUEST,
        BODYWEAR,
        HEADWEAR,
        LEGWEAR,
        WEAPON,
        FOOD
    }

    private int addedStrength;
    private int addedDexterity;
    private int addedIntelligence;

    private ItemType type;

    public Item(int x, int y, String displayName, String description, ItemType type, int addedStrength, int addedDexterity, int addedIntelligence) {
        super(x, y, displayName, description);
        this.type = type;
    }

    public InventoryItem toInventoryItem() {
        //todo: add switch that decides which class the item belongs to based on type
        return null;
    }

}
