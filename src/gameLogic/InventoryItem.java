package gameLogic;

public class InventoryItem {

    Item.ItemType type;
    String displayName;
    int addedStrength;
    int addedDexterity;
    int addedIntelligence;

    public InventoryItem(Item.ItemType type, String displayName, int addedStrength, int addedDexterity, int addedIntelligence) {
        this.type = type;
        this.displayName = displayName;
        this.addedStrength = addedStrength;
        this.addedDexterity = addedDexterity;
        this.addedIntelligence = addedIntelligence;
    }

}
