package prpg.gameLogic.items;

public abstract class InventoryItem {

    public enum ItemType {
        QUEST,
        BODYWEAR,
        HEADWEAR,
        LEGWEAR,
        WEAPON,
        FOOD
    }

    private static int numberOfItems = 0;
    protected int id;
    protected String displayName;
    protected ItemType type;

    public InventoryItem(String displayName, ItemType type) {
        this.id = numberOfItems;
        numberOfItems += 1;
        this.displayName = displayName;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public ItemType getType() {
        return this.type;
    }

    public abstract String getDisplayName();

    public abstract String getDescription();

    public abstract int getWorth();

}
