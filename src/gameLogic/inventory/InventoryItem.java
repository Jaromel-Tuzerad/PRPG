package gameLogic.inventory;

import gameLogic.entities.Item;

public abstract class InventoryItem {

    private static int numberOfItems = 0;
    private int id;
    private String displayName;

    public InventoryItem(String displayName) {
        this.id = numberOfItems;
        numberOfItems += 1;
        this.displayName = displayName;
    }

    // TODO - When the item drops, it creates an instance of Item
    /*public Item toItemAt(int x, int y) {
        return new Item(x, y, this.displayName, this.displayName + " is laying on the ground", this.type, this.addedStrength, this.addedDexterity, this.addedIntelligence);
    }*/

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return displayName;
    }

}
