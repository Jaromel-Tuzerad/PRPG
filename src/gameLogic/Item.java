package gameLogic;

public class Item extends Object {

    enum ItemType {
        QUEST,
        BODYWEAR,
        HEADWEAR,
        LEGWEAR,
        WEAPON
    }

    private int addedStrength;
    private int addedDexterity;
    private int addedIntelligence;

    private ItemType type;

    public Item(int x, int y, String displayName, String description, char icon, ItemType type, int addedStrength, int addedDexterity, int addedIntelligence) {
        super(x, y, displayName, description, icon);
        this.type = type;
    }

    public InventoryItem toInventoryItem() {
        return new InventoryItem(this.type, this.displayName, this.addedStrength, this.addedDexterity, this.addedIntelligence);
    }
}
