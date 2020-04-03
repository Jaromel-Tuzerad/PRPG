package gameLogic.inventory;

public class Equipment extends InventoryItem {

    private int addedStrength;
    private int addedDexterity;
    private int addedDefense;

    public Equipment(String displayName, int addedStrength, int addedDexterity, int addedDefense, ItemType type) {
        super(displayName, type);
        this.addedStrength = addedStrength;
        this.addedDexterity = addedDexterity;
        this.addedDefense = addedDefense;
    }

    public int getAddedStrength() {
        return addedStrength;
    }

    public int getAddedDexterity() {
        return addedDexterity;
    }

    public int getAddedDefense() {
        return addedDefense;
    }

    public String getDisplayName() {
        return this.displayName + " (" + addedStrength + ", " + addedDexterity + ", " + addedDefense +")";
    }

    public String getDescription() {
        return (this.displayName + "\nStrength: " + this.addedStrength + "\nDexterity: " + this.addedDexterity + "\nIntelligence: " + this.addedDefense);
    }
}
