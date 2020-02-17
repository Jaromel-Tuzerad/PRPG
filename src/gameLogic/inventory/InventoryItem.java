package gameLogic.inventory;

import gameLogic.entities.Item;

public class InventoryItem {

    public enum ItemType {
        QUEST,
        BODYWEAR,
        HEADWEAR,
        LEGWEAR,
        WEAPON,
        FOOD
    }

    private static int numberOfItems = 0;
    private int id;
    private String displayName;
    private ItemType type;

    private int addedStrength;
    private int addedDexterity;
    private int addedIntelligence;

    public InventoryItem(String displayName, ItemType type) {
        this.id = numberOfItems;
        numberOfItems += 1;
        this.displayName = displayName;
        this.type = type;
        this.addedDexterity = addedDexterity;
        this.addedStrength = addedStrength;
        this.addedIntelligence = addedIntelligence;
    }

    public Item dropItemAt(int x, int y) {
        return new Item(x, y, this.displayName, this.displayName + " is laying on the ground", this);
    }

    public int getId() {
        return this.id;
    }

    public ItemType getType() {
        return this.type;
    }

    public String getDisplayName() {
        return displayName;
    }

}
